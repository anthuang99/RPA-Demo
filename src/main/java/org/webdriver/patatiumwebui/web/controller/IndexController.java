package org.webdriver.patatiumwebui.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.web.model.CorpSetting;
import org.webdriver.patatiumwebui.web.model.TaxParameter;
import org.webdriver.patatiumwebui.web.service.CorpSettingService;
import org.webdriver.patatiumwebui.web.service.TaxParameterService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页,列表展示,取企业信息等
 */
@Controller
public class IndexController {
    @Autowired
    private CorpSettingService corpSettingService;
    @Autowired
    private TaxParameterService taxParameterService;

    /**
     * 首页数据展示
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        /*
        if (!corpSettingService.isInitReady()) {
            return "redirect:/init.htm";
        }
        */
        if (corpSettingService.getCorpSetting() == null) {
            return "redirect:/login.htm";
        }

        ControllerCommon.common(model);
        return "index";
    }

    @RequestMapping("/indexOld.htm")
    public String indexOld(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "indexOld";
    }

    @RequestMapping("/login.htm")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "login";
    }

    @RequestMapping("/indexFirst.htm")
    public String indexFirst(HttpServletRequest request, HttpServletResponse response, Model model) {
        ControllerCommon.common(model);
        return "indexFirst";
    }

    @RequestMapping("/setting.htm")
    public String setting(HttpServletRequest request, HttpServletResponse response, Model model) {
        ControllerCommon.common(model);

        {
            TaxParameter taxParameter = taxParameterService.getTaxParameter(TaxConstant.TAX_THREAD_COUNT);
            model.addAttribute(TaxConstant.TAX_THREAD_COUNT, taxParameter);
        }
        {
            TaxParameter taxParameter = taxParameterService.getTaxParameter(TaxConstant.CHECKCODE_OFFSET_X);
            model.addAttribute(TaxConstant.CHECKCODE_OFFSET_X, taxParameter);
        }
        {
            TaxParameter taxParameter = taxParameterService.getTaxParameter(TaxConstant.CHECKCODE_OFFSET_Y);
            model.addAttribute(TaxConstant.CHECKCODE_OFFSET_Y, taxParameter);
        }
        {
            TaxParameter taxParameter = taxParameterService.getTaxParameter(TaxConstant.BROWSER_PATH);
            model.addAttribute(TaxConstant.BROWSER_PATH, taxParameter);
        }
        {
            TaxParameter taxParameter = taxParameterService.getTaxParameter(TaxConstant.DRAG_CHECKCODE_REPEAT_COUNT);
            model.addAttribute(TaxConstant.DRAG_CHECKCODE_REPEAT_COUNT, taxParameter);
        }



        return "setting";
    }

    @RequestMapping("/exit.json")
    @ResponseBody
    public Map<String, Object> exit(HttpServletRequest request) {
        System.exit(0);
        return new HashMap();
    }
}
