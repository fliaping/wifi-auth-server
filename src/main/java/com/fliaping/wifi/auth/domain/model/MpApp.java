package com.fliaping.wifi.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Payne on 8/16/16.
 * 微信公众号
 */
@Entity
@Table(name = "mp_app")
public class MpApp implements Serializable{

    //ACCESS_TOKEN:  FN0reFfQb5-W2lWi5uNXVjeQzyk6Pj8Q17wCc-BEq_2zeqzlH1xbKnCT7MeJu5GsAzkuS2CrEZcr9fXQ_lV18gXiQjjIwpIxBKSzix1STZMVDFxcLO1_4OnpP_k8n9OIDVJhAAATLZ
    //APP_SECRET:  W2lWi5uNXVjeQzyk6Pj8Q17wCc

    @Id
    @Column(length = 512)
    private String appId;

    private String name;

    private String appSecret;

    private String accessToken;

    private String authUrl;

    @JsonIgnore //两个实体之间不能互相引用,否则在序列化为json时会产生infinite recursion
    @OneToMany(mappedBy = "mpApp")
    private List<MpShop> mpShops;


    public String getName() {
        return name;
    }

    public MpApp setName(String name) {
        this.name = name;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public MpApp setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public MpApp setAppSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public MpApp setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public MpApp setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
        return this;
    }

    public List<MpShop> getMpShops() {
        return mpShops;
    }

    public MpApp setMpShops(List<MpShop> mpShops) {
        this.mpShops = mpShops;
        return this;
    }
}
