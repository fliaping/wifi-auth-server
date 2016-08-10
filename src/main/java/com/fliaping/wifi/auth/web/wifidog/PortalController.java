package com.fliaping.wifi.auth.web.wifidog;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
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
@RequestMapping("/portal")
public class PortalController {

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response,
                      ModelMap map,
                      @RequestParam(value = "gw_id",required = true) String gw_id){

        System.out.println("PORTAL: "+request.getQueryString());
        map.addAttribute("host","hello");
        return ;
        /*try {
            PrintWriter out = response.getWriter();
            out.println(request.getQueryString());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
