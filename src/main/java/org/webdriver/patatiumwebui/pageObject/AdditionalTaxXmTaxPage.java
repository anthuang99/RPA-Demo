package org.webdriver.patatiumwebui.pageObject;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.webdriver.patatiumwebui.utils.BaseAction;
import org.webdriver.patatiumwebui.utils.Locator;
//厦门市网上税务局-城建税及附加_对象库类
public class AdditionalTaxXmTaxPage extends BaseAction {
//用于工程内运行查找对象库文件路径
private String path="org/webdriver/patatiumwebui/pageObjectConfig/UILibraryXmTax.xml";
 public   AdditionalTaxXmTaxPage() {
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
* 我的办税大厅-iframe1-main-税款所属期止
* @return
* @throws IOException
*/
public Locator 税款所属期止() throws IOException
 {
   Locator locator=getLocator("税款所属期止");
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
* 我的办税大厅-iframe1-main-征收项目
* @return
* @throws IOException
*/
public Locator 征收项目() throws IOException
 {
   Locator locator=getLocator("征收项目");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-征收品目
* @return
* @throws IOException
*/
public Locator 征收品目() throws IOException
 {
   Locator locator=getLocator("征收品目");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-本月销售额合计数
* @return
* @throws IOException
*/
public Locator 本月销售额合计数() throws IOException
 {
   Locator locator=getLocator("本月销售额合计数");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-添加城建税下一步
* @return
* @throws IOException
*/
public Locator 添加城建税下一步() throws IOException
 {
   Locator locator=getLocator("添加城建税下一步");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-增值税输入
* @return
* @throws IOException
*/
public Locator 增值税输入() throws IOException
 {
   Locator locator=getLocator("增值税输入");
   return locator;
 }

/***
* 我的办税大厅-iframe1-main-增值税免抵税额
* @return
* @throws IOException
*/
public Locator 增值税免抵税额() throws IOException
 {
   Locator locator=getLocator("增值税免抵税额");
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
}