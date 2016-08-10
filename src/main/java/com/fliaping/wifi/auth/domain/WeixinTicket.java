package com.fliaping.wifi.auth.domain;



import java.io.Serializable;

/**
 * Created by Payne on 8/3/16.
 */

public class WeixinTicket implements Serializable {

    private String ssid = "wifitest";
    private String shopId = "5761311";
    private String appId = "wxd0380c266c06b53f";
    private String secretKey = "23a88d8ef2099d059b9f533df2b8876b";
    private String extend = "hello";
    private String timestamp = ""+System.currentTimeMillis();
    private String authUrl = "http://stu-ali.xvping.cn:9408/weixinauth/";
    private String mac = "aa:aa:aa:aa:aa:aa";
    private String bssid = "ff:ff:ff:ff:ff:ff";
    private String sign = "test";

    public String getSsid() {
        return ssid;
    }

    public String getShopId() {
        return shopId;
    }

    public String getAppId() {
        return appId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getExtend() {
        return extend;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getMac() {
        return mac;
    }

    public String getBssid() {
        return bssid;
    }

    public String getSign() {
        return sign;
    }

    public WeixinTicket setSsid(String ssid) {
        this.ssid = ssid;
        return this;
    }

    public WeixinTicket setShopId(String shopId) {
        this.shopId = shopId;
        return this;
    }

    public WeixinTicket setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public WeixinTicket setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public WeixinTicket setExtend(String extend) {
        this.extend = extend;
        return this;
    }

    public WeixinTicket setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public WeixinTicket setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
        return this;
    }

    public WeixinTicket setMac(String mac) {
        this.mac = mac;
        return this;
    }

    public WeixinTicket setBssid(String bssid) {
        this.bssid = bssid;
        return this;
    }

    public WeixinTicket setSign(String sign) {
        this.sign = sign;
        return this;
    }
}
