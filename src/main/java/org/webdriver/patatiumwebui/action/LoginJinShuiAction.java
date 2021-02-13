package org.webdriver.patatiumwebui.action;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.webdriver.patatiumwebui.pageObject.JinShuiCheckPage;
import org.webdriver.patatiumwebui.pageObject.LoginJinShuiPage;
import org.webdriver.patatiumwebui.utils.ElementAction;
import org.webdriver.patatiumwebui.utils.Log;
import org.webdriver.patatiumwebui.utils.TestBaseCase;

import java.io.IOException;
import java.util.List;


public class LoginJinShuiAction extends TestBaseCase {
    private String Url;
    private String UserName;
    private String PassWord;

    private Log log=new Log(this.getClass());

    public LoginJinShuiAction(String Url, String UserName, String PassWord) throws IOException
    {
        this.Url = Url;
        this.UserName = UserName;
        this.PassWord = PassWord;

        LoginAction();
    }

    private void LoginAction() throws IOException
    {
        //此driver变量继承自TestBase变量
        LoginJinShuiPage loginPage=new LoginJinShuiPage();
        loginPage.open(Url);
        System.out.println(getWebDriver().getCurrentUrl());
        ElementAction action=new ElementAction();
//        action.click(loginPage.账户登录());
        action.sleep(5);
//        action.clear(loginPage.用户名输入框());
//        action.type(loginPage.用户名输入框(),UserName);
        //action.clear(loginPage.密码输入框());
        action.click(loginPage.密码输入框1()); //先点击这个之后下面才会显示真正的密码输入框
        action.sleep(1);
        action.type(loginPage.密码输入框(),PassWord);
        action.click(loginPage.登录按钮());
    }

    public void ListTableAction() throws IOException
    {
        //此driver变量继承自TestBase变量
        JinShuiCheckPage page=new JinShuiCheckPage();
        ElementAction action=new ElementAction();
        action.sleep(2);
        action.click_left(page.发票勾选页签());
        action.sleep(3);
        action.click(page.查询按钮());


        //-----读取列表table数据,并点击每一行的详情 --begin--------

        action.sleep(10);

        //遍历table.tr的数据
        try
        {
            WebElement table=action.findElement(page.查询结果table());
            List<WebElement> rows = table.findElements(By.tagName("tr"));
            int index =0;
            for(WebElement row:rows)
            {
                index ++;
                if(index ==1) {
                    log.info(row.getText());
                    //System.out.println(row.getText());
                    continue;//第一行标题
                }
                //WebElement td = row.findElements(By.tagName("td")).get(0);//索引从0开始 第一列是checkbox
                //WebElement td = row.findElements(By.tagName("td")).get(1);//索引从0开始 第一列是checkbox
                //System.out.print(row.findElements(By.tagName("td")).get(2).getText());
                StringBuffer sb = new StringBuffer();
                //String tdData =
                 sb.append(row.findElements(By.tagName("td")).get(1).getText() +" ");
                 sb.append(row.findElements(By.tagName("td")).get(2).getText() +" ");
                 sb.append(row.findElements(By.tagName("td")).get(3).getText() +" ");
                 sb.append(row.findElements(By.tagName("td")).get(4).getText() +" ");
                 sb.append(row.findElements(By.tagName("td")).get(5).getText() +" ");
                 sb.append(row.findElements(By.tagName("td")).get(6).getText() +" ");
                 sb.append(row.findElements(By.tagName("td")).get(7).getText() +" ");
                 sb.append(row.findElements(By.tagName("td")).get(8).getText() +" ");
                 sb.append(row.findElements(By.tagName("td")).get(9).getText() +" ");
                System.out.println(sb.toString());
                //log.info(sb.toString());
            }
        }
        catch (NoSuchElementException e)
        {
            log.error("找不到元素，ListTableAction失败:"+e.getMessage());
        }
        //-----读取列表table数据,并点击每一行的详情 --end--------
    }


}
