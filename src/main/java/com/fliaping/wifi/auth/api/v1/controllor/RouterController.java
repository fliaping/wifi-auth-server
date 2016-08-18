package com.fliaping.wifi.auth.api.v1.controllor;

import com.fliaping.wifi.auth.domain.model.Router;
import com.fliaping.wifi.auth.domain.model.User;
import com.fliaping.wifi.auth.domain.repository.RouterRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Payne on 8/16/16.
 */
@RestController
@RequestMapping("/api/v1/router")
public class RouterController {

    @Autowired
    RouterRepository routerRepository;

    // ADD
    @ApiOperation(value = "添加路由器",notes = "路由器的网关id(gwId)、所属微信店铺(mpShop)为必须字段")
    @ApiImplicitParam(name = "router",value = "路由器实体",required = true,dataType = "Router",paramType = "body")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public String addRouter(@RequestBody @Valid Router router){
        routerRepository.save(router);
        return "success";
    }

    //DELETE
    @ApiOperation(value = "通过路由id删除路由",notes = "")
    @ApiImplicitParam(name = "id",value = "路由id",required = true,dataType = "Long",paramType = "path")
    @RequestMapping(value = "/id/{id}",method = RequestMethod.DELETE)
    public String deleteRouterById(@PathVariable Long id){
        routerRepository.delete(id);
        return "success";
    }

    //UPDATE
    @ApiOperation(value = "更新路由信息",notes = "通过id来更新路由信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Long",paramType = "path"),
            @ApiImplicitParam(name = "router",value = "路由实体",required = true,dataType = "Router")
    })
    @RequestMapping(value = "/id/{id}",method = RequestMethod.PUT)
    public Router updateRouter(@PathVariable Long id,@RequestBody @Valid Router router){

        Router before = getRouterById(id);
        Router update = router;
        before.setGwId(update.getGwId())
                .setGwId(update.getGwId())
                .setEnable(update.isEnable())
                .setLastPing(update.getLastPing())
                .setBssid(update.getBssid())
                .setGwAddress(update.getGwAddress())
                .setGwPort(update.getGwPort())
                .setMpShop(update.getMpShop())
                .setName(update.getName())
                .setRemark(update.getRemark())
                .setSsid(update.getSsid());

        return routerRepository.save(before);
    }

    //FIND
    @ApiOperation(value = "通过路由id来查找路由信息",notes = "")
    @ApiImplicitParam(name = "id",value = "路由id",required = true,dataType = "Long",paramType = "path")
    @RequestMapping(value = "/id/{id}",method = RequestMethod.GET)
    public Router getRouterById(@PathVariable Long id){
        return routerRepository.findOne(id);
    }

    @ApiOperation(value = "通过路由gw_id来查找路由信息",notes = "")
    @ApiImplicitParam(name = "gwid",value = "路由gw_id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/gw_id/{gwid}",method = RequestMethod.GET)
    public Router getRouterByGwId(@PathVariable String gwid){
        return routerRepository.findByGwId(gwid);
    }

    @ApiOperation(value = "获取路由列表")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public List<Router> getRouters(Pageable pageable){
        // TODO: 8/18/16 实现分页查询
        return routerRepository.findAll();
    }

    // TODO: 8/18/16 验证用户身份
    @ApiOperation(value = "通过微信店铺id(shopId)来获取路由列表",notes = "")
    @ApiImplicitParam(name = "shop_id",value = "微信店铺id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/shop_id/{shop_id}",method = RequestMethod.GET)
    public List<Router> getRoutersByShopId(@PathVariable String shop_id){
        return routerRepository.findByMpShopId(shop_id);
    }

    @ApiOperation(value = "通过微信app_id来获取路由器列表",notes = "")
    @ApiImplicitParam(name = "app_id",value = "微信app_id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/app_id/{app_id}",method = RequestMethod.GET)
    public List<Router> getRoutersByAppId(@PathVariable String app_id){
        return routerRepository.findByMpAppId(app_id);
    }

}
