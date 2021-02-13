package org.webdriver.patatiumwebui.web.simulation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 回写当期申报信息:未申报,已申报
 */
@Controller
@RequestMapping("/writeBackTaxName")
public class WriteBackTaxNameController {
    /**
     * 获取企业信息
     * @param request
     * @return
     */
    @RequestMapping("/writeBackTaxName.json")
    @ResponseBody
    public Map<String, Object> writeBackTaxName(HttpServletRequest request,
                                            @RequestParam(required = true) String nationaltaxLoginname
                                            ,@RequestParam(required = true) String unReportTax
                                            ,@RequestParam(required = true) String hasReportTax) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
        System.out.println("nationaltaxLoginname:" + nationaltaxLoginname);
        System.out.println("unReportTax:" + unReportTax);
        System.out.println("hasReportTax:" + hasReportTax);
        return result;
    }

}
