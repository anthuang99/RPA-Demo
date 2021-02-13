package org.webdriver.patatiumwebui.web.controller;

import org.springframework.ui.Model;
import org.webdriver.patatiumwebui.web.model.CorpSetting;
import org.webdriver.patatiumwebui.web.service.CorpSettingService;
import org.webdriver.patatiumwebui.web.util.SpringUtil;

/**
 * 控制器类的公共方法
 */
public class ControllerCommon {
    public static void common(Model model) {
        CorpSettingService corpSettingService = (CorpSettingService) SpringUtil.getBean("corpSettingService");
        CorpSetting corpSetting = corpSettingService.getCorpSetting();
        model.addAttribute("corpSetting", corpSetting);
    }
}
