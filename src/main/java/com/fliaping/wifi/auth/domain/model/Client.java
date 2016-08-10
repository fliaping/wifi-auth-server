package com.fliaping.wifi.auth.domain.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * Created by Payne on 8/4/16.
 */
@Entity
@Table(name = "client")
public class Client {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = true)
    private User user;

    @NaturalId
    private String mac;

    @Column(length = 1024)
    private String firstUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id")
    private Session session;

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

    public Session getSession() {
        return session;
    }

    public Client setSession(Session session) {
        this.session = session;
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
