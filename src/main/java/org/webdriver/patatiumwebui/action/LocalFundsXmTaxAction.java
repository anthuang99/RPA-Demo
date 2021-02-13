package org.webdriver.patatiumwebui.action;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.httprequest.ZhangYiDaRequest;
import org.webdriver.patatiumwebui.httprequest.vo.WriteBackTaxResultVO;
import org.webdriver.patatiumwebui.pageObject.LocalFundsXmTaxPage;
import org.webdriver.patatiumwebui.utils.CommonUtil;
import org.webdriver.patatiumwebui.utils.ElementAction;
import org.webdriver.patatiumwebui.utils.Log;
import org.webdriver.patatiumwebui.utils.TaxUtil;
import org.webdriver.patatiumwebui.utils.TestBaseCase;
import org.webdriver.patatiumwebui.web.service.TaxExpireDateSettingService;
import org.webdriver.patatiumwebui.web.util.DateUtil;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * 工会经费
 */
public class LocalFundsXmTaxAction extends TestBaseCase {
    private Log log = new Log(this.getClass());
    private String belongDateBegin;
    private String belongDateEnd;

    /**
     * 报税
     * @throws IOException
     */
    public boolean applyTax(String nationaltaxLoginname) {
        ElementAction action = new ElementAction();
        try {
            // 新的页面内容,
            TaxUtil.switchToMainWindow();

            // 点击工会经费申报
            clickRadio1();

            // 点击右侧页面的下一步
            clickNext();

            JSONObject result = writeBackBelongDate(nationaltaxLoginname, "", true);
            return result != null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            writeBackBelongDate(nationaltaxLoginname, e.getMessage(), false);
            return false;
        }
    }

    /**
     * 点击工会经费申报
     * @throws IOException
     */
    private void clickRadio1() throws IOException {
        LocalFundsXmTaxPage stampTaxXmTaxPage = new LocalFundsXmTaxPage();
        ElementAction action = new ElementAction();
        action.switchToDefaultFrame();

        // 定位顶部页面包含的 table_content,
        action.switchToFrame(stampTaxXmTaxPage.table_content());

        // table_content 里面包含的 table_right_content
        action.switchToFrame(stampTaxXmTaxPage.table_right_content());

        // 点击工会经费申报
//        action.click(stampTaxXmTaxPage.工会经费申报());
        action.executeJS("$('#nzgr').click()");// 防 IE,工会经费申报 点击
        action.sleep(1);
    }

