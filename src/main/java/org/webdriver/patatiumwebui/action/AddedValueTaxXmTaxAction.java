package org.webdriver.patatiumwebui.action;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.exception.RPAException;
import org.webdriver.patatiumwebui.httprequest.ZhangYiDaRequest;
import org.webdriver.patatiumwebui.httprequest.vo.WriteBackTaxResultVO;
import org.webdriver.patatiumwebui.pageObject.AddedValueTaxXmTaxPage;
import org.webdriver.patatiumwebui.utils.*;
import org.webdriver.patatiumwebui.web.service.TaxExpireDateSettingService;
import org.webdriver.patatiumwebui.web.util.DateUtil;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 点击增值税及附加税费 进入 我的办事大厅 页面
 */
public class AddedValueTaxXmTaxAction extends TestBaseCase {
    private Log log = new Log(this.getClass());
    private String belongDateBegin;
    private String belongDateEnd;

    /**
     * 报税
     */
    public boolean applyTax(String nationaltaxLoginname) {
        ElementAction action = new ElementAction();
//        String taxName = TaxConstant.XM_ZZS;// xmYbZzs/厦门一般纳税人增值税,xmXgmZzs/厦门小规模增值税,一开始是不知道一般还是不规模,给其随便赋个值,
        String taxName = null;
        try {
            // 新的页面内容,
//            action.switchToWindowByTitle("国家税务总局厦门市税务局网上申报系统");
            // 小规模纳税人,点击一般纳税人菜单链接进来,会出现弹框提示:您当前不需要申报增值税一般纳税人申报表，或未被授权网上申报！
            // 在 menu.js 里面有一个 alert,因此,先每隔 1 秒判断一下,重复 10 次,
            /*for (int i = 0; i < 10; i++) {
                action.sleep(1);
                try {
                    action.alertConfirm();
                    break;
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }*/
            action.sleep(5);
            try {
                action.alertConfirm();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            action.switchToWindowByUrl("/public/jsp/wssb.jsp");

            // 小规模纳税人,可能增值税菜单不存在,有可能免报,连菜单都没有,
            JSONObject writeResult = null;
            int index = hasZzsMenu();
            if (index == -1) {
                taxName = TaxConstant.XM_XGM_ZZS;
                writeResult = writeBackBelongDate(nationaltaxLoginname, taxName, "没有增值税菜单,无法报税", false);
            } else {
                if (isSmallTaxUser(index)) {// 小规模纳税人,菜单比较少
                    taxName = TaxConstant.XM_XGM_ZZS;
                    applySmallTax(index);
                } else {// 一般纳税人
                    taxName = TaxConstant.XM_YB_ZZS;
                    applyNormalTax(index);
                }
                writeResult = writeBackBelongDate(nationaltaxLoginname, taxName, "", true);
            }
            return writeResult != null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            writeBackBelongDate(nationaltaxLoginname, taxName, e.getMessage(), false);
            return false;
        }
    }

    /**
     * 回写归属日期
     * @param nationaltaxLoginname
     */
    private JSONObject writeBackBelongDate(String nationaltaxLoginname, String taxName, String ycxx, boolean success) {
        ZhangYiDaRequest zhangYiDaRequest = new ZhangYiDaRequest();
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
     * 是否是小规模纳税人
     * @return
     * @throws IOException
     */
    private boolean isSmallTaxUser(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

            // 查找第一个菜单上的文字,是否出现小规模纳税人字样,
//            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/p[1]/a/span[1]".replace("{index}", String.valueOf(index)));
//            String text = action.getText(locator);
            String jsString = "return $('#nav > div:nth-child({index}) > div > p:nth-child(1) > a > span:nth-child(1)').html()";
            jsString = jsString.replace("{index}", String.valueOf(index));
            String text = action.executeJS(jsString).toString();
            if (text.indexOf("小规模纳税人") > -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否拥有增值税菜单
     * 菜单有可能为:
     * 1.增值税
     * 也有可能为:
     * 1.企业所得税(年度关联申报)
     * 2.增值税
     * 因此,需要遍历一遍,返回这个下标,后续用这个下标,来寻找菜单,
     * @return
     * @throws IOException
     */
    private int hasZzsMenu() throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());
            action.pagefoload(10);

            {
                String menuJs = "$('#nav > div:nth-child({index}) > p > a > span').html()";
                for (int i = 1; i < 6; i++) {
                    String line = menuJs;
                    line = line.replace("{index}", String.valueOf(i));
                    String menuText = action.executeJS("return " + line).toString().trim();
                    if (menuText.indexOf("增值税") > -1) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 一般纳税人
     * @throws IOException
     */
    private void applyNormalTax(int index) throws IOException {
        // 如果增值税菜单未展开,则点击增值税菜单,将其展开,
        expandMenuIfNot(index);

        // 1.增值税纳税申报表附列资料_一
        clickNormalAttachment1(index);

        // 2. 增值税纳税申报表附列资料_二
        clickNormalAttachment2(index);

        // 3.增值税纳税申报表附列资料_三
        clickNormalAttachment3(index);

        // 4.增值税纳税申报表附列资料_四
        clickNormalAttachment4(index);

        // 5.增值税纳税申报表附列资料_五
        clickNormalAttachment5(index);

        // 增值税减免税申报明细表
        clickNormalTaxReduce(index);

        // 增值税申报表_一般纳税人适用
        clickNormalTax(index);

        // 营改增税负分析测算明细表,暂存两次,保存一次,
//        clickNormalAnalyze(false);
//        clickNormalAnalyze(false);
        clickNormalAnalyze(index);

        // 农产品收购发票或普通发票明细表
        clickFarmerInvoice(index);

        // 正式申报
        clickOfficialReport(index);
    }

    /**
     * 点击　增值税纳税申报表附列资料_一
     */
    private void clickNormalAttachment1(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

//            action.click(indexXmTaxPage.增值税纳税申报表附列资料_一());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/p[2]/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);

            action.sleep(1);
        }
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            // 定位到 右侧的保存按钮
            if (!CommonUtil.isDev()) {
//                action.click(indexXmTaxPage.增值税纳税申报表附列资料_一_保存());
                action.executeJS("$('body > table > tbody > tr > td > table > tbody > tr:nth-child(3) > td > div > a:nth-child(5)').click()");// 直接js,避免 IE 无法执行,
                action.sleep(1);
            }
        }
    }

    /**
     * 点击　增值税纳税申报表附列资料_二
     */
    private void clickNormalAttachment2(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

//            action.click(indexXmTaxPage.增值税纳税申报表附列资料_二());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/p[3]/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);

            action.sleep(1);
        }
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            // 定位到 右侧的保存按钮
            if (!CommonUtil.isDev()) {
//                action.click(indexXmTaxPage.增值税纳税申报表附列资料_二_保存());
                action.executeJS("$('body > table > tbody > tr > td > table > tbody > tr:nth-child(3) > td > div > a:nth-child(4)').click()");// 防 IE
                action.sleep(1);
            }
        }
    }

    /**
     * 点击　增值税纳税申报表附列资料_三
     */
    private void clickNormalAttachment3(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

//            action.click(indexXmTaxPage.增值税纳税申报表附列资料_三());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/p[4]/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);

            action.sleep(1);
        }
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            // 定位到 右侧的保存按钮
            if (!CommonUtil.isDev()) {
//                action.click(indexXmTaxPage.增值税纳税申报表附列资料_三_保存());
                action.executeJS("$('#inputTable > tbody > tr:nth-child(2) > td > div:nth-child(5) > a:nth-child(4)').click()");// 防 IE
                action.sleep(1);
            }
        }
    }

    /**
     * 点击　增值税纳税申报表附列资料_四
     */
    private void clickNormalAttachment4(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

//            action.click(indexXmTaxPage.增值税纳税申报表附列资料_四());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/p[5]/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);

            action.sleep(1);
        }
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            // 定位到 右侧的保存按钮
            if (!CommonUtil.isDev()) {
//                action.click(indexXmTaxPage.增值税纳税申报表附列资料_四_保存());
                action.executeJS("$('body > table > tbody > tr > td > form > div > a:nth-child(4)').click()");// 防 IE
                action.sleep(1);
            }
        }
    }

    /**
     * 点击　增值税纳税申报表附列资料_五
     */
    private void clickNormalAttachment5(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

//            action.click(indexXmTaxPage.增值税纳税申报表附列资料_五());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/p[6]/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);

            action.sleep(1);
        }
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            // 定位到 右侧的保存按钮
            if (!CommonUtil.isDev()) {
//                action.click(indexXmTaxPage.增值税纳税申报表附列资料_五_保存());
                action.executeJS("$('body > table > tbody > tr > td > form > p > table > tbody > tr > td > a:nth-child(4)').click()");// 防 IE
                action.sleep(1);
            }
        }
    }

    /**
     * 点击　增值税减免税申报明细表
     */
    private void clickNormalTaxReduce(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

//            action.click(indexXmTaxPage.增值税减免税申报明细表());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/p[7]/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);
            action.sleep(1);
        }
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            // 定位到 右侧的保存按钮
            if (!CommonUtil.isDev()) {
//                action.click(indexXmTaxPage.增值税减免税申报明细表_保存());
                action.executeJS("$('body > table > tbody > tr > td > form > p > table > tbody > tr > td > a:nth-child(4)').click()");// 防 IE
                action.sleep(1);
            }
        }
    }

    /**
     * 点击　营改增税负分析测算明细表
     * 通常会提示:
     * 提    示：  您非税务机关确定的样本企业，无需填报《营改增税负分析测算明细表》
     * 或
     * 您本表部分栏次系统已自动根据使用
     * 或
     * 需要填写默认值,
     */
    private void clickNormalAnalyze(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

//            action.click(indexXmTaxPage.营改增税负分析测算明细表());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/p[8]/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);
            action.sleep(1);
        }
        action.pagefoload(10);
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            // 定位到 右侧的保存按钮

