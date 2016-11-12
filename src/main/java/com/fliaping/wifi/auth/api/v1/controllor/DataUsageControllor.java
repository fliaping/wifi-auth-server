package com.fliaping.wifi.auth.api.v1.controllor;

import com.fliaping.wifi.auth.api.v1.response.DataUsage;
import com.fliaping.wifi.auth.api.v1.service.DataUsageService;
import com.fliaping.wifi.auth.domain.model.Client;
import com.fliaping.wifi.auth.domain.model.User;
import com.fliaping.wifi.auth.domain.repository.ClientRepository;
import com.fliaping.wifi.auth.domain.repository.UserRepository;
import com.fliaping.wifi.auth.utils.MyDateUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import static com.fliaping.wifi.auth.utils.MyDateUtil.datePattern;

/**
 * Created by Payne on 8/11/16.
 */
@RestController
@RequestMapping("/api/v1/datausage")
public class DataUsageControllor {

    @Autowired
    private DataUsageService dataUsageService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    //FIND
    @ApiOperation(value = "按照用户id查询流量",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid",value = "用户id",required = true,dataType = "Long",paramType = "path"),
            @ApiImplicitParam(name = "startdate",value = "查询开始时间",defaultValue = "yyyy-MM-dd HH:mm:ss(今天0点)",required = true,dataType = "Date"),
            @ApiImplicitParam(name = "enddate",value = "查询结束时间",defaultValue = "yyyy-MM-dd HH:mm:ss(当前时间)",required = true,dataType = "Date")
    })
    @RequestMapping(value = "/user/id/{uid}",method = RequestMethod.GET)
    public DataUsage getByUserId(@PathVariable Long uid,
                            @RequestParam("startdate") @DateTimeFormat(pattern = datePattern) Date startdate,
                            @RequestParam("enddate") @DateTimeFormat(pattern = datePattern) Date enddate){
        DateRange dateRange = new DateRange(startdate,enddate);
        return dataUsageService.getUsageByUid(uid,dateRange.startdate,dateRange.enddate);
    }

    @ApiOperation(value = "按照用户openid查询流量",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId",value = "公众号id",required = true,dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "startdate",value = "查询开始时间",defaultValue = "yyyy-MM-dd HH:mm:ss(今天0点)",required = true,dataType = "Date"),
            @ApiImplicitParam(name = "enddate",value = "查询结束时间",defaultValue = "yyyy-MM-dd HH:mm:ss(当前时间)",required = true,dataType = "Date")
    })

    @RequestMapping(value = "/user/openid/{openid}",method = RequestMethod.GET)
    public DataUsage getByUserOpenId(@PathVariable String openid,
                                @RequestParam("startdate") @DateTimeFormat(pattern = datePattern) Date startdate,
                                @RequestParam("enddate") @DateTimeFormat(pattern = datePattern) Date enddate){
        DateRange dateRange = new DateRange(startdate,enddate);
        return dataUsageService.getUsageByOpenId(openid,dateRange.startdate,dateRange.enddate);
    }

    @ApiOperation(value = "按照终端id查询流量",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId",value = "终端id",required = true,dataType = "Long",paramType = "path"),
            @ApiImplicitParam(name = "justlast",value = "是否只请求最后一次",required = true,dataType = "Boolean"),
            @ApiImplicitParam(name = "startdate",value = "查询开始时间",defaultValue = "yyyy-MM-dd HH:mm:ss(今天0点)",required = true,dataType = "Date"),
            @ApiImplicitParam(name = "enddate",value = "查询结束时间",defaultValue = "yyyy-MM-dd HH:mm:ss(当前时间)",required = true,dataType = "Date")
    })
    @RequestMapping(value = "/client/id/{id}",method = RequestMethod.GET)
    public DataUsage getByClientId(@PathVariable Long id,
                              @RequestParam(value = "justlast",required = false) boolean justlast,
                              @RequestParam(value = "startdate",required = false) @DateTimeFormat(pattern = datePattern) Date startdate,
                              @RequestParam(value = "enddate",required = false) @DateTimeFormat(pattern = datePattern) Date enddate){
        if (justlast){
            return dataUsageService.getLastUsageByClientId(id);
        }else if (startdate!=null && enddate!=null){
            DateRange dateRange = new DateRange(startdate,enddate);
            return dataUsageService.getUsageByClientId(id,dateRange.startdate,dateRange.enddate);
        }else {
            return null;
        }

    }

    @ApiOperation(value = "按照终端mac查询流量",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientMac",value = "终端Mac",required = true,dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "justlast",value = "是否只请求最后一次",required = true,dataType = "Boolean"),
            @ApiImplicitParam(name = "startdate",value = "查询开始时间",defaultValue = "yyyy-MM-dd HH:mm:ss(今天0点)",required = true,dataType = "Date"),
            @ApiImplicitParam(name = "enddate",value = "查询结束时间",defaultValue = "yyyy-MM-dd HH:mm:ss(当前时间)",required = true,dataType = "Date")
    })
    @RequestMapping(value = "/client/mac/{mac}",method = RequestMethod.GET)
    public DataUsage getByClientMac(@PathVariable String mac,
                               @RequestParam(value = "justlast",required = false) boolean justlast,
                               @RequestParam(value = "startdate",required = false) @DateTimeFormat(pattern = datePattern) Date startdate,
                               @RequestParam(value = "enddate",required = false) @DateTimeFormat(pattern = datePattern) Date enddate){
        Client client = clientRepository.findByMac(mac);
        return getByClientId(client.getId(),justlast,startdate,enddate);
    }

    @ApiOperation(value = "查询时间段内客户端的流量用量",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "终端id",required = true,dataType = "long",paramType = "path"),
            @ApiImplicitParam(name = "span",value = "时间段类型(可选值:once,day,month,total)",required = false,dataType = "String"),
    })
    @RequestMapping(value = "/span/client_id/{id}",method = RequestMethod.GET)
    public DataUsage getClientUsage(@PathVariable long id,
                                   @RequestParam(value = "span",required = false) String span){
        Client client = clientRepository.findOne(id);
        long[] incoming = {0,0,0,0};
        long[] outgoing = {0,0,0,0};
        dataUsageService.getClientUsage(client,incoming,outgoing);

        DataUsage dataUsage = new DataUsage();
        Date[] spanTime = null;
        if (span == null) span = "once";
        switch (span){
            case "once" :
                dataUsage.setIncoming(incoming[0])
                        .setOutgoing(outgoing[0])
                        .setStartdate(null)
                        .setEnddate(null);
                break;
            case "day" :
                spanTime = MyDateUtil.getSpanTime("day");
                dataUsage.setIncoming(incoming[1])
                        .setOutgoing(outgoing[1])
                        .setStartdate(spanTime[0])
                        .setEnddate(spanTime[1]);
                break;
            case "month" :
                spanTime = MyDateUtil.getSpanTime("month");
                dataUsage.setIncoming(incoming[2])
                        .setOutgoing(outgoing[2])
                        .setStartdate(spanTime[0])
                        .setEnddate(spanTime[1]);
                break;
            case "total" :
                dataUsage.setIncoming(incoming[3])
                        .setOutgoing(outgoing[3])
                        .setStartdate(null)
                        .setEnddate(null);
                break;
        }
        return dataUsage;
    }

    @ApiOperation(value = "查询时间段内用户的流量用量",notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "long",paramType = "path"),
            @ApiImplicitParam(name = "span",value = "时间段类型(可选值:day,month,total)",required = false,dataType = "String"),
    })
    @RequestMapping(value = "/span/user_id/{id}",method = RequestMethod.GET)
    public DataUsage getUserUsage(@PathVariable long id,
                                  @RequestParam(value = "span",required = false) String span){
        User user = userRepository.findById(id);
        long[] incoming = {0,0,0};
        long[] outgoing = {0,0,0};
        dataUsageService.getUserUsage(user,incoming,outgoing);

        DataUsage dataUsage = new DataUsage();
        Date[] spanTime = null;

        if (span == null) span = "day";
        switch (span){
            case "day" :
                spanTime = MyDateUtil.getSpanTime("day");
                dataUsage.setIncoming(incoming[0])
                        .setOutgoing(outgoing[0])
                        .setStartdate(spanTime[0])
                        .setEnddate(spanTime[1]);
                break;
            case "month" :
                spanTime = MyDateUtil.getSpanTime("month");
                dataUsage.setIncoming(incoming[1])
                        .setOutgoing(outgoing[1])
                        .setStartdate(spanTime[0])
                        .setEnddate(spanTime[1]);
                break;
            case "total" :
                dataUsage.setIncoming(incoming[2])
                        .setOutgoing(outgoing[2])
                        .setStartdate(spanTime[0])
                        .setEnddate(spanTime[1]);
                break;

        }
        return dataUsage;
    }


    private class DateRange{
        private Date startdate;
        private Date enddate;

        public DateRange(Date startdate, Date enddate) {
            if (enddate == null) enddate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(enddate);
            calendar.set(Calendar.HOUR,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            if(startdate == null) startdate = calendar.getTime();

            if (enddate.before(startdate)){
                throw new RuntimeException("enddate before startdate,please check your parameter");
            }
            this.startdate = startdate;
            this.enddate = enddate;
        }
    }
}
