package org.webdriver.patatiumwebui;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.webdriver.patatiumwebui.Test.XmTaxTest;
import org.webdriver.patatiumwebui.common.RPAQueue;
import org.webdriver.patatiumwebui.common.RPATestNG;
import org.webdriver.patatiumwebui.common.RunningHolder;
import org.webdriver.patatiumwebui.config.ServerConfig;
import org.webdriver.patatiumwebui.config.YmlConfig;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.httprequest.vo.CorpTaxQueueVO;
import org.webdriver.patatiumwebui.thread.BrowserThread;
import org.webdriver.patatiumwebui.thread.SeleniumServerHubAndNodeThread;
import org.webdriver.patatiumwebui.utils.CommonUtil;
import org.webdriver.patatiumwebui.utils.Log;
import org.webdriver.patatiumwebui.utils.WinPlatformUtil;
import org.webdriver.patatiumwebui.web.model.TaxParameter;
import org.webdriver.patatiumwebui.web.service.CorpSettingService;
import org.webdriver.patatiumwebui.web.service.TaxDeclareProgressService;
import org.webdriver.patatiumwebui.web.service.TaxParameterService;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import java.io.InputStream;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({YmlConfig.class, ServerConfig.class})
public class App implements CommandLineRunner {
    private final Log log = new Log(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * 周期性运行申报,需要
     * 1.输入了企业用户名密码
     * 2.一键报税状态启动,
     * @throws Exception
     */
//    @Scheduled(cron = "*/5 * * * * ?")
    /*
    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 10 * 1000)
    public void perform() throws Exception {
        try {
            log.info("开始报税");
            CorpSettingService corpSettingService = (CorpSettingService)SpringUtil.getBean("corpSettingService");
            if (corpSettingService.canApplyTax()) {
                TestNG testNG = new TestNG();
                List<String> suites = new ArrayList<String>();
                suites.add(System.getProperty("user.dir")+"\\testngXmTax.xml");
                testNG.setTestSuites(suites);
                testNG.run();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    */

    /**
     * 运行一些周期性的任务
     * @throws Exception
     */
    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 30 * 60 * 1000)
    public void perform() throws Exception {
        // 每 30 分钟,清理一下进度表的数据,只保留 1000 条,
        TaxDeclareProgressService taxDeclareProgressService = (TaxDeclareProgressService)SpringUtil.getBean("taxDeclareProgressService");
        taxDeclareProgressService.deleteTaxDeclareProgress(1000);
    }

    @Override
    public void run(String... args) throws Exception {
        String browserPath = "chrome.exe";
        if (StringUtils.isNotEmpty(browserPath)) {
            TaxParameterService taxParameterService = (TaxParameterService) SpringUtil.getBean("taxParameterService");
            taxParameterService.saveOrUpdateTaxBrowserPathWhenInit(browserPath);
            // 如果从注册表取不到 chrome.exe,而又手工设置后,每次都从数据库里面来取,
            TaxParameter taxParameter = taxParameterService.getTaxParameter(TaxConstant.BROWSER_PATH);
            if (StringUtils.isNotEmpty(taxParameter.getValue())) {
                browserPath = taxParameter.getValue();
            }

            // 首次启动,10 秒后运行一次,
            if (!CommonUtil.isDev()) {
                new Thread() {
                    @Override
                    public void run() {
                        /*
                        try {
                            TimeUnit.SECONDS.sleep(10);
                            RPAQueue.put(new Object());
                        } catch (InterruptedException e) {
                            log.error(e.getMessage(), e);
                        }
                        */
                    }
                }.start();
            }
            while(true) {
                try {
//                    RPAQueue.poll(10, TimeUnit.MINUTES);// 10分钟
                    CorpTaxQueueVO corpTaxQueueVO = RPAQueue.take();// 永远等待,

                    log.info("开始报税");
                    CorpSettingService corpSettingService = (CorpSettingService)SpringUtil.getBean("corpSettingService");
                    if (corpSettingService.canApplyTax()) {
                        RunningHolder.IS_RUNNING = true;
                        YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
                        if (true || StringUtils.isNotEmpty(ymlConfig.getNodeURL())) {// 多线程
                            new XmTaxTest().run(corpTaxQueueVO);
                        } else {// 不会走这个分支,现在程序都是多线程,因此,这个分支的代码不再维护了,
                            /*
                            RPATestNG testNG = new RPATestNG();
                            InputStream in = null;
                            try {
                                in = this.getClass().getClassLoader().getResourceAsStream("testngXmTax.xml");
                                testNG.setXmlInputStream(in);
                                testNG.run();
                            } finally {
                                if (in != null) {
                                    in.close();
                                }
                            }
                            */
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    RunningHolder.IS_RUNNING = false;
                }
            }
        } else {
            System.exit(0);
        }
    }

    /**
     * 启动
     * 1.selenium-server-64bit.exe
     * 2.selenium-node-64bit.exe
     * 其中 selenium-server-64bit.exe 监听端口号 4444
     * 其中 selenium-node-64bit.exe 监听端口号 5555
     * 当存在时,先杀掉 node, 再杀掉 server
     */
    private void toggleSeleniumServerHubAndNode() {
        new SeleniumServerHubAndNodeThread().start();
    }

    /**
     * 读取注册表配置,启动 chrome 浏览器,
     */
    private void startBrowser(String chromeAppPath) {
        new BrowserThread(chromeAppPath).start();
    }
}
