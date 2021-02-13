package org.webdriver.patatiumwebui.common;

/**
 * 全局的变量,
 * 点击首页的启动运行,isRunning = true,
 * 再次点击启动运行时,提示,正在运行,不能重复运行
 * 某次运行结束后,isRunning = false,
 */
public class RunningHolder {
    public static volatile boolean IS_RUNNING = false;
}
