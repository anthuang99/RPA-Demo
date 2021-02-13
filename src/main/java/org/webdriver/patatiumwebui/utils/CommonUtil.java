package org.webdriver.patatiumwebui.utils;

import org.apache.commons.lang3.StringUtils;
import org.webdriver.patatiumwebui.config.YmlConfig;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

/**
 * 公共的方法
 * @author hongjinqiu 2018.10.18
 */
public class CommonUtil {
    /**
     * 是否是开发模式
     * @return
     */
    public static boolean isDev() {
        YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
        String isDev = ymlConfig.getIsDev();
        return StringUtils.isNotEmpty(isDev) && isDev.equals("true");
    }

    /**
     * 是否点击登录
     * @return
     */
    public static boolean isClickLogin() {
        YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
        String isClickLogin = ymlConfig.getIsClickLogin();
        return StringUtils.isNotEmpty(isClickLogin) && isClickLogin.equals("true");
    }
}
