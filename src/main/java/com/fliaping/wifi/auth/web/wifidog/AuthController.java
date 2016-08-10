package com.fliaping.wifi.auth.web.wifidog;

import com.fliaping.wifi.auth.domain.model.Client;
import com.fliaping.wifi.auth.domain.model.LogOnline;
import com.fliaping.wifi.auth.domain.model.Session;
import com.fliaping.wifi.auth.domain.model.User;
import com.fliaping.wifi.auth.domain.repository.ClientRepository;
import com.fliaping.wifi.auth.domain.repository.LogOnlineRepository;
import com.fliaping.wifi.auth.domain.repository.SessionRepository;
import com.fliaping.wifi.auth.service.DataQuotaService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Payne on 8/2/16.
 */

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LogOnlineRepository logOnlineRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DataQuotaService dataQuotaService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response,
                      @RequestParam(value = "stage",required = true) String stage,
                      @RequestParam(value = "ip",required = true) String ip,
                      @RequestParam(value = "mac",required = true) String mac,
                      @RequestParam(value = "token",required = true) String token,
                      @RequestParam(value = "incoming",required = true) String incoming,
                      @RequestParam(value = "outgoing",required = true) String outgoing,
                      @RequestParam(value = "gw_id",required = true) String gw_id){


        System.out.println("AUTH: "+request.getQueryString());

        Session session = sessionRepository.findByToken(token);
        if (session == null) {  //认证时没有该session,表示程序出错
            responseAuth(response,false);
            return;
        }

        LogOnline logOnline = null;
        int stageInt = -1;
        if("login".equals(stage)){
            stageInt = 0;
            Client client = clientRepository.findByMac(mac);
            logOnline= new LogOnline();
            logOnline.setToken(token)
                    .setClient(client)
                    .setRouter(session.getRouter())
                    .setBeginTime(System.currentTimeMillis())
                    .setIncoming(Long.parseLong(incoming))
                    .setOutgoing(Long.parseLong(outgoing));
        }else if ("counters".equals(stage)){
            stageInt = 1;
            logOnline = logOnlineRepository.findByToken(token);
            if (logOnline != null){
                logOnline.setIncoming(Long.parseLong(incoming))
                        .setOutgoing(Long.parseLong(outgoing))
                        .setUpdateTime(System.currentTimeMillis());
            }
        }else {
            responseAuth(response,false);
            return;
        }

        logOnline = logOnlineRepository.save(logOnline);

        // TODO: 8/5/16 判断是否流量用超,返回认证失败,并将session下线。
        Client client = session.getClient();
        if(client != null && isAllowConnect(client.getUser())){  //检查允许连接
            if (stageInt == 0) { //login认证,设置session信息
                session.setIp(ip)
                        .setLogOnline(logOnline);
                sessionRepository.save(session);
            }
            client.setEnable(true);
            clientRepository.save(client);
            //返回成功认证
            responseAuth(response,true);

        }else {  //不允许连接

            if(client != null){  //客户端状态改为禁用
                client.setEnable(false);
                clientRepository.save(client);
            }

            responseAuth(response,false);
            return;
        }
    }

    /**
     * 向wifidog返回认证结果
     * @param response http response对象
     * @param ok 是否认证通过
     */
    private void responseAuth(HttpServletResponse response,boolean ok){
            try {
                PrintWriter out = response.getWriter();
                if (ok){
                    out.print("Auth: 1");
                }else {
                    out.print("Auth: 0");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private boolean isAllowConnect(User user){
        boolean isOutUsage = dataQuotaService.isOutOfUsage(user);
        return !isOutUsage;
    }
}


