package com.fliaping.wifi.auth.domain.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Payne on 8/5/16.
 * 用户的流量配额
 */
@Entity
public class DataQuota implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NaturalId
    @OneToOne
    private User user;

    private long monthly;

    private long total = 10 * 1024 * 1024;

    private long daily;

    private long once;

    public DataQuota(){}

    public DataQuota(User user){
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public DataQuota setUser(User user) {
        this.user = user;
        return this;
    }

    public long getMonthly() {
        return monthly;
    }

    public DataQuota setMonthly(long monthly) {
        this.monthly = monthly;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public DataQuota setTotal(long total) {
        this.total = total;
        return this;
    }

    public long getDaily() {
        return daily;
    }

    public DataQuota setDaily(long daily) {
        this.daily = daily;
        return this;
    }

    public long getOnce() {
        return once;
    }

    public DataQuota setOnce(long once) {
        this.once = once;
        return this;
    }
}
