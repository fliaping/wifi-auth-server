package com.fliaping.wifi.auth.service;

import com.fliaping.wifi.auth.domain.model.*;
import com.fliaping.wifi.auth.domain.repository.DataQuotaRepository;
import com.fliaping.wifi.auth.domain.repository.LogOnlineRepository;
import com.fliaping.wifi.auth.domain.repository.SessionRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Payne on 8/10/16.
 */
@Component("dataQuotaService")
@Transactional
public class DataQuotaServiceImpl implements DataQuotaService {
    
}
