package org.webdriver.patatiumwebui.web.simulation;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webdriver.patatiumwebui.constant.TaxConstant;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 获取报税详情列表
 */
@Controller
@RequestMapping("/taxDeclareDetailSimulation")
public class TaxDeclareDetailSimulationController {

    /**
     * 获取企业信息
     * @param request
     * @return
     */
    @RequestMapping("/getTaxDeclareDetailSimulation.json")
    @ResponseBody
    public Map<String, Object> getTaxDeclareDetailSimulation(HttpServletRequest request,
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
                {
                    Map<String, Object> jsonObject = new HashMap<>();
                    jsonObject.put("id", "1");
                    jsonObject.put("name", "亿豪商贸");
                    jsonObject.put("nationaltaxLoginname", "91350200MA2XNRFW7P");
                    jsonObject.put("nationaltaxPassword", "sx154575");
                    jsonObject.put("declareStatus", "0");// 0:已申报,1:未申报,2:部分申报
                    jsonObject.put("createM", "201812");
                    jsonObject.put("nsrsbn", "xx_xxxx_xxxxxxxxxxxxxx");
                    jsonObject.put("sbywbm", TaxConstant.XM_YHS);// 印花税
                    String text = null;
                    try {
                        text = FileUtils.readFileToString(new File("E:\\hongjinqiu\\tmp\\1.txt"), "UTF-8");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    jsonObject.put("sbqkImg", text);
                    jsonObject.put("skssqq", "2018-11-01");
                    jsonObject.put("skssqz", "2018-11-30");
                    jsonObject.put("status", "4");// 4:成功,3:失败,
                    jsonObject.put("createTs", String.valueOf(new Date().getTime()));
                    corpTaxVOLi.add(jsonObject);
                }
                {
                    Map<String, Object> jsonObject = new HashMap<>();
                    jsonObject.put("id", "2");
                    jsonObject.put("name", "亿豪商贸");
                    jsonObject.put("nationaltaxLoginname", "91350203MA2XN5NB6M");
                    jsonObject.put("nationaltaxPassword", "77277ZYD");
                    jsonObject.put("declareStatus", "1");// 0:已申报,1:未申报,2:部分申报
                    jsonObject.put("createM", "201812");
                    jsonObject.put("nsrsbn", "xx_xxxx_xxxxxxxxxxxxxx");
                    jsonObject.put("sbywbm", TaxConstant.XM_XGM_ZZS);// 小规模增值税
                    String text = null;
                    try {
                        text = FileUtils.readFileToString(new File("E:\\hongjinqiu\\tmp\\2.txt"), "UTF-8");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    jsonObject.put("sbqkImg", text);
                    jsonObject.put("skssqq", "2018-11-01");
                    jsonObject.put("skssqz", "2018-11-30");
                    jsonObject.put("status", "3");// 4:成功,3:失败,
                    jsonObject.put("createTs", String.valueOf(new Date().getTime()));
                    corpTaxVOLi.add(jsonObject);
                }
                {
                    Map<String, Object> jsonObject = new HashMap<>();
                    jsonObject.put("id", "3");
                    jsonObject.put("name", "亿豪商贸");
                    jsonObject.put("nationaltaxLoginname", "91350203MA2XN5NB6M");
                    jsonObject.put("nationaltaxPassword", "77277ZYD");
                    jsonObject.put("declareStatus", "1");// 0:已申报,1:未申报,2:部分申报
                    jsonObject.put("createM", "201812");
                    jsonObject.put("nsrsbn", "xx_xxxx_xxxxxxxxxxxxxx");
                    jsonObject.put("sbywbm", TaxConstant.XM_QTSL);// (其他收入)工会经费
                    jsonObject.put("sbqkImg", null);
                    jsonObject.put("skssqq", "2018-11-01");
                    jsonObject.put("skssqz", "2018-11-30");
                    jsonObject.put("status", "4");// 4:成功,3:失败,
                    jsonObject.put("createTs", String.valueOf(new Date().getTime()));
                    corpTaxVOLi.add(jsonObject);
                }
                {
                    Map<String, Object> jsonObject = new HashMap<>();
                    jsonObject.put("id", "4");
                    jsonObject.put("name", "亿豪商贸");
                    jsonObject.put("nationaltaxLoginname", "91350203MA2XN5NB6M");
                    jsonObject.put("nationaltaxPassword", "77277ZYD");
                    jsonObject.put("declareStatus", "1");// 0:已申报,1:未申报,2:部分申报
                    jsonObject.put("createM", "201812");
                    jsonObject.put("nsrsbn", "xx_xxxx_xxxxxxxxxxxxxx");
                    jsonObject.put("sbywbm", TaxConstant.XM_FJSCJ);// 附加税
                    jsonObject.put("sbqkImg", null);
                    jsonObject.put("skssqq", "2018-11-01");
                    jsonObject.put("skssqz", "2018-11-30");
                    jsonObject.put("status", "3");// 4:成功,3:失败,
                    jsonObject.put("createTs", String.valueOf(new Date().getTime()));
                    corpTaxVOLi.add(jsonObject);
                }
                {
                    Map<String, Object> jsonObject = new HashMap<>();
                    jsonObject.put("id", "5");
                    jsonObject.put("name", "亿豪商贸3");
                    jsonObject.put("nationaltaxLoginname", "91350206MA348HF44U");
                    jsonObject.put("nationaltaxPassword", "77277ZYD");
                    jsonObject.put("declareStatus", "2");// 0:已申报,1:未申报,2:部分申报
                    jsonObject.put("createM", "201812");
                    jsonObject.put("nsrsbn", "xx_xxxx_xxxxxxxxxxxxxx");
                    jsonObject.put("sbywbm", TaxConstant.XM_YB_ZZS);// 一般增值税
                    jsonObject.put("sbqkImg", null);
                    jsonObject.put("skssqq", "2018-11-01");
                    jsonObject.put("skssqz", "2018-11-30");
                    jsonObject.put("status", "4");// 4:成功,3:失败,
                    jsonObject.put("createTs", String.valueOf(new Date().getTime()));
                    corpTaxVOLi.add(jsonObject);
                }
                {
                    Map<String, Object> jsonObject = new HashMap<>();
                    jsonObject.put("id", "6");
                    jsonObject.put("name", "亿豪商贸4");
                    jsonObject.put("nationaltaxLoginname", "91350200MA344M8T68");
                    jsonObject.put("nationaltaxPassword", "sbcw1101");
                    jsonObject.put("declareStatus", "2");// 0:已申报,1:未申报,2:部分申报
                    jsonObject.put("createM", "201811");
                    jsonObject.put("nsrsbn", "xx_xxxx_xxxxxxxxxxxxxx");
                    jsonObject.put("sbywbm", TaxConstant.XM_YHS);
                    jsonObject.put("sbqkImg", null);
                    jsonObject.put("skssqq", "2018-10-01");
                    jsonObject.put("skssqz", "2018-10-30");
                    jsonObject.put("status", "4");// 4:成功,3:失败,
                    jsonObject.put("createTs", String.valueOf(new Date().getTime()));
                    corpTaxVOLi.add(jsonObject);
                }
            } else if (pageNo == 2) {
                Map<String, Object> jsonObject = new HashMap<>();
                jsonObject.put("id", "4");
                jsonObject.put("name", "测试2");
                jsonObject.put("nationaltaxLoginname", "91350206089926087M");
                jsonObject.put("nationaltaxPassword", "guohao0310");
                jsonObject.put("declareStatus", "2");// 0:已申报,1:未申报,2:部分申报
                jsonObject.put("createM", "201811");
                jsonObject.put("nsrsbn", "xx_xxxx_xxxxxxxxxxxxxx");
                jsonObject.put("sbywbm", TaxConstant.XM_YHS);
                jsonObject.put("sbqkImg", null);
                jsonObject.put("skssqq", "2018-10-01");
                jsonObject.put("skssqz", "2018-10-30");
                jsonObject.put("status", "4");// 4:成功,3:失败,
                jsonObject.put("createTs", String.valueOf(new Date().getTime()));
                corpTaxVOLi.add(jsonObject);
            }
            info.put("records", corpTaxVOLi);
        }
        result.put("info", info);

        return result;
    }

}
