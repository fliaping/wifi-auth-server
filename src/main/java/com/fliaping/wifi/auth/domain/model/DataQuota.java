package com.fliaping.wifi.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @NaturalId
    @OneToOne
    private User user;

    private long month;

    private long total = 10 * 1024 * 1024;

    private long day;

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

    public long getMonth() {
        return month;
    }

    public DataQuota setMonth(long monthly) {
        this.month = monthly;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public DataQuota setTotal(long total) {
        this.total = total;
        return this;
    }

    public long getDay() {
        return day;
    }

    public DataQuota setDay(long daily) {
        this.day = daily;
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
