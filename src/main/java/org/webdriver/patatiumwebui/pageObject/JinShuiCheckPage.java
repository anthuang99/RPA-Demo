package org.webdriver.patatiumwebui.pageObject;

import org.webdriver.patatiumwebui.utils.BaseAction;
import org.webdriver.patatiumwebui.utils.Locator;

import java.io.IOException;

public class JinShuiCheckPage extends BaseAction {
//用于工程内运行查找对象库文件路径
private String path="src/main/java/org/webdriver/patatiumwebui/pageObjectConfig/UILibraryJinShui.xml";
 public   JinShuiCheckPage() {
//工程内读取对象库文件
	setXmlObjectPath(path);
getLocatorMap();
}
/***
* 发票勾选页签
* @return
* @throws IOException
*/
public Locator 发票勾选页签() throws IOException
 {
   Locator locator=getLocator("发票勾选页签");
   return locator;
 }

/***
* 开始日期
* @return
* @throws IOException
*/
public Locator 开始日期输入框() throws IOException
 {
   Locator locator=getLocator("开始日期输入框");
   return locator;
 }

/***
* 结束日期
* @return
* @throws IOException
*/
public Locator 结束日期输入框() throws IOException
 {
   Locator locator=getLocator("结束日期输入框");
   return locator;
 }

/***
* 未认证勾选框
* @return
* @throws IOException
*/
public Locator 未认证勾选框() throws IOException
 {
   Locator locator=getLocator("未认证勾选框");
   return locator;
 }

/***
* 已认证勾选框
* @return
* @throws IOException
*/
public Locator 已认证勾选框() throws IOException
 {
   Locator locator=getLocator("已认证勾选框");
   return locator;
 }

/***
* 查询按钮
* @return
* @throws IOException
*/
public Locator 查询按钮() throws IOException
 {
   Locator locator=getLocator("查询按钮");
   return locator;
 }

/***
* 查询结果table
* @return
* @throws IOException
*/
public Locator 查询结果table() throws IOException
 {
   Locator locator=getLocator("查询结果table");
   return locator;
 }

/***
* 开始日期
* @return
* @throws IOException
*/
public Locator 下一页() throws IOException
 {
   Locator locator=getLocator("下一页");
   return locator;
 }

/***
* 保存按钮
* @return
* @throws IOException
*/
public Locator 保存按钮() throws IOException
 {
   Locator locator=getLocator("保存按钮");
   return locator;
 }
}