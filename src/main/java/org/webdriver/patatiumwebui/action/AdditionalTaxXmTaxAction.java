package org.webdriver.patatiumwebui.action;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.httprequest.ZhangYiDaRequest;
import org.webdriver.patatiumwebui.httprequest.vo.WriteBackTaxResultVO;
import org.webdriver.patatiumwebui.pageObject.AdditionalTaxXmTaxPage;
import org.webdriver.patatiumwebui.utils.CommonUtil;
import org.webdriver.patatiumwebui.utils.ElementAction;
import org.webdriver.patatiumwebui.utils.Log;
import org.webdriver.patatiumwebui.utils.TaxUtil;
import org.webdriver.patatiumwebui.utils.TestBaseCase;
import org.webdriver.patatiumwebui.web.service.TaxExpireDateSettingService;
import org.webdriver.patatiumwebui.web.util.DateUtil;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 印花税
 */
public class AdditionalTaxXmTaxAction extends TestBaseCase {
    private Log log = new Log(this.getClass());
    private String belongDateBegin;
    private String belongDateEnd;
    private boolean isMonthReport = false;

    /**
     * 报税
     * @throws IOException
     */
    public boolean applyTax(String nationaltaxLoginname) {
        ElementAction action = new ElementAction();
        try {
            // 新的页面内容,
            TaxUtil.switchToMainWindow();

            // 一般纳税人月报,小规模季报, 1,4,7,10 才能进行季报,
            if (isMonthReport() || isMonthCanSeasonReport()) {
                // 点击右侧页面的下一步
                clickNext(nationaltaxLoginname);

                action.pagefoload(10);

                // 选择一个申报记录,并保存,
                clickAddTaxReport(nationaltaxLoginname);

                action.pagefoload(10);

                // 确认提交
                clickConfirm(nationaltaxLoginname);

                JSONObject result = writeBackBelongDate(nationaltaxLoginname, "", true);
                return result != null;
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            writeBackBelongDate(nationaltaxLoginname, e.getMessage(), false);
            return false;
        }
    }

    /**
     * 月份是否可季报
     * [1,2,3], 4月份申报
     [4,5,6], 7月份申报
     [7,8,9], 10月份申报
     [10,11,12], 明年1月份申报
     */
    private boolean isMonthCanSeasonReport() {
        Calendar calendar = Calendar.getInstance();
        List<Integer> months = Arrays.asList(1,4,7,10);
        return months.contains(calendar.get(Calendar.MONTH) + 1);
    }

    /**
     * 月报还是季报,季报需要判断是否是季的下一月,例如:
     */
    private boolean isMonthReport() throws IOException {
        AdditionalTaxXmTaxPage additionalTaxXmTaxPage = new AdditionalTaxXmTaxPage();
        ElementAction action = new ElementAction();
        action.switchToDefaultFrame();

        // 定位顶部页面包含的 table_content,
        action.switchToFrame(additionalTaxXmTaxPage.table_content());

        // table_content 里面包含的 table_right_content
        action.switchToFrame(additionalTaxXmTaxPage.table_right_content());

        isMonthReport = !action.isElementDisplayed(additionalTaxXmTaxPage.税款所属期止());

        return isMonthReport;
    }

    /**
     * 点击下一步
     * @throws IOException
     */
    private void clickNext(String nationaltaxLoginname) throws IOException {
        AdditionalTaxXmTaxPage additionalTaxXmTaxPage = new AdditionalTaxXmTaxPage();
        ElementAction action = new ElementAction();
        action.switchToDefaultFrame();

        // 定位顶部页面包含的 table_content,
        action.switchToFrame(additionalTaxXmTaxPage.table_content());

        // table_content 里面包含的 table_right_content
        action.switchToFrame(additionalTaxXmTaxPage.table_right_content());

        // 取得所属时期,并保存到私有变量中,后面调用账益达接口回写成功或失败时,会用到
        String belongDateBeginText = action.getAttribute(additionalTaxXmTaxPage.税款所属期(), "value");
        String belongDateEndText = null;
        if (!isMonthReport) {
            belongDateEndText = action.getAttribute(additionalTaxXmTaxPage.税款所属期止(), "value");
        }
        applyBelongDate(belongDateBeginText, belongDateEndText);

        // 点击下一步
//        action.click(additionalTaxXmTaxPage.下一步());// IE 可能不运行,
        action.executeJS("Fjssubmit()");
        action.sleep(1);
    }

    /**
     * 点击添加申报记录
     * @throws IOException
     */
    private void clickAddTaxReport(String nationaltaxLoginname) throws IOException {
        AdditionalTaxXmTaxPage additionalTaxXmTaxPage = new AdditionalTaxXmTaxPage();
        ElementAction action = new ElementAction();

        action.switchToDefaultFrame();
        {
            // 定位顶部页面包含的 table_content
            action.switchToFrame(additionalTaxXmTaxPage.table_content());

            // table_content 里面包含的 table_right_content
            action.switchToFrame(additionalTaxXmTaxPage.table_right_content());

            // 点击添加申报记录
            /*
            action.DisplayElement(additionalTaxXmTaxPage.添加申报记录());
            action.click(additionalTaxXmTaxPage.添加申报记录());
            */
            action.executeJS("tjsbjl()");
            action.sleep(1);
        }

        // 选择应征凭证名称:购销合同并并保存
        action.switchToDefaultFrame();
        {
            // 定位顶部页面包含的 table_content
            action.switchToFrame(additionalTaxXmTaxPage.table_content());

            // table_content 里面包含的 table_right_content
            action.switchToFrame(additionalTaxXmTaxPage.table_right_content());

            /*
<option value="10109">城市维护建设税</option>
             */
            action.click(additionalTaxXmTaxPage.征收项目());
            action.selectByValue(additionalTaxXmTaxPage.征收项目(), "10109");

            /*
<option value="101090101">市区（增值税附征）</option></select>
             */
            action.click(additionalTaxXmTaxPage.征收品目());
            action.selectByValue(additionalTaxXmTaxPage.征收品目(), "101090101");

            /*
<option value="11">增值税入库税款为0（月报）</option>
<option value="12">月销售额合计10万（含）以下</option>
<option value="13">月销售额合计数为10万以上</option>
             */
            action.click(additionalTaxXmTaxPage.本月销售额合计数());
            action.selectByValue(additionalTaxXmTaxPage.本月销售额合计数(), "11");

            action.DisplayElement(additionalTaxXmTaxPage.添加城建税下一步());
//            action.click(additionalTaxXmTaxPage.添加城建税下一步());
            action.executeJS("$('#myForm2 > table > tbody > tr:nth-child(3) > td > table > tbody > tr:nth-child(4) > td > div > input:nth-child(1)').click()");// 直接js,避免 IE 无法执行,
            action.sleep(1);
        }

        action.switchToDefaultFrame();
        {// 增值税（一般增值税）
// 定位顶部页面包含的 table_content
            action.switchToFrame(additionalTaxXmTaxPage.table_content());

            // table_content 里面包含的 table_right_content
            action.switchToFrame(additionalTaxXmTaxPage.table_right_content());

            action.click(additionalTaxXmTaxPage.增值税输入());
            action.type(additionalTaxXmTaxPage.增值税输入(), "0");

            action.click(additionalTaxXmTaxPage.增值税免抵税额());

            action.executeJS("qrtj('tj')");// 确认添加
            action.sleep(1);
        }
    }

    /**
     * 确认提交
     * @throws IOException
     */
    private void clickConfirm(String nationaltaxLoginname) throws IOException {
        AdditionalTaxXmTaxPage additionalTaxXmTaxPage = new AdditionalTaxXmTaxPage();
        ElementAction action = new ElementAction();
        action.switchToDefaultFrame();

        // 定位顶部页面包含的 table_content,
        action.switchToFrame(additionalTaxXmTaxPage.table_content());

        // table_content 里面包含的 table_right_content
        action.switchToFrame(additionalTaxXmTaxPage.table_right_content());

        // 点击确认提交
        if (!CommonUtil.isDev()) {
            //action.click(additionalTaxXmTaxPage.确认提交());
            action.executeJS("tjsbb()");// 先保存
            action.executeJS("tjsbbsb()");// 防 IE,申报
            action.alertConfirm();
        }
        action.sleep(1);
        action.pagefoload(10);
    }

    /**
     * 回写归属日期
     * @param nationaltaxLoginname
     */
    private JSONObject writeBackBelongDate(String nationaltaxLoginname, String ycxx, boolean success) {
        ZhangYiDaRequest zhangYiDaRequest = new ZhangYiDaRequest();
        String taxName = TaxConstant.XM_FJSCJ;
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
     * @param belongDateBeginText 格式格式类似于:2018-10
     * @param belongDateEndText 格式格式类似于:2018-10,当为月报时为空,季报时,非空,
     */
    private void applyBelongDate(String belongDateBeginText, String belongDateEndText) {
        belongDateBeginText = belongDateBeginText.replace("-", "");
        Date date = DateUtil.getDateFromString(belongDateBeginText + "01", "yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        belongDateBegin = DateUtil.getDateShow(calendar.getTime(), "yyyyMMdd");

        if (StringUtils.isEmpty(belongDateEndText)) {// 月报
            // 前一个月最后一天=后一个月第一天,的前一天
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DATE, 1);
            calendar.add(Calendar.DATE, -1);
            belongDateEnd = DateUtil.getDateShow(calendar.getTime(), "yyyyMMdd");
        } else {// 季报,
            belongDateEnd = getBelongDateEnd(belongDateEndText);
        }
    }

    /**
     * 传入年月,返回这个月的最后一天,
     * 例如:传入 2018-09,返回 20180930,
     * @param belongDateEndText
     * @return
     */
    private String getBelongDateEnd(String belongDateEndText) {
        belongDateEndText = belongDateEndText.replace("-", "");
        Date date = DateUtil.getDateFromString(belongDateEndText + "01", "yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 前一个月最后一天=后一个月第一天,的前一天
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        return DateUtil.getDateShow(calendar.getTime(), "yyyyMMdd");
    }
}
