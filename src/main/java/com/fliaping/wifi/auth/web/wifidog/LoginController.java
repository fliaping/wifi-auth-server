package com.fliaping.wifi.auth.web.wifidog;

import com.fliaping.wifi.auth.domain.model.*;
import com.fliaping.wifi.auth.domain.repository.*;
import com.fliaping.wifi.auth.service.ClientService;
import com.fliaping.wifi.auth.service.ClientServiceImpl;
import com.fliaping.wifi.auth.service.DataQuotaService;
import com.fliaping.wifi.auth.service.DataQuotaServiceImpl;
import com.fliaping.wifi.auth.utils.CodecUtil;
import com.fliaping.wifi.auth.domain.WeixinTicket;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    /* 门店名称 : 优拼城际
          ssid : wifitest
          shopId : 5761311
          appId : wxd0380c266c06b53f
          secretKey : 23a88d8ef2099d059b9f533df2b8876b
          ----复用demo代码说明----
          若认证Portal页直接使用此Demo源代码，请注意填写代码中的以下参数（由您的网络环境动态获取）：
          extend
          timestamp
          authUrl
          mac
          bssid
          sign
          其中sign签名请在后台完成，例如：
          var toSign = appId + extend + timestamp + shopId + authUrl + mac + ssid + bssid + secretKey;
          var sign= md5(toSign);*/

    private final long timeout = 3 * 60L;

    WeixinTicket  weixinTicket = new WeixinTicket();

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private RouterRepository routerRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LogOnlineRepository logOnlineRepository;

    @Autowired
    private DataQuotaRepository dataQuotaRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private DataQuotaService dataQuotaService;

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

        // 验证gw_id 在路由器列表中,不在则是未配置
        Router router = routerRepository.findByGwId(gw_id);
        if(router == null) return "routerUnconfig"; //路由器未配置,返回配置介绍页
        else { //更新网关wifidog地址端口
            router.setGwAddress(gw_address)
                    .setGwPort(Integer.parseInt(gw_port));
            routerRepository.save(router);

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

                LogOnline logOnline = null;
                long fromLastDuration =   (logOnline = session.getLogOnline()) != null ?
                        System.currentTimeMillis() - logOnline.getUpdateTime() :
                        Long.MAX_VALUE;


                boolean isOline = clientService.isOnline(client);
                //登录请求下,匿名设备,流量不会超额
                boolean isOutOfUsage = client.getUser()==null ? false : dataQuotaService.isOutOfUsage(client.getUser());

                logger.warn("isOline:"+isOline+" isOutOfUsage:"+isOutOfUsage);
                if( isOline && !isOutOfUsage) {  //若在线返回页面提示
                    logger.warn("在线返回页面提示");
                    return "youAlreadyOnline";
                }else {  //不在线取得之前登陆信息
                    // TODO: 8/8/16 取得之前的流量使用信息。 可以通过rsetful接口获取
                    logger.warn("设备不在线或流量用光");
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
            }else { //不存在该设备会话
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
            client.setSession(session);
            session.setIp(ip)
                    .setBeginTime(System.currentTimeMillis());
        }
        //保存session到数据库
        sessionRepository.save(session);
        clientRepository.save(client);




        //Session session = sessionRepository.findByClientId(client.getId());


        weixinTicket.setTimestamp(System.currentTimeMillis()+"")
                .setMac(mac)
                .setAuthUrl("http://192.168.1.186:9408/weixinauth/")
                .setExtend(token)
                .setSign(toSign(weixinTicket)); //必须在最后,要对前面的属性签名


        model.addAttribute("appId",weixinTicket.getAppId());
        model.addAttribute("extend",weixinTicket.getExtend());
        model.addAttribute("timestamp",weixinTicket.getTimestamp());
        model.addAttribute("sign",weixinTicket.getSign());
        model.addAttribute("shopId",weixinTicket.getShopId());
        model.addAttribute("authUrl",weixinTicket.getAuthUrl());
        model.addAttribute("mac",weixinTicket.getMac());
        model.addAttribute("ssid",weixinTicket.getSsid());
        model.addAttribute("bssid",weixinTicket.getBssid());


        //appId, extend, timestamp, sign, shopId, authUrl, mac, ssid, bssid

        return "weixinPortal";
    }

    /**
     * 得到传给微信的签名值
     * @param weixinTicket 初次传给微信为了得到ticket的参数对象
     * @return MD5签名值
     */
   private String toSign(WeixinTicket weixinTicket){
        //sign = MD5(appId + extend + timestamp + shop_id + authUrl + mac + ssid + bssid + secretkey);
        String originString = weixinTicket.getAppId() +
                weixinTicket.getExtend() +
                weixinTicket.getTimestamp() +
                weixinTicket.getShopId() +
                weixinTicket.getAuthUrl() +
                weixinTicket.getMac() +
                weixinTicket.getSsid() +
                weixinTicket.getBssid() +
                weixinTicket.getSecretKey();

        //System.out.println("originString: "+ originString);

        String md5 = CodecUtil.MD5(originString);
        //System.out.println("md5: "+md5);

        return md5;
    }
}
