package com.fliaping.wifi.auth.api.v1.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fliaping.wifi.auth.domain.model.LogOnline;

import java.util.Date;
import static com.fliaping.wifi.auth.utils.MyDateUtil.datePattern;

/**
 * Created by Payne on 8/11/16.
 */
public class DataUsage {

    private long incoming;
    private long outgoing;


    @JsonFormat(pattern = datePattern)
    private Date startdate;

    @JsonFormat(pattern = datePattern)
    private Date enddate;

    public long getIncoming() {
        return incoming;
    }

    public DataUsage setIncoming(long incoming) {
        this.incoming = incoming;
        return this;
    }

    public long getOutgoing() {
        return outgoing;
    }

    public DataUsage setOutgoing(long outgoing) {
        this.outgoing = outgoing;
        return this;
    }

    public Date getStartdate() {
        return startdate;
    }

    public DataUsage setStartdate(Date startdate) {
        this.startdate = startdate;
        return this;
    }
    public Date getEnddate() {
        return enddate;
    }

    public DataUsage setEnddate(Date enddate) {
        this.enddate = enddate;
        return this;
    }
}
