package org.webdriver.patatiumwebui.web.model;

/**
 * 报税进度表,与 TAX_DECLARE_PROGRESS 相对应
 */
public class TaxDeclareProgress {
    private Integer id;
    private String startTime;// 报税启动时间
    private Integer userTotalCount;// 报税用户总数
    private Integer userFinishCount;// 已报税完成数
    private Integer userFinishSuccessCount;// 已报税完成成功数
    private Integer userFinishFailCount;// 已报税完成失败数,不存 DB, 值 = userFinishCount - userFinishSuccessCount
    private String currentUser;// 当前报税人
    private String currentTaxTime;// 当前报税时间
    private String createTime;// 创建时间
    private String updateTime;// 更新时间

    // 显示字段
    private String startTimeShow;
    private String currentTaxTimeShow;
    private String timeSpendMinute;
    private String createTimeShow;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getUserTotalCount() {
        return userTotalCount;
    }

    public void setUserTotalCount(Integer userTotalCount) {
        this.userTotalCount = userTotalCount;
    }

    public Integer getUserFinishCount() {
        return userFinishCount;
    }

    public void setUserFinishCount(Integer userFinishCount) {
        this.userFinishCount = userFinishCount;
    }

    public Integer getUserFinishSuccessCount() {
        return userFinishSuccessCount;
    }

    public void setUserFinishSuccessCount(Integer userFinishSuccessCount) {
        this.userFinishSuccessCount = userFinishSuccessCount;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrentTaxTime() {
        return currentTaxTime;
    }

    public void setCurrentTaxTime(String currentTaxTime) {
        this.currentTaxTime = currentTaxTime;
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

    public String getStartTimeShow() {
        return startTimeShow;
    }

    public void setStartTimeShow(String startTimeShow) {
        this.startTimeShow = startTimeShow;
    }

    public String getCurrentTaxTimeShow() {
        return currentTaxTimeShow;
    }

    public void setCurrentTaxTimeShow(String currentTaxTimeShow) {
        this.currentTaxTimeShow = currentTaxTimeShow;
    }

    public String getTimeSpendMinute() {
        return timeSpendMinute;
    }

    public void setTimeSpendMinute(String timeSpendMinute) {
        this.timeSpendMinute = timeSpendMinute;
    }

    public String getCreateTimeShow() {
        return createTimeShow;
    }

    public void setCreateTimeShow(String createTimeShow) {
        this.createTimeShow = createTimeShow;
    }

    public Integer getUserFinishFailCount() {
        return userFinishFailCount;
    }

    public void setUserFinishFailCount(Integer userFinishFailCount) {
        this.userFinishFailCount = userFinishFailCount;
    }
}
