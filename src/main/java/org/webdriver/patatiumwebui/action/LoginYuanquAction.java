package org.webdriver.patatiumwebui.action;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.webdriver.patatiumwebui.pageObject.LoginYuanquPage;
import org.webdriver.patatiumwebui.utils.ElementAction;
import org.webdriver.patatiumwebui.utils.Log;
import org.webdriver.patatiumwebui.utils.TestBaseCase;

import java.io.IOException;
import java.util.List;


public class LoginYuanquAction extends TestBaseCase{
    private String Url;
    private String UserName;
    private String PassWord;

    private Log log=new Log(this.getClass());

    public LoginYuanquAction(String Url, String UserName, String PassWord) throws IOException
    {
        this.Url = Url;
        this.UserName = UserName;
        this.PassWord = PassWord;

        LoginAction();
    }

    private void LoginAction() throws IOException
    {
        //此driver变量继承自TestBase变量
        LoginYuanquPage loginPage=new LoginYuanquPage();
        loginPage.open(Url);
        System.out.println(getWebDriver().getCurrentUrl());
        ElementAction action=new ElementAction();

        action.sleep(2);
        //action.click(loginPage.账户密码登录());
        //action.sleep(2);

        action.click(loginPage.账号输入框());
        action.type(loginPage.账号输入框(),UserName);
        action.sleep(1);
        action.click(loginPage.密码输入框());
        action.type(loginPage.密码输入框(),PassWord);

        action.sleep(5);

        String js = "var messageStr = '提示信息';\n" +
                "var defaultStr = '输入框内默认值';\n" +
                "var content = window.prompt(messageStr, defaultStr);\n"+
                "document.getElementByName('yzm').value=content";
        action.executeJS(js);

        action.sleep(10);

        action.click(loginPage.登录按钮());
    }

}
