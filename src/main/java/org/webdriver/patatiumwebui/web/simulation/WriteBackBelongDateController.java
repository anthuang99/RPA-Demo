package org.webdriver.patatiumwebui.web.simulation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webdriver.patatiumwebui.exception.RPAException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 回写所属日期
 */
@Controller
@RequestMapping("/writeBackBelongDate")
public class WriteBackBelongDateController {
    /**
     * 获取企业信息
     * @param request
     * @return
     */
    @RequestMapping("/writeBackBelongDate.json")
    @ResponseBody
    public Map<String, Object> writeBackBelongDate(HttpServletRequest request,
                                            @RequestParam(required = true) String validateJson
                                            ,@RequestParam(required = true) String dataJson
                                            ) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
        System.out.println("validateJson:" + validateJson);
        System.out.println("dataJson:" + dataJson);
        return result;
    }

}
