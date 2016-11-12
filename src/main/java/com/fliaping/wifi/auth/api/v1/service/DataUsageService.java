package com.fliaping.wifi.auth.api.v1.service;

import com.fliaping.wifi.auth.api.v1.response.DataUsage;
import com.fliaping.wifi.auth.domain.model.Client;
import com.fliaping.wifi.auth.domain.model.DataQuota;
import com.fliaping.wifi.auth.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Payne on 8/11/16.
 */
@Service
public interface DataUsageService {

    DataUsage getUsageByUid(Long uid, Date startdate, Date enddate);
    DataUsage getUsageByOpenId(String openid,Date startdate, Date enddate);

    DataUsage getUsageByClientId(Long client_id, Date startdate, Date enddate);
    DataUsage getUsageByMac(String mac, Date startdate, Date enddate);

    DataUsage getUsageByRouter_id(Long router_id, Date startdate, Date enddate);


    DataUsage getLastUsageByMac(String mac);

    DataUsage getLastUsageByClientId(long client_id);

    int isOutOfUsage(Client client,boolean ignoreOnce);

    DataQuota getQuota(User user);

    DataQuota getQuota(Client client);

    long getClientUsage(Client client, long incoming[], long outgoing[]);

    long getUserUsage(User user, long incoming[], long outgoing[]);


}
