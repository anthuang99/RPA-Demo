package org.webdriver.patatiumwebui.action;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.webdriver.patatiumwebui.action.vo.TaxVo;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.httprequest.ZhangYiDaRequest;
import org.webdriver.patatiumwebui.httprequest.vo.WriteBackTaxResultVO;
import org.webdriver.patatiumwebui.pageObject.StampTaxXmTaxPage;
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
 * 印花税
 */
public class StampTaxXmTaxAction extends TestBaseCase {
    private Log log = new Log(this.getClass());
    private String belongDateBegin;
    private String belongDateEnd;

    /**
     * 报税
     * @throws IOException
     */
    public boolean applyTax(TaxVo taxVo) {
        String nationaltaxLoginname = taxVo.getNationaltaxLoginname();
        ElementAction action = new ElementAction();
        try {
            // 新的页面内容,
            TaxUtil.switchToMainWindow();

            // 点击右侧页面的下一步
            clickNext(nationaltaxLoginname);

            // 选择一个应征凭证,并保存,
            clickAddVoucher(taxVo);

            // 确认提交
            clickConfirm(nationaltaxLoginname);

            JSONObject result = writeBackBelongDate(nationaltaxLoginname, "", true);
            return result != null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            writeBackBelongDate(nationaltaxLoginname, e.getMessage(), false);
            return false;
        }
    }

    /**
     * 点击下一步
     */
    private void clickNext(String nationaltaxLoginname) throws IOException {
        StampTaxXmTaxPage stampTaxXmTaxPage = new StampTaxXmTaxPage();
        ElementAction action = new ElementAction();
        action.switchToDefaultFrame();

        // 定位顶部页面包含的 table_content,
        action.switchToFrame(stampTaxXmTaxPage.table_content());

        // table_content 里面包含的 table_right_content
        action.switchToFrame(stampTaxXmTaxPage.table_right_content());

        // 取得所属时期,并保存到私有变量中,后面调用账益达接口回写成功或失败时,会用到
        applyBelongDate(action.getAttribute(stampTaxXmTaxPage.税款所属期(), "value"));

        // 点击下一步
        action.DisplayElement(stampTaxXmTaxPage.submitBtn());
//        action.click(stampTaxXmTaxPage.submitBtn());
        action.executeJS("$('#submitBtn').click()");// 直接js,避免 IE 无法执行,
        action.sleep(1);
    }

