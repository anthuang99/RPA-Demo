package org.webdriver.patatiumwebui.common;

import org.testng.TestNG;
import org.testng.collections.Lists;
import org.testng.internal.OverrideProcessor;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.webdriver.patatiumwebui.exception.RPAException;
import org.webdriver.patatiumwebui.utils.Log;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * 修改配置文件可以以流的方式来读取,避免打成 jar 包后,读取不到 xml 文件
 * 拷贝父类的一些方法,再用反射修改一些私有变量为可访问
 */
public class RPATestNG extends TestNG {
    private Log log = new Log(this.getClass());
    // testNG 的 xml 配置文件,用流的形式传递进来,
    private InputStream xmlInputStream;

    /**
     * 拷贝父类的方法,加入了流的处理方式
     */
    public void initializeSuitesAndJarFile() {
        super.initializeSuitesAndJarFile();
        if (this.xmlInputStream != null) {
            Parser parser = getParser(xmlInputStream);
            try {
                Collection<XmlSuite> suites = parser.parse();
                for (XmlSuite suite : suites) {
                    // If test names were specified, only run these test names
                    List<String> m_testNames = getValueByReflect(TestNG.class, this, "m_testNames");
                    if (m_testNames != null) {
                        m_suites.add(extractTestNames(suite, m_testNames));
                    } else {
                        m_suites.add(suite);
                    }
                }
            } catch (ParserConfigurationException | IOException | SAXException e) {
                log.error(e.getMessage(), e);
                throw new RPAException(e);
            }
        }
    }

    /**
     * 通过反射获取值
     */
    private <T> T getValueByReflect(Class cls, Object obj, String fieldName) {
        try {
//            Field field = obj.getClass().getDeclaredField(fieldName);
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (NoSuchFieldException e) {
            log.error(e.getMessage(), e);
            throw new RPAException(e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new RPAException(e);
        }
    }

    /**
     * 拷贝父类的方法
     */
    private static XmlSuite extractTestNames(XmlSuite s, List<String> testNames) {
        List<XmlTest> tests = Lists.newArrayList();
        for (XmlTest xt : s.getTests()) {
            for (String tn : testNames) {
                if (xt.getName().equals(tn)) {
                    tests.add(xt);
                }
            }
        }

        if (tests.size() == 0) {
            return s;
        }
        else {
            XmlSuite result = (XmlSuite) s.clone();
            result.getTests().clear();
            result.getTests().addAll(tests);
            return result;
        }
    }

    /**
     * 拷贝 父类的方法
     */
    private Parser getParser(InputStream is) {
        Parser result = new Parser(is);
        initProcessor(result);
        return result;
    }

    /**
     * 拷贝父类的方法
     */
    private void initProcessor(Parser result) {
        String[] m_includedGroups = getValueByReflect(TestNG.class, this, "m_includedGroups");
        String[] m_excludedGroups = getValueByReflect(TestNG.class, this, "m_excludedGroups");
        result.setPostProcessor(new OverrideProcessor(m_includedGroups, m_excludedGroups));
    }

    public void setXmlInputStream(InputStream xmlInputStream) {
        this.xmlInputStream = xmlInputStream;
    }
}
