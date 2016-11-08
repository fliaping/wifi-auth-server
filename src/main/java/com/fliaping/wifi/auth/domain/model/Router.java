package com.fliaping.wifi.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Payne on 8/4/16.
 */
@Entity
@Table(name = "router")
public class Router implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "gw_id",nullable = false,unique = true)
    private String gwId;

    @Column(name = "gw_address",columnDefinition = "varchar(15) default '192.168.1.1'")
    private String gwAddress;

    @Column(name = "gw_port",columnDefinition = "int(5) default 2060")
    private int gwPort;

    private String name;

    private String remark;

    private String ssid;

    private String bssid;

    @ManyToOne/*(cascade = CascadeType.ALL)*/ //注释掉之后,如果MpShop表中没有该记录不能添加成功
    @JoinColumn(name = "mp_shop_id"/*,nullable = false*/)
    private MpShop mpShop;

    @OneToMany(mappedBy = "router")
    private List<Session> sessions;

    @OneToOne(cascade = CascadeType.ALL)
    private LogPing lastPing;

    @Column(columnDefinition = "boolean default false")
    private boolean enable;

    public Router(){}

    public Long getId() {
        return id;
    }

    public Router setId(Long id) {
        this.id = id;
        return this;
    }

    public String getGwId() {
        return gwId;
    }

    public Router setGwId(String gwId) {
        this.gwId = gwId;
        return this;
    }

    public String getSsid() {
        return ssid;
    }

    public Router setSsid(String ssid) {
        this.ssid = ssid;
        return this;
    }

    public String getBssid() {
        return bssid;
    }

    public Router setBssid(String bssid) {
        this.bssid = bssid;
        return this;
    }

    public String getName() {
        return name;
    }

    public Router setName(String name) {
        this.name = name;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public Router setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public boolean isEnable() {
        return enable;
    }

    public Router setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }

    public String getGwAddress() {
        return gwAddress;
    }

    public Router setGwAddress(String gwAddress) {
        this.gwAddress = gwAddress;
        return this;
    }

    public int getGwPort() {
        return gwPort;
    }

    public Router setGwPort(int gwPort) {
        this.gwPort = gwPort;
        return this;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public Router setSessions(List<Session> sessions) {
        this.sessions = sessions;
        return this;
    }

    public LogPing getLastPing() {
        return lastPing;
    }

    public Router setLastPing(LogPing lastPing) {
        this.lastPing = lastPing;
        return this;
    }

    public MpShop getMpShop() {
        return mpShop;
    }

    public Router setMpShop(MpShop mpShop) {
        this.mpShop = mpShop;
        return this;
    }
}