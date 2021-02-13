package org.webdriver.patatiumwebui.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webdriver.patatiumwebui.web.service.CorpSettingService;
import org.webdriver.patatiumwebui.web.service.InitService;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 系统初始化
 */
@Controller
public class InitController {
    @Autowired
    private CorpSettingService corpSettingService;

    /**
     * 首页数据展示
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/init.htm")
    public String init(HttpServletRequest request, HttpServletResponse response, Model model) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Thread(){
            @Override
            public void run() {
                InitService initService = (InitService) SpringUtil.getBean("initService");
                initService.init();
            }
        });
        return "init";
    }

    /**
     * 是否初始化完成
     * @param request
     * @return
     */
    @RequestMapping("/isInitReady.json")
    @ResponseBody
    public Map<String, Object> isInitReady(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
        result.put("isInitReady", corpSettingService.isInitReady());
        return result;
    }
}
