package org.webdriver.patatiumwebui.pageObject;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.webdriver.patatiumwebui.utils.BaseAction;
import org.webdriver.patatiumwebui.utils.Locator;
//厦门市网上税务局-工会经费_对象库类
public class LocalFundsXmTaxPage extends BaseAction {
//用于工程内运行查找对象库文件路径
private String path="org/webdriver/patatiumwebui/pageObjectConfig/UILibraryXmTax.xml";
 public   LocalFundsXmTaxPage() {
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
* table_content
* @return
* @throws IOException
*/
public Locator table_content() throws IOException
 {
   Locator locator=getLocator("table_content");
   return locator;
 }

/***
* table_right_content
* @return
* @throws IOException
*/
public Locator table_right_content() throws IOException
 {
   Locator locator=getLocator("table_right_content");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-工会经费申报
* @return
* @throws IOException
*/
public Locator 工会经费申报() throws IOException
 {
   Locator locator=getLocator("工会经费申报");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-下一步
* @return
* @throws IOException
*/
public Locator 下一步() throws IOException
 {
   Locator locator=getLocator("下一步");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-税款所属期
* @return
* @throws IOException
*/
public Locator 税款所属期() throws IOException
 {
   Locator locator=getLocator("税款所属期");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-添加申报记录
* @return
* @throws IOException
*/
public Locator 添加申报记录() throws IOException
 {
   Locator locator=getLocator("添加申报记录");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-下一步2
* @return
* @throws IOException
*/
public Locator 下一步2() throws IOException
 {
   Locator locator=getLocator("下一步2");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-确认提交
* @return
* @throws IOException
*/
public Locator 确认提交() throws IOException
 {
   Locator locator=getLocator("确认提交");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-应税项
* @return
* @throws IOException
*/
public Locator 应税项() throws IOException
 {
   Locator locator=getLocator("应税项");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-确认添加
* @return
* @throws IOException
*/
public Locator 确认添加() throws IOException
 {
   Locator locator=getLocator("确认添加");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-提交申请表
* @return
* @throws IOException
*/
public Locator 提交申请表() throws IOException
 {
   Locator locator=getLocator("提交申请表");
   return locator;
 }
}