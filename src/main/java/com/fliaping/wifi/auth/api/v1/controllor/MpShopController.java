package com.fliaping.wifi.auth.api.v1.controllor;

import com.fliaping.wifi.auth.domain.model.MpShop;
import com.fliaping.wifi.auth.domain.repository.MpShopRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Payne on 8/16/16.
 */
@RestController
@RequestMapping("/api/v1/mp_shop")
public class MpShopController {

    @Autowired
    private MpShopRepository mpShopRepository;

    //ADD
    @ApiOperation(value = "添加微信店铺",notes = "")
    @ApiImplicitParam(name = "mpShop",value = "店铺实体",required = true,dataType = "MpShop")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public String addMpShop(@RequestBody @Valid final MpShop mpShop){
        mpShopRepository.save(mpShop);
        return "success";
    }

    //DELETE
    @ApiOperation(value = "通过id来删除店铺",notes = "")
    @ApiImplicitParam(name = "shop_id",value = "店铺id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/{shop_id}",method = RequestMethod.DELETE)
    public String deleteMpShop(@PathVariable String shop_id){
        mpShopRepository.delete(shop_id);
        return "success";
    }


    // TODO: 8/18/16 异常情况处理
    //UPDATE
    @ApiOperation(value = "通过id更新店铺信息",notes = "")
    @ApiImplicitParam(name = "id",value = "路由id",required = true,dataType = "Long",paramType = "path")
    @RequestMapping(value = "/{shopId}",method = RequestMethod.PUT)
    public String updateMpShop(@PathVariable String shopId,@RequestBody @Valid final MpShop mpShop){
        MpShop before = mpShopRepository.findOne(shopId);
        before.setMpApp(mpShop.getMpApp())
                .setName(mpShop.getName())
                .setSecretKey(mpShop.getSecretKey());
        if (null == mpShopRepository.save(before))
            throw new UpdateShopFailedException(shopId);
        return "success";
    }

    // FIND
    @ApiOperation(value = "通过店铺id获得店铺信息",notes = "")
    @ApiImplicitParam(name = "shopId",value = "店铺id",required = true,dataType = "String",paramType = "path")
    @RequestMapping(value = "/{shopId}",method = RequestMethod.GET)
    public MpShop findMpShop(@PathVariable String shopId){
        return mpShopRepository.findOne(shopId);
    }

    @ApiOperation(value = "获取店铺列表",notes = "")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public List<MpShop> findMpShop(){
        // TODO: 8/18/16 分页查询
        return mpShopRepository.findAll();
    }


    class CanNotFindShopException extends RuntimeException{

    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    class UpdateShopFailedException extends RuntimeException{
        UpdateShopFailedException(String shopId){
            super("update shop failed, shopId:"+shopId);
        }
    }


}
