package org.webdriver.patatiumwebui.Test;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;
import org.webdriver.patatiumwebui.action.IndexXmTaxAction;
import org.webdriver.patatiumwebui.action.LoginXmTaxAction;
import org.webdriver.patatiumwebui.config.YmlConfig;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.exception.ApplyTaxFailException;
import org.webdriver.patatiumwebui.exception.RPAException;
import org.webdriver.patatiumwebui.httprequest.ZhangYiDaRequest;
import org.webdriver.patatiumwebui.httprequest.vo.CorpTaxQueueVO;
import org.webdriver.patatiumwebui.httprequest.vo.CorpTaxVO;
import org.webdriver.patatiumwebui.httprequest.vo.WriteBackTaxResultVO;
import org.webdriver.patatiumwebui.utils.ElementAction;
import org.webdriver.patatiumwebui.utils.Log;
import org.webdriver.patatiumwebui.utils.TaxUtil;
import org.webdriver.patatiumwebui.utils.TestBaseCase;
import org.webdriver.patatiumwebui.web.model.CorpSetting;
import org.webdriver.patatiumwebui.web.model.TaxDeclareProgress;
import org.webdriver.patatiumwebui.web.service.CorpSettingService;
import org.webdriver.patatiumwebui.web.service.TaxDeclareProgressService;
import org.webdriver.patatiumwebui.web.service.TaxExpireDateSettingService;
import org.webdriver.patatiumwebui.web.service.TaxParameterService;
import org.webdriver.patatiumwebui.web.util.DateUtil;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class XmTaxTest extends TestBaseCase {
//    private final Log log = new Log(this.getClass());


//    private final ElementAction action = new ElementAction();

    //数据驱动
    /*@DataProvider(name="loginData")
    public Object[][] loginData()
	{
		//读取登录用例测试数据
		String filePath="src/main/resources/data/JinShuiData.xls";
		//读取第一个sheet，第2行到第2行-第2到第4列之间的数据:用户数据
		return ExcelReadUtil.case_data_excel(0, 1, 1, 1, 3,filePath);
	}
	*/
    /*
    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        try {
            String userName = XmlReadUtil.getTestngParametersValue("testngXmTax.xml", "UserName");
            String password = XmlReadUtil.getTestngParametersValue("testngXmTax.xml", "PassWord");

            List<Object[]> list = new ArrayList<>();
            Object[] objects = new Object[]{
                    userName, password
            };
            list.add(objects);
            {
                list.add(new Object[]{
                    "91350200MA31JMWX0Y", "sbcw1101"
                });
                *//*
                list.add(new Object[]{
                    "91350206089924081L", "xm123321"
                });
                *//*
            }
            Object[][] array = new Object[list.size()][objects.length];
            list.toArray(array);
            return array;
        } catch (Exception e) {
            throw new RPAException(e);
        }
    }
    */
    @Test(description = "一键零申报")
    public void run(CorpTaxQueueVO corpTaxQueueVO) {
        Log log = new Log(this.getClass());
        try {
            int pageSize = 20;

            YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
            // 全部取完,再一次性运行,避免报税后,企业被更新,再次请求接口,记录可能变少,pageNo 不变,导致数据不全
            Map<String, Object> taxMapParam = getTaxMapParam(corpTaxQueueVO, pageSize);
            JSONArray array = (JSONArray) taxMapParam.get("array");
            int taxDeclareProgressId = (int) taxMapParam.get("taxDeclareProgressId");
            if (array.length() > 0) {
                    runMultipleThread(array, pageSize, taxDeclareProgressId);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RPAException(e);
        }
    }

    /**
     * 根据队列中的值 CorpTaxQueueVO, 决定
     * 1.调用接口,取回对应的企业,
     * 或者
     * 2.只获取用户选择的企业,
     * @param pageSize
     * @return
     */
    private Map<String, Object> getTaxMapParam(CorpTaxQueueVO corpTaxQueueVO, int pageSize) {
        if (corpTaxQueueVO.getArray() != null && corpTaxQueueVO.getArray().size() > 0) {
            int taxDeclareProgressId = addTaxDeclareProgress(corpTaxQueueVO.getArray().size());
            JSONArray array = new JSONArray(corpTaxQueueVO.getArray());
            Map<String, Object> result = new HashMap<>();
            result.put("taxDeclareProgressId", taxDeclareProgressId);
            result.put("array", array);
            return result;
        }

        Map<String, Object> result = new HashMap<>();
        ZhangYiDaRequest zhangYiDaRequest = new ZhangYiDaRequest();
        CorpSettingService corpSettingService = (CorpSettingService)SpringUtil.getBean("corpSettingService");
        CorpSetting corpSetting = corpSettingService.getCorpSetting();

        int pageNo = 1;
        int taxDeclareProgressId = 0;
        JSONArray array = new JSONArray();
        while (true) {
            CorpTaxVO corpTaxVO = new CorpTaxVO();
            corpTaxVO.setUserName(corpSetting.getUsername());
            corpTaxVO.setPassword(corpSetting.getPassword());
            corpTaxVO.setDomainName(corpSetting.getDomainName());
            corpTaxVO.setPageNo(pageNo);
            corpTaxVO.setPageSize(pageSize);
            corpTaxVO.setCreateM(DateUtil.getDateShow(new Date(), "yyyyMM"));
//            corpTaxVO.setIsReport("0");

            corpTaxVO.setName(corpTaxQueueVO.getName());
            if (StringUtils.isEmpty(corpTaxQueueVO.getDeclareStatus())) {
                corpTaxVO.setDeclareStatus("1,2");// declareStatus 0:已申报,1:未申报,2:部分申报
            } else {
                corpTaxVO.setDeclareStatus(corpTaxQueueVO.getDeclareStatus());// declareStatus 0:已申报,1:未申报,2:部分申报
            }
            corpTaxVO.setIsShowPicture("0");// 0:否,1:是
            corpTaxVO.setIsReport("1");

            JSONObject jsonObject = zhangYiDaRequest.getCorpList(corpTaxVO);
            JSONObject info = jsonObject.getJSONObject("info");
            int totalResult = info.getInt("totalRecord");
            if (totalResult > 0) {
                if (pageNo == 1) {// 第一页保存进度,后续每一条记录更新进度,
                    taxDeclareProgressId = addTaxDeclareProgress(totalResult);
                }
                JSONArray corpTaxVOLi = info.getJSONArray("records");
                if (corpTaxVOLi.length() == 0) {
                    break;
                }

                for (int i = 0; i < corpTaxVOLi.length(); i++) {
                    JSONObject item = corpTaxVOLi.getJSONObject(i);
                    array.put(item);
                }

                if (totalResult <= (pageNo * pageSize)) {
                    break;
                }
                pageNo++;
            } else {// 为 0 时也保存一条记录,使得用户在界面上可以看得到,
                addTaxDeclareProgress(totalResult);
                break;
            }
        }
        result.put("taxDeclareProgressId", taxDeclareProgressId);
        result.put("array", array);
        return result;
    }

    /**
     * 连接测试
     */
    public void pingSelenium() {
        Log log = new Log(this.getClass());
        try {
            super.setup();
            if (getWebDriver() == null) {
                throw new RPAException("getWebDriver() return null");
            }
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            throw new RPAException(e);
        } finally {
            try {
                super.tearDown();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 验证码截图测试
     * @return
     */
    public Map<String, Object> pingCheckcode() {
        Map<String, Object> result = new HashMap<>();

        Log log = new Log(this.getClass());
        try {
            super.setup();
            if (getWebDriver() == null) {
                throw new RPAException("pingCheckcode().getWebDriver() return null");
            }

            LoginXmTaxAction loginXmTaxAction = new LoginXmTaxAction();
            Map<String, Object> base64AndCheckcodeMap = loginXmTaxAction.getBase64AndCheckcode();
            result.putAll(base64AndCheckcodeMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RPAException(e);
        } finally {
            try {
                super.tearDown();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 单线程运行
     */
    private void runSingleThread(JSONArray corpTaxVOLi, int taxDeclareProgressId) throws InterruptedException {
        CorpSettingService corpSettingService = (CorpSettingService)SpringUtil.getBean("corpSettingService");
        for (int i = 0; i < corpTaxVOLi.length(); i++) {
            JSONObject item = corpTaxVOLi.getJSONObject(i);
            final String nationaltaxLoginname = item.getString("nationaltaxLoginname");
            final String nationaltaxPassword = item.getString("nationaltaxPassword");
            if (corpSettingService.canApplyTax()) {
                login(nationaltaxLoginname, nationaltaxPassword, taxDeclareProgressId);
            }
        }
    }

    /**
     * 多线程运行,多线程是直接 new XmTaxTest().run() 来运行,此时,不会触发 setup,tearDown,
     * 需要手工触发,
     * setup()
     * ......
     * tearDown()
     */
    private void runMultipleThread(JSONArray corpTaxVOLi, int pageSize, final int taxDeclareProgressId) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(getThreadCount());
        for (int i = 0; i < corpTaxVOLi.length(); i++) {
            JSONObject item = corpTaxVOLi.getJSONObject(i);
            final String nationaltaxLoginname = item.getString("nationaltaxLoginname");
            final String nationaltaxPassword = item.getString("nationaltaxPassword");
            executorService.execute(new Thread(){
                @Override
                public void run() {
                    Log log = new Log(this.getClass());
                    CorpSettingService corpSettingService = (CorpSettingService)SpringUtil.getBean("corpSettingService");
                    if (corpSettingService.canApplyTax()) {
                        try {
                            setup();
                            login(nationaltaxLoginname, nationaltaxPassword, taxDeclareProgressId);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        } finally {
                            try {
                                tearDown();
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(pageSize * 5, TimeUnit.MINUTES);
    }

    /**
     * 获取并行线程数
     * @return
     */
    private int getThreadCount() {
        TaxParameterService taxParameterService = (TaxParameterService) SpringUtil.getBean("taxParameterService");
        String taxThreadCount = taxParameterService.getTaxParameter(TaxConstant.TAX_THREAD_COUNT).getValue();

        if (StringUtils.isEmpty(taxThreadCount)) {
            return 1;
        }
        int count = Integer.valueOf(taxThreadCount);
        if (count < 1) {
            return 1;
        }
        return count;
    }

    /**
     * 运行一次,保存一次报税结果,返回 id,
     */
    private int addTaxDeclareProgress(int totalResult) {
        TaxDeclareProgressService taxDeclareProgressService = (TaxDeclareProgressService)SpringUtil.getBean("taxDeclareProgressService");
        TaxDeclareProgress taxDeclareProgress = new TaxDeclareProgress();

//        taxDeclareProgress.setId(Integer);
        String currentDateTime = DateUtil.getDateShow(new Date(), "yyyyMMddHHmmss");
        taxDeclareProgress.setStartTime(currentDateTime);
        taxDeclareProgress.setUserTotalCount(totalResult);
        taxDeclareProgress.setUserFinishCount(0);
        taxDeclareProgress.setUserFinishSuccessCount(0);
//        taxDeclareProgress.setCurrentUser(String);
//        taxDeclareProgress.setCurrentTaxTime(String);
        taxDeclareProgress.setCreateTime(currentDateTime);
//        taxDeclareProgress.setUpdateTime(String);
        return taxDeclareProgressService.addTaxDeclareProgress(taxDeclareProgress);
    }

    //@Test(description = "登录", dataProvider = "loginData")
    public void login(String userName, String password, int taxDeclareProgressId) {
        Log log = new Log(this.getClass());
        ElementAction action = new ElementAction();
        YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
        TaxDeclareProgressService taxDeclareProgressService = (TaxDeclareProgressService) SpringUtil.getBean("taxDeclareProgressService");
        try {
            String baseUrl = ymlConfig.getBaseUrl();

            doLogin(baseUrl, userName, password);
            action.pagefoload(10);
            doIndexApplyTax(userName);

            // 成功,更新进度
            taxDeclareProgressService.updateTaxDeclareProgress(taxDeclareProgressId, userName, true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (!(e instanceof ApplyTaxFailException)) {
                writeBackBelongDateQuite(userName, e.getMessage(), false);
            }
            // 失败,更新进度
            taxDeclareProgressService.updateTaxDeclareProgress(taxDeclareProgressId, userName, false);
        }
    }

    /**
     * 登录
     *
     * @throws IOException
     * @throws DocumentException
     */
    private void doLogin(String BaseUrl, String userName, String password) throws IOException, DocumentException {
        //		String userName,String password,String message
        //代替testng参数化的方法
//		String BaseUrl ="http://wsbsfwt1.xmtax.gov.cn:8001/";
//		userName = "";
        ElementAction action = new ElementAction();
        //调用方法:1.初始化并登录
        LoginXmTaxAction jinshuiAction = new LoginXmTaxAction(BaseUrl, userName, password);
        action.sleep(1);
        //设置检查点
        //Assertion.VerityTextPresent(message,"验证是否出现预期的错误提示信息:"+message);
        //设置断言
        //Assertion.VerityError();

        jinshuiAction.doNothing();
    }

    /**
     * 首页-申请报税
     *
     * @throws IOException
     * @throws DocumentException
     */
    public void doIndexApplyTax(String userName) throws IOException, DocumentException {
        IndexXmTaxAction indexXmTaxAction = new IndexXmTaxAction();
//        indexXmTaxAction.applyTaxNeedToReport(userName);
        indexXmTaxAction.applyTaxNeedToReport(userName);
    }

    /**
     * 登录到进入首页的过程中发生了异常,例如密码错误等,截图记录下来,
     */
    private void writeBackBelongDateQuite(String nationaltaxLoginname, String ycxx, boolean success) {
        try {
            ZhangYiDaRequest zhangYiDaRequest = new ZhangYiDaRequest();
            String taxName = "";
            // 用前一个月的起始时间来代替
            Calendar calendar = Calendar.getInstance();
            // 前一个月第一天
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DATE, 1);

            String belongDateBegin = DateUtil.getDateShow(calendar.getTime(), "yyyyMMdd");

            // 前一个月最后一天=后一个月第一天,的前一天
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DATE, 1);
            calendar.add(Calendar.DATE, -1);
            String belongDateEnd = DateUtil.getDateShow(calendar.getTime(), "yyyyMMdd");

            String screenBase64 = TaxUtil.getScreenShotBase64(getWebDriver());

            TaxExpireDateSettingService taxExpireDateSettingService = (TaxExpireDateSettingService) SpringUtil.getBean("taxExpireDateSettingService");
            String taxEndDate = taxExpireDateSettingService.getTaxEndDateString();

            WriteBackTaxResultVO writeBackTaxResultVO = new WriteBackTaxResultVO();
            writeBackTaxResultVO.setNationaltaxLoginname(nationaltaxLoginname);
            writeBackTaxResultVO.setTaxName(taxName);
            writeBackTaxResultVO.setScreenBase64(screenBase64);
            writeBackTaxResultVO.setBelongDateBegin(belongDateBegin);
            writeBackTaxResultVO.setBelongDateEnd(belongDateEnd);
            writeBackTaxResultVO.setTaxEndDate(taxEndDate);
            writeBackTaxResultVO.setYcxx(ycxx);
            writeBackTaxResultVO.setSuccess(success);

            zhangYiDaRequest.writeBackTaxResult(writeBackTaxResultVO);
        } catch (Exception e) {
            Log log = new Log(getClass());
            log.error(e.getMessage(), e);
        }
    }
}