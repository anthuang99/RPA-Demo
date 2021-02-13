package org.webdriver.patatiumwebui.web.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webdriver.patatiumwebui.constant.TaxConstant;
import org.webdriver.patatiumwebui.exception.RPAException;
import org.webdriver.patatiumwebui.httprequest.ZhangYiDaRequest;
import org.webdriver.patatiumwebui.httprequest.vo.CorpTaxVO;
import org.webdriver.patatiumwebui.utils.Log;
import org.webdriver.patatiumwebui.web.model.CorpSetting;
import org.webdriver.patatiumwebui.web.service.CorpSettingService;
import org.webdriver.patatiumwebui.web.util.DateUtil;
import org.webdriver.patatiumwebui.web.util.ExportUtil;
import org.webdriver.patatiumwebui.web.util.SpringUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 报税列表
 */
@Controller
@RequestMapping("/taxDeclareDetail")
public class TaxDeclareDetailController {
    private Log log = new Log(getClass());

    /**
     * 页面跳转
     */
    @RequestMapping("/gotoTaxDeclareDetail.htm")
    public String gotoTaxDeclareDetail(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(required = false) String detailStatus) {
        ControllerCommon.common(model);
        if (detailStatus == null) {
            detailStatus = "";
        }
        model.addAttribute("detailStatus", detailStatus);
        return "taxDeclareDetail";
    }

    @RequestMapping("/gotoTaxDeclareDetailOld.htm")
    public String gotoTaxDeclareDetailOld(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "taxDeclareDetailOld";
    }

    /**
     * 获取报税结果详情
     * page=2&rows=10,
     * @param request
     * @return
     */
    @RequestMapping("/getTaxDeclareDetail.json")
    @ResponseBody
    public Map<String, Object> getTaxDeclareDetail(HttpServletRequest request,
                                                   @RequestParam(required = false) String name
                                                    ,@RequestParam(required = false) String nationaltaxLoginname
                                                    ,@RequestParam(required = false) String detailStatus
                                                    ,@RequestParam(required = false) String createM
                                                    ,@RequestParam(required = false) Integer page
                                                    ,@RequestParam(required = false) Integer rows) {
        ZhangYiDaRequest zhangYiDaRequest = new ZhangYiDaRequest();
        CorpTaxVO corpTaxVO = getCorpTaxVO(name, nationaltaxLoginname, detailStatus, createM, page, rows);
        JSONObject jsonObject = zhangYiDaRequest.getTaxDeclareDetail(corpTaxVO);
        JSONObject info = jsonObject.getJSONObject("info");
        int totalResult = info.getInt("totalRecord");

        Map<String, Object> result = new HashMap<>();
        result.put("total", totalResult);

        List<Map<String, Object>> rowsLi = new ArrayList<>();
        JSONArray corpTaxVOLi = info.getJSONArray("records");
        for (int i = 0; i < corpTaxVOLi.length(); i++) {
            Map<String, Object> itemMap = corpTaxVOLi.getJSONObject(i).toMap();
            Map<String, Object> item = applyRecordFormat(itemMap);
            rowsLi.add(item);
        }
        result.put("rows", rowsLi);
        return result;
    }

