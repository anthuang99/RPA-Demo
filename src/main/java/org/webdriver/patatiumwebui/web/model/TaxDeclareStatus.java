package org.webdriver.patatiumwebui.web.model;

/**
 * 一键报税状态表,与 TAX_DECLARE_STATUS 相对应
 */
public class TaxDeclareStatus {
    private Integer id;
    private String status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
