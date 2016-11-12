package com.fliaping.wifi.auth.api.v1.controllor;

import com.fliaping.wifi.auth.domain.model.MpApp;
import com.fliaping.wifi.auth.domain.repository.MpAppRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Payne on 8/16/16.
 */
@RestController
@RequestMapping("/api/v1/mp_app")
public class MpAppController {

    @Autowired
    private MpAppRepository mpAppRepository;

    //ADD
    @ApiOperation(value = "添加微信公众号(app)",notes = "")
    @ApiImplicitParam(name = "mpApp",value = "微信公众号实体",required = true,dataType = "MpApp")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public String addMpApp(@RequestBody @Valid final MpApp mpApp){
        mpAppRepository.save(mpApp);
        return "success";
    }

    //DELETE
    @ApiOperation(value = "删除一个公众号",notes = "")
    @ApiImplicitParam(name = "appId",value = "公众号id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/{appId}",method = RequestMethod.DELETE)
    public String deleteMpApp(@PathVariable String appId){
        mpAppRepository.delete(appId);
        return "success";
    }

    //UPDATE
    @ApiOperation(value = "通过id更新店铺信息",notes = "")
    @ApiImplicitParam(name = "appid",value = "公众号id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/{appid}",method = RequestMethod.PUT)
    public String updateMpApp(@PathVariable String appid,@RequestBody @Valid final MpApp mpApp){
        MpApp before = mpAppRepository.findOne(appid);
        before.setName(mpApp.getName())
                .setAccessToken(mpApp.getAccessToken())
                .setAppSecret(mpApp.getAppSecret())
                .setAuthUrl(mpApp.getAuthUrl());
        mpAppRepository.save(before);
        return "success";
    }

    //FIND
    @ApiOperation(value = "查找加入系统的微信公众号列表",notes = "")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public List<MpApp> findMpApp(){
        return mpAppRepository.findAll();
    }

    @ApiOperation(value = "通过appId查找微信公众号",notes = "")
    @ApiImplicitParam(name = "appId",value = "公众号id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/{appId}",method = RequestMethod.GET)
    public MpApp findMpAppById(@PathVariable String appId){

        return mpAppRepository.findOne(appId);
    }

}