//                action.click(indexXmTaxPage.增值税减免税申报明细表_保存());
            Object obj = action.executeJS("return $(document.body).html()");
            if (obj.toString().indexOf("无需填报《营改增税负分析测算明细表》") == -1) {
                if (obj.toString().indexOf("您本表部分栏次系统已自动根据使用") == -1) {
                    action.click(indexXmTaxPage.应税项目代码及名称());
//                    action.selectByValue(indexXmTaxPage.应税项目代码及名称(), "079900");// 都默认 其他生活服务,
                    action.selectByValue(indexXmTaxPage.应税项目代码及名称(), "069900");// 都默认 其他生活服务,

                    action.click(indexXmTaxPage.增值税税率或征收率());
                    action.selectByValue(indexXmTaxPage.增值税税率或征收率(), "0.06");

                    action.click(indexXmTaxPage.营业税税率());
                    action.selectByValue(indexXmTaxPage.营业税税率(), "0.05");

                    if (!CommonUtil.isDev()) {
                        String length = action.executeJS("return $('a[name=save]').length").toString();
                        if (length.equalsIgnoreCase("1")) {// 新增页,
                            action.executeJS("$('a[name=save]').click()");
                        } else {
                            action.executeJS("$($('a[name=update]').get(1)).click()");
                        }
                    }
                } else {// 已自动带值,直接保存即可
                    if (!CommonUtil.isDev()) {
                        String length = action.executeJS("return $('a[name=save]').length").toString();
                        if (length.equalsIgnoreCase("1")) {// 新增页,
                            action.executeJS("$('a[name=save]').click()");
                        } else {
                            action.executeJS("$($('a[name=update]').get(1)).click()");
                        }
                    }
                }
            }
        }
    }

    /**
     * 农产品收购发票或普通发票明细表
     */
    private void clickFarmerInvoice(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

//            action.click(indexXmTaxPage.农产品收购发票或普通发票明细表());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/p[9]/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);
            action.sleep(1);
        }
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            // 定位到 右侧的保存按钮
            if (!CommonUtil.isDev()) {
//                action.click(indexXmTaxPage.农产品收购发票或普通发票明细表_保存());
                action.executeJS("$('body > table > tbody > tr > td > form > table > tbody > tr:nth-child(6) > td > div > a:nth-child(4)').click()");// 防 IE
                action.sleep(1);
            }
        }
    }

    /**
     * 点击　增值税申报表_一般纳税人适用
     */
    private void clickNormalTax(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

//            action.click(indexXmTaxPage.增值税申报表_一般纳税人适用());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/p[1]/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);
            action.sleep(1);
        }
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            // 取得所属时期,并保存到私有变量中,后面调用账益达接口回写成功或失败时,会用到
            String belongText = action.getText(indexXmTaxPage.增值税申报表_一般纳税人适用_所属时期());
            applyBelongDate(belongText);

            // 选择是,连附加税一起申报,
