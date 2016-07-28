package com.fliaping.wifi.wechat.controller;

import com.fliaping.wifi.wechat.util.SignUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Payne on 7/28/16.
 */
@Controller
@RequestMapping("/wechat")
public class WechatSecurity {
    private static Logger logger = Logger.getLogger(WechatSecurity.class);

    @RequestMapping(value = "security",method = RequestMethod.GET)
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response,
                      @RequestParam(value = "signature",required = true) String signature,
                      @RequestParam(value = "timestamp",required = true) String timestamp,
                      @RequestParam(value = "nonce",required = true) String nonce,
                      @RequestParam(value = "echostr",required = true) String echostr){
        try{
            if(SignUtil.checkSignature(signature,timestamp,nonce)){
                PrintWriter out = response.getWriter();
                out.print(echostr);
                out.close();
            } else {
                logger.info("非法请求");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "security", method = RequestMethod.POST)
    // post 方法用于接收微信服务端消息
    public void DoPost() {
        System.out.println("这是 post 方法！");
    }
}
