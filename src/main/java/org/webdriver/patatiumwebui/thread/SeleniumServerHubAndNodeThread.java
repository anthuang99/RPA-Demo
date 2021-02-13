package org.webdriver.patatiumwebui.thread;

import com.sun.jna.Native;
import com.sun.jna.WString;
import org.apache.commons.lang3.StringUtils;
import org.webdriver.patatiumwebui.config.YmlConfig;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.utils.Log;
import org.webdriver.patatiumwebui.utils.ProcessBuilderUtil;
import org.webdriver.patatiumwebui.utils.WinPlatformUtil;
import org.webdriver.patatiumwebui.web.model.TaxParameter;
import org.webdriver.patatiumwebui.web.service.TaxParameterService;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 启动
 * 1.selenium-server-64bit.exe
 * 2.selenium-node-64bit.exe
 * 其中 selenium-server-64bit.exe 监听端口号 application.yml 配置 seleniumServerPort,
 * 其中 selenium-node-64bit.exe 监听端口号 application.yml 配置 seleniumNodePort,
 * 当存在时,先杀掉 node, 再杀掉 server
 */
public class SeleniumServerHubAndNodeThread extends Thread {
    private final Log log = new Log(getClass());
    /**
     * 调 netstat -aon 查询是否存在指定的进程
     */
    private String getPidByPort(String port) {
        return ThreadCommon.getPidByPort(port);
    }

    private boolean hasPort(String port) {
        return ThreadCommon.hasPort(port);
    }

    /**
     * 调 taskkill /F /PID {PID} 来杀掉进程
     */
    private void killProcessByPort(String port, int waitCount, int waitMilliSeconds) {
        int i = 0;
        String pid = getPidByPort(port);
        while (StringUtils.isNotEmpty(pid)) {
            List<String> array = new ArrayList<>();
            array.add("taskkill");
            array.add("/F");
            array.add("/PID");
            array.add(pid);
//            ProcessBuilderUtil.executeCommand("taskkill /F /PID {PID}".replace("{PID}", pid));
            ProcessBuilderUtil.executeCommand(array.toArray(new String[array.size()]));
            try {
                TimeUnit.MILLISECONDS.sleep(waitMilliSeconds);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
            i++;
            if (i > waitCount) {
                break;
            }
            pid = getPidByPort(port);
        }
    }

    /**
     * 启动进程
     */
    private void startProcess(String exeCommand, String port, int waitCount, int waitMilliSeconds) {
        boolean isStart = false;
        int i = 0;
        while(!hasPort(port)) {
            if (!isStart) {
                ProcessBuilderUtil.executeCommand(exeCommand);
                isStart = true;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(waitMilliSeconds);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
            if (i > waitCount) {
                break;
            }
        }
    }

    @Override
    public void run() {
        YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
        // 杀掉 selenium-node 进程
        killProcessByPort(ymlConfig.getSeleniumNodePort(), 10, 500);
        // 杀掉 selenium-server 进程
        killProcessByPort(ymlConfig.getSeleniumServerPort(), 10, 500);

        // 启动 selenium-server 进程
        startProcess(ymlConfig.getSeleniumServerExe(), ymlConfig.getSeleniumServerPort(), 10, 500);

        // 启动 selenium-node 进程
        TaxParameterService taxParameterService = (TaxParameterService) SpringUtil.getBean("taxParameterService");
        TaxParameter taxParameter = taxParameterService.getTaxParameter(TaxConstant.BROWSER_PATH);
        if (taxParameter != null) {
            if (taxParameter.getValue().indexOf("safari.exe") > -1) {
                startProcess(ymlConfig.getSeleniumNodeIEExe(), ymlConfig.getSeleniumNodePort(), 10, 500);
            } else if (taxParameter.getValue().indexOf("chrome.exe") > -1) {
                startProcess(ymlConfig.getSeleniumNodeChromeExe(), ymlConfig.getSeleniumNodePort(), 10, 500);
            } else if (taxParameter.getValue().indexOf("firefox.exe") > -1) {
                startProcess(ymlConfig.getSeleniumNodeFirefoxExe(), ymlConfig.getSeleniumNodePort(), 10, 500);
            }
        } else {
            WinPlatformUtil.User32 lib = (WinPlatformUtil.User32) Native.loadLibrary("User32", WinPlatformUtil.User32.class);
            System.out.println("Presenting Message Box ...");
            String message = "找不到注册表项:HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\IEXPLORE.EXE\n";
            message += "找不到注册表项:HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\chrome.exe\n";
            message += "找不到注册表项:HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\firefox.exe\n";
            message += "有可能没有安装IE,Chrome,Firefox浏览器!!!";
            lib.MessageBoxW(0, new WString(message), new WString("注意"), 0);
        }

        /*
        if (StringUtils.isNotEmpty(WinPlatformUtil.getIEAppPath())) {
            startProcess(ymlConfig.getSeleniumNodeIEExe(), ymlConfig.getSeleniumNodePort(), 10, 500);
        } else if (StringUtils.isNotEmpty(WinPlatformUtil.getChromeAppPath())) {
            startProcess(ymlConfig.getSeleniumNodeChromeExe(), ymlConfig.getSeleniumNodePort(), 10, 500);
        } else if (StringUtils.isNotEmpty(WinPlatformUtil.getFirefoxAppPath())) {
            startProcess(ymlConfig.getSeleniumNodeFirefoxExe(), ymlConfig.getSeleniumNodePort(), 10, 500);
        } else {
            WinPlatformUtil.User32 lib = (WinPlatformUtil.User32) Native.loadLibrary("User32", WinPlatformUtil.User32.class);
            System.out.println("Presenting Message Box ...");
            String message = "找不到注册表项:HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\IEXPLORE.EXE\n";
            message += "找不到注册表项:HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\chrome.exe\n";
            message += "找不到注册表项:HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\firefox.exe\n";
            message += "有可能没有安装IE,Chrome,Firefox浏览器!!!";
            lib.MessageBoxW(0, new WString(message), new WString("注意"), 0);
        }
        */
    }
}
