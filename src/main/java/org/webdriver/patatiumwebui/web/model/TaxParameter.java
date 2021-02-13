package org.webdriver.patatiumwebui.web.model;

/**
 * 一键报税参数表,与 TAX_PARAMETER 相对应
 */
public class TaxParameter {
    private Integer id;
    private String key;
    private String value;
    private String createTime;
    private String updateTime;
    private String modifyTimeShow;

    public String getModifyTimeShow() {
        return modifyTimeShow;
    }

    public void setModifyTimeShow(String modifyTimeShow) {
        this.modifyTimeShow = modifyTimeShow;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
