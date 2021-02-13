package org.webdriver.patatiumwebui.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 命令行执行命令的类
 */
public class ProcessBuilderUtil {
    private static final Log log = new Log(ProcessBuilderUtil.class);

    /**
     * 执行命令并返回结果
     */
    public static String getCommandResult(String... command) {
        Process p = null;
        try {
            p = new ProcessBuilder(command).start();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        byte[] b = new byte[1024];
        int readbytes = -1;
        StringBuilder sb = new StringBuilder();
        //读取进程输出值
        //在JAVA IO中,输入输出是针对JVM而言,读写是针对外部数据源而言
        InputStream in = p.getInputStream();
        try {
            while ((readbytes = in.read(b)) != -1) {
                sb.append(new String(b, 0, readbytes));
            }
        } catch (IOException e1) {
        } finally {
            try {
                in.close();
            } catch (IOException e2) {
            }
        }
        return sb.toString();
    }

    /**
     * 执行命令
     * @param command
     */
    public static void executeCommand(String... command) {
        try {
            new ProcessBuilder(command).start();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        /*String command = "netstat";
        String result = ProcessBuilderUtil.getCommandResult(command, "-aon");
        for (String line: result.split("\r\n")) {
            if (line.indexOf("0.0.0.0:{port}".replace("{port}", "135")) > 0) {
                if (line.indexOf("LISTENING") > 0) {
                    System.out.println("listen 8085");
                }
            }
        }
        System.out.println("not listen");*/
        {
            List<String> array = new ArrayList<>();
            // cmd /c start chrome http://hw1287789687.iteye.com/blog/2153709
//            array.add("start");
            array.add("C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe");
            array.add("http://localhost:{port}/".replace("{port}", "8085"));
            String[] arrayStrLi = new String[array.size()];
            ProcessBuilderUtil.executeCommand(array.toArray(arrayStrLi));
        }
    }
}
