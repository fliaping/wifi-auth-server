package com.fliaping.wifi.auth.web.wifidog;

import com.fliaping.wifi.auth.domain.model.*;
import com.fliaping.wifi.auth.domain.repository.ClientRepository;
import com.fliaping.wifi.auth.domain.repository.DataQuotaRepository;
import com.fliaping.wifi.auth.domain.repository.SessionRepository;
import com.fliaping.wifi.auth.domain.repository.UserRepository;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Payne on 8/3/16.
 */
@Controller
@RequestMapping("/weixinauth")
public class WeixinAuthController {

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DataQuotaRepository dataQuotaRepository;

    private Logger logger = Logger.getLogger(getClass());

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String doGet(HttpServletRequest request,
                      HttpServletResponse response,
                      @RequestParam(value = "extend",required = true) String extend,
                      @RequestParam(value = "openId",required = true) String openId,
                      @RequestParam(value = "tid",required = true) String tid) {

        System.out.println("weixinauth:"+request.getQueryString());


        String token = extend;


        Session session = sessionRepository.findByToken(token);

        if (session == null){ //出错认证失败
            response.setStatus(500);
            logger.warn("weixinAuthError");
            return "weixinAuthError";
        }

        User user = session.getClient() != null ? session.getClient().getUser() : null;
        if (null != user){  //该设备存在用户,不是匿名设备

            user.setWxOpenId(openId)
                    .setWxTid(tid)
                    .setEnable(true)
                    .setLastLogin(System.currentTimeMillis())
                    .setLoginCount(user.getLoginCount()+1);
            long id = userRepository.save(user).getId();

            logger.warn("user not null ");

            // TODO: 8/5/16 判断是否流量用超,若用超就返回其它代码。

        }else { //该设备为匿名设备
            logger.warn("匿名设备");
            user = userRepository.findByWxOpenId(openId); //查询该微信账户是否是已存在账户

            Client client = session.getClient();

            if (user != null){ //已存在该用户,用户有多个设备

                user.getClients().add(session.getClient()); //为用户添加设备
                user.setLastLogin(System.currentTimeMillis())
                        .setLoginCount(user.getLoginCount()+1);


            }else { //该用户为新接入
                user = new User();

                Set clients = new HashSet<Client>();
                clients.add(client);

                user.setWxOpenId(openId)
                        .setWxTid(tid)
                        .setClients(clients)
                        .setEnable(true)
                        .setLastLogin(System.currentTimeMillis())
                        .setLoginCount(user.getLoginCount()+1);

                user = userRepository.save(user); //用户信息写入数据库
                dataQuotaRepository.save(new DataQuota(user));

            }
            client.setUser(user);
            clientRepository.save(client); //客户端信息写入数据库

        }

        Router router = session.getRouter();
        String gw_url = "http://"+router.getGwAddress()+":"+router.getGwPort()+"/wifidog/auth?token="+token;

        //重定向微信请求认证的地址
        response.addHeader("location", gw_url);
        response.setStatus(302);

        return "weixinAuthAccess";

    }
}
