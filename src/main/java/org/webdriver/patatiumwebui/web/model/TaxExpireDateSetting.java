package org.webdriver.patatiumwebui.web.model;

/**
 * 纳税申报日期配置表,与 tax_expire_date_setting 相对应
 */
public class TaxExpireDateSetting {
    private Integer id;
    private String yearString;// 纳税申报日期配置表-年
    private String monthString;// 纳税申报日期配置表-月
    private String dateString;// 纳税申报日期配置表-日
    private String createTime;// 创建时间
    private String updateTime;// 更新时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYearString() {
        return yearString;
    }

    public void setYearString(String yearString) {
        this.yearString = yearString;
    }

    public String getMonthString() {
        return monthString;
    }

    public void setMonthString(String monthString) {
        this.monthString = monthString;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
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
}
