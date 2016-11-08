package com.fliaping.wifi.auth.service;

import com.fliaping.wifi.auth.domain.model.Client;

/**
 * Created by Payne on 8/10/16.
 */
public interface ClientService {

    /**
     * 判断终端是否在线
     * @param client 终端
     * @return 在线:true 不在线:false
     */
    boolean isOnline(Client client);
}