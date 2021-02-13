package org.webdriver.patatiumwebui.utils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import org.apache.commons.lang3.StringUtils;

import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;

/**
 * 调 jna, jna-platform 的类,
 */
public class WinPlatformUtil {
    private static Log log = new Log(WinPlatformUtil.class);
    // https://github.com/twall/jna#readme
    //  you need 2 jars : jna-3.5.1.jar and platform-3.5.1.jar

    public interface User32 extends Library {
        int MessageBoxW(int something, WString text, WString caption, int flags);
    }

    /**
     * 取得谷歌浏览器安装目录
     * 注册表项:HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\App Paths\chrome.exe
     *
     * @return
     */
    public static String getChromeAppPath() {
        try {
            return Advapi32Util.registryGetStringValue
                    (HKEY_LOCAL_MACHINE,
                            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\chrome.exe", "");
        } catch (Win32Exception e) {
            log.error(e.getMessage(), e);
            /*
            User32 lib = (User32) Native.loadLibrary("User32", User32.class);
            System.out.println("Presenting Message Box ...");
            String message = "找不到注册表项:HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\chrome.exe\n";
            message += "系统即将退出:有可能没有安装谷歌浏览器!!!";
            lib.MessageBoxW(0, new WString(message), new WString("注意"), 0);
            */
            return null;
        }
    }

    /**
     * 取得 IE 浏览器的安装路径
     * 注册表项:HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\App Paths\IEXPLORE.EXE
     *
     * @return
     */
    public static String getIEAppPath() {
        try {
            return Advapi32Util.registryGetStringValue
                    (HKEY_LOCAL_MACHINE,
                            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\IEXPLORE.EXE", "");
        } catch (Win32Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 取得 Firefox 浏览器的安装路径
     * 注册表项:HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\App Paths\firefox.exe
     *
     * @return
     */
    public static String getFirefoxAppPath() {
        try {
            return Advapi32Util.registryGetStringValue
                    (HKEY_LOCAL_MACHINE,
                            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\firefox.exe", "");
        } catch (Win32Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 按 Chrome,Firefox,IE 的顺序,获取浏览器路径,
     * @return
     */
    public static String getBrowserPathIfPresent() {
        String iePath = getIEAppPath();
        /*{
            if (true) {
                return iePath;
            }
        }*/
        String chromePath = getChromeAppPath();
        String firefoxPath = getFirefoxAppPath();
        if (StringUtils.isNotEmpty(chromePath)) {
            return chromePath;
        } else if (StringUtils.isNotEmpty(firefoxPath)) {
            return firefoxPath;
        } else if (StringUtils.isNotEmpty(iePath)) {
            return iePath;
        }
        return null;
    }

    /*
    public static void main(String[] args) {
        {
            String path = getChromeAppPath();
            System.out.println("path is:" + path);
        }
        {
            String path = getFirefoxAppPath();
            System.out.println("path is:" + path);
        }
        {
            String path = getIEAppPath();
            System.out.println("path is:" + path);
        }
    }
    */
}
