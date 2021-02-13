package org.webdriver.patatiumwebui.pageObject;
import java.io.IOException;
import java.io.InputStream;
import org.webdriver.patatiumwebui.utils.BaseAction;
import org.webdriver.patatiumwebui.utils.Locator;
public class LoginYuanquPage extends BaseAction {
//用于工程内运行查找对象库文件路径
private String path="src/main/java/org/webdriver/patatiumwebui/pageObjectConfig/UILibraryYuanqu.xml";
 public   LoginYuanquPage() {
//工程内读取对象库文件
	setXmlObjectPath(path);
getLocatorMap();
}
/***
* 账户密码登录
* @return
* @throws IOException
*/
public Locator 账户密码登录() throws IOException
 {
   Locator locator=getLocator("账户密码登录");
   return locator;
 }

/***
* 账号输入框
* @return
* @throws IOException
*/
public Locator 账号输入框() throws IOException
 {
   Locator locator=getLocator("账号输入框");
   return locator;
 }

/***
* 密码输入框
* @return
* @throws IOException
*/
public Locator 密码输入框() throws IOException
 {
   Locator locator=getLocator("密码输入框");
   return locator;
 }

/***
* 验证码
* @return
* @throws IOException
*/
public Locator 验证码() throws IOException
 {
   Locator locator=getLocator("验证码");
   return locator;
 }

/***
* 验证码图片
* @return
* @throws IOException
*/
public Locator 验证码图片() throws IOException
 {
   Locator locator=getLocator("验证码图片");
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