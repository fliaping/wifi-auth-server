package com.fliaping.wifi.auth.domain.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Payne on 8/4/16.
 * 连接到路由器的移动终端model
 */
@Entity
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @NaturalId
    private String mac;

    @Column(length = 1024)
    private String firstUrl;

    private boolean enable = false;

    public Client(){}

    public Client(String mac){
        this.mac = mac;
    }

    public User getUser() {
        return user;
    }

    public Client setUser(User user) {
        this.user = user;
        return this;
    }

    public String getMac() {
        return mac;
    }

    public Client setMac(String mac) {
        this.mac = mac;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Client setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstUrl() {
        return firstUrl;
    }

    public Client setFirstUrl(String firstUrl) {
        this.firstUrl = firstUrl;
        return this;
    }

    public boolean isEnable() {
        return enable;
    }

    public Client setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }
}
