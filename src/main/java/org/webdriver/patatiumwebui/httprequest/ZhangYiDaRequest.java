package org.webdriver.patatiumwebui.httprequest;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.webdriver.patatiumwebui.config.YmlConfig;
import org.webdriver.patatiumwebui.exception.RPAException;
import org.webdriver.patatiumwebui.httprequest.vo.CorpTaxVO;
import org.webdriver.patatiumwebui.httprequest.vo.WriteBackTaxResultVO;
import org.webdriver.patatiumwebui.utils.HttpUtil;
import org.webdriver.patatiumwebui.utils.Log;
import org.webdriver.patatiumwebui.web.util.DateUtil;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 账益达接口请求
 */
public class ZhangYiDaRequest {
    private static Log log = new Log(ZhangYiDaRequest.class);

    /**
     * 根据企业账号取符合0申报的企业,返回用户名/密码,可以登录厦门国税,分页接口
     * @return
     */
    public JSONObject getCorpList(CorpTaxVO corpTaxVO) {
        YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
        String url = ymlConfig.getCorpListUrl();
        JSONObject params = new JSONObject();
        params.put("userName", corpTaxVO.getUserName());
        params.put("password", corpTaxVO.getPassword());
        params.put("domainName", corpTaxVO.getDomainName());
        params.put("currentPage", String.valueOf(corpTaxVO.getPageNo()));
        params.put("pageSize", String.valueOf(corpTaxVO.getPageSize()));
        params.put("name", ObjectUtils.toString(corpTaxVO.getName(), ""));
        params.put("createM", ObjectUtils.toString(corpTaxVO.getCreateM(), ""));
//        params.put("isReport", ObjectUtils.toString(corpTaxVO.getIsReport(), ""));
        params.put("nationaltaxLoginname", ObjectUtils.toString(corpTaxVO.getNationaltaxLoginname(), ""));
        params.put("declareStatus", ObjectUtils.toString(corpTaxVO.getDeclareStatus(), ""));
//        params.put("isShowPicture", ObjectUtils.toString(corpTaxVO.getIsShowPicture(), ""));

        String content = HttpUtil.doPost(url, params.toString());
        if (StringUtils.isNotEmpty(content)) {
            return new JSONObject(content);
        }
        return null;
    }

    /**
     * 报税结果详情接口
     * @param corpTaxVO
     * @return
     */
    public JSONObject getTaxDeclareDetail(CorpTaxVO corpTaxVO) {
        YmlConfig ymlConfig = (YmlConfig) SpringUtil.getBean("ymlConfig");
        String url = ymlConfig.getTaxDeclareDetailUrl();
        JSONObject params = new JSONObject();
        params.put("userName", corpTaxVO.getUserName());
        params.put("password", corpTaxVO.getPassword());
        params.put("domainName", corpTaxVO.getDomainName());
        params.put("currentPage", String.valueOf(corpTaxVO.getPageNo()));
        params.put("pageSize", String.valueOf(corpTaxVO.getPageSize()));
        params.put("name", ObjectUtils.toString(corpTaxVO.getName(), ""));
        params.put("createM", ObjectUtils.toString(corpTaxVO.getCreateM(), ""));
        params.put("isReport", ObjectUtils.toString(corpTaxVO.getIsReport(), ""));
        params.put("nationaltaxLoginname", ObjectUtils.toString(corpTaxVO.getNationaltaxLoginname(), ""));
        params.put("detailStatus", ObjectUtils.toString(corpTaxVO.getDetailStatus(), ""));
        params.put("declareStatus",ObjectUtils.toString(corpTaxVO.getDeclareStatus(), ""));
        params.put("isShowPicture", ObjectUtils.toString(corpTaxVO.getIsShowPicture(), ""));

        String content = HttpUtil.doPost(url, params.toString());
        if (StringUtils.isNotEmpty(content)) {
            return new JSONObject(content);
        }
        return null;
    }

    /**
     * 回写归属日期
     * @return
     */
    public JSONObject writeBackTaxResult(WriteBackTaxResultVO writeBackTaxResultVO) {
        String nationaltaxLoginname = writeBackTaxResultVO.getNationaltaxLoginname();
        String taxName = writeBackTaxResultVO.getTaxName();
        String screenBase64 = writeBackTaxResultVO.getScreenBase64();
        String belongDateBegin = writeBackTaxResultVO.getBelongDateBegin();
        String belongDateEnd = writeBackTaxResultVO.getBelongDateEnd();
        String taxEndDate = writeBackTaxResultVO.getTaxEndDate();
        boolean success = writeBackTaxResultVO.getSuccess();

        YmlConfig ymlConfig = (YmlConfig)SpringUtil.getBean("ymlConfig");
        String url = ymlConfig.getWriteBackTaxResult();

        JSONObject jsonObject = new JSONObject();
        {
            JSONObject data = new JSONObject();
            data.put("NSRSBH", nationaltaxLoginname);
            data.put("SBQK_IMG", screenBase64);
            if (success) {
                data.put("SBZT_DM", "4");
            } else {
                data.put("SBZT_DM", "3");
            }
            data.put("SB_ZLBH", taxName);
            data.put("SKSSQQ", DateUtil.getDateShow(belongDateBegin, "yyyyMMdd", "yyyy-MM-dd"));
            data.put("SKSSQZ", DateUtil.getDateShow(belongDateEnd, "yyyyMMdd", "yyyy-MM-dd"));
            data.put("YCXX", ObjectUtils.toString(writeBackTaxResultVO.getYcxx(), ""));
            data.put("CREATE_M", DateUtil.getDateShow(new Date(), "yyyyMM"));
            data.put("YSBQX", taxEndDate);

            jsonObject.put("DATA", data);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("validateJson", getCheckJSON());
        params.put("dataJson", jsonObject.toString());

        String content = HttpUtil.doPost(url, params);
        if (StringUtils.isNotEmpty(content)) {
            return new JSONObject(content);
        }
        return null;
    }

    /**
     * 返回 MD5(ts+cs+AppKey)
     * private final static String ZYD_TOKEN_KEY_CS = "ZYD_XM_TAX_CLIENT";
     * private final static String ZYD_TOKEN_KEY_CHS = "TAX_INTERFACE_ZYD_XM_CLIENT";
     * @return
     */
    private static String getCheckJSON() {
        YmlConfig ymlConfig = (YmlConfig)SpringUtil.getBean("ymlConfig");
        String cs = ymlConfig.getZyd_token_key_cs();
        String appKey = ymlConfig.getZyd_token_key_chs();

        JSONObject validateJson = new JSONObject();
        try {
            //检验数据拼装(
            validateJson.put("cs", cs);

            Date date = new Date();
//            String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
            String dateStr = String.valueOf(System.currentTimeMillis());
            validateJson.put("ts", dateStr);

            //MD5(ts+cs+AppKey)
            String chs = MD5Util.convert(dateStr + cs + appKey);
            validateJson.put("chs", chs);
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
            throw new RPAException(e);
        }
        return validateJson.toString();
    }
}