//            action.click(indexXmTaxPage.增值税申报表_一般纳税人适用_是否采用_主附税一体化申报缴税功能_是());
            action.executeJS("$('#GDSYTHJS_FALG1').click()");// 防 IE
            action.sleep(1);

            // 定位到 右侧的保存按钮
            if (!CommonUtil.isDev()) {
//                action.click(indexXmTaxPage.增值税申报表_一般纳税人适用_保存());
                action.executeJS("$('#inputTable > tbody > tr > td > table:nth-child(7) > tbody > tr:nth-child(3) > td > div > a:nth-child(4)').click()");// 防 IE
                action.sleep(1);
            }
        }
    }

    /**
     * 正式申报
     */
    private void clickOfficialReport(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

//            action.click(indexXmTaxPage.正式申报());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/ul/li/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);
            if (!CommonUtil.isDev()) {
                action.alertConfirm();
            } else {
                action.alertDismiss();
            }
            action.sleep(1);
            action.pagefoload(10);
        }
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            String html = action.executeJS("return $(document.body).html()").toString();
            if (html.indexOf("申报成功") == -1) {
                throw new RPAException("未出现回执页面");
            }
        }
    }

    /**
     * 小规模纳税人
     * @throws IOException
     */
    private void applySmallTax(int index) throws IOException {
        // 如果增值税菜单未展开,则点击增值税菜单,将其展开,
        expandMenuIfNot(index);

        // 1.《增值税纳税申报表（小规模纳税人适用）附列资料》
        clickAttachmentData(index);

        // 2. 《增值税纳税申报表（小规模纳税人适用）》
        clickAddedValueTaxMainTable(index);

        // 3.增值税减免税申报明细表
        clickAddedValueTaxReduce(index);

        // 点击正式申报
        clickSmallOfficialReport(index);
    }

    /**
     * 如果增值税菜单没有展开,将其展开,
     * @param index
     * @throws IOException
     */
    private void expandMenuIfNot(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());
            String displayText = action.executeJS("return $('#nav > div:nth-child({index}) > div').css('display')".replace("{index}", String.valueOf(index))).toString();
            if (displayText.equalsIgnoreCase("none")) {
                // 点击增值税菜单
                action.executeJS("$('#nav > div:nth-child({index}) > p > a > span').click()".replace("{index}", String.valueOf(index)));
                action.sleep(1);
            }
        }
    }

    /**
     * 增值税减免税申报明细表_小规模纳税人适用
     * @throws IOException
     */
    private void clickAddedValueTaxReduce(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

//            action.click(indexXmTaxPage.增值税减免税申报明细表_小规模纳税人适用());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/p[3]/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);
            action.sleep(1);
        }

        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            // 定位到 右侧的保存按钮
            if (!CommonUtil.isDev()) {
//                action.click(indexXmTaxPage.增值税减免税申报明细表_小规模纳税人适用_保存());
                // 新增页:$("a[name=save]").length 返回1,编辑页返回0
                //
                String length = action.executeJS("return $('a[name=save]').length").toString();
                if (length.equalsIgnoreCase("1")) {// 新增页,
                    action.executeJS("$('a[name=save]').click()");
                } else {
                    action.executeJS("$($('a[name=update]').get(1)).click()");
                }
                action.sleep(1);
            }
        }
    }

    /**
     * 《增值税纳税申报表（小规模纳税人适用）》
     * @throws IOException
     */
    private void clickAddedValueTaxMainTable(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 点击 1.《增值税纳税申报表（小规模纳税人适用）》
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

            // 定位到 《增值税纳税申报表（小规模纳税人适用）附列资料》
//            action.click(indexXmTaxPage.增值税纳税申报表_小规模纳税人适用());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/p[1]/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);

            action.sleep(1);
        }

        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            // 取得所属时期,并保存到私有变量中,后面调用账益达接口回写成功或失败时,会用到
