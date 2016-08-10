package com.fliaping.wifi.auth.domain.model;


import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Payne on 8/4/16.
 */
@Entity
@Table(name = "session")
public class Session implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id",unique = true)
    private Client client;

    private String ip;

    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private Router router;

    @Column(unique = true)
    private String token;

    @OneToOne
    private LogOnline logOnline;

    @Column(name = "begin_time")
    private long beginTime;

    public Session(){}

    public Session(Client client,Router router,String token){
        this.client = client;
        this.router = router;
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public Session setToken(String token) {
        this.token = token;
        return this;
    }


    public String getIp() {
        return ip;
    }

    public Session setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Session setId(Long id) {
        this.id = id;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public Session setClient(Client client) {
        this.client = client;
        return this;
    }

    public Router getRouter() {
        return router;
    }

    public Session setRouter(Router router) {
        this.router = router;
        return this;
    }

    public LogOnline getLogOnline() {
        return logOnline;
    }

    public Session setLogOnline(LogOnline logOnline) {
        this.logOnline = logOnline;
        return this;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public Session setBeginTime(long beginTime) {
        this.beginTime = beginTime;
        return this;
    }
}
