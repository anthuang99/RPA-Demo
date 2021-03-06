package org.webdriver.patatiumwebui.action;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.h2.engine.User;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.webdriver.patatiumwebui.config.YmlConfig;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.exception.RPAException;
import org.webdriver.patatiumwebui.pageObject.LoginXmTaxPage;
import org.webdriver.patatiumwebui.utils.CommonUtil;
import org.webdriver.patatiumwebui.utils.ElementAction;
import org.webdriver.patatiumwebui.utils.Log;
import org.webdriver.patatiumwebui.utils.TaxUtil;
import org.webdriver.patatiumwebui.utils.TestBaseCase;
import org.webdriver.patatiumwebui.web.model.TaxParameter;
import org.webdriver.patatiumwebui.web.service.TaxParameterService;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class LoginXmTaxAction extends TestBaseCase {
    private String Url;
    private String UserName;
    private String PassWord;

    private Log log=new Log(this.getClass());

    public LoginXmTaxAction(){}

    public LoginXmTaxAction(String Url, String UserName, String PassWord) throws IOException
    {
        this.Url = Url;
        this.UserName = UserName;
        this.PassWord = PassWord;

        loginAction();
//        dragCrack();
    }

    private void loginAction() throws IOException
    {
        //此driver变量继承自TestBase变量
        LoginXmTaxPage loginPage = new LoginXmTaxPage();
        loginPage.open(Url);

        ElementAction action=new ElementAction();
//        action.click(loginPage.账户登录());
        action.sleep(1);

        {// 如果是 chrome 浏览器,点击掉那个浏览器的弹出框,
//            YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
            if (TaxUtil.isChromeBrowser()) {
                action.click(loginPage.浏览器版本提示确定按钮());
            } else if (TaxUtil.isFirefoxBrowser()) {
                action.click(loginPage.浏览器版本提示确定按钮());
            }
        }

        action.click(loginPage.登录链接());
        action.sleep(1);

        action.click(loginPage.用户名()); //先点击这个之后下面才会显示真正的密码输入框
        action.sleep(1);
        action.type(loginPage.用户名(),UserName);
        action.sleep(1);

        action.click(loginPage.密码2());
        action.sleep(1);
        action.type(loginPage.密码2(),PassWord);
        action.sleep(1);

        YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
        if (ymlConfig.getIsManualCheckcode().equalsIgnoreCase("false")) {
            WebDriver driver = getWebDriver();
            TaxParameterService taxParameterService = (TaxParameterService) SpringUtil.getBean("taxParameterService");
            TaxParameter taxParameter = taxParameterService.getTaxParameter(TaxConstant.DRAG_CHECKCODE_REPEAT_COUNT);
            int dragCheckcodeRepeatCount = Integer.valueOf(taxParameter.getValue());
            boolean success = false;
            String trail_x = "";
            for (int i = 0; i < dragCheckcodeRepeatCount; i++) {
    //            WebElement element = driver.findElement(By.cssSelector("img#main_img"));
    //            trail_x = captureElement1(element, 0);
                Map<String, Object> base64AndCheckcodeMap = TaxUtil.getBase64AndTrialX();
                trail_x = (String)base64AndCheckcodeMap.get("checkcode");

                //        String token_yzm = executeJsSelenium(driver, "return token_yzm;");
                String token_yzm = action.executeJS("return token_yzm;").toString();
                String trailurl = "https://wsbsfwt2.xmtax.gov.cn/tlogin/checkImgTrailCode.do";
                List<NameValuePair> requestParameters = new ArrayList<>();
                WebRequest request = new WebRequest(new URL(trailurl));
                request.setHttpMethod(HttpMethod.POST);
                requestParameters.add(new NameValuePair("trail_token", token_yzm));
                requestParameters.add(new NameValuePair("trail_x", ""+trail_x));
                request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
                request.setRequestParameters(requestParameters);
                String content = getContent(requestPost(driver, request));
                JSONObject jsonObject = new JSONObject(content);
                success = jsonObject.getBoolean("success");
                if (success) {
                    break;
                } else {// 刷新图片
                    LoginXmTaxPage loginXmTaxPage = new LoginXmTaxPage();
                    action.click(loginXmTaxPage.刷新图片());
                    action.sleep(1);
                }
            }

            /*
            action.click(loginPage.验证码());
            action.type(loginPage.验证码(), checkcode);

            action.sleep(1);
            */
            if (success) {
                action.executeJS("trail_x=" + trail_x);
                action.executeJS("SlideValidFlag=true");

                if (CommonUtil.isClickLogin()) {
                    action.click(loginPage.登录());
                }
            }
        } else {// 手工验证码
            action.sleep(10);
        }
    }

    public Map<String, Object> getBase64AndCheckcode() throws IOException {
        YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
        String baseUrl = ymlConfig.getBaseUrl();
        LoginXmTaxPage loginXmTaxPage = new LoginXmTaxPage();
        loginXmTaxPage.open(baseUrl);
        ElementAction action=new ElementAction();
        action.pagefoload(10);

        {// 如果是 chrome 浏览器,点击掉那个浏览器的弹出框,
//            YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
            if (TaxUtil.isChromeBrowser()) {
                action.click(loginXmTaxPage.浏览器版本提示确定按钮());
            } else if (TaxUtil.isFirefoxBrowser()) {
                action.click(loginXmTaxPage.浏览器版本提示确定按钮());
            }
        }

        action.click(loginXmTaxPage.登录链接());
        action.sleep(1);

        return TaxUtil.getBase64AndCheckcode(loginXmTaxPage.验证码图片());
    }

    public void doNothing() {
        System.out.println("doNothing");
    }

    /**
     * 拖动的破解
     */
    public void dragCrack() {
        try {
            ElementAction action = new ElementAction();
//            System.out.println(61.0 / 357.0);
//        WebDriver driver = BaseTest.getFirefox("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
            /* // 旧的登录代码
            WebDriver driver = getWebDriver();
            driver.manage().window().maximize();
            driver.get("https://wsbsfwt2.xmtax.gov.cn/");
            action.sleep(1);
            driver.findElement(By.cssSelector("a.layui-layer-btn0")).click();// 非IE浏览器的提示框,里面的确定按钮
            action.sleep(1);
            driver.findElement(By.cssSelector("div#Login img")).click();// 登录按钮
            */
            // 用上新代码,
            LoginXmTaxPage loginPage = new LoginXmTaxPage();
            loginPage.open(Url);

//        action.click(loginPage.账户登录());
            action.sleep(1);

            {// 如果是 chrome 浏览器,点击掉那个浏览器的弹出框,
//            YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
                if (TaxUtil.isChromeBrowser()) {
                    action.click(loginPage.浏览器版本提示确定按钮());
                } else if (TaxUtil.isFirefoxBrowser()) {
                    action.click(loginPage.浏览器版本提示确定按钮());
                }
            }

            action.click(loginPage.登录链接());
            action.sleep(1);

            action.click(loginPage.用户名()); //先点击这个之后下面才会显示真正的密码输入框
            action.sleep(1);
            action.type(loginPage.用户名(),UserName);
            action.sleep(1);

            action.click(loginPage.密码2());
            action.sleep(1);
            action.type(loginPage.密码2(),PassWord);
            action.sleep(1);

            WebDriver driver = getWebDriver();
            TaxParameterService taxParameterService = (TaxParameterService) SpringUtil.getBean("taxParameterService");
            TaxParameter taxParameter = taxParameterService.getTaxParameter(TaxConstant.DRAG_CHECKCODE_REPEAT_COUNT);
            int dragCheckcodeRepeatCount = Integer.valueOf(taxParameter.getValue());
            boolean success = false;
            String token_yzm = null;
            double trail_x = 0;
            for (int i = 0; i < dragCheckcodeRepeatCount; i++) {
                WebElement element = driver.findElement(By.cssSelector("img#main_img"));
                trail_x = captureElement1(element, 0);

                //        String token_yzm = executeJsSelenium(driver, "return token_yzm;");
                token_yzm = action.executeJS("return token_yzm;").toString();
                String trailurl = "https://wsbsfwt2.xmtax.gov.cn/tlogin/checkImgTrailCode.do";
                List<NameValuePair> requestParameters = new ArrayList<>();
                WebRequest request = new WebRequest(new URL(trailurl));
                request.setHttpMethod(HttpMethod.POST);
                requestParameters.add(new NameValuePair("trail_token", token_yzm));
                requestParameters.add(new NameValuePair("trail_x", ""+trail_x));
                request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
                request.setRequestParameters(requestParameters);
                String content = getContent(requestPost(driver, request));
                System.out.println(content);
                JSONObject jsonObject = new JSONObject(content);
                success = jsonObject.getBoolean("success");
                if (success) {
                    break;
                } else {// 刷新图片
                    LoginXmTaxPage loginXmTaxPage = new LoginXmTaxPage();
                    action.click(loginXmTaxPage.刷新图片());
                    action.sleep(1);
                }
            }

            /*
            {
                LoginXmTaxPage loginXmTaxPage = new LoginXmTaxPage();
                action.click(loginXmTaxPage.刷新图片());
            }
            */

            if (success && CommonUtil.isClickLogin()) {
                action.executeJS("trail_x=" + trail_x);
                action.executeJS("SlideValidFlag=true");

                /* // 旧的登录代码
                String url = "https://wsbsfwt2.xmtax.gov.cn/tlogin/checkLoginNew.do";
                List<NameValuePair> requestParameters = new ArrayList<>();
                WebRequest request = new WebRequest(new URL(url));
                request.setHttpMethod(HttpMethod.POST);
                String pwd = action.executeJS("return base64encode('{password}');".replace("{password}", PassWord)).toString();
                requestParameters.add(new NameValuePair("loginId", UserName));
                requestParameters.add(new NameValuePair("userPassword", pwd));
                requestParameters.add(new NameValuePair("trail_token", token_yzm));
                requestParameters.add(new NameValuePair("trail_x", ""+trail_x));
                requestParameters.add(new NameValuePair("open_trail", "Y"));
                request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
                request.setRequestParameters(requestParameters);
                String content = getContent(requestPost(driver, request));
                System.out.println(content);

                driver.get("https://wsbsfwt2.xmtax.gov.cn/tnsfwHome/index.do");
                 */
                action.click(loginPage.登录());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RPAException(e);
        }
    }

    public static double captureElement1(WebElement element, int i) {
        try {
            WrapsDriver wrapsDriver = (WrapsDriver) element;
            // 截图整个页面
            File screen = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
            BufferedImage img = ImageIO.read(screen);
            // 创建一个矩形使用上面的高度，和宽度
            Rectangle rect = element.getRect();
            // 得到元素的坐标
            Point p = element.getLocation();
            int offset = 1;
            BufferedImage dest = img.getSubimage(p.getX() + offset, p.getY() + offset, rect.width, rect.height);
            // 存为png格式
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(dest, "png", baos);
            byte[] bytes = baos.toByteArray();
            FileOutputStream out = new FileOutputStream("D:\\rgb"+i+".png");
            IOUtils.write(bytes, out);
            out.close();
            return checkImagePixel(dest);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.00;
    }

    public static double checkImagePixel(BufferedImage bi){
        int x = 0;
        int[] rgb = new int[3];
        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        List<String> list = new ArrayList<>();
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                list.add("i=" + i + ",j=" + j + ":(" + rgb[0] + ","
                        + rgb[1] + "," + rgb[2] + ")");
                if(rgb[0] > 240 && rgb[1] > 250 && rgb[2] > 250){
                    x = i;
                    break;
                }
            }
            if(x > 0){
                break;
            }
        }
        try {
            FileUtils.writeLines(new File("d:\\rgb.txt"), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Double.valueOf(x) / Double.valueOf(width);
    }

    public static <T> Object requestPost(T client, WebRequest request) throws RPAException {
        return requestPost(client, request, 30);
    }

    /**
     * 封装http post请求，返回请求内容
     * @param client  支持WebDriver或WebClient
     * @param request 请求参数
     * @param timeout 超时时间 - 暂时只对WebDriver生效
     * @return WebDriver返回请求内容， WebClient返回请求后的page对象
     */

    @SuppressWarnings("unchecked")
    public static <T> T requestPost(T client, WebRequest request, int timeout) throws RPAException {
        T ret = null;
        if(client instanceof WebDriver){
            String msg = "";
            WebDriver driver = (WebDriver) client;
            JavascriptExecutor js = (JavascriptExecutor) driver;
            driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
            String script = "var ajax = new XMLHttpRequest();url=arguments[0];var newdata=arguments[1];";
            List<NameValuePair> requestParameters = request.getRequestParameters();
            String rawQuery = getRawQuery(requestParameters);

            script += ""
                    + "var callback = arguments[arguments.length - 1];"
                    //+ "try{ajax = new ActiveXObject(\"Msxml2.XMLHTTP\");}catch (e){try{ajax = new ActiveXObject(\"Microsoft.XMLHTTP\");}catch (e) {}}"
                    + "ajax.open(\"post\", url, true);"
                    + " ajax.setRequestHeader(\"Content-Type\",\"application/x-www-form-urlencoded\");";

            if(!request.getAdditionalHeaders().isEmpty()){
                Map<String, String> headers = request.getAdditionalHeaders();
                for(String header : headers.keySet()){
                    script += "ajax.setRequestHeader(\""+header+"\",\""+headers.get(header)+"\");";
                }
            }

            script += "ajax.onreadystatechange = function () {"
                    + " if(ajax.readyState == 4 ) { "
                    + "if( ajax.status == 200 ) {"
                    + " callback(ajax.responseText) ;" + "}" + "}};"
                    + "ajax.send(newdata)";
            try{
                Object execute = js.executeAsyncScript(script, request.getUrl().toString(), rawQuery);
                if(execute instanceof Map){
                    Map<String,Object> map = (Map<String, Object>) execute;
//                    msg = JSONObject.toJSONString(map);
                    msg = new JSONObject(map).toString();
                } else if(execute instanceof String){
                    msg = (String) execute;
                }
            }catch(Exception e){
                throw new RPAException("WebDriver Post request timeout, timeout = " + timeout, e);
            }
            ret = (T) msg;
        } else if(client instanceof WebClient){
            WebClient driver = (WebClient) client;
            try {
                Page page = driver.getPage(request);
                ret = (T) page;
            } catch (FailingHttpStatusCodeException | IOException e) {
                throw new RPAException("WebClient Post request fail", e);
            }
        }
        return ret;
    }

    public static String getRawQuery(List<NameValuePair> requestParameters){
        String query = "";
        try {
            ArrayList<org.apache.http.NameValuePair> parameters = new ArrayList<org.apache.http.NameValuePair>();
            for (NameValuePair nv : requestParameters) {
                parameters.add(new BasicNameValuePair(nv.getName(), nv.getValue()));
            }
            URIBuilder uri = new URIBuilder();
            uri.addParameters(parameters);
            query = uri.build().getRawQuery();
        } catch (Exception e) {
        }
        return query;
    }

    public static <T> String getContent(T get){
        return getContent(get, null);
    }

    public static <T> String getContent(T get, String charset){
        String content = "";
        if(get instanceof WebDriver){
            WebDriver driver = (WebDriver) get;
            content = driver.getPageSource();
        } else if(get instanceof Page){
            Page page = (Page) get;
            if(charset == null){
                content = page.getWebResponse().getContentAsString();
            } else {
                Charset charset1 = Charset.forName(charset);
//                content = page.getWebResponse().getContentAsString(charset);
                content = page.getWebResponse().getContentAsString(charset1);
            }
        } else if(get instanceof String){
            content = (String) get;
        }
        return content;
    }
}
