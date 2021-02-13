package org.webdriver.patatiumwebui.utils;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * 用 ThreadLocal 来存储全局变量方便程序访问,例如存储 webdriver,使得可支持多线程来访问 web
 */
public class RPAThreadLocal {
    private static ThreadLocal<WebDriver> webDriverThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<List<Exception>> noSuchElementExceptionsThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<List<Error>> errorsThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<List<String>> assertInfolListThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<List<String>> messageListThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Integer> errorIndexThreadLocal = new ThreadLocal<>();

    public static WebDriver getWebDriver() {
        return webDriverThreadLocal.get();
    }

    public static void setWebDriver(WebDriver webDriver) {
        webDriverThreadLocal.set(webDriver);
    }

    public static List<Exception> getNoSuchElementExceptions() {
        if (noSuchElementExceptionsThreadLocal.get() == null) {
            noSuchElementExceptionsThreadLocal.set(new ArrayList<>());
        }
        return noSuchElementExceptionsThreadLocal.get();
    }

    public static List<Error> getErrors() {
        if (errorsThreadLocal.get() == null) {
            errorsThreadLocal.set(new ArrayList<>());
        }
        return errorsThreadLocal.get();
    }

    public static List<String> getAssertInfolList() {
        if (assertInfolListThreadLocal.get() == null) {
            assertInfolListThreadLocal.set(new ArrayList<>());
        }
        return assertInfolListThreadLocal.get();
    }

    public static List<String> getMessageList() {
        if (messageListThreadLocal.get() == null) {
            messageListThreadLocal.set(new ArrayList<>());
        }
        return messageListThreadLocal.get();
    }

    public static Integer getErrorIndex() {
        if (errorIndexThreadLocal.get() == null) {
            errorIndexThreadLocal.set(0);
        }
        return errorIndexThreadLocal.get();
    }

    public static void setErrorIndex(int num) {
        errorIndexThreadLocal.set(num);
    }

    public static void addErrorIndex(int num) {
        errorIndexThreadLocal.set(getErrorIndex() + num);
    }
}