//            所属时期：20181001 至 20181231
//            String belongText = action.getText(indexXmTaxPage.增值税纳税申报表_小规模纳税人适用_所属时期());
            String html = action.executeJS("return $(document.body).html()").toString();
            Pattern pattern = Pattern.compile("所属时期：\\d{8} 至 \\d{8}");
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                applyBelongDate(matcher.group(0));
            }

            // 选择否,如果选择是,会弹框提示,小规模纳税人的附加税零申报信息由系统自动同步，无需进行网上一体化申报！因此，只能选择否
            action.click(indexXmTaxPage.增值税纳税申报表_小规模纳税人适用_是否采用_主附税一体化申报缴税功能_否());
            action.sleep(1);

            // 定位到 右侧的保存按钮
            if (!CommonUtil.isDev()) {
//                action.click(indexXmTaxPage.增值税纳税申报表_小规模纳税人适用_保存());
                // 新增页:$("a[name=save]").length 返回1,编辑页返回0
                //
                String length = action.executeJS("return $('a[name=save]').length").toString();
                if (length.equalsIgnoreCase("1")) {// 新增页,
                    action.executeJS("$('a[name=save]').click()");
                } else {
                    action.executeJS("$($('a[name=update]').get(1)).click()");
                }
                action.sleep(1);
            }
        }
    }

    /**
     * 点击并保存
     * 《增值税纳税申报表（小规模纳税人适用）附列资料》
     */
    private void clickAttachmentData(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 点击 1.《增值税纳税申报表（小规模纳税人适用）附列资料》
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

            // 定位到 《增值税纳税申报表（小规模纳税人适用）附列资料》
//            action.click(indexXmTaxPage.增值税纳税申报表_小规模纳税人适用_附列资料());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/p[2]/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);

            action.sleep(1);
        }

        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();

        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            // 定位到 右侧的保存按钮