    /**
     * 点击添加申报记录
     * @throws IOException
     */
    private void clickAddVoucher(TaxVo taxVo) throws IOException {
        StampTaxXmTaxPage stampTaxXmTaxPage = new StampTaxXmTaxPage();
        ElementAction action = new ElementAction();

        action.switchToDefaultFrame();
        {
            // 定位顶部页面包含的 table_content,
            action.switchToFrame(stampTaxXmTaxPage.table_content());

            // table_content 里面包含的 table_right_content
            action.switchToFrame(stampTaxXmTaxPage.table_right_content());

            // 点击添加申报记录
//            action.DisplayElement(stampTaxXmTaxPage.添加申报记录());
//            action.click(stampTaxXmTaxPage.添加申报记录());
            action.executeJS("$('#myForm1 > div > table > tbody > tr:nth-child(2) > td > div > input:nth-child(1)').click()");// 直接js,避免 IE 无法执行,
            action.sleep(1);
        }

        // 选择应征凭证名称:购销合同并并保存
        action.switchToDefaultFrame();
        {
            // 定位顶部页面包含的 table_content,
            action.switchToFrame(stampTaxXmTaxPage.table_content());

            // table_content 里面包含的 table_right_content
            action.switchToFrame(stampTaxXmTaxPage.table_right_content());

            /*
<option value="101110101" selected="">购销合同</option>
<option value="101110102">加工承揽合同</option>
<option value="101110103">建设工程勘察设计合同</option>
<option value="101110104">建筑安装工程承包合同</option>
<option value="101110105">财产租赁合同</option>
<option value="101110106">货物运输合同(按运输费用万分之五贴花)</option>
<option value="101110107">仓储保管合同</option>
<option value="101110108">借款合同</option>
<option value="101110109">财产保险合同</option>
<option value="101110110">技术合同</option>
<option value="101110200">产权转移书据</option>
<option value="101110400">权利、许可证照</option>
<option value="101110501">资金账簿</option>
<option value="101110599">其他营业账簿</option>
<option value="101119800">其他凭证</option>
            */
            // 应征凭证名称默认为:购销合同,
            if (StringUtils.isNotEmpty(taxVo.getStampTaxBelongItem())) {
                action.click(stampTaxXmTaxPage.应征凭证名称());
//                action.selectByValue(stampTaxXmTaxPage.应征凭证名称(), "101110101");
                action.selectByText(stampTaxXmTaxPage.应征凭证名称(), taxVo.getStampTaxBelongItem());
            }
//            action.click(stampTaxXmTaxPage.添加申报记录下一步());
            action.executeJS("$('#myForm2 > table > tbody > tr:nth-child(3) > td > table > tbody > tr:nth-child(4) > td > div > input:nth-child(1)').click()");// 直接js,避免 IE 无法执行,
            action.sleep(1);
        }

        // 填写申报记录
        action.switchToDefaultFrame();
        {
            // 定位顶部页面包含的 table_content,
            action.switchToFrame(stampTaxXmTaxPage.table_content());

            // table_content 里面包含的 table_right_content
            action.switchToFrame(stampTaxXmTaxPage.table_right_content());

            // 计税金额或件数
            action.click(stampTaxXmTaxPage.计税金额或件数());
            action.type(stampTaxXmTaxPage.计税金额或件数(), "0");

            action.executeJS("jsjeChange()");

            /*
<option value="0009011701" title="0009011701|对个人销售或购买住房暂免征收印花税|《财政部 国家税务总局关于调整房地产交易环节税收政策的通知》 财税〔2008〕137号第二条">0009011701|对个人销售或购买住房暂免征收印花税|《财政部 国家税务总局关于调整房地产交易环节税收政策的通知》 财税〔2008〕137号第二条</option>
<option value="0009011702" title="0009011702|对廉租住房、经济适用住房经营管理单位与廉租住房、经济适用住房相关的印花税以及廉租住房承租人、经济适用住房购买人涉及的印花税予以免征|《财政部 国家税务总局关于廉租住房经济适用住房和住房租赁有关税收政策的通知》 财税〔2008〕24号第一条第（四）项">0009011702|对廉租住房、经济适用住房经营管理单位与廉租住房、经济适用住房相关的印花税以及廉租住房承租人、经济适用住房购买人涉及的印花税予以免征|《财政部 国家税务总局关于廉租住房经济适用住房和住房租赁有关税收政策的通知》 财税〔2008〕24号第一条第（四）项</option>
<option value="0009011704" title="0009011704|保障性住房免征印花税|《财政部 国家税务总局关于棚户区改造有关税收政策的通知》 财税〔2013〕101号第一条">0009011704|保障性住房免征印花税|《财政部 国家税务总局关于棚户区改造有关税收政策的通知》 财税〔2013〕101号第一条</option>
<option value="0009011706" title="0009011706|对开发商建造廉租房和经济适用住房有关印花税予以免征|《财政部 国家税务总局关于廉租住房经济适用住房和住房租赁有关税收政策的通知》 财税〔2008〕24号第一条第（四）项">0009011706|对开发商建造廉租房和经济适用住房有关印花税予以免征|《财政部 国家税务总局关于廉租住房经济适用住房和住房租赁有关税收政策的通知》 财税〔2008〕24号第一条第（四）项</option>
<option value="0009011707" title="0009011707|免征个人出租承租住房签订的租赁合同印花税|《财政部 国家税务总局关于廉租住房经济适用住房和住房租赁有关税收政策的通知》 财税〔2008〕24号第二条第（二）项">0009011707|免征个人出租承租住房签订的租赁合同印花税|《财政部 国家税务总局关于廉租住房经济适用住房和住房租赁有关税收政策的通知》 财税〔2008〕24号第二条第（二）项</option>
<option value="0009011709" title="0009011709|对公租房经营管理单位建造、管理公租房、购买住房作为公租房免征印花税|《财政部 国家税务总局关于公共租赁住房税收优惠政策的通知》 财税〔2015〕139号第二条">0009011709|对公租房经营管理单位建造、管理公租房、购买住房作为公租房免征印花税|《财政部 国家税务总局关于公共租赁住房税收优惠政策的通知》 财税〔2015〕139号第二条</option>
<option value="0009011710" title="0009011710|对公共租赁住房双方免征租赁协议印花税|《财政部 国家税务总局关于公共租赁住房税收优惠政策的通知》 财税〔2015〕139号第三条">0009011710|对公共租赁住房双方免征租赁协议印花税|《财政部 国家税务总局关于公共租赁住房税收优惠政策的通知》 财税〔2015〕139号第三条</option>
<option value="0009012701" title="0009012701|房地产管理部门与个人订立的租房合同免征印花税|《国家税务局关于印花税若干具体问题的规定》 （88）国税地字第025号第三条">0009012701|房地产管理部门与个人订立的租房合同免征印花税|《国家税务局关于印花税若干具体问题的规定》 （88）国税地字第025号第三条</option>
<option value="0009012702" title="0009012702|铁路、公路、航运、水路承运快件行李、包裹开具的托运单据免征印花税|《国家税务局关于印花税若干具体问题的规定》 （88）国税地字第025号第六条">0009012702|铁路、公路、航运、水路承运快件行李、包裹开具的托运单据免征印花税|《国家税务局关于印花税若干具体问题的规定》 （88）国税地字第025号第六条</option>
<option value="0009033301" title="0009033301|青藏铁路公司及其所属单位营业账簿免征印花税|《财政部 国家税务总局关于青藏铁路公司运营期间有关税收等政策问题的通知》 财税〔2007〕11号第二条">0009033301|青藏铁路公司及其所属单位营业账簿免征印花税|《财政部 国家税务总局关于青藏铁路公司运营期间有关税收等政策问题的通知》 财税〔2007〕11号第二条</option>
<option value="0009041503" title="0009041503|对金融机构与小型企业、微型企业签订的借款合同免征印花税|《财政部 税务总局关于支持小微企业融资有关税收政策的通知》 财税〔2017〕77号第二条">0009041503|对金融机构与小型企业、微型企业签订的借款合同免征印花税|《财政部 税务总局关于支持小微企业融资有关税收政策的通知》 财税〔2017〕77号第二条</option>
<option value="0009052401" title="0009052401|企业改制、重组过程中印花税予以免征|《财政部 国家税务总局关于中国邮政储蓄银行改制上市有关税收政策的通知》 财税〔2013〕53号第五条">0009052401|企业改制、重组过程中印花税予以免征|《财政部 国家税务总局关于中国邮政储蓄银行改制上市有关税收政策的通知》 财税〔2013〕53号第五条</option>
<option value="0009052501" title="0009052501|对中国铁路总公司改革过程中涉及的印花税进行减免|《财政部 国家税务总局关于组建中国铁路总公司有关印花税政策的通知》 财税〔2015〕57号">0009052501|对中国铁路总公司改革过程中涉及的印花税进行减免|《财政部 国家税务总局关于组建中国铁路总公司有关印花税政策的通知》 财税〔2015〕57号</option>
<option value="0009059901" title="0009059901|企业改制、重组过程中印花税予以免征|《财政部 国家税务总局关于明确中国邮政集团公司邮政速递物流业务重组改制过程中有关契税和印花税政策的通知》 财税〔2010〕92号第二、三、四条">0009059901|企业改制、重组过程中印花税予以免征|《财政部 国家税务总局关于明确中国邮政集团公司邮政速递物流业务重组改制过程中有关契税和印花税政策的通知》 财税〔2010〕92号第二、三、四条</option>
<option value="0009059902" title="0009059902|企业改制、重组过程中印花税予以免征|《财政部 国家税务总局关于企业改制过程中有关印花税政策的通知》 财税〔2003〕183号">0009059902|企业改制、重组过程中印花税予以免征|《财政部 国家税务总局关于企业改制过程中有关印花税政策的通知》 财税〔2003〕183号</option>
<option value="0009059903" title="0009059903|对企业改制、资产整合过程中涉及的所有产权转移书据及股权转让协议印花税予以免征|《财政部 国家税务总局关于中国联合网络通信集团有限公司转让CDMA网及其用户资产企业合并资产整合过程中涉及的增值税营业税印花税和土地增值税政策问题的通知》 财税〔2011〕13号第五、六、七条">0009059903|对企业改制、资产整合过程中涉及的所有产权转移书据及股权转让协议印花税予以免征|《财政部 国家税务总局关于中国联合网络通信集团有限公司转让CDMA网及其用户资产企业合并资产整合过程中涉及的增值税营业税印花税和土地增值税政策问题的通知》 财税〔2011〕13号第五、六、七条</option>
<option value="0009059904" title="0009059904|对联通新时空移动通信有限公司接受中国联合网络通信集团固定通信资产增加资本金涉及的印花税予以免征|《财政部 国家税务总局关于中国联合网络通信集团有限公司转让CDMA网及其用户资产企业合并资产整合过程中涉及的增值税营业税印花税和土地增值税政策问题的通知》 财税〔2011〕13号第八条">0009059904|对联通新时空移动通信有限公司接受中国联合网络通信集团固定通信资产增加资本金涉及的印花税予以免征|《财政部 国家税务总局关于中国联合网络通信集团有限公司转让CDMA网及其用户资产企业合并资产整合过程中涉及的增值税营业税印花税和土地增值税政策问题的通知》 财税〔2011〕13号第八条</option>
<option value="0009059905" title="0009059905|对2011年中国移动增加的资本公积、股权调整协议、盈余公积转增实收资本印花税予以免征|《财政部 国家税务总局关于中国移动集团股权结构调整及盈余公积转增实收资本有关印花税政策的通知》 财税〔2012〕62号第一、二条">0009059905|对2011年中国移动增加的资本公积、股权调整协议、盈余公积转增实收资本印花税予以免征|《财政部 国家税务总局关于中国移动集团股权结构调整及盈余公积转增实收资本有关印花税政策的通知》 财税〔2012〕62号第一、二条</option>
<option value="0009081502" title="0009081502|买卖封闭式证券投资基金免征印花税|《财政部 国家税务总局关于对买卖封闭式证券投资基金继续予以免征印花税的通知》 财税〔2004〕173号">0009081502|买卖封闭式证券投资基金免征印花税|《财政部 国家税务总局关于对买卖封闭式证券投资基金继续予以免征印花税的通知》 财税〔2004〕173号</option>
<option value="0009081503" title="0009081503|股权分置改革过程中发生的股权转让免征印花税|《财政部 国家税务总局关于股权分置试点改革有关税收政策问题的通知》 财税〔2005〕103号第一条">0009081503|股权分置改革过程中发生的股权转让免征印花税|《财政部 国家税务总局关于股权分置试点改革有关税收政策问题的通知》 财税〔2005〕103号第一条</option>
<option value="0009081504" title="0009081504|贴息贷款合同免征印花税|《财政部 国家税务总局关于国家开发银行缴纳印花税问题的复函 》 财税字〔1995〕47号第一条">0009081504|贴息贷款合同免征印花税|《财政部 国家税务总局关于国家开发银行缴纳印花税问题的复函 》 财税字〔1995〕47号第一条</option>
<option value="0009081505" title="0009081505|国有股东向全国社会保障基金理事会转持国有股免征证券（股票）交易印花税|《财政部 国家税务总局关于境内证券市场转持部分国有股充实全国社会保障基金有关证券（股票）交易印花税政策的通知》 财税〔2009〕103号">0009081505|国有股东向全国社会保障基金理事会转持国有股免征证券（股票）交易印花税|《财政部 国家税务总局关于境内证券市场转持部分国有股充实全国社会保障基金有关证券（股票）交易印花税政策的通知》 财税〔2009〕103号</option>
<option value="0009081509" title="0009081509|企业改制、重组过程中印花税予以免征|《财政部 国家税务总局关于外国银行分行改制为外商独资银行有关税收问题的通知》 财税〔2007〕45号第三条">0009081509|企业改制、重组过程中印花税予以免征|《财政部 国家税务总局关于外国银行分行改制为外商独资银行有关税收问题的通知》 财税〔2007〕45号第三条</option>
<option value="0009081510" title="0009081510|信贷资产证券化免征印花税|《财政部 国家税务总局关于信贷资产证券化有关税收政策问题的通知》 财税〔2006〕5号第一条">0009081510|信贷资产证券化免征印花税|《财政部 国家税务总局关于信贷资产证券化有关税收政策问题的通知》 财税〔2006〕5号第一条</option>
<option value="0009081512" title="0009081512|证券投资者保护基金免征印花税|《财政部 国家税务总局关于证券投资者保护基金有关印花税政策的通知》 财税〔2006〕104号">0009081512|证券投资者保护基金免征印花税|《财政部 国家税务总局关于证券投资者保护基金有关印花税政策的通知》 财税〔2006〕104号</option>
<option value="0009081515" title="0009081515|无息、贴息贷款合同免征印花税|《中华人民共和国印花税暂行条例实施细则》 财税字〔1988〕255号第十三条第（二）项">0009081515|无息、贴息贷款合同免征印花税|《中华人民共和国印花税暂行条例实施细则》 财税字〔1988〕255号第十三条第（二）项</option>
<option value="0009081516" title="0009081516|被撤销金融机构接收债权、清偿债务签订的产权转移书据免征印花税|《财政部 国家税务总局关于被撤销金融机构有关税收政策问题的通知》 财税〔2003〕141号第二条第1项">0009081516|被撤销金融机构接收债权、清偿债务签订的产权转移书据免征印花税|《财政部 国家税务总局关于被撤销金融机构有关税收政策问题的通知》 财税〔2003〕141号第二条第1项</option>
<option value="0009081517" title="0009081517|外国政府或者国际金融组织向我国政府及国家金融机构提供优惠贷款所书立的合同免征印花税|《中华人民共和国印花税暂行条例实施细则》 财税字〔1988〕255号第十三条第（三）项">0009081517|外国政府或者国际金融组织向我国政府及国家金融机构提供优惠贷款所书立的合同免征印花税|《中华人民共和国印花税暂行条例实施细则》 财税字〔1988〕255号第十三条第（三）项</option>
<option value="0009081518" title="0009081518|新设立的资金账簿免征印花税|《财政部 税务总局关于保险保障基金有关税收政策问题的通知》 财税〔2018〕41号第二条第一款">0009081518|新设立的资金账簿免征印花税|《财政部 税务总局关于保险保障基金有关税收政策问题的通知》 财税〔2018〕41号第二条第一款</option>
<option value="0009081519" title="0009081519|对保险公司进行风险处置和破产救助过程中签订的产权转移书据免征印花税|《财政部 税务总局关于保险保障基金有关税收政策问题的通知》 财税〔2018〕41号第二条第二款">0009081519|对保险公司进行风险处置和破产救助过程中签订的产权转移书据免征印花税|《财政部 税务总局关于保险保障基金有关税收政策问题的通知》 财税〔2018〕41号第二条第二款</option>
<option value="0009081520" title="0009081520|对保险公司进行风险处置过程中与中国人民银行签订的再贷款合同免征印花税|《财政部 税务总局关于保险保障基金有关税收政策问题的通知》 财税〔2018〕41号第二条第三款">0009081520|对保险公司进行风险处置过程中与中国人民银行签订的再贷款合同免征印花税|《财政部 税务总局关于保险保障基金有关税收政策问题的通知》 财税〔2018〕41号第二条第三款</option>
<option value="0009081521" title="0009081521|以保险保障基金自有财产和接收的受偿资产与保险公司签订的财产保险合同免征印花税|《财政部 税务总局关于保险保障基金有关税收政策问题的通知》 财税〔2018〕41号第二条第四款">0009081521|以保险保障基金自有财产和接收的受偿资产与保险公司签订的财产保险合同免征印花税|《财政部 税务总局关于保险保障基金有关税收政策问题的通知》 财税〔2018〕41号第二条第四款</option>
<option value="0009083901" title="0009083901|国有商业银行划转给金融资产管理公司的资产免征印花税|《财政部 国家税务总局关于4家资产管理公司接收资本金项下的资产在办理过户时有关税收政策问题的通知》 财税〔2003〕21号">0009083901|国有商业银行划转给金融资产管理公司的资产免征印花税|《财政部 国家税务总局关于4家资产管理公司接收资本金项下的资产在办理过户时有关税收政策问题的通知》 财税〔2003〕21号</option>
<option value="0009083902" title="0009083902|证券投资基金免征印花税|《财政部 国家税务总局关于开放式证券投资基金有关税收问题的通知》 财税〔2002〕128号第三条">0009083902|证券投资基金免征印花税|《财政部 国家税务总局关于开放式证券投资基金有关税收问题的通知》 财税〔2002〕128号第三条</option>
<option value="0009083903" title="0009083903|金融资产管理公司收购、承接、处置不良资产免征印花税|《财政部 国家税务总局关于中国信达等4家金融资产管理公司税收政策问题的通知》 财税〔2001〕10号">0009083903|金融资产管理公司收购、承接、处置不良资产免征印花税|《财政部 国家税务总局关于中国信达等4家金融资产管理公司税收政策问题的通知》 财税〔2001〕10号</option>
<option value="0009083904" title="0009083904|农村信用社接受农村合作基金会财产产权转移书免征印花税|《中国人民银行 农业部 国家发展计划委员会 财政部 国家税务总局关于免缴农村信用社接收农村合作基金会财产产权过户税费的通知》 银发〔2000〕21号">0009083904|农村信用社接受农村合作基金会财产产权转移书免征印花税|《中国人民银行 农业部 国家发展计划委员会 财政部 国家税务总局关于免缴农村信用社接收农村合作基金会财产产权过户税费的通知》 银发〔2000〕21号</option>
<option value="0009083906" title="0009083906|对中国信达资产管理股份有限公司、中国华融资产管理股份有限公司及其分支机构处置剩余政策性剥离不良资产以及出让上市公司股权免征印花税|《财政部 国家税务总局关于中国信达资产管理股份有限公司等4家金融资产管理公司有关税收政策问题的通知》 财税〔2013〕56号第一条">0009083906|对中国信达资产管理股份有限公司、中国华融资产管理股份有限公司及其分支机构处置剩余政策性剥离不良资产以及出让上市公司股权免征印花税|《财政部 国家税务总局关于中国信达资产管理股份有限公司等4家金融资产管理公司有关税收政策问题的通知》 财税〔2013〕56号第一条</option>
<option value="0009092301" title="0009092301|对农民专业合作社与本社成员签订的农业产品和农业生产资料购销合同，免征印花税|《财政部 国家税务总局关于农民专业合作社有关税收政策的通知》 财税〔2008〕81号第四条">0009092301|对农民专业合作社与本社成员签订的农业产品和农业生产资料购销合同，免征印花税|《财政部 国家税务总局关于农民专业合作社有关税收政策的通知》 财税〔2008〕81号第四条</option>
<option value="0009092305" title="0009092305|饮水工程运营管理单位为建设饮水工程取得土地使用权签订的产权转移书据，以及与施工单位签订的建设工程承包合同免征印花税|《财政部 国家税务总局关于继续实行农村饮水安全工程建设运营税收优惠政策的通知》 财税〔2016〕19号第二条">0009092305|饮水工程运营管理单位为建设饮水工程取得土地使用权签订的产权转移书据，以及与施工单位签订的建设工程承包合同免征印花税|《财政部 国家税务总局关于继续实行农村饮水安全工程建设运营税收优惠政策的通知》 财税〔2016〕19号第二条</option>
<option value="0009092306" title="0009092306|农村集体经济组织清产核资免征印花税|《财政部 税务总局关于支持农村集体产权制度改革有关税收政策的通知》 财税〔2017〕55号第二条第二款">0009092306|农村集体经济组织清产核资免征印花税|《财政部 税务总局关于支持农村集体产权制度改革有关税收政策的通知》 财税〔2017〕55号第二条第二款</option>
<option value="0009101401" title="0009101401|对财产所有人将财产赠给学校所书立的书据免征印花税|《财政部 国家税务总局关于教育税收政策的通知》 财税〔2004〕39号第二条">0009101401|对财产所有人将财产赠给学校所书立的书据免征印花税|《财政部 国家税务总局关于教育税收政策的通知》 财税〔2004〕39号第二条</option>
<option value="0009101401" title="0009101401|财产所有人将财产赠给政府、社会福利单位、学校所立的书据|《财政部 国家税务总局关于教育税收政策的通知》 财税〔2004〕39号第二条">0009101401|财产所有人将财产赠给政府、社会福利单位、学校所立的书据|《财政部 国家税务总局关于教育税收政策的通知》 财税〔2004〕39号第二条</option>
<option value="0009101405" title="0009101405|高校学生公寓租赁合同免征印花税|《财政部 国家税务总局关于继续执行高校学生公寓和食堂有关税收政策的通知》 财税〔2016〕82号第一条">0009101405|高校学生公寓租赁合同免征印花税|《财政部 国家税务总局关于继续执行高校学生公寓和食堂有关税收政策的通知》 财税〔2016〕82号第一条</option>
<option value="0009102905" title="0009102905|对北京冬奥组委、北京冬奥会测试赛赛事组委会使用的营业账簿和签订的各类合同免征印花税|《财政部 税务总局 海关总署关于北京2022年冬奥会和冬残奥会税收政策的通知》 财税〔2017〕60号第一条第（九）款">0009102905|对北京冬奥组委、北京冬奥会测试赛赛事组委会使用的营业账簿和签订的各类合同免征印花税|《财政部 税务总局 海关总署关于北京2022年冬奥会和冬残奥会税收政策的通知》 财税〔2017〕60号第一条第（九）款</option>
<option value="0009102906" title="0009102906|对国际奥委会签订的与北京2022年冬奥会有关的各类合同，免征国际奥委会的印花税|《财政部 税务总局 海关总署关于北京2022年冬奥会和冬残奥会税收政策的通知》 财税〔2017〕60号第二条第（二）款">0009102906|对国际奥委会签订的与北京2022年冬奥会有关的各类合同，免征国际奥委会的印花税|《财政部 税务总局 海关总署关于北京2022年冬奥会和冬残奥会税收政策的通知》 财税〔2017〕60号第二条第（二）款</option>
<option value="0009102907" title="0009102907|对中国奥委会签订的与北京2022年冬奥会有关的各类合同，免征中国奥委会的印花税|《财政部 税务总局 海关总署关于北京2022年冬奥会和冬残奥会税收政策的通知》 财税〔2017〕60号第二条第（二）款">0009102907|对中国奥委会签订的与北京2022年冬奥会有关的各类合同，免征中国奥委会的印花税|《财政部 税务总局 海关总署关于北京2022年冬奥会和冬残奥会税收政策的通知》 财税〔2017〕60号第二条第（二）款</option>
<option value="0009102908" title="0009102908|对国际残奥委会取得的与北京2022年冬残奥会有关的收入免征印花税|《财政部 税务总局 海关总署关于北京2022年冬奥会和冬残奥会税收政策的通知》 财税〔2017〕60号第二条第（五）款">0009102908|对国际残奥委会取得的与北京2022年冬残奥会有关的收入免征印花税|《财政部 税务总局 海关总署关于北京2022年冬奥会和冬残奥会税收政策的通知》 财税〔2017〕60号第二条第（五）款</option>
<option value="0009102909" title="0009102909|对中国残奥委会取得的由北京冬奥组委分期支付的收入免征印花税|《财政部 税务总局 海关总署关于北京2022年冬奥会和冬残奥会税收政策的通知》 财税〔2017〕60号第二条第（六）款">0009102909|对中国残奥委会取得的由北京冬奥组委分期支付的收入免征印花税|《财政部 税务总局 海关总署关于北京2022年冬奥会和冬残奥会税收政策的通知》 财税〔2017〕60号第二条第（六）款</option>
<option value="0009102910" title="0009102910|对财产所有人将财产捐赠给北京冬奥组委所书立的产权转移书据免征印花税|《财政部 税务总局 海关总署关于北京2022年冬奥会和冬残奥会税收政策的通知》 财税〔2017〕60号第三条第（四）款">0009102910|对财产所有人将财产捐赠给北京冬奥组委所书立的产权转移书据免征印花税|《财政部 税务总局 海关总署关于北京2022年冬奥会和冬残奥会税收政策的通知》 财税〔2017〕60号第三条第（四）款</option>
<option value="0009103201" title="0009103201|发行单位之间，发行单位与订阅单位或个人之间书立的征订凭证，暂免征印花税|《国家税务局关于图书、报刊等征订凭证征免印花税问题的通知》 （89）国税地字第142号第二条">0009103201|发行单位之间，发行单位与订阅单位或个人之间书立的征订凭证，暂免征印花税|《国家税务局关于图书、报刊等征订凭证征免印花税问题的通知》 （89）国税地字第142号第二条</option>
<option value="0009103203" title="0009103203|文化单位转制为企业时的印花税优惠|《财政部 国家税务总局 中宣部关于继续实施文化体制改革中经营性文化事业单位转制为企业若干税收政策的通知》 财税〔2014〕84号第一条第（四）项">0009103203|文化单位转制为企业时的印花税优惠|《财政部 国家税务总局 中宣部关于继续实施文化体制改革中经营性文化事业单位转制为企业若干税收政策的通知》 财税〔2014〕84号第一条第（四）项</option>
<option value="0009120601" title="0009120601|财产所有人将财产赠给政府、社会福利单位、学校所立的书据|《中华人民共和国印花税暂行条例》 中华人民共和国国务院令第11号第四条第2项">0009120601|财产所有人将财产赠给政府、社会福利单位、学校所立的书据|《中华人民共和国印花税暂行条例》 中华人民共和国国务院令第11号第四条第2项</option>
<option value="0009121301" title="0009121301|特殊货运凭证免征印花税|《国家税务总局关于货运凭证征收印花税几个具体问题的通知》 国税发〔1990〕173号">0009121301|特殊货运凭证免征印花税|《国家税务总局关于货运凭证征收印花税几个具体问题的通知》 国税发〔1990〕173号</option>
<option value="0009121302" title="0009121302|免征飞机租赁企业购机环节购销合同印花税|《财政部 国家税务总局关于飞机租赁企业有关印花税政策的通知》 财税〔2014〕18号">0009121302|免征飞机租赁企业购机环节购销合同印花税|《财政部 国家税务总局关于飞机租赁企业有关印花税政策的通知》 财税〔2014〕18号</option>
<option value="0009122602" title="0009122602|对国家石油储备基地第一期项目建设过程中涉及的印花税，予以免征|《财政部 国家税务总局关于国家石油储备基地建设有关税收政策的通知》 财税〔2005〕23号第一条">0009122602|对国家石油储备基地第一期项目建设过程中涉及的印花税，予以免征|《财政部 国家税务总局关于国家石油储备基地建设有关税收政策的通知》 财税〔2005〕23号第一条</option>
<option value="0009122604" title="0009122604|对国家石油储备基地第一期项目建设过程中涉及的印花税，予以免征|《财政部 国家税务总局关于国家石油储备基地有关税收政策的通知》 财税〔2011〕80号">0009122604|对国家石油储备基地第一期项目建设过程中涉及的印花税，予以免征|《财政部 国家税务总局关于国家石油储备基地有关税收政策的通知》 财税〔2011〕80号</option>
<option value="0009122604" title="0009122604|对国家石油储备基地第二期项目建设过程中应缴的印花税，予以免征|《财政部 国家税务总局关于国家石油储备基地有关税收政策的通知》 财税〔2011〕80号">0009122604|对国家石油储备基地第二期项目建设过程中应缴的印花税，予以免征|《财政部 国家税务总局关于国家石油储备基地有关税收政策的通知》 财税〔2011〕80号</option>
<option value="0009122605" title="0009122605|储备公司资金账簿和购销合同印花税减免|《财政部 国家税务总局关于部分国家储备商品有关税收政策的通知》 财税〔2016〕28号第一条">0009122605|储备公司资金账簿和购销合同印花税减免|《财政部 国家税务总局关于部分国家储备商品有关税收政策的通知》 财税〔2016〕28号第一条</option>
<option value="0009129904" title="0009129904|其他|《中华人民共和国印花税暂行条例》 中华人民共和国国务院令第11号第四条第1项">0009129904|其他|《中华人民共和国印花税暂行条例》 中华人民共和国国务院令第11号第四条第1项</option>
<option value="0009129904" title="0009129904|已缴纳印花税的凭证的副本或者抄本免纳印花税|《中华人民共和国印花税暂行条例》 中华人民共和国国务院令第11号第四条第1项">0009129904|已缴纳印花税的凭证的副本或者抄本免纳印花税|《中华人民共和国印花税暂行条例》 中华人民共和国国务院令第11号第四条第1项</option>
<option value="0009129906" title="0009129906|资金账簿减半征收印花税|《财政部 税务总局关于对营业账簿减免印花税的通知》 财税〔2018〕50号">0009129906|资金账簿减半征收印花税|《财政部 税务总局关于对营业账簿减免印花税的通知》 财税〔2018〕50号</option>
<option value="0009129907" title="0009129907|其他账簿免征印花税|《财政部 税务总局关于对营业账簿减免印花税的通知》 财税〔2018〕50号">0009129907|其他账簿免征印花税|《财政部 税务总局关于对营业账簿减免印花税的通知》 财税〔2018〕50号</option>
<option value="0009129999" title="0009129999|其他|其他">0009129999|其他|其他</option>
             */
            // 减免性质,默认放空,
//            action.click(stampTaxXmTaxPage.减免性质());
//            action.selectByValue(stampTaxXmTaxPage.减免性质(), "0009041503");
            // 可能还需要输入本期减免税额

            // 本期应纳税额
//            action.executeJS("$('#myForm3 > table > tbody > tr:nth-child(3) > td > table:nth-child(1) > tbody > tr:nth-child(6) > td > table > tbody > tr:nth-child(5) > td:nth-child(3) > input').click()");// 直接js,避免 IE 无法执行,
//            action.executeJS("$('#myForm3 > table > tbody > tr:nth-child(3) > td > table:nth-child(1) > tbody > tr:nth-child(6) > td > table > tbody > tr:nth-child(5) > td:nth-child(3) > input').val('0')");// 直接js,避免 IE 无法执行,


//            action.click(stampTaxXmTaxPage.确认添加());
//            action.executeJS("$('#myForm3 > table > tbody > tr:nth-child(3) > td > table:nth-child(1) > tbody > tr:nth-child(8) > td > div > input:nth-child(1)').click()");// 直接js,避免 IE 无法执行,
            action.executeJS("qrtj('tj')");
            action.sleep(1);
        }
    }

    /**
     * 确认提交
     * @throws IOException
     */
    private void clickConfirm(String nationaltaxLoginname) throws IOException {
        StampTaxXmTaxPage stampTaxXmTaxPage = new StampTaxXmTaxPage();
        ElementAction action = new ElementAction();
        action.switchToDefaultFrame();

        // 定位顶部页面包含的 table_content,
        action.switchToFrame(stampTaxXmTaxPage.table_content());

        // table_content 里面包含的 table_right_content
        action.switchToFrame(stampTaxXmTaxPage.table_right_content());

        // 点击确认提交
        if (!CommonUtil.isDev()) {
//            action.click(stampTaxXmTaxPage.确认提交());
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
        String taxName = TaxConstant.XM_YHS;
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
