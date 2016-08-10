package com.fliaping.wifi.auth.service;

import com.fliaping.wifi.auth.domain.model.Client;
import com.fliaping.wifi.auth.domain.model.LogOnline;
import com.fliaping.wifi.auth.domain.model.Session;
import com.fliaping.wifi.auth.domain.repository.SessionRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by Payne on 8/10/16.
 */
@Component("clientService")
public class ClientServiceImpl implements ClientService {

    private final SessionRepository sessionRepository;

    @Autowired
    private Environment env;


    Logger logger = Logger.getLogger(getClass());

    public ClientServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public boolean isOnline(Client client) {
        Session session = sessionRepository.findByClientId(client.getId());
        if (session == null) return false;
        LogOnline logOnline = session.getLogOnline();

        if (logOnline == null){
            return false;
        }else {
            logger.warn("logoline:"+ logOnline.getId());
            Long fromLastDuration = System.currentTimeMillis() - logOnline.getUpdateTime();
            int timeout = Integer.parseInt(env.getProperty("wifidog.timeout"));
            logger.warn("wifidog.timeout:"+timeout + " fromLastDuration:"+fromLastDuration + "update:" +logOnline.getUpdateTime());
            if(fromLastDuration > timeout * 1000 ){
                return false;
            }else if (client.isEnable()){
                return true;
            }else return false;
        }
    }
}