    /**
     * 公共的值参数构建方法
     */
    private CorpTaxVO getCorpTaxVO(String name, String nationaltaxLoginname, String detailStatus, String createM, Integer page, Integer rows) {
        CorpSettingService corpSettingService = (CorpSettingService) SpringUtil.getBean("corpSettingService");
        CorpSetting corpSetting = corpSettingService.getCorpSetting();
        CorpTaxVO corpTaxVO = new CorpTaxVO();
        corpTaxVO.setUserName(corpSetting.getUsername());
        corpTaxVO.setPassword(corpSetting.getPassword());
        corpTaxVO.setDomainName(corpSetting.getDomainName());
        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 10;
        }
        corpTaxVO.setPageNo(page);
        corpTaxVO.setPageSize(rows);
        corpTaxVO.setName(name);
        corpTaxVO.setNationaltaxLoginname(nationaltaxLoginname);
        corpTaxVO.setDetailStatus(detailStatus);
        corpTaxVO.setIsShowPicture("1");// 0:否,1:是
        corpTaxVO.setCreateM(createM);
        corpTaxVO.setIsReport("1");
        return corpTaxVO;
    }

    /**
     * 显示格式赋值
     * @return
     */
    private Map<String, Object> applyRecordFormat(Map<String, Object> itemMap) {
        Map<String, Object> item = new HashMap<>();
        item.putAll(itemMap);
        String createTs = ObjectUtils.toString(item.get("createTs"), "");
        if (StringUtils.isNotEmpty(createTs)) {
            Date date = new Date();
            date.setTime(Long.valueOf(createTs));
            item.put("createTsShow", DateUtil.getDateShow(date, "yyyy-MM-dd HH:mm:ss"));
        }
        String createMItem = ObjectUtils.toString(item.get("createM"), "");
        if (createMItem.length() == 6) {
            int year = Integer.valueOf(createMItem.substring(0, 4));
            int month = Integer.valueOf(createMItem.substring(4, 6));
            String createMShow = getYyyyMMOffset(year, month, -1);
            item.put("createMShow", createMShow);
        }
        String sbywbm = ObjectUtils.toString(item.get("sbywbm"), "");
        if (sbywbm.equals(TaxConstant.XM_YB_ZZS)) {
            item.put("sbywbmShow", TaxConstant.XM_YB_ZZS_ZH_CN);
        } else if (sbywbm.equals(TaxConstant.XM_XGM_ZZS)) {// 增值税
            item.put("sbywbmShow", TaxConstant.XM_XGM_ZZS_ZH_CN);
        } else if (sbywbm.equals(TaxConstant.XM_YHS)) {// 印花税
            item.put("sbywbmShow", TaxConstant.XM_YHS_ZH_CN);
        } else if (sbywbm.equals(TaxConstant.XM_FJSCJ)) {// 附加税
            item.put("sbywbmShow", TaxConstant.XM_FJSCJ_ZH_CN);
        } else if (sbywbm.equals(TaxConstant.XM_QTSL)) {// 其他收入
            item.put("sbywbmShow", TaxConstant.XM_QTSL_SHOW_ZH_CN);
        }
        {
            String status = ObjectUtils.toString(item.get("status"), "");
            if (status.equals("3")) {
                item.put("statusShow", "失败");
            } else if (status.equals("4")) {
                item.put("statusShow", "成功");
            }
        }
        return item;
    }

    /**
     * 报税详情导出
     */
    @RequestMapping("/exportTaxDeclareDetail.json")
    @ResponseBody
    public void exportTaxDeclareDetail(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(required = false) String name
            ,@RequestParam(required = false) String nationaltaxLoginname
            ,@RequestParam(required = false) String detailStatus
            ,@RequestParam(required = false) String createM) throws Exception {
        int rows = 20;
        ZhangYiDaRequest zhangYiDaRequest = new ZhangYiDaRequest();
        CorpTaxVO corpTaxVO = getCorpTaxVO(name, nationaltaxLoginname, detailStatus, createM, 1, rows);
        JSONObject jsonObject = zhangYiDaRequest.getTaxDeclareDetail(corpTaxVO);
        JSONObject info = jsonObject.getJSONObject("info");
        int totalResult = info.getInt("totalRecord");

        int pageCount = totalResult / rows;
        if (totalResult % rows != 0) {
            pageCount++;
        }

        List<Map<String, Object>> rowsLi = getRowsLi(info);
        Map<String, Object> excelMap = createExportFile();
        writeToExcel(excelMap, rowsLi, 0);

        for (int j = 1; j < pageCount; j++) {
            corpTaxVO = getCorpTaxVO(name, nationaltaxLoginname, detailStatus, createM, j+1, rows);
            jsonObject = zhangYiDaRequest.getTaxDeclareDetail(corpTaxVO);
            info = jsonObject.getJSONObject("info");

            int offset = rowsLi.size();
            rowsLi = getRowsLi(info);
            writeToExcel(excelMap, rowsLi, offset);
        }
        closeExcel(excelMap);
        String filePath = (String)excelMap.get("filePath");
        String showName = "报税详情_" + createM + ".xls";
        ExportUtil.exportFile(new File(filePath), showName, response);
    }

    /**
     * 创建导出的文件
     */
    private Map<String, Object> createExportFile() {
        Map<String, Object> result = new HashMap<>();
        synchronized (TaxDeclareDetailController.class) {
            File f = new File("excel");
            if (!f.exists()) {
                f.mkdirs();
            }
        }
        String filePath = null;
        FileOutputStream out = null;
        Workbook wb = null;
        Sheet s = null;
        try {
            // create a new file
            String fileName = UUID.randomUUID().toString() + ".xls";
            filePath = "excel" + File.separator + fileName;
            out = new FileOutputStream(filePath);
            // create a new workbook
            wb = new HSSFWorkbook();
            // create a new sheet
            s = wb.createSheet();
            // declare a row object reference
            Row r = null;
            // declare a cell object reference
            Cell c = null;
            // create 3 cell styles
            CellStyle cs = wb.createCellStyle();
            r = s.createRow(0);
            String titleArray[] = {"公司名称", "国税登录号", "申报所属期（月份）", "申报税种", "状态", "截图", "创建时间"};
            /*
             */
            for (int i = 0; i < titleArray.length; i++) {
                c = r.createCell(i);
                c.setCellValue(titleArray[i]);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            IOUtils.closeQuietly(out);
            throw new RPAException(e);
        }
        result.put("out", out);
        result.put("filePath", filePath);
        result.put("workbook", wb);
        result.put("sheet", s);
        return result;
    }

    /**
     * 写到 Excel 目录下
     * 参考:http://poi.apache.org/components/spreadsheet/how-to.html#user_api
     */
    private void writeToExcel(Map<String, Object> excelMap, List<Map<String, Object>> rowsLi, int offset) {
        Sheet s = (Sheet)excelMap.get("sheet");
        // declare a row object reference
        Row r = null;
        // declare a cell object reference
        Cell c = null;
        String imageField = "sbqkImg";
        String fieldLi[] = {"name", "nationaltaxLoginname", "createMShow", "sbywbmShow", "statusShow", imageField, "createTsShow"};

        for (int i = 0; i < rowsLi.size(); i++) {
            Map<String, Object> line = rowsLi.get(i);
            r = s.createRow(i + 1 + offset);
            for (int j = 0; j < fieldLi.length; j++) {
                c = r.createCell(j);
                String value;
                if (!fieldLi[j].equals(imageField)) {
                    value = ObjectUtils.toString(line.get(fieldLi[j]), "");
                    c.setCellValue(value);
                } else {// 图片
                    value = "";
                    c.setCellValue(value);
                    /*
                    // 暂不输出图片
                    value = ObjectUtils.toString(line.get(fieldLi[j]), "");
                    if (StringUtils.isNotEmpty(value)) {
                        BASE64Decoder decoder = new BASE64Decoder();
                        byte[] imageBytes = decoder.decodeBuffer(value);
                        int pictureIdx = wb.addPicture(imageBytes, Workbook.PICTURE_TYPE_JPEG);
                        CreationHelper helper = wb.getCreationHelper();
                        // Create the drawing patriarch.  This is the top level container for all shapes.
                        Drawing drawing = s.createDrawingPatriarch();

                        //add a picture shape
                        ClientAnchor anchor = helper.createClientAnchor();
                        //set top-left corner of the picture,
                        //subsequent call of Picture#resize() will operate relative to it
                        anchor.setCol1(j);
                        anchor.setRow1(i + 1);
                        Picture pict = drawing.createPicture(anchor, pictureIdx);

                        //auto-size picture relative to its top-left corner
                        pict.resize();
                    }
                    */
                }
            }
        }
    }

    /**
     * 关闭excel
     */
    private void closeExcel(Map<String, Object> excelMap) {
        FileOutputStream out = (FileOutputStream)excelMap.get("out");
        Workbook wb = (Workbook)excelMap.get("workbook");
        try {
            wb.write(out);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 从返回的 JSONObject 中拼凑成 List,
     * @return
     */
    private List<Map<String, Object>> getRowsLi(JSONObject info) {
        List<Map<String, Object>> rowsLi = new ArrayList<>();
        JSONArray corpTaxVOLi = info.getJSONArray("records");
        for (int i = 0; i < corpTaxVOLi.length(); i++) {
            Map<String, Object> itemMap = corpTaxVOLi.getJSONObject(i).toMap();
            Map<String, Object> item = applyRecordFormat(itemMap);
            rowsLi.add(item);
        }
        return rowsLi;
    }

    /**
     * 申报所属期为创建月份的前一月,
     * @return
     */
    private String getYyyyMMOffset(int year, int month, int offset) {
        month = month + offset;
        if (month > 12) {
            year += 1;
            month -= 12;
        } else if (month < 1) {
            year -= 1;
            month += 12;
        }
        if (month < 10) {
            return year + "-0" + month;
        }
        return year + "-" + month;
    }
}
