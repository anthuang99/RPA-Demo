package org.webdriver.patatiumwebui.thread;

import org.apache.commons.lang3.StringUtils;
import org.webdriver.patatiumwebui.config.ServerConfig;
import org.webdriver.patatiumwebui.utils.ProcessBuilderUtil;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

/**
 * 公用方法类
 */
public class ThreadCommon {
    /**
     * 调 netstat -aon 查询是否存在指定的进程
     * @param port
     * @return
     */
    public static String getPidByPort(String port) {
        String result = ProcessBuilderUtil.getCommandResult("netstat", "-aon");
        for (String line: result.split("\r\n")) {
            if (line.indexOf("0.0.0.0:{port}".replace("{port}", port)) > 0) {
                if (line.indexOf("LISTENING") > 0) {
                    String[] lineLi = line.split("\\s+");
                    return lineLi[lineLi.length - 1];
                }
            }
        }
        return null;
    }

    /**
     * 对应端口是否存在
     */
    public static boolean hasPort(String port) {
        return StringUtils.isNotEmpty(getPidByPort(port));
    }
}
