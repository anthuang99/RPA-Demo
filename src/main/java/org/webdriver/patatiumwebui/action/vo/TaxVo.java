package org.webdriver.patatiumwebui.action.vo;

/**
 * 报税 Vo
 */
public class TaxVo {
    private String nationaltaxLoginname;// 国税登录名,例如:91350200MA31E44KXC
    private String stampTaxBelongItem;// 印花税征收品目,从首页->我的信息->纳税人信息->纳税人税（费）种认定信息中查询

    public String getNationaltaxLoginname() {
        return nationaltaxLoginname;
    }

    public void setNationaltaxLoginname(String nationaltaxLoginname) {
        this.nationaltaxLoginname = nationaltaxLoginname;
    }

    public String getStampTaxBelongItem() {
        return stampTaxBelongItem;
    }

    public void setStampTaxBelongItem(String stampTaxBelongItem) {
        this.stampTaxBelongItem = stampTaxBelongItem;
    }
}
