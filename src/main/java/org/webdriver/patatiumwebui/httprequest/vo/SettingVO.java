package org.webdriver.patatiumwebui.httprequest.vo;

/**
 * 设置页的表单值映射VO
 */
public class SettingVO {
    private String username;
    private String password;
    private String domainName;
    private String taxThreadCount;
    private String dragCheckCodeRepeatCount;
    private String checkcodeOffsetX;
    private String checkcodeOffsetY;
    private String browserPath;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getTaxThreadCount() {
        return taxThreadCount;
    }

    public void setTaxThreadCount(String taxThreadCount) {
        this.taxThreadCount = taxThreadCount;
    }

    public String getCheckcodeOffsetX() {
        return checkcodeOffsetX;
    }

    public void setCheckcodeOffsetX(String checkcodeOffsetX) {
        this.checkcodeOffsetX = checkcodeOffsetX;
    }

    public String getCheckcodeOffsetY() {
        return checkcodeOffsetY;
    }

    public void setCheckcodeOffsetY(String checkcodeOffsetY) {
        this.checkcodeOffsetY = checkcodeOffsetY;
    }

    public String getBrowserPath() {
        return browserPath;
    }

    public void setBrowserPath(String browserPath) {
        this.browserPath = browserPath;
    }

    public String getDragCheckCodeRepeatCount() {
        return dragCheckCodeRepeatCount;
    }

    public void setDragCheckCodeRepeatCount(String dragCheckCodeRepeatCount) {
        this.dragCheckCodeRepeatCount = dragCheckCodeRepeatCount;
    }
}
