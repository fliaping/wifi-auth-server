package com.fliaping.wifi.auth.domain;



import com.fliaping.wifi.auth.utils.CodecUtil;

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

    public WeixinTicket(String appId , String extend ,long timestamp, String shopId , String authUrl ,
                        String mac , String ssid , String bssid , String secretKey){
        this.ssid = ssid;
        this.shopId = shopId;
        this.appId = appId;
        this.secretKey = secretKey;
        this.extend = extend;
        this.authUrl = authUrl;
        this.mac = mac;
        this.bssid = bssid;
        this.timestamp = ""+timestamp;

        //得到签名
        toSign();

    }

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

    /**
     * 得到传给微信的签名值
     */
    public void toSign(){
        //sign = MD5(appId + extend + timestamp + shop_id + authUrl + mac + ssid + bssid + secretkey);
        String originString = appId + extend + timestamp + shopId + authUrl + mac + ssid + bssid + secretKey;

        //System.out.println("originString: "+ originString);
        sign = CodecUtil.MD5(originString);
    }
}