    /**
     * 点击下一步
     * @throws IOException
     */
    private void clickNext() throws IOException {
        LocalFundsXmTaxPage stampTaxXmTaxPage = new LocalFundsXmTaxPage();
        ElementAction action = new ElementAction();
        action.switchToDefaultFrame();
        {
            // 定位顶部页面包含的 table_content,
            action.switchToFrame(stampTaxXmTaxPage.table_content());

            // table_content 里面包含的 table_right_content
            action.switchToFrame(stampTaxXmTaxPage.table_right_content());

            // 取得所属时期,并保存到私有变量中,后面调用账益达接口回写成功或失败时,会用到
            applyBelongDate(action.getAttribute(stampTaxXmTaxPage.税款所属期(), "value"));

            // 点击下一步
            action.DisplayElement(stampTaxXmTaxPage.下一步());
//            action.click(stampTaxXmTaxPage.下一步());
            action.executeJS("$('#submitBtn').click()");// 防 IE,下一步
            action.sleep(1);
        }

        action.pagefoload(10);
        action.switchToDefaultFrame();
        {
            // 定位顶部页面包含的 table_content,
            action.switchToFrame(stampTaxXmTaxPage.table_content());

            // table_content 里面包含的 table_right_content
            action.switchToFrame(stampTaxXmTaxPage.table_right_content());

            // 添加申报记录
            action.DisplayElement(stampTaxXmTaxPage.添加申报记录());
//            action.click(stampTaxXmTaxPage.添加申报记录());
            action.executeJS("$('#addItem').click()");// 防 IE,添加申报记录
            action.sleep(1);
        }

        action.pagefoload(10);
        action.switchToDefaultFrame();
        {
            // 定位顶部页面包含的 table_content,
            action.switchToFrame(stampTaxXmTaxPage.table_content());

            // table_content 里面包含的 table_right_content
            action.switchToFrame(stampTaxXmTaxPage.table_right_content());

            // 添加申报记录
            action.DisplayElement(stampTaxXmTaxPage.下一步2());
//            action.click(stampTaxXmTaxPage.下一步2());
            action.executeJS("$('#next').click()");// 防 IE,下一步2
            action.sleep(1);
        }

        // 确认添加
        action.pagefoload(10);
        action.switchToDefaultFrame();
        {
            // 定位顶部页面包含的 table_content,
            action.switchToFrame(stampTaxXmTaxPage.table_content());

            // table_content 里面包含的 table_right_content
            action.switchToFrame(stampTaxXmTaxPage.table_right_content());

            action.DisplayElement(stampTaxXmTaxPage.应税项());// 点击下一步时,会有一个动画,此时,需要等待一会儿,让元素可见
//            action.click(stampTaxXmTaxPage.应税项());
//            action.type(stampTaxXmTaxPage.应税项(), "0");
            action.executeJS("$('#ysx').click()");// 防 IE,应税项
            action.executeJS("$('#ysx').val('0')");// 防 IE,应税项
            action.executeJS("$('#jcx').click()");// 防 IE,减除项（2）

//            action.click(stampTaxXmTaxPage.确认添加());
            action.executeJS("$('#addToList').click()");// 防 IE,确认添加

            action.sleep(1);
        }

        // 点击确认提交
        action.pagefoload(10);
        action.switchToDefaultFrame();
        {
            // 定位顶部页面包含的 table_content,
            action.switchToFrame(stampTaxXmTaxPage.table_content());

            // table_content 里面包含的 table_right_content
            action.switchToFrame(stampTaxXmTaxPage.table_right_content());

            action.sleep(3);// 请添加申报记录这个页面,也存在 ensure 这个按钮,因此,需要停止 3 秒
//            action.DisplayElement(stampTaxXmTaxPage.确认提交());
//            action.click(stampTaxXmTaxPage.确认提交());
//            action.executeJS("$('#ensure').click()");

//            action.DisplayElement(stampTaxXmTaxPage.提交申请表());
            // 添加确认提交
            if (!CommonUtil.isDev()) {
                action.executeJS("$('#ensure').click()");// 保存

                action.DisplayElement(stampTaxXmTaxPage.提交申请表());
//                action.click(stampTaxXmTaxPage.提交申请表());
                action.executeJS("$('#submitList').click()");// 防 IE,申报
                action.executeJS("$('#layui-layer1 > div.layui-layer-btn > a.layui-layer-btn0').click()");// 点击确实弹出框,
//                action.alertConfirm();
            }
            action.sleep(1);
            action.pagefoload(10);
        }
    }

    private JSONObject writeBackBelongDate(String nationaltaxLoginname, String ycxx, boolean success) {
        ZhangYiDaRequest zhangYiDaRequest = new ZhangYiDaRequest();
        String taxName = TaxConstant.XM_QTSL;
        // 报税过程中,页面出错,belongDateBegin 有可能为空,为空时,用前一个月的起始时间来代替
        if (StringUtils.isEmpty(belongDateBegin)) {
            Calendar calendar = Calendar.getInstance();
            // 前一个月第一天
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DATE, 1);

            belongDateBegin = DateUtil.getDateShow(calendar.getTime(), "yyyyMMdd");

            // 前一个月最后一天=后一个月第一天,的前一天
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DATE, 1);
            calendar.add(Calendar.DATE, -1);
            belongDateEnd = DateUtil.getDateShow(calendar.getTime(), "yyyyMMdd");
        }

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

        return zhangYiDaRequest.writeBackTaxResult(writeBackTaxResultVO);
    }

    /**
     * 税款所属期:
     * @param belongText 2018-10
     */
    private void applyBelongDate(String belongText) {
        belongText = belongText.replace("-", "");
        Date date = DateUtil.getDateFromString(belongText + "01", "yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        belongDateBegin = DateUtil.getDateShow(calendar.getTime(), "yyyyMMdd");

        // 前一个月最后一天=后一个月第一天,的前一天
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        belongDateEnd = DateUtil.getDateShow(calendar.getTime(), "yyyyMMdd");
    }
}
