package com.fliaping.wifi.auth.api.v1.controllor;

import com.fliaping.wifi.auth.domain.model.User;
import com.fliaping.wifi.auth.domain.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Payne on 8/17/16.
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserControllor {

    @Autowired
    private UserRepository userRepository;

    /*//UPDATE
    @ApiOperation(value = "更新用户信息",notes = "只能更新username和passwd,其它字段由认证程序来完成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "path"),
            @ApiImplicitParam(name = "user",value = "用户实体",required = true,dataType = "User")
    })
    @RequestMapping(value = "/id/{id}",method = RequestMethod.PUT)
    public User updateUser(@PathVariable Long id,@RequestBody User update){
        User user = findUserById(id);
        //user.setUsername(update.getUsername())
         //       .setPassword(update.getPassword());
        return userRepository.save(user);
    }*/

    //FIND
    @ApiOperation(value = "通过用户id来查找用户信息",notes = "",response = User.class)
    @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "path")
    @RequestMapping(value = "/id/{id}",method = RequestMethod.GET)
    public User findUserById(@PathVariable Long id){
        return userRepository.findById(id);
    }

    @ApiOperation(value = "通过用户微信OpenId来查找用户信息",notes = "",response = User.class)
    @ApiImplicitParam(name = "openId",value = "用户微信OpenId",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/wx_open_id/{openId}",method = RequestMethod.GET)
    public User findUserByWxOpenId(@PathVariable String openId){
        return userRepository.findByWxOpenId(openId);
    }


}
