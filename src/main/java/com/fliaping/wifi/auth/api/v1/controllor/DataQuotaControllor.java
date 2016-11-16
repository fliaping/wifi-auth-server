package com.fliaping.wifi.auth.api.v1.controllor;

import com.fliaping.wifi.auth.domain.model.DataQuota;
import com.fliaping.wifi.auth.domain.repository.DataQuotaRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Payne on 8/17/16.
 */
@RestController
@RequestMapping("/api/v1/dataquota")
public class DataQuotaControllor {

    @Autowired
    private DataQuotaRepository dataQuotaRepository;

    Logger logger = Logger.getLogger(this.getClass());

    //UPDATE
    @ApiOperation(value = "按照用户id更新配额",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid",value = "用户id",required = true,dataType = "Long",paramType = "path"),
            @ApiImplicitParam(name = "update",value = "DataQuota实体",required = true,dataType = "DataQuota")
    })

    @RequestMapping(value = "/uid/{uid}",method = RequestMethod.PUT)
    public DataQuota updateByUserId(@PathVariable Long uid, @RequestBody DataQuota update){
        DataQuota dataQuota = findByUserId(uid);
        dataQuota.setMonth(update.getMonth())
                .setDay(update.getDay())
                .setOnce(update.getOnce())
                .setTotal(update.getTotal());
        return dataQuotaRepository.saveAndFlush(dataQuota);
    }


    //FIND
    @ApiOperation(value = "按照用户id查询流量配额",notes = "")
    @ApiImplicitParam(name = "uid",value = "用户id",required = true,dataType = "Long",paramType = "path")
    @RequestMapping(value = "/uid/{uid}",method = RequestMethod.GET)
    public DataQuota findByUserId(@PathVariable Long uid){
        return dataQuotaRepository.findByUserId(uid);
    }
}
