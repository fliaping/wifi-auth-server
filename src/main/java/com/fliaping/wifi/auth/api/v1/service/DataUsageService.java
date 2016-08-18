package com.fliaping.wifi.auth.api.v1.service;

import com.fliaping.wifi.auth.api.v1.response.DataUsage;

import java.util.Date;

/**
 * Created by Payne on 8/11/16.
 */
public interface DataUsageService {

    DataUsage getUsageByUid(Long uid, Date startdate, Date enddate);
    DataUsage getUsageByOpenId(String openid,Date startdate, Date enddate);

    DataUsage getUsageByClientId(Long client_id, Date startdate, Date enddate);
    DataUsage getUsageByMac(String mac, Date startdate, Date enddate);

    DataUsage getUsageByRouter_id(Long router_id, Date startdate, Date enddate);


    DataUsage getLastUsageByMac(String mac);

    DataUsage getLastUsageByClientId(long client_id);
}
