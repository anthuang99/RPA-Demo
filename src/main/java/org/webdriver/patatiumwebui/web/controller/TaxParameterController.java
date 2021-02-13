package org.webdriver.patatiumwebui.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webdriver.patatiumwebui.web.service.TaxParameterService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页,列表展示,取一键报税参数
 */
@Controller
@RequestMapping("/taxParameter")
public class TaxParameterController {
    @Autowired
    private TaxParameterService taxParameterService;

    /**
     * 获取一键报税参数表
     * @param request
     * @return
     */
    @RequestMapping("/getTaxParameter.json")
    @ResponseBody
    public Map<String, Object> getTaxParameter(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
        result.put("taxParameterLi", taxParameterService.getTaxParameterLi());
        return result;
    }

    /**
     * 保存一键报税参数表,并行报税用户数
     * @param request
     * @return
     */
    @RequestMapping("/saveTaxThreadCount.json")
    @ResponseBody
    public Map<String, Object> saveTaxThreadCount(HttpServletRequest request, @RequestParam(required = false) String taxThreadCount) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
        taxParameterService.saveOrUpdateTaxThreadCount(taxThreadCount);
        return result;
    }

    /**
     * 保存一键报税参数表,验证码X偏移
     * @param request
     * @return
     */
    @RequestMapping("/saveCheckcodeOffsetX.json")
    @ResponseBody
    public Map<String, Object> saveCheckcodeOffsetX(HttpServletRequest request, @RequestParam(required = false) String checkcodeOffsetX) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
        taxParameterService.saveOrUpdateCheckcodeOffsetX(checkcodeOffsetX);
        return result;
    }

    /**
     * 保存一键报税参数表,验证码Y偏移
     * @param request
     * @return
     */
    @RequestMapping("/saveCheckcodeOffsetY.json")
    @ResponseBody
    public Map<String, Object> saveCheckcodeOffsetY(HttpServletRequest request, @RequestParam(required = false) String checkcodeOffsetY) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
        taxParameterService.saveOrUpdateCheckcodeOffsetY(checkcodeOffsetY);
        return result;
    }

    /**
     * 保存一键报税参数表,浏览器路径
     * @param request
     * @return
     */
    @RequestMapping("/saveBrowserPath.json")
    @ResponseBody
    public Map<String, Object> saveBrowserPath(HttpServletRequest request, @RequestParam(required = false) String browserPath) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
        taxParameterService.saveOrUpdateBrowserPathAndType(browserPath);
        return result;
    }
}
