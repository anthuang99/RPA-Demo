package org.webdriver.patatiumwebui.pageObject;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.webdriver.patatiumwebui.utils.BaseAction;
import org.webdriver.patatiumwebui.utils.Locator;
//厦门税务登录页面_对象库类
public class LoginXmTaxPage extends BaseAction {
//用于工程内运行查找对象库文件路径
private String path="org/webdriver/patatiumwebui/pageObjectConfig/UILibraryXmTax.xml";
 public   LoginXmTaxPage() {
//工程内读取对象库文件
	 InputStream in =null;
 try {
 	in = this.getClass().getClassLoader().getResourceAsStream(path);
 	setXmlObjectPathForLocator(in);
 	getLocatorMap();
 } finally {
 	if (in != null) {
 		IOUtils.closeQuietly(in);
 	}
 }

}
/***
* 用户名
* @return
* @throws IOException
*/
public Locator 用户名() throws IOException
 {
   Locator locator=getLocator("用户名");
   return locator;
 }

/***
* 密码2
* @return
* @throws IOException
*/
public Locator 密码2() throws IOException
 {
   Locator locator=getLocator("密码2");
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
* 登录
* @return
* @throws IOException
*/
public Locator 登录() throws IOException
 {
   Locator locator=getLocator("登录");
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
* 浏览器版本提示确定按钮
* @return
* @throws IOException
*/
public Locator 浏览器版本提示确定按钮() throws IOException
 {
   Locator locator=getLocator("浏览器版本提示确定按钮");
   return locator;
 }

/***
* 登录链接
* @return
* @throws IOException
*/
public Locator 登录链接() throws IOException
 {
   Locator locator=getLocator("登录链接");
   return locator;
 }

/***
* 验证码图片_拖动
* @return
* @throws IOException
*/
public Locator 验证码图片_拖动() throws IOException
 {
   Locator locator=getLocator("验证码图片_拖动");
   return locator;
 }

/***
* 刷新图片
* @return
* @throws IOException
*/
public Locator 刷新图片() throws IOException
 {
   Locator locator=getLocator("刷新图片");
   return locator;
 }
}