package com.fliaping.wifi.auth.api.v1.service;

import com.fliaping.wifi.auth.api.v1.response.DataUsage;
import com.fliaping.wifi.auth.domain.model.*;
import com.fliaping.wifi.auth.domain.repository.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private final DataQuotaRepository dataQuotaRepository;

    Logger logger = Logger.getLogger(this.getClass());

    public DataUsageServiceImpl(LogOnlineRepository logOnlineRepository, UserRepository userRepository, ClientRepository clientRepository, SessionRepository sessionRepository, DataQuotaRepository dataQuotaRepository) {
        this.logOnlineRepository = logOnlineRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.sessionRepository = sessionRepository;
        this.dataQuotaRepository = dataQuotaRepository;
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

    /**
     *
     * @param client
     * @return 1:流量没有用完,0:当次流量用完,-1:当天流量用完,-2:当月流量用完,-3:总流量用完
     */
    @Override
    public int isOutOfUsage(Client client,boolean ignoreOnce) {

        if (client == null) throw new RuntimeException("client shouldn't be null");

        User user = client.getUser();
        if (user == null) return 1; //TODO:匿名设备,允许通行?

        long incoming[] = {0,0,0};
        long outgoing[] = {0,0,0};

        long[] once = new long[0];
        if (!ignoreOnce) once = getClientOnceUsage(client);

        getUserUsage(user,incoming,outgoing); //该设备所属用户的用量

        DataQuota dataQuota = getQuota(user);

        if (dataQuota.getTotal() > 0 && incoming[2]+outgoing[2] > dataQuota.getTotal()){ //总用量超出
            return -3;
        }else if (dataQuota.getMonth() > 0 && incoming[1]+outgoing[1] > dataQuota.getMonth()){ //当月用量超出
            return -2;
        }else if (dataQuota.getDay() > 0 && incoming[0]+outgoing[0] > dataQuota.getDay()){ //当天用量超出
            return -1;
        }else if (dataQuota.getOnce() > 0 && once[0]+once[1] > dataQuota.getOnce()){ //当次用量超出
            if (ignoreOnce) return 1;
            else return 0;
        }else {
            return 1;
        }
    }

    public long[] getClientOnceUsage(Client client){
        //检测终端本次流量
        Session session = sessionRepository.findByClientId(client.getId());
        LogOnline logOnline = session.getLogOnline();
        if (logOnline == null) throw new RuntimeException("error:找不到当前终端:"+client.getId()+" 的在线记录");
        //当次流量统计
        long once[] = new long[2];
        once[0] = logOnline.getIncoming();
        once[1] = logOnline.getOutgoing();
        return once;
    }

    @Override
    public DataQuota getQuota(User user){
        if (user == null) return null;
        return dataQuotaRepository.findByUserId(user.getId());
    }

    @Override
    public DataQuota getQuota(Client client){
        if (client == null) return null;
        return getQuota(client.getUser());
    }

    /**
     * 获取终端用量
     * @param client 终端
     * @param incoming 长度为4的数组
     * @param outgoing 长度为4的数组
     * @return 返回最后一次终端总流量
     */
    @Override
    public long getClientUsage(Client client, long incoming[], long outgoing[]) {
        if (client == null) throw new RuntimeException("client is null, please check !!");
        if (incoming.length!=4 || outgoing.length!=4) throw new RuntimeException("参数传递错误");

        logger.warn("getClient Usage client:"+client.getId());

        //当次流量统计
        long[] once = getClientOnceUsage(client);
        incoming[0] = once[0];
        outgoing[0] = once[1];

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long start = calendar.getTimeInMillis();
        long now = System.currentTimeMillis();
        if (logOnlineRepository == null) throw new RuntimeException("logOnlineRepository is null");
        //当天流量统计

        Long tmp = null;

        incoming[1] = null == (tmp = logOnlineRepository.getIncomingByClientId(client.getId(), start, now))?0:tmp;
        outgoing[1] = null == (tmp = logOnlineRepository.getOutgoingByClientId(client.getId(), start, now))?0:tmp;

        //当月流量统计
        calendar.set(Calendar.DATE, 1);
        start = calendar.getTimeInMillis();
        incoming[2] = null == (tmp =logOnlineRepository.getIncomingByClientId(client.getId(), start, now))?0:tmp;
        outgoing[2] = null == (tmp =logOnlineRepository.getOutgoingByClientId(client.getId(), start, now))?0:tmp;

        //总流量统计
        incoming[3] = null == (tmp =logOnlineRepository.getIncomingByClientId(client.getId()))?0:tmp;
        outgoing[3] = null == (tmp =logOnlineRepository.getOutgoingByClientId(client.getId()))?0:tmp;

        return incoming[0] + outgoing[0]; //返回最后一次终端总流量
    }

    /**
     *
     * @param user 一个用户
     * @param incoming 数组长度为3,day,month,total流量
     * @param outgoing 数组长度为3,day,month,total流量
     * @return 该用户当天总流量
     */
    @Override
    public long getUserUsage(User user, long incoming[], long outgoing[]){


        if (user == null) throw new RuntimeException("get user Usage parm user is null");
        if (incoming.length!=3 || outgoing.length!=3) throw new RuntimeException("参数传递错误");

        List<Client> clients = new ArrayList<Client>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long dayStart = calendar.getTimeInMillis();
        calendar.set(Calendar.DATE, 1);
        long monthStart = calendar.getTimeInMillis();
        long now = System.currentTimeMillis();

        for (Client client :
                clients) {
            //当天流量统计
            incoming[0] += logOnlineRepository.getIncomingByClientId(client.getId(), dayStart, now);
            outgoing[0] += logOnlineRepository.getOutgoingByClientId(client.getId(), dayStart, now);

            //当月流量统计
            incoming[1] += logOnlineRepository.getIncomingByClientId(client.getId(), monthStart, now);
            outgoing[1] += logOnlineRepository.getOutgoingByClientId(client.getId(), monthStart, now);

            //总流量统计
            incoming[2] += logOnlineRepository.getIncomingByClientId(client.getId());
            outgoing[2] += logOnlineRepository.getOutgoingByClientId(client.getId());
        }
        return incoming[0]+outgoing[0];
    }


}
