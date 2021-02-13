package org.webdriver.patatiumwebui.pageObject;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.webdriver.patatiumwebui.utils.BaseAction;
import org.webdriver.patatiumwebui.utils.Locator;
//厦门税务首页_对象库类
public class IndexXmTaxPage extends BaseAction {
//用于工程内运行查找对象库文件路径
private String path="org/webdriver/patatiumwebui/pageObjectConfig/UILibraryXmTax.xml";
 public   IndexXmTaxPage() {
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
* 首页
* @return
* @throws IOException
*/
public Locator indexImg() throws IOException
 {
   Locator locator=getLocator("indexImg");
   return locator;
 }

/***
* 申请报税
* @return
* @throws IOException
*/
public Locator applyTax() throws IOException
 {
   Locator locator=getLocator("applyTax");
   return locator;
 }

/***
* 增值税及附加税费
* @return
* @throws IOException
*/
public Locator addedValueTax() throws IOException
 {
   Locator locator=getLocator("addedValueTax");
   return locator;
 }

/***
* 企业所得税
* @return
* @throws IOException
*/
public Locator enterpriseIncomeTax() throws IOException
 {
   Locator locator=getLocator("enterpriseIncomeTax");
   return locator;
 }

/***
* 个人所得税
* @return
* @throws IOException
*/
public Locator individualIncomeTax() throws IOException
 {
   Locator locator=getLocator("individualIncomeTax");
   return locator;
 }

/***
* 印花税
* @return
* @throws IOException
*/
public Locator stampTax() throws IOException
 {
   Locator locator=getLocator("stampTax");
   return locator;
 }

/***
* 城建税及附加
* @return
* @throws IOException
*/
public Locator additionalTax() throws IOException
 {
   Locator locator=getLocator("additionalTax");
   return locator;
 }

/***
* 文化事业建设费
* @return
* @throws IOException
*/
public Locator culturalConstructionCosts() throws IOException
 {
   Locator locator=getLocator("culturalConstructionCosts");
   return locator;
 }

/***
* 地方各项基金费(在工会经费菜单下)
* @return
* @throws IOException
*/
public Locator localFunds() throws IOException
 {
   Locator locator=getLocator("localFunds");
   return locator;
 }

/***
* 消费税
* @return
* @throws IOException
*/
public Locator consumptionTax() throws IOException
 {
   Locator locator=getLocator("consumptionTax");
   return locator;
 }

/***
* 社保
* @return
* @throws IOException
*/
public Locator socialSecurity() throws IOException
 {
   Locator locator=getLocator("socialSecurity");
   return locator;
 }

/***
* 未申报
* @return
* @throws IOException
*/
public Locator 未申报() throws IOException
 {
   Locator locator=getLocator("未申报");
   return locator;
 }

/***
* 已申报
* @return
* @throws IOException
*/
public Locator 已申报() throws IOException
 {
   Locator locator=getLocator("已申报");
   return locator;
 }

/***
* iframe1
* @return
* @throws IOException
*/
public Locator iframe1() throws IOException
 {
   Locator locator=getLocator("iframe1");
   return locator;
 }

/***
* iframe1_日期框
* @return
* @throws IOException
*/
public Locator iframe1_日期框() throws IOException
 {
   Locator locator=getLocator("iframe1_日期框");
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

/***
* table_right_content2
* @return
* @throws IOException
*/
public Locator table_right_content2() throws IOException
 {
   Locator locator=getLocator("table_right_content2");
   return locator;
 }

/***
* 公众服务
* @return
* @throws IOException
*/
public Locator 公众服务() throws IOException
 {
   Locator locator=getLocator("公众服务");
   return locator;
 }

/***
* 办税日历
* @return
* @throws IOException
*/
public Locator 办税日历() throws IOException
 {
   Locator locator=getLocator("办税日历");
   return locator;
 }

/***
* 本月纳税申报截止日期
* @return
* @throws IOException
*/
public Locator 本月纳税申报截止日期() throws IOException
 {
   Locator locator=getLocator("本月纳税申报截止日期");
   return locator;
 }

/***
* 我的待办表身内容
* @return
* @throws IOException
*/
public Locator 我的待办表身内容() throws IOException
 {
   Locator locator=getLocator("我的待办表身内容");
   return locator;
 }

/***
* 办税中心_税费申报及缴纳
* @return
* @throws IOException
*/
public Locator 办税中心_税费申报及缴纳() throws IOException
 {
   Locator locator=getLocator("办税中心_税费申报及缴纳");
   return locator;
 }

/***
* 办税中心_税费申报及缴纳_文字
* @return
* @throws IOException
*/
public Locator 办税中心_税费申报及缴纳_文字() throws IOException
 {
   Locator locator=getLocator("办税中心_税费申报及缴纳_文字");
   return locator;
 }

/***
* 增值税及附加税费申报
* @return
* @throws IOException
*/
public Locator 增值税及附加税费申报() throws IOException
 {
   Locator locator=getLocator("增值税及附加税费申报");
   return locator;
 }

/***
* 增值税申报
* @return
* @throws IOException
*/
public Locator 增值税申报() throws IOException
 {
   Locator locator=getLocator("增值税申报");
   return locator;
 }

/***
* 其他申报
* @return
* @throws IOException
*/
public Locator 其他申报() throws IOException
 {
   Locator locator=getLocator("其他申报");
   return locator;
 }

/***
* 综合申报
* @return
* @throws IOException
*/
public Locator 综合申报() throws IOException
 {
   Locator locator=getLocator("综合申报");
   return locator;
 }

/***
* 工会经费申报
* @return
* @throws IOException
*/
public Locator 工会经费申报() throws IOException
 {
   Locator locator=getLocator("工会经费申报");
   return locator;
 }

/***
* 城建税及附加申报
* @return
* @throws IOException
*/
public Locator 城建税及附加申报() throws IOException
 {
   Locator locator=getLocator("城建税及附加申报");
   return locator;
 }

/***
* 印花税申报
* @return
* @throws IOException
*/
public Locator 印花税申报() throws IOException
 {
   Locator locator=getLocator("印花税申报");
   return locator;
 }

/***
* 申报信息查询
* @return
* @throws IOException
*/
public Locator 申报信息查询() throws IOException
 {
   Locator locator=getLocator("申报信息查询");
   return locator;
 }

/***
* 纳税人信息表格
* @return
* @throws IOException
*/
public Locator 纳税人信息表格() throws IOException
 {
   Locator locator=getLocator("纳税人信息表格");
   return locator;
 }

/***
* 纳税人税费种认定信息
* @return
* @throws IOException
*/
public Locator 纳税人税费种认定信息() throws IOException
 {
   Locator locator=getLocator("纳税人税费种认定信息");
   return locator;
 }
}