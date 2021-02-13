package org.webdriver.patatiumwebui.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.exception.RPAException;
import org.webdriver.patatiumwebui.httprequest.CheckcodeUtil;
import org.webdriver.patatiumwebui.web.model.TaxParameter;
import org.webdriver.patatiumwebui.web.service.TaxParameterService;
import org.webdriver.patatiumwebui.web.util.SpringUtil;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 放一些报税用到的公共方法
 */
public class TaxUtil {
    /**
     * 截图,返回 base64
     * @param driver
     * @return
     */
    public static String getScreenShotBase64(WebDriver driver) {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        FileInputStream in = null;
        try {
            in = new FileInputStream(srcFile);
            byte[] bytes = IOUtils.toByteArray(in, srcFile.length());
            BASE64Encoder encoder = new BASE64Encoder();
            String base64 = encoder.encode(bytes);
            return base64.replaceAll("\r\n", "");
        } catch (IOException e) {
            throw new RPAException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 是否是 chrome 浏览器
     * @return
     */
    public static boolean isChromeBrowser() {
        TaxParameterService taxParameterService = (TaxParameterService) SpringUtil.getBean("taxParameterService");
        TaxParameter taxParameter = taxParameterService.getTaxParameter(TaxConstant.BROWSER_PATH);
        if (taxParameter.getValue().indexOf("chrome.exe") > -1) {
            return true;
        }
        return false;
    }

    /**
     * 是否是 firefox 浏览器
     * @return
     */
    public static boolean isFirefoxBrowser() {
        TaxParameterService taxParameterService = (TaxParameterService) SpringUtil.getBean("taxParameterService");
        TaxParameter taxParameter = taxParameterService.getTaxParameter(TaxConstant.BROWSER_PATH);
        if (taxParameter.getValue().indexOf("firefox.exe") > -1) {
            return true;
        }
        return false;
    }

    /**
     * 是否是 IE 浏览器
     * @return
     */
    public static boolean isIEBrowser() {
        TaxParameterService taxParameterService = (TaxParameterService) SpringUtil.getBean("taxParameterService");
        TaxParameter taxParameter = taxParameterService.getTaxParameter(TaxConstant.BROWSER_PATH);
        if (taxParameter.getValue().indexOf("IEXPLORE.EXE") > -1) {
            return true;
        }
        return false;
    }

    public static void switchToMainWindow() {
        ElementAction action = new ElementAction();
        if (TaxUtil.isIEBrowser()) {
            String title = "国家税务总局厦门市电子税务局";
            action.switchToWindowByTitle(title);
        } else {
            action.switchToWindowByUrl("/tnsfwHome/index.do");
        }
    }

    /**
     * 拖动的图片识别
     * @return
     * @throws IOException
     */
    public static Map<String, Object> getBase64AndTrialX() throws IOException {
        /*
        ElementAction action = new ElementAction();
        action.executeJS("document.getElementById(\"checkCodeImage\").width = 60");
        action.executeJS("document.getElementById(\"checkCodeImage\").height = 25");
        action.executeJS("document.getElementById(\"checkCodeImage\").style = ''");
        WebElement element = action.findElement(picture);
        */
        ElementAction action = new ElementAction();
        String base64 = action.executeJS("return $('#main_img').attr('src')").toString();
        String fileType = "jpeg";
        if (base64.indexOf("data:image/png;base64,") > -1) {
            base64 = base64.replaceFirst("data:image/png;base64,", "");
            fileType = "png";
        } else {
            base64 = base64.replaceFirst("data:image/jpeg;base64,", "");
        }

        String checkcode = CheckcodeUtil.getCheckcode(base64, fileType);

        Map<String, Object> result = new HashMap<>();
        result.put("base64", base64);
        result.put("checkcode", checkcode);
        return result;
    }

    /**
     * 登录页截图,并取回验证码
     * @param picture
     * @return
     */
    public static Map<String, Object> getBase64AndCheckcode(Locator picture) throws IOException {
        ElementAction action = new ElementAction();
        action.executeJS("document.getElementById(\"checkCodeImage\").width = 60");
        action.executeJS("document.getElementById(\"checkCodeImage\").height = 25");
        action.executeJS("document.getElementById(\"checkCodeImage\").style = ''");
        WebElement element = action.findElement(picture);
        String base64 = shootWebElementGetBase64(element);
        String checkcode = CheckcodeUtil.getCheckcode(base64, "png");

        Map<String, Object> result = new HashMap<>();
        result.put("base64", base64);
        result.put("checkcode", checkcode);
        return result;
    }

    /**
     * 截图取 base64
     * @param element
     * @return
     * @throws IOException
     */
    private static String shootWebElementGetBase64(WebElement element) throws IOException {
        ElementAction action = new ElementAction();
        File screen = ((TakesScreenshot) action.getWebDriver()).getScreenshotAs(OutputType.FILE);

        Point p = element.getLocation();

        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();


        BufferedImage img = ImageIO.read(screen);

        BufferedImage dest = null;
//        YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
        TaxParameterService taxParameterService = (TaxParameterService) SpringUtil.getBean("taxParameterService");
        TaxParameter offsetXTaxParameter = taxParameterService.getTaxParameter(TaxConstant.CHECKCODE_OFFSET_X);
        TaxParameter offsetYTaxParameter = taxParameterService.getTaxParameter(TaxConstant.CHECKCODE_OFFSET_Y);
        int offsetX = 0;
        if (StringUtils.isNotEmpty(offsetXTaxParameter.getValue())) {
            offsetX = Integer.valueOf(offsetXTaxParameter.getValue());
        }
        int offsetY = 0;
        if (StringUtils.isNotEmpty(offsetYTaxParameter.getValue())) {
            offsetY = Integer.valueOf(offsetYTaxParameter.getValue());
        }
        if (TaxUtil.isChromeBrowser()) {
            dest = img.getSubimage(p.getX() + offsetX, p.getY() + offsetY, width, height);// 新版 UI,需要 + 1, 否则会截不全,
        } else if (TaxUtil.isIEBrowser()) {
            dest = img.getSubimage(p.getX() + offsetX, p.getY() + offsetY, width, height);// 新版 UI,需要 + 1, 否则会截不全,
        } else if (TaxUtil.isFirefoxBrowser()) {
            dest = img.getSubimage(p.getX() + offsetX, p.getY() + offsetY, width, height);// 新版 UI,需要 + 1, 否则会截不全,
        } else {
            dest = img.getSubimage(p.getX() + offsetX, p.getY() + offsetY, width, height);// 新版 UI,需要 + 1, 否则会截不全,
        }


        /*{// 截图写文件
            ImageIO.write(dest, "png", screen);

            File f = new File("E:\\hongjinqiu\\tmp\\tmp\\checkcode_prog.png");
            FileUtils.copyFile(screen, f);
        }*/
        {// 截图存 base64
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(dest, "png", outputStream);
            /*{
                byte[] array = outputStream.toByteArray();
                FileOutputStream out = new FileOutputStream("E:\\test.png");
                out.write(array, 0, array.length);
                out.close();
            }*/
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(outputStream.toByteArray());
        }
    }

    /**
     * 是否 win7
     * @return
     */
    public static boolean isWin7() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.indexOf("windows 7") > -1;
    }
}
