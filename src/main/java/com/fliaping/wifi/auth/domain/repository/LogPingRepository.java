package com.fliaping.wifi.auth.domain.repository;

import com.fliaping.wifi.auth.domain.model.LogPing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Payne on 8/5/16.
 */
@Repository
public interface LogPingRepository extends JpaRepository<LogPing,Long>{
}
