package com.fliaping.wifi.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Payne on 7/28/16.
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class XpApplication {

    public static void main(String[] args){
        SpringApplication.run(XpApplication.class,args);
    }
}
