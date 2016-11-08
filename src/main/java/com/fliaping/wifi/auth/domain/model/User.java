package com.fliaping.wifi.auth.domain.model;


import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Payne on 8/4/16.
 */
@Entity
public class User implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String wxOpenId;

    private String wxTid;

    private long lastLogin;

    private int loginCount;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Client> clients;

    @Column(columnDefinition = "boolean default false")
    private boolean enable;

    public User(){}

    public User(String wxOpenId){
        this.wxOpenId = wxOpenId;
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public User setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
        return this;
    }

    public String getWxTid() {
        return wxTid;
    }

    public User setWxTid(String wxTid) {
        this.wxTid = wxTid;
        return this;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public User setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public User setLoginCount(int loginCount) {
        this.loginCount = loginCount;
        return this;
    }

    public List<Client> getClients() {
        return clients;
    }

    public User setClients(List<Client> clients) {
        this.clients = clients;
        return this;
    }

    public boolean isEnable() {
        return enable;
    }

    public User setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }
}
