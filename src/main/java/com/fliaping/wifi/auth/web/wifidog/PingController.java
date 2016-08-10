package com.fliaping.wifi.auth.web.wifidog;

import com.fliaping.wifi.auth.domain.model.LogPing;
import com.fliaping.wifi.auth.domain.model.Router;
import com.fliaping.wifi.auth.domain.repository.LogPingRepository;
import com.fliaping.wifi.auth.domain.repository.RouterRepository;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
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
@RequestMapping("/ping")
public class PingController {

    private Logger logger = Logger.getLogger(getClass());


    @Autowired
    private LogPingRepository logPingRepository;

    @Autowired
    private RouterRepository routerRepository;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response,
                      @RequestParam(value = "gw_id",required = true) String gw_id,
                      @RequestParam(value = "sys_uptime",required = true) String sys_uptime,
                      @RequestParam(value = "sys_memfree",required = true) String sys_memfree,
                      @RequestParam(value = "sys_load",required = true) String sys_load,
                      @RequestParam(value = "wifidog_uptime",required = true) String wifidog_uptime){


        System.out.println("PING: "+request.getQueryString());

        LogPing logPing = new LogPing();

        Router router = routerRepository.findByGwId(gw_id);

        if (router != null){
            logPing.setRouter(router)
                    .setSysUptime(Long.parseLong(sys_uptime))
                    .setSysLoad(Float.parseFloat(sys_load))
                    .setWifidogUptime(Long.parseLong(wifidog_uptime))
                    .setUpdateTime(System.currentTimeMillis());

            logPing  = logPingRepository.save(logPing); //取得当前一条logPing,并作为router的lastPing
            router.setLastPing(logPing).setEnable(true); //设置最后一ping,并设置为可用
            routerRepository.save(router);

            logger.info("router saved :gw_id:"+router.getGwId());

            try {
                PrintWriter out = response.getWriter();
                out.print("Pong");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                PrintWriter out = response.getWriter();
                out.print("this router haven't be registered");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }





    }
}
