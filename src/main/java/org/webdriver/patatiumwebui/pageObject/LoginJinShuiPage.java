package org.webdriver.patatiumwebui.pageObject;

import org.webdriver.patatiumwebui.utils.BaseAction;
import org.webdriver.patatiumwebui.utils.Locator;

import java.io.IOException;

public class LoginJinShuiPage extends BaseAction {
//用于工程内运行查找对象库文件路径
private String path="src/main/java/org/webdriver/patatiumwebui/pageObjectConfig/UILibraryJinShui.xml";
 public   LoginJinShuiPage() {
//工程内读取对象库文件
	setXmlObjectPath(path);
getLocatorMap();
}
/***
* 密码
* @return
* @throws IOException
*/
public Locator 密码输入框1() throws IOException
 {
   Locator locator=getLocator("密码输入框1");
   return locator;
 }

/***
* 密码
* @return
* @throws IOException
*/
public Locator 密码输入框() throws IOException
 {
   Locator locator=getLocator("密码输入框");
   return locator;
 }

/***
* 登录
* @return
* @throws IOException
*/
public Locator 登录按钮() throws IOException
 {
   Locator locator=getLocator("登录按钮");
   return locator;
 }
}