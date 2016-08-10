package com.fliaping.wifi.auth.web.wifidog;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Payne on 8/2/16.
 */

@Controller
@RequestMapping("/gw_message")
public class GwMessageController {

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response,
                      @RequestParam(value = "message",required = true) String message){

        System.out.println("gw_message: "+request.getQueryString());


    }
}
