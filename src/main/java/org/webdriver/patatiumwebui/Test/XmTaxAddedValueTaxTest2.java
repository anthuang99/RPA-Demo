package org.webdriver.patatiumwebui.Test;

import org.dom4j.DocumentException;
import org.testng.annotations.Test;
import org.webdriver.patatiumwebui.action.IndexXmTaxAction;
import org.webdriver.patatiumwebui.action.LoginXmTaxAction;
import org.webdriver.patatiumwebui.utils.ElementAction;
import org.webdriver.patatiumwebui.utils.TestBaseCase;
import org.webdriver.patatiumwebui.utils.XmlReadUtil;

import java.io.IOException;

public class XmTaxAddedValueTaxTest2 extends TestBaseCase {
	ElementAction action=new ElementAction();

	//数据驱动
	/*@DataProvider(name="longinData")
	public Object[][] loginData()
	{
		//读取登录用例测试数据
		String filePath="src/main/resources/data/JinShuiData.xls";
		//读取第一个sheet，第2行到第2行-第2到第4列之间的数据:用户数据
		return ExcelReadUtil.case_data_excel(0, 1, 1, 1, 3,filePath);
	}
	*/

	@Test(description="登录")
	public void login() throws IOException, DocumentException {
		doLogin();
		action.pagefoload(10);
		doIndexApplyTax();

		action.sleep(10);
	}

	/**
	 * 登录
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void doLogin() throws IOException, DocumentException {
		//		String userName,String password,String message
		//代替testng参数化的方法
		String BaseUrl= XmlReadUtil.getTestngParametersValue("testngXmTax.xml","BaseUrl");
		String userName = XmlReadUtil.getTestngParametersValue("testngXmTax.xml","UserName");
		String password = XmlReadUtil.getTestngParametersValue("testngXmTax.xml","PassWord");
//		String BaseUrl ="http://wsbsfwt1.xmtax.gov.cn:8001/";
//		userName = "";
		//调用方法:1.初始化并登录
//		LoginJinShuiAction jinshuiAction=new LoginJinShuiAction(BaseUrl,userName,password);
		LoginXmTaxAction jinshuiAction = new LoginXmTaxAction(BaseUrl,userName,password);
		action.sleep(1);
		//设置检查点
		//Assertion.VerityTextPresent(message,"验证是否出现预期的错误提示信息:"+message);
		//设置断言
		//Assertion.VerityError();

		jinshuiAction.doNothing();
	}

	/**
	 * 首页-申请报税
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void doIndexApplyTax() throws IOException, DocumentException {
		IndexXmTaxAction indexXmTaxAction = new IndexXmTaxAction();
//		indexXmTaxAction.applyAddedValueTax2();
	}
}