package com.fliaping.wifi.auth.api.v1.controllor;

import com.fliaping.wifi.auth.domain.model.DataQuota;
import com.fliaping.wifi.auth.domain.repository.DataQuotaRepository;
import com.mysql.cj.fabric.xmlrpc.base.Data;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

    //UPDATE
    @ApiOperation(value = "按照用户id查看配额",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid",value = "用户id",required = true,dataType = "Long",paramType = "path"),
            @ApiImplicitParam(name = "dataQuota",value = "DataQuota实体",required = true,dataType = "DataQuota")
    })

    @RequestMapping(value = "/uid/{uid}",method = RequestMethod.PUT)
    public DataQuota updateByUserId(@PathVariable Long uid, @RequestBody DataQuota update){
        DataQuota dataQuota = findByUserId(uid);
        dataQuota.setMonthly(update.getMonthly())
                .setDaily(update.getDaily())
                .setOnce(update.getOnce())
                .setTotal(update.getTotal());
        return dataQuotaRepository.save(dataQuota);
    }


    //FIND
    @ApiOperation(value = "按照用户id查询流量配额",notes = "")
    @ApiImplicitParam(name = "uid",value = "用户id",required = true,dataType = "Long",paramType = "path")
    @RequestMapping(value = "/uid/{uid}",method = RequestMethod.GET)
    public DataQuota findByUserId(@PathVariable Long uid){
        return dataQuotaRepository.findByUserId(uid);
    }
}
