package com.fliaping.wifi.auth.domain.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Payne on 8/4/16.
 * 终端的在线记录
 */
@Entity
public class LogOnline implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @NaturalId
    private String token;

    private long incoming;

    private long outgoing;

    private long beginTime;

    private long updateTime;


    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private Client client;

    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private Router router;

    public LogOnline(){}

    public long getId() {
        return id;
    }

    public LogOnline setId(long id) {
        this.id = id;
        return this;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public LogOnline setBeginTime(long beginTime) {
        this.beginTime = beginTime;
        return this;
    }

    public long getIncoming() {
        return incoming;
    }

    public LogOnline setIncoming(long incoming) {
        this.incoming = incoming;
        return this;
    }

    public long getOutgoing() {
        return outgoing;
    }

    public LogOnline setOutgoing(long outgoing) {
        this.outgoing = outgoing;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public LogOnline setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public LogOnline setClient(Client client) {
        this.client = client;
        return this;
    }

    public String getToken() {
        return token;
    }

    public LogOnline setToken(String token) {
        this.token = token;
        return this;
    }

    public Router getRouter() {
        return router;
    }

    public LogOnline setRouter(Router router) {
        this.router = router;
        return this;
    }
}
