package org.webdriver.patatiumwebui.pageObject;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.webdriver.patatiumwebui.utils.BaseAction;
import org.webdriver.patatiumwebui.utils.Locator;
//我的办事大厅-增值税及附加税费_对象库类
public class AddedValueTaxXmTaxPage extends BaseAction {
//用于工程内运行查找对象库文件路径
private String path="org/webdriver/patatiumwebui/pageObjectConfig/UILibraryXmTax.xml";
 public   AddedValueTaxXmTaxPage() {
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
* 我的办税大厅-mainFrame
* @return
* @throws IOException
*/
public Locator mainFrame() throws IOException
 {
   Locator locator=getLocator("mainFrame");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame
* @return
* @throws IOException
*/
public Locator leftFrame() throws IOException
 {
   Locator locator=getLocator("leftFrame");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-rightFrame
* @return
* @throws IOException
*/
public Locator rightFrame() throws IOException
 {
   Locator locator=getLocator("rightFrame");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-企业会计准则（一般企业）财务报表资产负债表
* @return
* @throws IOException
*/
public Locator balanceSheetMenu() throws IOException
 {
   Locator locator=getLocator("balanceSheetMenu");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-(2018年企业会计准则财务报表-第一季)
* @return
* @throws IOException
*/
public Locator 第一季() throws IOException
 {
   Locator locator=getLocator("第一季");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-(2018年企业会计准则财务报表-第一季)-(企业会计准则（一般企业）财务报表信息查询)
* @return
* @throws IOException
*/
public Locator 第一季_财务报表信息查询() throws IOException
 {
   Locator locator=getLocator("第一季_财务报表信息查询");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-rightFrame-货币资金_期末余额
* @return
* @throws IOException
*/
public Locator 货币资金_期末余额() throws IOException
 {
   Locator locator=getLocator("货币资金_期末余额");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-第一个菜单
* @return
* @throws IOException
*/
public Locator 第一个菜单() throws IOException
 {
   Locator locator=getLocator("第一个菜单");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-第一个主菜单
* @return
* @throws IOException
*/
public Locator 第一个主菜单() throws IOException
 {
   Locator locator=getLocator("第一个主菜单");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-增值税纳税申报表_小规模纳税人适用_附列资料
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表_小规模纳税人适用_附列资料() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表_小规模纳税人适用_附列资料");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-rightFrame-增值税纳税申报表_小规模纳税人适用_附列资料_保存
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表_小规模纳税人适用_附列资料_保存() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表_小规模纳税人适用_附列资料_保存");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-增值税纳税申报表_小规模纳税人适用
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表_小规模纳税人适用() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表_小规模纳税人适用");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-rightFrame-增值税纳税申报表_小规模纳税人适用_所属时期
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表_小规模纳税人适用_所属时期() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表_小规模纳税人适用_所属时期");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-rightFrame-增值税纳税申报表_小规模纳税人适用_保存
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表_小规模纳税人适用_保存() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表_小规模纳税人适用_保存");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-rightFrame-增值税纳税申报表_小规模纳税人适用_是否采用_主附税一体化申报缴税功能_是
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表_小规模纳税人适用_是否采用_主附税一体化申报缴税功能_是() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表_小规模纳税人适用_是否采用_主附税一体化申报缴税功能_是");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-rightFrame-增值税纳税申报表_小规模纳税人适用_是否采用_主附税一体化申报缴税功能_否
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表_小规模纳税人适用_是否采用_主附税一体化申报缴税功能_否() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表_小规模纳税人适用_是否采用_主附税一体化申报缴税功能_否");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-增值税减免税申报明细表_小规模纳税人适用
* @return
* @throws IOException
*/
public Locator 增值税减免税申报明细表_小规模纳税人适用() throws IOException
 {
   Locator locator=getLocator("增值税减免税申报明细表_小规模纳税人适用");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-增值税减免税申报明细表_小规模纳税人适用_保存
* @return
* @throws IOException
*/
public Locator 增值税减免税申报明细表_小规模纳税人适用_保存() throws IOException
 {
   Locator locator=getLocator("增值税减免税申报明细表_小规模纳税人适用_保存");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-增值税申报表_一般纳税人适用
* @return
* @throws IOException
*/
public Locator 增值税申报表_一般纳税人适用() throws IOException
 {
   Locator locator=getLocator("增值税申报表_一般纳税人适用");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-增值税申报表_一般纳税人适用_是否采用_主附税一体化申报缴税功能_是
* @return
* @throws IOException
*/
public Locator 增值税申报表_一般纳税人适用_是否采用_主附税一体化申报缴税功能_是() throws IOException
 {
   Locator locator=getLocator("增值税申报表_一般纳税人适用_是否采用_主附税一体化申报缴税功能_是");
   return locator;
 }

/***
* 增值税申报表_一般纳税人适用_保存
* @return
* @throws IOException
*/
public Locator 增值税申报表_一般纳税人适用_保存() throws IOException
 {
   Locator locator=getLocator("增值税申报表_一般纳税人适用_保存");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-rightFrame-增值税申报表_一般纳税人适用_所属时期
* @return
* @throws IOException
*/
public Locator 增值税申报表_一般纳税人适用_所属时期() throws IOException
 {
   Locator locator=getLocator("增值税申报表_一般纳税人适用_所属时期");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-增值税纳税申报表附列资料_一
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表附列资料_一() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表附列资料_一");
   return locator;
 }

/***
* 增值税纳税申报表附列资料_一_保存
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表附列资料_一_保存() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表附列资料_一_保存");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-增值税纳税申报表附列资料_二
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表附列资料_二() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表附列资料_二");
   return locator;
 }

/***
* 增值税纳税申报表附列资料_二_保存
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表附列资料_二_保存() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表附列资料_二_保存");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-增值税纳税申报表附列资料_三
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表附列资料_三() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表附列资料_三");
   return locator;
 }

/***
* 增值税纳税申报表附列资料_三_保存
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表附列资料_三_保存() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表附列资料_三_保存");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-增值税纳税申报表附列资料_四
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表附列资料_四() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表附列资料_四");
   return locator;
 }

/***
* 增值税纳税申报表附列资料_四_保存
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表附列资料_四_保存() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表附列资料_四_保存");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-增值税纳税申报表附列资料_五
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表附列资料_五() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表附列资料_五");
   return locator;
 }

/***
* 增值税纳税申报表附列资料_五_保存
* @return
* @throws IOException
*/
public Locator 增值税纳税申报表附列资料_五_保存() throws IOException
 {
   Locator locator=getLocator("增值税纳税申报表附列资料_五_保存");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-增值税减免税申报明细表
* @return
* @throws IOException
*/
public Locator 增值税减免税申报明细表() throws IOException
 {
   Locator locator=getLocator("增值税减免税申报明细表");
   return locator;
 }

/***
* 增值税减免税申报明细表_保存
* @return
* @throws IOException
*/
public Locator 增值税减免税申报明细表_保存() throws IOException
 {
   Locator locator=getLocator("增值税减免税申报明细表_保存");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-农产品收购发票或普通发票明细表
* @return
* @throws IOException
*/
public Locator 农产品收购发票或普通发票明细表() throws IOException
 {
   Locator locator=getLocator("农产品收购发票或普通发票明细表");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-农产品收购发票或普通发票明细表_保存
* @return
* @throws IOException
*/
public Locator 农产品收购发票或普通发票明细表_保存() throws IOException
 {
   Locator locator=getLocator("农产品收购发票或普通发票明细表_保存");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-营改增税负分析测算明细表
* @return
* @throws IOException
*/
public Locator 营改增税负分析测算明细表() throws IOException
 {
   Locator locator=getLocator("营改增税负分析测算明细表");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-营改增税负分析测算明细表_应税项目代码及名称
* @return
* @throws IOException
*/
public Locator 应税项目代码及名称() throws IOException
 {
   Locator locator=getLocator("应税项目代码及名称");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-营改增税负分析测算明细表_增值税税率或征收率
* @return
* @throws IOException
*/
public Locator 增值税税率或征收率() throws IOException
 {
   Locator locator=getLocator("增值税税率或征收率");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-leftFrame-营改增税负分析测算明细表_营业税税率
* @return
* @throws IOException
*/
public Locator 营业税税率() throws IOException
 {
   Locator locator=getLocator("营业税税率");
   return locator;
 }

/***
* 正式申报
* @return
* @throws IOException
*/
public Locator 正式申报() throws IOException
 {
   Locator locator=getLocator("正式申报");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-rightFrame-查询范围_开始年
* @return
* @throws IOException
*/
public Locator 查询范围_开始年() throws IOException
 {
   Locator locator=getLocator("查询范围_开始年");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-rightFrame-查询范围_开始月
* @return
* @throws IOException
*/
public Locator 查询范围_开始月() throws IOException
 {
   Locator locator=getLocator("查询范围_开始月");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-rightFrame-查询范围_结束年
* @return
* @throws IOException
*/
public Locator 查询范围_结束年() throws IOException
 {
   Locator locator=getLocator("查询范围_结束年");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-rightFrame-查询范围_结束月
* @return
* @throws IOException
*/
public Locator 查询范围_结束月() throws IOException
 {
   Locator locator=getLocator("查询范围_结束月");
   return locator;
 }

/***
* 我的办税大厅-mainFrame-rightFrame-查找按钮
* @return
* @throws IOException
*/
public Locator 查找按钮() throws IOException
 {
   Locator locator=getLocator("查找按钮");
   return locator;
 }
}