package com.fliaping.wifi.auth.service;

import com.fliaping.wifi.auth.domain.model.Client;

/**
 * Created by Payne on 8/10/16.
 */
public interface ClientService {

    boolean isOnline(Client client);
}
