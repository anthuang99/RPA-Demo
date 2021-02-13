package org.webdriver.patatiumwebui.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webdriver.patatiumwebui.common.RunningHolder;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.httprequest.vo.CorpTaxQueueVO;
import org.webdriver.patatiumwebui.web.service.TaxDeclareStatusService;
import org.webdriver.patatiumwebui.web.service.TaxExpireDateSettingService;
import org.webdriver.patatiumwebui.web.service.TaxParameterService;
import org.webdriver.patatiumwebui.web.util.DateUtil;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页,列表展示,一键报税状态
 */
@Controller
@RequestMapping("/taxDeclareStatus")
public class TaxDeclareStatusController {
    @Autowired
    private TaxDeclareStatusService taxDeclareStatusService;
    @Autowired
    private TaxParameterService taxParameterService;

    /**
     * 获取一键报税状态
     * @param request
     * @return
     */
    @RequestMapping("/getTaxDeclareStatus.json")
    @ResponseBody
    public Map<String, Object> getTaxDeclareStatus(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
        result.put("taxDeclareStatus", taxParameterService.getTaxParameter(TaxConstant.TAX_DECLARE_STATUS));
        return result;
    }

    /**
     * 保存一键报税状态
     * @param request
     * @return
     */
    @RequestMapping("/saveTaxDeclareStatus.json")
    @ResponseBody
//    public Map<String, Object> saveTaxDeclareStatus(HttpServletRequest request, @RequestParam(required = false) String status) {
    public Map<String, Object> saveTaxDeclareStatus(HttpServletRequest request, @RequestBody CorpTaxQueueVO corpTaxQueueVO) {
        if ("1".equals(corpTaxQueueVO.getStatus())) {
            // 检查是否正在运行
            if (RunningHolder.IS_RUNNING) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", "false");
                result.put("message", "报税进行中，无法启动报税，需要报税结束后才能重新启动报税。");
                return result;
            }
            // 检查当前日期是否在报税日期内,
            TaxExpireDateSettingService taxExpireDateSettingService = (TaxExpireDateSettingService) SpringUtil.getBean("taxExpireDateSettingService");
            if (!taxExpireDateSettingService.isCurrentDateLTETaxExpireDate()) {
                Map<String, Object> result = new HashMap<>();
                String currentTaxExpireDateShow = taxExpireDateSettingService.getCurrentTaxExpireDateShow();
                String currentYyyyMMdd = DateUtil.getDateShow(new Date(), "yyyy-MM-dd");
                result.put("success", "false");
                result.put("message", "报税截止日期：" + currentTaxExpireDateShow + "，当前日期：" + currentYyyyMMdd + "，不能报税！");
                return result;
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
//        taxParameterService.updateTaxParameter(TaxConstant.TAX_DECLARE_STATUS, status);
        taxDeclareStatusService.saveOrUpdateTaxDeclareStatus(corpTaxQueueVO);
        return result;
    }
}
