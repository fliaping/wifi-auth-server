package com.fliaping.wifi.auth.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Payne on 8/4/16.
 * 路由器在线记录,记录每一次 ping 状态,定期删除
 */
@Entity
public class LogPing implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private Router router;

    private long sysUptime;

    private long sysMemfree;

    private float sysLoad;

    private long wifidogUptime;

    private long updateTime;

    public LogPing(){}


    public long getId() {
        return id;
    }

    public LogPing setId(long id) {
        this.id = id;
        return this;
    }

    public Router getRouter() {
        return router;
    }

    public LogPing setRouter(Router router) {
        this.router = router;
        return this;
    }

    public long getSysUptime() {
        return sysUptime;
    }

    public LogPing setSysUptime(long sysUptime) {
        this.sysUptime = sysUptime;
        return this;
    }

    public long getSysMemfree() {
        return sysMemfree;
    }

    public LogPing setSysMemfree(long sysMemfree) {
        this.sysMemfree = sysMemfree;
        return this;
    }

    public float getSysLoad() {
        return sysLoad;
    }

    public LogPing setSysLoad(float sysLoad) {
        this.sysLoad = sysLoad;
        return this;
    }

    public long getWifidogUptime() {
        return wifidogUptime;
    }

    public LogPing setWifidogUptime(long wifidogUptime) {
        this.wifidogUptime = wifidogUptime;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public LogPing setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
