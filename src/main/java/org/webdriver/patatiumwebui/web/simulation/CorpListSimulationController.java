package org.webdriver.patatiumwebui.web.simulation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 根据企业账号取符合0申报的企业,返回用户名/密码,可以登录厦门国税,分页接口模拟
 */
@Controller
@RequestMapping("/corpListSimulation")
public class CorpListSimulationController {

    @RequestMapping("/sleep.json")
    @ResponseBody
    public Map<String, Object> sleep(HttpServletRequest request) {
        if (true) {
            try {
                TimeUnit.SECONDS.sleep(32);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        result.put("statusCode", "1002");
        return result;
    }

    /**
     * 获取企业信息
     * @param request
     * @return
     */
    @RequestMapping("/getCorpListSimulation.json")
    @ResponseBody
    public Map<String, Object> getCorpListSimulation(HttpServletRequest request,
                                            @RequestBody Map<String, Object> corpTaxVO) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "true");
        Map<String, Object> info = new HashMap<>();
        {
            int pageSize = Integer.valueOf(corpTaxVO.get("pageSize").toString());
            info.put("totalRecord", 2 * pageSize);
            List<Map<String, Object>> corpTaxVOLi = new ArrayList<>();
            int pageNo = Integer.valueOf(corpTaxVO.get("currentPage").toString());
            if (pageNo == 1) {
                {// 小规模
                    List<String[]> list = new ArrayList<>();
                    list.add(new String[]{"91350206699949905M", "guohao0310"});
                    list.add(new String[]{"91350203575017700A", "guohao0310"});
                    list.add(new String[]{"91350203MA2YQGJR98 ", "guohao0310"});
                    list.add(new String[]{"91350200MA2YPJ2884", "guohao0310"});
                    list.add(new String[]{"91350205M0001WXB32", "guohao0310"});
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> jsonObject = new HashMap<>();
                        jsonObject.put("id", String.valueOf("10_" + i));
                        jsonObject.put("name", "小规模_" + i);
//                    jsonObject.put("nationaltaxLoginname", "91350203MA31XNKN1F");
//                    jsonObject.put("nationaltaxPassword", "yy220011");
                        jsonObject.put("nationaltaxLoginname", list.get(i)[0]);
                        jsonObject.put("nationaltaxPassword", list.get(i)[1]);
                        jsonObject.put("declareStatus", "1");// 0:已申报,1:未申报,2:部分申报
                        jsonObject.put("taxDate", 1545877673572L);// 报税时间
                        corpTaxVOLi.add(jsonObject);
                    }
                }
                {// 一般纳税人
                    List<String[]> list = new ArrayList<>();
                    list.add(new String[]{"91350206089926087M", "guohao0310"});
                    list.add(new String[]{"913502030793857828", "guohao0310"});
                    list.add(new String[]{"91350203079373079Y", "guohao0310"});
                    list.add(new String[]{"9135020558786742XU", "guohao0310"});
                    list.add(new String[]{"913502035878841237", "guohao0310"});
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> jsonObject = new HashMap<>();
                        jsonObject.put("id", String.valueOf("10_" + i));
                        jsonObject.put("name", "一般纳税人_" + i);
//                    jsonObject.put("nationaltaxLoginname", "91350203MA31XNKN1F");
//                    jsonObject.put("nationaltaxPassword", "yy220011");
                        jsonObject.put("nationaltaxLoginname", list.get(i)[0]);
                        jsonObject.put("nationaltaxPassword", list.get(i)[1]);
                        jsonObject.put("declareStatus", "1");// 0:已申报,1:未申报,2:部分申报
                        jsonObject.put("taxDate", 1545877673572L);// 报税时间
                        corpTaxVOLi.add(jsonObject);
                    }
                }
                {
                    Map<String, Object> jsonObject = new HashMap<>();
                    jsonObject.put("id", "1");
                    jsonObject.put("name", "亿豪商贸");
//                    jsonObject.put("nationaltaxLoginname", "91350203MA31XNKN1F");
//                    jsonObject.put("nationaltaxPassword", "yy220011");
                    jsonObject.put("nationaltaxLoginname", "91350200MA2XNRFW7P");
                    jsonObject.put("nationaltaxPassword", "sx154575");
                    jsonObject.put("declareStatus", "0");// 0:已申报,1:未申报,2:部分申报
                    jsonObject.put("taxDate", 1545877673572L);// 报税时间
                    corpTaxVOLi.add(jsonObject);
                }
                {
                    Map<String, Object> jsonObject = new HashMap<>();
                    jsonObject.put("id", "2");
                    jsonObject.put("name", "亿豪商贸2");
                    jsonObject.put("nationaltaxLoginname", "91350203MA2XN5NB6M");
                    jsonObject.put("nationaltaxPassword", "77277ZYD");
                    jsonObject.put("declareStatus", "1");// 0:已申报,1:未申报,2:部分申报
                    jsonObject.put("taxDate", 1545877813843L);// 报税时间
                    corpTaxVOLi.add(jsonObject);
                }
                {
                    Map<String, Object> jsonObject = new HashMap<>();
                    jsonObject.put("id", "3");
                    jsonObject.put("name", "亿豪商贸3");
                    jsonObject.put("nationaltaxLoginname", "91350206MA348HF44U");
                    jsonObject.put("nationaltaxPassword", "77277ZYD");
                    jsonObject.put("declareStatus", "2");// 0:已申报,1:未申报,2:部分申报
                    jsonObject.put("taxDate", 1545877870062L);// 报税时间
                    corpTaxVOLi.add(jsonObject);
                }
            } else if (pageNo == 2) {
                Map<String, Object> jsonObject = new HashMap<>();
                jsonObject.put("id", "4");
                jsonObject.put("name", "测试2");
                jsonObject.put("nationaltaxLoginname", "91350206089926087M");
                jsonObject.put("nationaltaxPassword", "guohao0310");
                jsonObject.put("declareStatus", "2");// 0:已申报,1:未申报,2:部分申报
                jsonObject.put("taxDate", 1545877918064L);// 报税时间
                corpTaxVOLi.add(jsonObject);
            }
            info.put("records", corpTaxVOLi);
        }
        result.put("info", info);

        return result;
    }

}
