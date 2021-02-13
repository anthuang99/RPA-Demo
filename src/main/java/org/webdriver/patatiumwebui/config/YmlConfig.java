package org.webdriver.patatiumwebui.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="tax-prop") //application.yml中的myProps下的属性
public class YmlConfig {
    private String checkcode_testUri;
    private String checkcode_uri;
    private String checkcode_taskUrl;
    private String checkcode_CS;
    private String checkcode_APP_KEY;
    private String baseUrl;
//    private String driver;
    private String nodeURL;

    private String zyd_token_key_cs;
    private String zyd_token_key_chs;
    private String timeOutInSeconds;

    private String corpListUrl;
    private String taxDeclareDetailUrl;
    private String writeBackTaxNameUrl;
    private String writeBackTaxResult;

    private String isDev;
    private String isClickLogin;
    private String isManualCheckcode;
    private String seleniumServerExe;
    private String seleniumNodeChromeExe;
    private String seleniumNodeFirefoxExe;
    private String seleniumNodeIEExe;
    private String seleniumServerPort;
    private String seleniumNodePort;

    public String getCheckcode_testUri() {
        return checkcode_testUri;
    }

    public void setCheckcode_testUri(String checkcode_testUri) {
        this.checkcode_testUri = checkcode_testUri;
    }

    public String getIsDev() {
        return isDev;
    }

    public void setIsDev(String isDev) {
        this.isDev = isDev;
    }

    public String getIsClickLogin() {
        return isClickLogin;
    }

    public void setIsClickLogin(String isClickLogin) {
        this.isClickLogin = isClickLogin;
    }

    public String getCheckcode_uri() {
        return checkcode_uri;
    }

    public void setCheckcode_uri(String checkcode_uri) {
        this.checkcode_uri = checkcode_uri;
    }

    public String getCheckcode_taskUrl() {
        return checkcode_taskUrl;
    }

    public void setCheckcode_taskUrl(String checkcode_taskUrl) {
        this.checkcode_taskUrl = checkcode_taskUrl;
    }

    public String getCheckcode_CS() {
        return checkcode_CS;
    }

    public void setCheckcode_CS(String checkcode_CS) {
        this.checkcode_CS = checkcode_CS;
    }

    public String getCheckcode_APP_KEY() {
        return checkcode_APP_KEY;
    }

    public void setCheckcode_APP_KEY(String checkcode_APP_KEY) {
        this.checkcode_APP_KEY = checkcode_APP_KEY;
    }

    public String getWriteBackTaxNameUrl() {
        return writeBackTaxNameUrl;
    }

    public void setWriteBackTaxNameUrl(String writeBackTaxNameUrl) {
        this.writeBackTaxNameUrl = writeBackTaxNameUrl;
    }

    public String getWriteBackTaxResult() {
        return writeBackTaxResult;
    }

    public void setWriteBackTaxResult(String writeBackTaxResult) {
        this.writeBackTaxResult = writeBackTaxResult;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /*public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }*/

    public String getNodeURL() {
        return nodeURL;
    }

    public void setNodeURL(String nodeURL) {
        this.nodeURL = nodeURL;
    }

    public String getZyd_token_key_cs() {
        return zyd_token_key_cs;
    }

    public void setZyd_token_key_cs(String zyd_token_key_cs) {
        this.zyd_token_key_cs = zyd_token_key_cs;
    }

    public String getZyd_token_key_chs() {
        return zyd_token_key_chs;
    }

    public void setZyd_token_key_chs(String zyd_token_key_chs) {
        this.zyd_token_key_chs = zyd_token_key_chs;
    }

    public String getTimeOutInSeconds() {
        return timeOutInSeconds;
    }

    public void setTimeOutInSeconds(String timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
    }

    public String getSeleniumServerExe() {
        return seleniumServerExe;
    }

    public void setSeleniumServerExe(String seleniumServerExe) {
        this.seleniumServerExe = seleniumServerExe;
    }

    public String getSeleniumServerPort() {
        return seleniumServerPort;
    }

    public void setSeleniumServerPort(String seleniumServerPort) {
        this.seleniumServerPort = seleniumServerPort;
    }

    public String getSeleniumNodePort() {
        return seleniumNodePort;
    }

    public void setSeleniumNodePort(String seleniumNodePort) {
        this.seleniumNodePort = seleniumNodePort;
    }

    public String getSeleniumNodeChromeExe() {
        return seleniumNodeChromeExe;
    }

    public void setSeleniumNodeChromeExe(String seleniumNodeChromeExe) {
        this.seleniumNodeChromeExe = seleniumNodeChromeExe;
    }

    public String getSeleniumNodeFirefoxExe() {
        return seleniumNodeFirefoxExe;
    }

    public void setSeleniumNodeFirefoxExe(String seleniumNodeFirefoxExe) {
        this.seleniumNodeFirefoxExe = seleniumNodeFirefoxExe;
    }

    public String getSeleniumNodeIEExe() {
        return seleniumNodeIEExe;
    }

    public void setSeleniumNodeIEExe(String seleniumNodeIEExe) {
        this.seleniumNodeIEExe = seleniumNodeIEExe;
    }

    public String getCorpListUrl() {
        return corpListUrl;
    }

    public void setCorpListUrl(String corpListUrl) {
        this.corpListUrl = corpListUrl;
    }

    public String getTaxDeclareDetailUrl() {
        return taxDeclareDetailUrl;
    }

    public void setTaxDeclareDetailUrl(String taxDeclareDetailUrl) {
        this.taxDeclareDetailUrl = taxDeclareDetailUrl;
    }

    public String getIsManualCheckcode() {
        return isManualCheckcode;
    }

    public void setIsManualCheckcode(String isManualCheckcode) {
        this.isManualCheckcode = isManualCheckcode;
    }
}
