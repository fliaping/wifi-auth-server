package com.fliaping.wifi.auth.domain.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Payne on 8/16/16.
 * 公众号的门店信息
 */
@Entity
public class MpShop implements Serializable{

    @Id
    @Column(length = 512)
    private String shopId;

    private String name;


    private String secretKey;

    @ManyToOne
    private MpApp mpApp;


    public String getName() {
        return name;
    }

    public MpShop setName(String name) {
        this.name = name;
        return this;
    }

    public String getShopId() {
        return shopId;
    }

    public MpShop setShopId(String shopId) {
        this.shopId = shopId;
        return this;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public MpShop setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public MpApp getMpApp() {
        return mpApp;
    }

    public MpShop setMpApp(MpApp mpApp) {
        this.mpApp = mpApp;
        return this;
    }
}
