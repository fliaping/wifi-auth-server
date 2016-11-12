package com.fliaping.wifi.auth.web.wifidog;

import com.fliaping.wifi.auth.api.v1.service.DataUsageService;
import com.fliaping.wifi.auth.domain.model.*;
import com.fliaping.wifi.auth.domain.repository.*;
import com.fliaping.wifi.auth.service.ClientService;
import com.fliaping.wifi.auth.utils.CodecUtil;
import com.fliaping.wifi.auth.domain.WeixinTicket;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Payne on 8/2/16.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private RouterRepository routerRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private DataUsageService dataUsageService;

    @Autowired
    private Environment environment;

    private Logger logger = Logger.getLogger(getClass());


    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String doGet(HttpServletRequest request,
                        HttpServletResponse response,
                        ModelMap model,
                        @RequestParam(value = "gw_address",required = true) String gw_address,
                        @RequestParam(value = "gw_port",required = true) String gw_port,
                        @RequestParam(value = "gw_id",required = true) String gw_id,
                        @RequestParam(value = "mac",required = true) String mac,
                        @RequestParam(value = "url",required = true) String url,
                        @RequestParam(value = "ip",required = true) String ip){


        String APP_ID = null;
        String SHOP_ID = null;
        String SECRET_KEY = null;
        String AUTH_URL = null;


        // 验证gw_id 在路由器列表中,不在则是未配置
        Router router = routerRepository.findByGwId(gw_id);
        if(router == null) return "routerUnconfig"; //路由器未配置,返回配置介绍页
        else { //更新网关wifidog地址端口
            router.setGwAddress(gw_address)
                    .setGwPort(Integer.parseInt(gw_port));
            router = routerRepository.save(router);

            //获取微信参数
            MpShop mpShop = router.getMpShop();
            //this router have no belonged MpShop ,please check!!
            if (mpShop == null) return "routerUnconfig";
            SHOP_ID = mpShop.getShopId();
            SECRET_KEY = mpShop.getSecretKey();
            MpApp mpApp = mpShop.getMpApp();
            if (mpApp == null) return "routerUnconfig";
            APP_ID = mpApp.getAppId();
            AUTH_URL = mpApp.getAuthUrl();

            logger.info("router update");
        }

        System.out.println("LOGIN: "+request.getQueryString());

        //查询数据库是否有该客户端
        Client client = clientRepository.findByMac(mac);

        //生成token
        String token = CodecUtil.MD5(mac+System.currentTimeMillis());
        //查询session
        Session session = null;
        if (client != null) { //设备之前连接过

            logger.warn("设备之前连接过");

            session = sessionRepository.findByClientId(client.getId());

            if (session != null){ //若存在该设备的会话

                logger.warn("存在该设备的会话");

                boolean isOline = clientService.isOnline(client);
                //登录请求下,匿名设备,流量设置为不超额
                int isOutOfUsage = 1;
                if (client.getUser()==null){
                    isOutOfUsage = 1;
                }else {
                    isOutOfUsage = dataUsageService.isOutOfUsage(client,true);
                    logger.error("有名设备,client:" + client.getId()+" user:"+client.getUser().getId());
                }

                //int isOutOfUsage = client.getUser()!=null ? dataQuotaService.isOutOfUsage(client) : 1;
                logger.warn("isOline:"+isOline+" isOutOfUsage:"+isOutOfUsage);

                //处理流量超额的情况
                if (isOutOfUsage < 1){
                    long clientIncoming[] = {0,0,0,0};
                    long clientOutgoing[] = {0,0,0,0};
                    long userIncoming[] = {0,0,0};
                    long userOutgoing[] = {0,0,0};

                    dataUsageService.getClientUsage(client,clientIncoming,clientOutgoing);
                    dataUsageService.getUserUsage(client.getUser(),userIncoming,userOutgoing);
                    DataQuota quota = dataUsageService.getQuota(client);

                    model.addAttribute("clientIncoming",clientIncoming);
                    model.addAttribute("clientOutgoing",clientOutgoing);
                    model.addAttribute("userIncoming",userIncoming);
                    model.addAttribute("userOutgoing",userIncoming);
                    model.addAttribute("quota",quota);
                    logger.warn("once:"+quota.getOnce()+" day:"+quota.getDay()+" month:"+quota.getMonth()+" total:"+quota.getTotal() );
                    return "outOfUsage";
                }

                if(isOline) {  //在线且流量未超额
                    logger.warn("在线返回页面提示");
                    return "youAlreadyOnline";
                }else {  //设备不在线
                    logger.warn("设备不在线");
                    client.setFirstUrl(url);

                    long expire = session.getBeginTime() + 1*60*1000;
                    //token超期才重设token
                    if (System.currentTimeMillis() > expire){
                        session.setToken(token);
                    } else {
                        token = session.getToken();
                    }
                    session.setRouter(router)
                            .setIp(ip)
                            .setBeginTime(System.currentTimeMillis());
                }
            }else { //不存在该设备会话则创建新会话
                logger.warn("不存在该设备会话");
                session = new Session(client,router,token);
                session.setRouter(router)
                        .setClient(client)
                        .setToken(token)
                        .setIp(ip)
                        .setBeginTime(System.currentTimeMillis());

            }

        }else { //设备之前没连过
            logger.warn("设备之前没连过");
            client = new Client(mac); //新建匿名设备
            client.setFirstUrl(url);

            session = new Session(client,router,token);
            session.setIp(ip)
                    .setBeginTime(System.currentTimeMillis());
        }
        //保存session到数据库
        sessionRepository.save(session);
        clientRepository.save(client);




        //Session session = sessionRepository.findByClientId(client.getId());
        String extend = token;
        String ssid = router.getSsid();
        String bssid = router.getBssid();

        long timestamp = System.currentTimeMillis();
        WeixinTicket weixinTicket = new WeixinTicket(APP_ID , extend  ,timestamp, SHOP_ID , AUTH_URL , mac , ssid , bssid , SECRET_KEY);

        //appId + extend + timestamp + shopId + authUrl + mac + ssid + bssid + secretKey

        model.addAttribute("appId",weixinTicket.getAppId());
        model.addAttribute("extend",weixinTicket.getExtend());
        model.addAttribute("timestamp",weixinTicket.getTimestamp());
        model.addAttribute("sign",weixinTicket.getSign());
        model.addAttribute("shopId",weixinTicket.getShopId());
        model.addAttribute("authUrl",weixinTicket.getAuthUrl());
        model.addAttribute("mac",weixinTicket.getMac());
        model.addAttribute("ssid",weixinTicket.getSsid());
        model.addAttribute("bssid",weixinTicket.getBssid());

        return "weixinPortal2";
    }


}
