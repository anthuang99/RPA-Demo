package org.webdriver.patatiumwebui.Test;

import org.dom4j.DocumentException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.webdriver.patatiumwebui.action.LoginYuanquAction;
import org.webdriver.patatiumwebui.utils.*;

import java.io.IOException;

public class YuanquTest extends TestBaseCase{
	ElementAction action=new ElementAction();

	//数据驱动
	@DataProvider(name="longinData")
	public Object[][] loginData()
	{
		//读取登录用例测试数据
		String filePath="src/main/resources/data/YuanquData.xls";
		//读取第一个sheet，第2行到第2行-第2到第4列之间的数据:用户数据
		return ExcelReadUtil.case_data_excel(0, 1, 1, 1, 3,filePath);
	}

	@Test(description="登录",dataProvider = "longinData")
	public void login (String userName,String password,String message) throws IOException, DocumentException {
		//代替testng参数化的方法
		String BaseUrl= XmlReadUtil.getTestngParametersValue("testngYuanqu.xml","BaseUrl");
		//String BaseUrl ="https://fpdk.xm-n-tax.gov.cn/";
		//调用方法:1.初始化并登录
		LoginYuanquAction yuanquAction=new LoginYuanquAction(BaseUrl,userName,password);
		action.sleep(2);
		//设置检查点
		Assertion.VerityTextPresent(message,"验证是否出现预期的错误提示信息:"+message);
		//设置断言
		Assertion.VerityError();


		action.sleep(10);

	}


}