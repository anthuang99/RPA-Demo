package org.webdriver.patatiumwebui.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.webdriver.patatiumwebui.config.YmlConfig;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.exception.RPAException;
import org.webdriver.patatiumwebui.web.model.TaxParameter;
import org.webdriver.patatiumwebui.web.service.TaxParameterService;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class TestBaseCase {
//	public static WebDriver driver;
	//方法描述
//	public static String description;
//	public final Log log=new Log(this.getClass().getSuperclass());

//	@Parameters({"driver","nodeURL"})
	//String driver,String nodeURL
	@BeforeTest
	public void  setup() throws MalformedURLException {
		Log log = new Log(this.getClass().getSuperclass());
		TaxParameterService taxParameterService = (TaxParameterService) SpringUtil.getBean("taxParameterService");
		TaxParameter taxParameter = taxParameterService.getTaxParameter(TaxConstant.BROWSER_PATH);
		String browserPath = taxParameter.getValue();
		String driver = null;
		if (browserPath.indexOf("IEXPLORE.EXE") > -1) {
			driver = TaxConstant.INTERNET_EXPLORER_DRIVER;
		} else if (browserPath.indexOf("chrome.exe") > -1) {
			driver = TaxConstant.CHROME_DRIVER;
		} else if (browserPath.indexOf("firefox.exe") > -1) {
			driver = TaxConstant.FIREFOX_DRIVER;
		}
		YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
		String nodeURL = ymlConfig.getNodeURL();
		log.info("------------------开始执行测试---------------");
		if(nodeURL.equals("")||nodeURL.isEmpty())
		{
//			log.info("读取testng.xml配置的"+driver+"浏览器并将其初始化\n");
			log.info("读取 application.yml 配置的"+driver+"浏览器并将其初始化\n");
			try {
//				this.driver=setDriver(driver);
				setWebDriver(setDriver(driver));
			} catch (Exception e) {
				log.error("没有成功浏览器环境配置错误");
				e.printStackTrace();
			}
			System.out.println(nodeURL);
//			this.driver.manage().window().maximize();
			getWebDriver().manage().window();
		}
		else {
//			log.info("读取xml配置：浏览器:"+driver+"；gridNodeURL:"+nodeURL);
			log.info("读取 application.yml 配置：浏览器:"+driver+"；gridNodeURL:"+nodeURL);
			try {
//				this.driver=setRemoteDriver(getWebDriver(),nodeURL);
				setWebDriver(setRemoteDriver(driver,nodeURL));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				log.error("没有成功浏览器环境配置错误");
			}

//			this.driver.manage().window().maximize();
			getWebDriver().manage().window().maximize();
		}

	}

	@AfterTest
	public void tearDown() {
		Log log = new Log(this.getClass().getSuperclass());
//		this.driver.close();
		getWebDriver().close();
//		this.driver.quit();
		getWebDriver().quit();
		setWebDriver(null);
		log.info("-------------结束测试，并关闭退出浏览器-------------");
	}

	/**
	 * 从 ThreadLocal 中获取 WebDriver,
	 * @return
	 */
	public static WebDriver getWebDriver() {
		return RPAThreadLocal.getWebDriver();
	}

	/**
	 * 从 ThreadLocal 中获取 WebDriver,
	 */
	public static void setWebDriver(WebDriver webDriver) {
		RPAThreadLocal.setWebDriver(webDriver);
	}

	/**
	 * 用枚举类型列出浏览器列表，用于设置浏览器类型的函数参数
	 * @author zheng
	 *
	 */
	private WebDriver setDriver(String browsername)
	{
		Log log = new Log(this.getClass().getSuperclass());
		switch (browsername)
		{

			case "FirefoxDriver" :
				//System.setProperty("webdriver.firefox.bin", "C:/Program Files (x86)/Mozilla Firefox/firefox.exe");
				System.setProperty("webdriver.firefox.bin", "C:/Program Files/Mozilla Firefox/firefox.exe");
				FirefoxProfile firefoxProfile=new FirefoxProfile();
//				this.driver=new FirefoxDriver(firefoxProfile);
//				this.driver=new FirefoxDriver();
				setWebDriver(new FirefoxDriver());
				break;
			case "ChromeDriver":
//				System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver");
				String path = extractDriverAndReturnPath(browsername);
				log.error("driver copy path is:" + path);
				System.setProperty("webdriver.chrome.driver", path);
//				this.driver=new ChromeDriver();
				setWebDriver(new ChromeDriver());
				break;
			case "InternetExplorerDriver":
				System.setProperty("webdriver.ie.driver", "src\\main\\resources\\IEDriverServer.exe");
				DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
				dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				dc.setCapability("ignoreProtectedModeSettings", true);
//				this.driver=new InternetExplorerDriver(dc);
				applyIECapability(dc);
				setWebDriver(new InternetExplorerDriver(dc));
				break;
			case "HtmlUnitDriver":
//				this.driver=new HtmlUnitDriver();
//				setWebDriver(new HtmlUnitDriver());
				break;
			default:
//				this.driver=new FirefoxDriver();
				setWebDriver(new FirefoxDriver());
				break;
		}
//		return driver;
		return getWebDriver();
	}

	/**
	 * 将驱动解压到某个目录下,并返回绝对路径
	 * @param browsername
	 * @return
	 */
	private String extractDriverAndReturnPath(String browsername) {
		switch (browsername) {
			case "ChromeDriver":
				File f = new File("driver");
				if (!f.exists()) {
					f.mkdirs();
				}
				File chromeDriver = new File("driver" + File.separator + "chromedriver");
				if (!chromeDriver.exists()) {
					InputStream inputStream = getClass().getClassLoader().getResourceAsStream("chromedriver");
					try {
						chromeDriver.createNewFile();
						FileUtils.copyInputStreamToFile(inputStream, chromeDriver);
					} catch (IOException e) {
						throw new RPAException(e);
					} finally {
						IOUtils.closeQuietly(inputStream);
					}
				}
				return chromeDriver.getAbsolutePath();
		}
		return null;
	}

	private WebDriver setRemoteDriver(String browsername,String nodeURL) throws MalformedURLException
	{
		switch (browsername)
		{

			case TaxConstant.FIREFOX_DRIVER:
				DesiredCapabilities capabilities=DesiredCapabilities.firefox();
				capabilities.setBrowserName("firefox");
				capabilities.setVersion("63.0");
				capabilities.setPlatform(Platform.WINDOWS);
				//driver= new RemoteWebDriver(new URL("http://192.168.0.205:4455/wd/hub"), capabilities);
//				driver= new RemoteWebDriver(new URL(nodeURL), capabilities);
				WebDriver webDriver = new RemoteWebDriver(new URL(nodeURL), capabilities);
				webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				webDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
				webDriver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
				setWebDriver(webDriver);
//				setWebDriver(new RemoteWebDriver(new URL(nodeURL), capabilities));
				break;
			case TaxConstant.CHROME_DRIVER:
				// System.setProperty("webdriver.chrome.driver", "E:\\autotest\\autotmaiton\\resource\\chromedriver");
				//driver=new ChromeDriver();
				DesiredCapabilities dcchorme=DesiredCapabilities.chrome();
//				dcchorme.setBrowserName("chrome");
				dcchorme.setVersion("2.42.591088");// browserName 与 version 要与服务器注册 node 时提供的相一致,要不然跑不起来,
				dcchorme.setPlatform(Platform.WINDOWS);
//				driver=new RemoteWebDriver(new URL(nodeURL), dcchorme);
				setWebDriver(new RemoteWebDriver(new URL(nodeURL), dcchorme));
				break;
			case TaxConstant.INTERNET_EXPLORER_DRIVER: {
				DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
				dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				dc.setCapability("ignoreProtectedModeSettings", true);
				dc.setBrowserName("internet explorer");
				dc.setVersion("2.53.1.0");
				dc.setPlatform(Platform.WINDOWS);
				applyIECapability(dc);
//				driver= new RemoteWebDriver(new URL(nodeURL), dc);
				setWebDriver(new RemoteWebDriver(new URL(nodeURL), dc));
				break;
			}
			case "InternetExplorerDriver-8":
				DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
				dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				dc.setCapability("ignoreProtectedModeSettings", true);
				dc.setBrowserName("internet explorer");
				dc.setVersion("8.0.6001.18702");
				dc.setPlatform(Platform.XP);
//				driver= new RemoteWebDriver(new URL(nodeURL), dc);
				setWebDriver(new RemoteWebDriver(new URL(nodeURL), dc));
				break;
			case "InternetExplorerDriver-9":
				DesiredCapabilities dc2 = DesiredCapabilities.internetExplorer();
				dc2.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				dc2.setCapability("ignoreProtectedModeSettings", true);
				dc2.setBrowserName("internet explorer");
				dc2.setVersion("9.0.8112.16421");
				dc2.setPlatform(Platform.WINDOWS);
//				driver= new RemoteWebDriver(new URL(nodeURL), dc2);
				setWebDriver(new RemoteWebDriver(new URL(nodeURL), dc2));
				//driver=new InternetExplorerDriver(dc2);
				break;
			case "HtmlUnitDriver":
//				this.driver=new HtmlUnitDriver();
//				setWebDriver(new HtmlUnitDriver());
				break;
			default:
//				this.driver=new FirefoxDriver();
				setWebDriver(new FirefoxDriver());
				break;
		}
//		return driver;
		return getWebDriver();
	}

	/**
	 * 设置 IE 的默认行为
	 * @param dc
	 */
	private void applyIECapability(DesiredCapabilities dc) {
		dc.setCapability("nativeEvents", false);
		dc.setCapability("unexpectedAlertBehaviour", "accept");
		dc.setCapability("ignoreProtectedModeSettings", true);
		dc.setCapability("disable-popup-blocking", true);
		dc.setCapability("enablePersistentHover", true);
		dc.setCapability("ignoreZoomSetting", true);

		dc.setCapability("ensureCleanSession", false);
		dc.setCapability("ie.ensureCleanSession", false);
	}

	public static void main(String args[])
	{
		/*
		WebDriver driver2=new FirefoxDriver();
		driver2.get("http://www.baidu.com");
		*/

		System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver");
		WebDriver driver3=new ChromeDriver();
		driver3.get("http://www.baidu.com");
	}


}
