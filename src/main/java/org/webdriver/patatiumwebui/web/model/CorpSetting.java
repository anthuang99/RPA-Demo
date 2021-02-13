package org.webdriver.patatiumwebui.web.model;

/**
 * 企业设置,与数据库表 corp_setting 相对应
 */
public class CorpSetting {
    private Integer id;
    private String username;
    private String password;
    private String domainName;// 域名
    private String createTime;
    private String updateTime;
    private String modifyTimeShow;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getModifyTimeShow() {
        return modifyTimeShow;
    }

    public void setModifyTimeShow(String modifyTimeShow) {
        this.modifyTimeShow = modifyTimeShow;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
