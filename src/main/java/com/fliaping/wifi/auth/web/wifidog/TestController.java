package com.fliaping.wifi.auth.web.wifidog;

import com.fliaping.wifi.auth.domain.model.User;
import com.fliaping.wifi.auth.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Payne on 8/5/16.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public void test() throws Exception{

        User user = new User();
        user.setId(1);
        user.setWxOpenId("wx1");
        user.setUsername("hello");
        user.setLastLogin(System.currentTimeMillis());
        userRepository.save(user);

        user.setId(2);
        user.setWxOpenId("wx2");
        user.setUsername("hello2");
        user.setLastLogin(System.currentTimeMillis());

        userRepository.save(user);

        System.out.println(userRepository.findAll().size());
        System.out.println(userRepository.findById(2L).getWxOpenId());

        return;

    }

}
