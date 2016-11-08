package com.fliaping.wifi.auth.service;

import com.fliaping.wifi.auth.domain.model.*;
import com.fliaping.wifi.auth.domain.repository.DataQuotaRepository;
import com.fliaping.wifi.auth.domain.repository.LogOnlineRepository;
import com.fliaping.wifi.auth.domain.repository.SessionRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Payne on 8/10/16.
 */
@Component("dataQuotaService")
@Transactional
public class DataQuotaServiceImpl implements DataQuotaService {

    private final DataQuotaRepository dataQuotaRepository;

    private final LogOnlineRepository logOnlineRepository;

    private final SessionRepository sessionRepository;

    private Logger logger = Logger.getLogger(getClass());

    public DataQuotaServiceImpl(DataQuotaRepository dataQuotaRepository, LogOnlineRepository logOnlineRepository, SessionRepository sessionRepository) {
        this.dataQuotaRepository = dataQuotaRepository;
        this.logOnlineRepository = logOnlineRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * 是否流量用超
     * @param client 终端
     * @return 1:流量没有用完,0:当次流量用完,-1:当天流量用完,-2:当月流量用完,-3:总流量用完
     */
    @Override
    public int isOutOfUsage(Client client) {
        User user = client.getUser();



        if (user == null) throw new RuntimeException("isOutOfUsage parm user is null");
        DataQuota dataQuota = dataQuotaRepository.findByUserId(user.getId());

        if (dataQuota != null){
            //检测终端本次流量
            long incoming[] = {0,0,0,0};
            long outgoing[] = {0,0,0,0};

            Session session = sessionRepository.findByClientId(client.getId());
            LogOnline logOnline = session.getLogOnline();
            if (logOnline == null) throw new RuntimeException("error:找不到当前终端:"+client.getId()+" 的在线记录");
            //当次流量统计
            incoming[0] = logOnline.getIncoming();
            outgoing[0] = logOnline.getOutgoing();


            List<Client> clients = user.getClients();

            for (Client clientone :
                    clients) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                long start = calendar.getTimeInMillis();
                long now = System.currentTimeMillis();
                //当天流量统计
                incoming[1] += logOnlineRepository.getIncomingByClientId(client.getId(),start,now);
                outgoing[1] += logOnlineRepository.getOutgoingByClientId(client.getId(),start,now);

                //当月流量统计
                calendar.set(Calendar.DATE,1);
                start = calendar.getTimeInMillis();
                incoming[2] += logOnlineRepository.getIncomingByClientId(client.getId(),start,now);
                outgoing[2] += logOnlineRepository.getOutgoingByClientId(client.getId(),start,now);

                //总流量统计
                incoming[3] += logOnlineRepository.getIncomingByClientId(clientone.getId());
                outgoing[3] += logOnlineRepository.getOutgoingByClientId(clientone.getId());
            }

            //logger.warn("用户:"+user.getId()+" 使用流量: in("+incoming/1024.0/1024.0+"M) out("+outgoing/1024.0/1024.0+"M) total("+(incoming+outgoing)/1024.0/1024.0+"M)");

            if (dataQuota.getTotal() > 0 && incoming[3]+outgoing[3] > dataQuota.getTotal()){ //总用量超出
                return -3;
            }else if (dataQuota.getMonthly() > 0 && incoming[2]+outgoing[2] > dataQuota.getMonthly()){ //当月用量超出
                return -2;
            }else if (dataQuota.getDaily() > 0 && incoming[1]+outgoing[1] > dataQuota.getDaily()){ //当天用量超出
                 return -1;
            }else if (dataQuota.getOnce() > 0 && incoming[0]+outgoing[0] > dataQuota.getOnce()){ //当次用量超出
                 return 0;
            }else {
                return 1;
            }
        }else {
            throw new RuntimeException("dataQuota is null");
        }
    }
}
