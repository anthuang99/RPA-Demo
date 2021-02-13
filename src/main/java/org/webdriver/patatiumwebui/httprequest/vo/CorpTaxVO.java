package org.webdriver.patatiumwebui.httprequest.vo;

public class CorpTaxVO {
    private String userName;
    private String password;
    private String domainName;
    private int pageNo;
    private int pageSize;
    private String name;
    private String createM;
    private String isReport;// 0:申报失败,或者是未申报的企业数据,(主要是登录接口调用) 1:成功或失败都返回,在查看结果列表时传值
    private String nationaltaxLoginname;
    private String declareStatus;// 已申报(0),未申报(1),部分申报(2)
    private String detailStatus;// 4:成功,3:失败
    private String isShowPicture;// isShowPicture,0(否,返回字段中否添加截图),1(是,返回字段中否添加截图)

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationaltaxLoginname() {
        return nationaltaxLoginname;
    }

    public void setNationaltaxLoginname(String nationaltaxLoginname) {
        this.nationaltaxLoginname = nationaltaxLoginname;
    }

    public String getCreateM() {
        return createM;
    }

    public void setCreateM(String createM) {
        this.createM = createM;
    }

    public String getIsReport() {
        return isReport;
    }

    public void setIsReport(String isReport) {
        this.isReport = isReport;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDeclareStatus() {
        return declareStatus;
    }

    public void setDeclareStatus(String declareStatus) {
        this.declareStatus = declareStatus;
    }

    public String getIsShowPicture() {
        return isShowPicture;
    }

    public void setIsShowPicture(String isShowPicture) {
        this.isShowPicture = isShowPicture;
    }

    public String getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(String detailStatus) {
        this.detailStatus = detailStatus;
    }
}
