package org.webdriver.patatiumwebui.httprequest.vo;

public class WriteBackTaxResultVO {
    private String nationaltaxLoginname;

    /*
    xmYbZzs/厦门一般纳税人增值税
    xmXgmZzs/厦门小规模增值税
    xmYhs/厦门印花税
     */
    private String taxName;
    private String screenBase64;
    private String belongDateBegin;
    private String belongDateEnd;
    private String taxEndDate;
    private String ycxx;// 异常信息
    private boolean success;

    public String getNationaltaxLoginname() {
        return nationaltaxLoginname;
    }

    public void setNationaltaxLoginname(String nationaltaxLoginname) {
        this.nationaltaxLoginname = nationaltaxLoginname;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getScreenBase64() {
        return screenBase64;
    }

    public void setScreenBase64(String screenBase64) {
        this.screenBase64 = screenBase64;
    }

    public String getBelongDateBegin() {
        return belongDateBegin;
    }

    public void setBelongDateBegin(String belongDateBegin) {
        this.belongDateBegin = belongDateBegin;
    }

    public String getBelongDateEnd() {
        return belongDateEnd;
    }

    public void setBelongDateEnd(String belongDateEnd) {
        this.belongDateEnd = belongDateEnd;
    }

    public String getTaxEndDate() {
        return taxEndDate;
    }

    public void setTaxEndDate(String taxEndDate) {
        this.taxEndDate = taxEndDate;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getYcxx() {
        return ycxx;
    }

    public void setYcxx(String ycxx) {
        this.ycxx = ycxx;
    }
}
