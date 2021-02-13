package org.webdriver.patatiumwebui.httprequest.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 首页点击启动报税时,往队列里面塞的值参,
 * 带页面参数和表格选中的数据
 */
public class CorpTaxQueueVO {
    private String status;// 0:暂停,1:启动
    private String name;// 页面上的关键字,企业名称
    private String declareStatus;// 0:已申报,1:未申报,2:部分申报
    private String createM;// 空代表当前月
    private List<Map<String, Object>> array = new ArrayList<>();// 表格的选择数据

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeclareStatus() {
        return declareStatus;
    }

    public void setDeclareStatus(String declareStatus) {
        this.declareStatus = declareStatus;
    }

    public List<Map<String, Object>> getArray() {
        return array;
    }

    public void setArray(List<Map<String, Object>> array) {
        this.array = array;
    }

    public String getCreateM() {
        return createM;
    }

    public void setCreateM(String createM) {
        this.createM = createM;
    }
}
