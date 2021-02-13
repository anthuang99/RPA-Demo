package org.webdriver.patatiumwebui.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.h2.store.fs.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webdriver.patatiumwebui.Test.XmTaxTest;
import org.webdriver.patatiumwebui.httprequest.ZhangYiDaRequest;
import org.webdriver.patatiumwebui.httprequest.vo.CorpTaxVO;
import org.webdriver.patatiumwebui.httprequest.vo.SettingVO;
import org.webdriver.patatiumwebui.utils.Log;
import org.webdriver.patatiumwebui.web.model.CorpSetting;
import org.webdriver.patatiumwebui.web.service.CorpSettingService;
import org.webdriver.patatiumwebui.web.service.TaxParameterService;
import org.webdriver.patatiumwebui.web.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页,列表展示,取企业信息等
 */
@Controller
@RequestMapping("/corpSetting")
public class CorpSettingController {
    private Log log = new Log(getClass());

    @Autowired
    private CorpSettingService corpSettingService;
    @Autowired
    private TaxParameterService taxParameterService;
    private String browserPathErrorMessage = "浏览器路径不存在对应文件";

    /**
     * 获取企业信息
     * @param request
     * @return
     */
    @RequestMapping("/getCorpSetting.json")
    @ResponseBody
    public Map<String, Object> getCorpSetting(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
        result.put("corpSetting", corpSettingService.getCorpSetting());
        return result;
    }

    /**
     * 保存企业信息
     * @param request
     * @return
     */
    @RequestMapping("/saveCorpSetting.json")
    @ResponseBody
    public Map<String, Object> saveCorpSetting(HttpServletRequest request, @RequestBody CorpSetting corpSetting) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
        corpSettingService.saveOrUpdateCorpSetting(corpSetting);
        return result;
    }

    /**
     * 返回格式
     接口测试成功:
     totalRecord:{totalRecord}
     第一页企业名列表:

     * @param request
     * @return
     */
    @RequestMapping("/pingZhangYiDaRequest.json")
    @ResponseBody
    public Map<String, Object> pingZhangYiDaRequest(HttpServletRequest request) {
        CorpSetting corpSetting = corpSettingService.getCorpSetting();
        if (corpSetting == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", "false");
            result.put("message", "企业用户名为空");
            return result;
        }
        CorpTaxVO corpTaxVO = new CorpTaxVO();
        corpTaxVO.setUserName(corpSetting.getUsername());
        corpTaxVO.setPassword(corpSetting.getPassword());
        corpTaxVO.setDomainName(corpSetting.getDomainName());
        corpTaxVO.setPageNo(1);
        corpTaxVO.setPageSize(20);
        corpTaxVO.setCreateM(DateUtil.getDateShow(new Date(), "yyyyMM"));
        corpTaxVO.setIsReport("0");

        Map<String, Object> result = new HashMap<>();

        ZhangYiDaRequest zhangYiDaRequest = new ZhangYiDaRequest();
        try {
            JSONObject jsonObject = zhangYiDaRequest.getCorpList(corpTaxVO);
            JSONObject info = jsonObject.getJSONObject("info");
            int totalResult = info.getInt("totalRecord");
            StringBuilder sb = new StringBuilder();
            sb.append("接口测试成功:<br />");
            sb.append("企业总数:{totalRecord}<br />".replace("{totalRecord}", String.valueOf(totalResult)));
            if (totalResult > 0) {
                sb.append("第一页企业名列表:<br />");
                JSONArray corpTaxVOLi = info.getJSONArray("records");
                for (int i = 0; i < corpTaxVOLi.length(); i++) {
                    String name = corpTaxVOLi.getJSONObject(i).getString("name");
                    sb.append(name + "<br />");
                }
            }
            result.put("success", "true");
            result.put("message", sb.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.put("success", "false");
        }

        return result;
    }

    @RequestMapping("/pingSelenium.json")
    @ResponseBody
    public Map<String, Object> pingSelenium(HttpServletRequest request, @RequestParam(required = false) String browserPath) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (StringUtils.isNotEmpty(browserPath)) {
                taxParameterService.saveOrUpdateBrowserPathAndType(browserPath);
            }

            new XmTaxTest().pingSelenium();
            result.put("success", "true");
        } catch (Exception e) {
            result.put("success", "false");
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping("/pingCheckcode.json")
    @ResponseBody
    public Map<String, Object> pingCheckcode(HttpServletRequest request, @RequestParam(required = false) String checkcodeOffsetX, @RequestParam(required = false) String checkcodeOffsetY) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (StringUtils.isNotEmpty(checkcodeOffsetX)) {
                taxParameterService.saveOrUpdateCheckcodeOffsetX(checkcodeOffsetX);
            }
            if (StringUtils.isNotEmpty(checkcodeOffsetY)) {
                taxParameterService.saveOrUpdateCheckcodeOffsetY(checkcodeOffsetY);
            }

            XmTaxTest xmTaxTest = new XmTaxTest();
            Map<String, Object> checkcodeResult = xmTaxTest.pingCheckcode();
            result.put("success", "true");
            result.putAll(checkcodeResult);
        } catch (Exception e) {
            result.put("success", "false");
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping("/saveSetting.json")
    @ResponseBody
    public Map<String, Object> saveSetting(HttpServletRequest request, @ModelAttribute SettingVO settingVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (StringUtils.isNotEmpty(settingVO.getBrowserPath())) {
                if (!FileUtils.exists(settingVO.getBrowserPath())) {
                    result.put("success", "false");
                    result.put("message", browserPathErrorMessage);
                    return result;
                }
            }
            CorpSetting corpSetting = new CorpSetting();
            corpSetting.setUsername(settingVO.getUsername());
            corpSetting.setPassword(settingVO.getPassword());
            corpSetting.setDomainName(settingVO.getDomainName());
            corpSettingService.saveOrUpdateCorpSetting(corpSetting);
            taxParameterService.saveOrUpdateSetting(settingVO);

            result.put("success", "true");
        } catch (Exception e) {
            result.put("success", "false");
            log.error(e.getMessage(), e);
        }
        return result;
    }
}
