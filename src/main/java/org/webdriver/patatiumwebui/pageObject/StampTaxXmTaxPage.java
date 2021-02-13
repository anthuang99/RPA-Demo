package org.webdriver.patatiumwebui.pageObject;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.webdriver.patatiumwebui.utils.BaseAction;
import org.webdriver.patatiumwebui.utils.Locator;
//厦门市网上税务局-印花税_对象库类
public class StampTaxXmTaxPage extends BaseAction {
//用于工程内运行查找对象库文件路径
private String path="org/webdriver/patatiumwebui/pageObjectConfig/UILibraryXmTax.xml";
 public   StampTaxXmTaxPage() {
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
* 我的办税大厅-iframe1-main-submitBtn
* @return
* @throws IOException
*/
public Locator submitBtn() throws IOException
 {
   Locator locator=getLocator("submitBtn");
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
* 我的办税大厅-iframe1-main-应征凭证名称
* @return
* @throws IOException
*/
public Locator 应征凭证名称() throws IOException
 {
   Locator locator=getLocator("应征凭证名称");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-添加申报记录下一步
* @return
* @throws IOException
*/
public Locator 添加申报记录下一步() throws IOException
 {
   Locator locator=getLocator("添加申报记录下一步");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-计税金额或件数
* @return
* @throws IOException
*/
public Locator 计税金额或件数() throws IOException
 {
   Locator locator=getLocator("计税金额或件数");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-减免性质
* @return
* @throws IOException
*/
public Locator 减免性质() throws IOException
 {
   Locator locator=getLocator("减免性质");
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
* 我的办税大厅-iframe1-main-确认提交
* @return
* @throws IOException
*/
public Locator 确认提交() throws IOException
 {
   Locator locator=getLocator("确认提交");
   return locator;
 }
}