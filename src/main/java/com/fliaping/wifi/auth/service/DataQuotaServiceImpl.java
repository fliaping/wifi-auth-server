package com.fliaping.wifi.auth.service;

import com.fliaping.wifi.auth.domain.model.Client;
import com.fliaping.wifi.auth.domain.model.DataQuota;
import com.fliaping.wifi.auth.domain.model.User;
import com.fliaping.wifi.auth.domain.repository.DataQuotaRepository;
import com.fliaping.wifi.auth.domain.repository.LogOnlineRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by Payne on 8/10/16.
 */
@Component("dataUsageService")
@Transactional
public class DataQuotaServiceImpl implements DataQuotaService {

    private final DataQuotaRepository dataQuotaRepository;

    private final LogOnlineRepository logOnlineRepository;

    private Logger logger = Logger.getLogger(getClass());

    public DataQuotaServiceImpl(DataQuotaRepository dataQuotaRepository, LogOnlineRepository logOnlineRepository) {
        this.dataQuotaRepository = dataQuotaRepository;
        this.logOnlineRepository = logOnlineRepository;
    }

    @Override
    public boolean isOutOfUsage(User user) {

        if (user == null) throw new RuntimeException("isOutOfUsage parm user is null");
        DataQuota dataQuota = dataQuotaRepository.findByUserId(user.getId());

        if (dataQuota != null){
            long totalQuota = dataQuota.getTotal();

            Set<Client> clients = user.getClients();
            long incoming = 0;
            long outgoing = 0;
            for (Client client :
                    clients) {
                incoming += logOnlineRepository.getIncomingByClientId(client.getId());
                outgoing += logOnlineRepository.getOutgoingByClientId(client.getId());
            }

            logger.warn("用户:"+user.getId()+" 使用流量: in("+incoming/1024.0/1024.0+"M) out("+outgoing/1024.0/1024.0+"M) total("+(incoming+outgoing)/1024.0/1024.0+"M)");

            if ((incoming+outgoing) < totalQuota) return false;
            else return true;
        }else {
            throw new RuntimeException("dataQuota is null");
        }
    }
}
