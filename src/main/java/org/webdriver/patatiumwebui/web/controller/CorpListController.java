package org.webdriver.patatiumwebui.web.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webdriver.patatiumwebui.httprequest.ZhangYiDaRequest;
import org.webdriver.patatiumwebui.httprequest.vo.CorpTaxVO;
import org.webdriver.patatiumwebui.web.model.CorpSetting;
import org.webdriver.patatiumwebui.web.service.CorpSettingService;
import org.webdriver.patatiumwebui.web.util.DateUtil;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/corpList")
public class CorpListController {
    /**
     * 获取企业列表
     * page=2&rows=10,
     * @param request
     * @return
     */
    @RequestMapping("/getCorpList.json")
    @ResponseBody
    public Map<String, Object> getCorpList(HttpServletRequest request,
                                                   @RequestParam(required = false) String name
            , @RequestParam(required = false) String nationaltaxLoginname
            , @RequestParam(required = false) String declareStatus
            , @RequestParam(required = false) String createM
            , @RequestParam(required = false) Integer page
            , @RequestParam(required = false) Integer rows) {
        ZhangYiDaRequest zhangYiDaRequest = new ZhangYiDaRequest();
        CorpSettingService corpSettingService = (CorpSettingService) SpringUtil.getBean("corpSettingService");
        CorpSetting corpSetting = corpSettingService.getCorpSetting();
        CorpTaxVO corpTaxVO = new CorpTaxVO();
        corpTaxVO.setUserName(corpSetting.getUsername());
        corpTaxVO.setPassword(corpSetting.getPassword());
        corpTaxVO.setDomainName(corpSetting.getDomainName());
        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 10;
        }
        corpTaxVO.setPageNo(page);
        corpTaxVO.setPageSize(rows);
        corpTaxVO.setName(name);
        corpTaxVO.setNationaltaxLoginname(nationaltaxLoginname);
        corpTaxVO.setDeclareStatus(declareStatus);
        corpTaxVO.setIsShowPicture("0");// 0:否,1:是
        corpTaxVO.setCreateM(createM);
        corpTaxVO.setIsReport("1");
        JSONObject jsonObject = zhangYiDaRequest.getCorpList(corpTaxVO);
        JSONObject info = jsonObject.getJSONObject("info");
        int totalResult = info.getInt("totalRecord");

        Map<String, Object> result = new HashMap<>();
        result.put("total", totalResult);

        List<Map<String, Object>> rowsLi = new ArrayList<>();
        JSONArray corpTaxVOLi = info.getJSONArray("records");
        for (int i = 0; i < corpTaxVOLi.length(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.putAll(corpTaxVOLi.getJSONObject(i).toMap());
            String taxDate = ObjectUtils.toString(item.get("taxDate"), "");
            if (StringUtils.isNotEmpty(taxDate)) {
                Date date = new Date();
                date.setTime(Long.valueOf(taxDate));
                item.put("taxDateShow", DateUtil.getDateShow(date, "yyyy-MM-dd HH:mm:ss"));
            }
            /*
            List<Map<String, Object>> invtoryVOList = (List<Map<String, Object>>)item.get("invtoryVOList");
            if (invtoryVOList != null && invtoryVOList.size() > 0) {
                for (Map<String, Object> innerItem: invtoryVOList) {
                    if (innerItem.get("createTs") != null) {
                        String createTs = ObjectUtils.toString(innerItem.get("createTs"), "");
                        if (StringUtils.isNotEmpty(createTs)) {
                            Date date = new Date();
                            date.setTime(Long.valueOf(createTs));
                            innerItem.put("createTsShow", DateUtil.getDateShow(date, "yyyy-MM-dd HH:mm:ss"));
                        }
                    }
                }
            }
            */
            rowsLi.add(item);
        }
        result.put("rows", rowsLi);
        return result;
    }
}
