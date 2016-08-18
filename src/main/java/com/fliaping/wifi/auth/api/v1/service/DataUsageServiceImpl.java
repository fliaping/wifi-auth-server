package com.fliaping.wifi.auth.api.v1.service;

import com.fliaping.wifi.auth.api.v1.response.DataUsage;
import com.fliaping.wifi.auth.domain.model.Client;
import com.fliaping.wifi.auth.domain.model.LogOnline;
import com.fliaping.wifi.auth.domain.model.User;
import com.fliaping.wifi.auth.domain.repository.ClientRepository;
import com.fliaping.wifi.auth.domain.repository.LogOnlineRepository;
import com.fliaping.wifi.auth.domain.repository.SessionRepository;
import com.fliaping.wifi.auth.domain.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.List;

import static com.fliaping.wifi.auth.utils.MyDateUtil.*;

/**
 * Created by Payne on 8/11/16.
 */
@Component("dataUsageService")
@Transactional
public class DataUsageServiceImpl implements DataUsageService {

    private final LogOnlineRepository logOnlineRepository;

    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    private final SessionRepository sessionRepository;

    public DataUsageServiceImpl(LogOnlineRepository logOnlineRepository, UserRepository userRepository, ClientRepository clientRepository, SessionRepository sessionRepository) {
        this.logOnlineRepository = logOnlineRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public DataUsage getUsageByUid(Long uid, Date startdate, Date enddate) {
        User user = userRepository.findById(uid);
        return getUsageByUser(user,startdate,enddate);
    }

    @Override
    public DataUsage getUsageByOpenId(String openid, Date startdate, Date enddate) {
        User user = userRepository.findByWxOpenId(openid);
        return getUsageByUser(user,startdate,enddate);
    }

    public DataUsage getUsageByUser(User user, Date startdate, Date enddate) {
        if (user == null) return null;
        List<Client> clients = user.getClients();
        long incoming = 0,outgoing = 0;
        for (Client client :
                clients) {
            incoming += logOnlineRepository.getIncomingByClientId(client.getId());
            outgoing += logOnlineRepository.getOutgoingByClientId(client.getId());
        }

        return  buildDataUsage(incoming,outgoing,startdate,enddate);
    }



    @Override
    public DataUsage getUsageByClientId(Long client_id, Date startdate, Date enddate) {

        long incoming = logOnlineRepository.getIncomingByClientId(client_id,date2Long(startdate),date2Long(enddate));
        long outgoing = logOnlineRepository.getOutgoingByClientId(client_id,date2Long(startdate),date2Long(enddate));

        return buildDataUsage(incoming,outgoing,startdate,enddate);
    }

    @Override
    public DataUsage getUsageByMac(String mac, Date startdate, Date enddate) {
        Client client = clientRepository.findByMac(mac);

        return getUsageByClientId(client.getId(),startdate,enddate);
    }

    @Override
    public DataUsage getUsageByRouter_id(Long router_id, Date startdate, Date enddate) {
        return null;
    }


    @Override
    public DataUsage getLastUsageByMac(String mac) {
        Client client = clientRepository.findByMac(mac);
        if (client == null) return null;
        return getLastUsageByClientId(client.getId());
    }

    @Override
    public DataUsage getLastUsageByClientId(long client_id){
        LogOnline logOnline = logOnlineRepository.getLastByClient(client_id);
        if (logOnline != null){
            DataUsage dataUsage = new DataUsage();
            dataUsage.setIncoming(logOnline.getIncoming())
                    .setOutgoing(logOnline.getOutgoing());
            return dataUsage;
        }
        return null;
    }

    private DataUsage buildDataUsage(long incoming, long outgoing, Date startdate,Date enddate){
        DataUsage dataUsage = new DataUsage();
        dataUsage.setIncoming(incoming)
                .setOutgoing(outgoing)
                .setStartdate(startdate)
                .setEnddate(enddate);
        return dataUsage;
    }
}