//            action.click(indexXmTaxPage.增值税纳税申报表_小规模纳税人适用_附列资料_保存());
            // 新增页:$("a[name=save]").length 返回1,编辑页返回0
            //
            String length = action.executeJS("return $('a[name=save]').length").toString();
            if (length.equalsIgnoreCase("1")) {// 新增页,
                action.executeJS("$('a[name=save]').click()");
            } else {
                action.executeJS("$($('a[name=update]').get(1)).click()");
            }

            action.sleep(1);
        }
    }

    /**
     * 正式申报
     */
    private void clickSmallOfficialReport(int index) throws IOException {
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        ElementAction action = new ElementAction();
        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

//            action.click(indexXmTaxPage.正式申报());
            Locator locator = new Locator("//*[@id='nav']/div[{index}]/div/ul/li/a/span[1]".replace("{index}", String.valueOf(index)));
            action.click(locator);
            if (!CommonUtil.isDev()) {
                action.alertConfirm();
            } else {
                action.alertDismiss();
            }
            action.sleep(1);
            action.pagefoload(10);
        }
        action.switchToDefaultFrame();
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            String html = action.executeJS("return $(document.body).html()").toString();
            if (html.indexOf("申报成功") == -1) {
                throw new RPAException("未出现回执页面");
            }
        }
    }

    /**
     * belongText 的格式为:所属时期：20181001 至 20181031
     */
    private void applyBelongDate(String belongText) {
        Pattern pattern = Pattern.compile("\\d{8}");
        Matcher matcher = pattern.matcher(belongText);
        if (matcher.find()) {
            belongDateBegin = matcher.group(0);
        }
        if (matcher.find()) {
            belongDateEnd = matcher.group(0);
        }
    }

    private void demo() throws IOException {
        ElementAction action = new ElementAction();
        AddedValueTaxXmTaxPage indexXmTaxPage = new AddedValueTaxXmTaxPage();
        // 新的页面内容,
        action.switchToWindowByTitle("国家税务总局厦门市税务局网上申报系统");
        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

            // 找到左侧 iframe 的第一个下拉菜单,并点击,
            action.click(indexXmTaxPage.balanceSheetMenu());
        }

        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();

        {
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 找到右侧 iframe
            action.switchToFrame(indexXmTaxPage.rightFrame());

            // 定位到 货币资金_期末余额
            action.click(indexXmTaxPage.货币资金_期末余额());

//        action.type(indexXmTaxPage.货币资金_期末余额(), "");
        }

        // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
        action.switchToDefaultFrame();

        {// 点击第一季,并点击,企业会计准则（一般企业）财务报表信息查询,输入 2018-04~2018-06
            // 定位 mainFrame
            action.switchToFrame(indexXmTaxPage.mainFrame());

            // 定位 mainFrame 里面的 左侧 iframe,
            action.switchToFrame(indexXmTaxPage.leftFrame());

            // 找到左侧 iframe 的第一季菜单,并点击,
            action.click(indexXmTaxPage.第一季());

            // 等待下拉菜单展开
            action.until(ExpectedConditions.visibilityOf(action.findElement(indexXmTaxPage.第一季_财务报表信息查询())), 20);

            // 找到左侧 iframe 的第一季菜单-财务报表信息查询,并点击,
            action.click(indexXmTaxPage.第一季_财务报表信息查询());

            // 需要重新切回到顶部 frame, 再往下查找,否则查找不到元素,
            action.switchToDefaultFrame();

            {
                // 定位 mainFrame
                action.switchToFrame(indexXmTaxPage.mainFrame());

                // 找到右侧 iframe
                action.switchToFrame(indexXmTaxPage.rightFrame());

                // 定位到 货币资金_期末余额
                action.click(indexXmTaxPage.查询范围_开始年());
                action.type(indexXmTaxPage.查询范围_开始年(), "2018");

                action.click(indexXmTaxPage.查询范围_开始月());
                action.type(indexXmTaxPage.查询范围_开始月(), "04");

                action.click(indexXmTaxPage.查询范围_结束年());
                action.type(indexXmTaxPage.查询范围_结束月(), "06");

                action.sleep(1);
                action.click(indexXmTaxPage.查找按钮());
//        action.type(indexXmTaxPage.货币资金_期末余额(), "");
            }
        }
    }
}
