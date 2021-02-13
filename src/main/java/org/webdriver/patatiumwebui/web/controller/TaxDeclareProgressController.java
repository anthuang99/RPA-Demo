package org.webdriver.patatiumwebui.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webdriver.patatiumwebui.web.model.TaxDeclareProgress;
import org.webdriver.patatiumwebui.web.service.TaxDeclareProgressService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页,列表展示,取企业信息等
 */
@Controller
@RequestMapping("/taxDeclareProgress")
public class TaxDeclareProgressController {
    @Autowired
    private TaxDeclareProgressService taxDeclareProgressService;

    /**
     * 获取企业信息
     * @param request
     * @return
     */
    @RequestMapping("/getTaxDeclareProgress.json")
    @ResponseBody
    public Map<String, Object> getTaxDeclareProgress(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
//        result.put("success", "true");
//        result.put("taxDeclareProgressLi", taxDeclareProgressService.getTaxDeclareProgress(10));
        List<TaxDeclareProgress> taxDeclareProgresses = taxDeclareProgressService.getTaxDeclareProgress(10);
        result.put("total", taxDeclareProgresses.size());
        result.put("rows", taxDeclareProgresses);
        return result;
    }

}
