package com.fliaping.wifi.auth.service;

import com.fliaping.wifi.auth.domain.model.Client;
import com.fliaping.wifi.auth.domain.model.User;

/**
 * Created by Payne on 8/5/16.
 */
public interface DataQuotaService {

    int isOutOfUsage(Client client);
}
