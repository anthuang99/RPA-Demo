package org.webdriver.patatiumwebui;

import org.testng.TestNG;
import org.webdriver.patatiumwebui.utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @CreatedBy:chaoqiang
 * @Description:
 */
public class TestNGApplication {
    private Log log=new Log(this.getClass());

    private static void testExecution() {
        System.out.println(System.currentTimeMillis());
        try {
            for (int j = 0; j < 10; j++) {
                final int count = 2;
                ExecutorService executorService = Executors.newFixedThreadPool(count);
                for (int i = 0; i < count; i++) {
                    executorService.execute(new Thread() {
                        @Override
                        public void run() {
                            System.out.println("run here, ");
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                executorService.shutdown();
                executorService.awaitTermination(30, TimeUnit.MINUTES);
                System.out.println(System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        System.out.println("end");
    }

    public static void main(String[] args) {
        if (true) {
            testExecution();
            return;
        }
        TestNG testNG = new TestNG();
        List<String> suites = new ArrayList<String>();
//        suites.add(System.getProperty("user.dir")+"\\testngZEJ.xml");
//        suites.add(System.getProperty("user.dir")+"\\testngXmTax.xml");
        suites.add(System.getProperty("user.dir")+"\\testngXmTaxAddedValueTax.xml");
        testNG.setTestSuites(suites);
        testNG.run();

        //以下两种启动没有进入到方法去执行，原因待查
        //No.2
        /*TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[] {org.webdriver.patatiumwebui.Test.ZEJTest.class});
        testNG.run();*/

        //No.3
        /*XmlSuite suite = new XmlSuite();
        suite.setName("Suite");

        XmlTest test = new XmlTest(suite);
        test.setName("登录成功并进入渠道编辑的测试用例");
        List<XmlClass> classes = new ArrayList<XmlClass>();
        classes.add(new XmlClass("org.webdriver.patatiumwebui.Test.ZEJTest"));
        test.setXmlClasses(classes) ;

        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        suites.add(suite);
        TestNG tng = new TestNG();
        tng.setXmlSuites(suites);
        tng.run();*/

    }
}
