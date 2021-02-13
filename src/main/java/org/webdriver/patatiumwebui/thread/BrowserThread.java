package org.webdriver.patatiumwebui.thread;

import org.webdriver.patatiumwebui.config.ServerConfig;
import org.webdriver.patatiumwebui.utils.Log;
import org.webdriver.patatiumwebui.utils.ProcessBuilderUtil;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 读取注册表配置,启动 chrome 浏览器线程
 */
public class BrowserThread extends Thread {
    private final String browserPath;

    public BrowserThread(String browserPath) {
        this.browserPath = browserPath;
    }

    /*
    命令
     netstat -aon|find "0.0.0.0:8085"|find "LISTENING"
     返回有值,表示 spring boot 中的tomcat 启动成功,
    */
    private boolean hasPort() {
        ServerConfig serverConfig = (ServerConfig) SpringUtil.getBean("serverConfig");
        return ThreadCommon.hasPort(serverConfig.getPort());
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (hasPort()) {
                    ServerConfig serverConfig = (ServerConfig) SpringUtil.getBean("serverConfig");
                    List<String> array = new ArrayList<>();
                    // C:\Users\Administrator\AppData\Local\Google\Chrome\Application\chrome.exe http://localhost:8085/
                    array.add(browserPath);
                    array.add("http://localhost:{port}/".replace("{port}", serverConfig.getPort()));
                    String[] arrayStrLi = new String[array.size()];
                    ProcessBuilderUtil.executeCommand(array.toArray(arrayStrLi));
                    break;
                } else {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log log = new Log(getClass());
            log.error(e.getMessage(), e);
        }
    }
}
