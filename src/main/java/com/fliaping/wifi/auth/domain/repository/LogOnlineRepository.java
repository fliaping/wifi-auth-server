package com.fliaping.wifi.auth.domain.repository;

import com.fliaping.wifi.auth.domain.model.LogOnline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Created by Payne on 8/5/16.
 */
public interface LogOnlineRepository extends JpaRepository<LogOnline,Long> {

    LogOnline findByToken(String token);

    @Query("select c from LogOnline as c where c.client.id = ?1")
    Set<LogOnline> findByClientId(Long client_id);

    @Query("select c from LogOnline as c where c.client.id = ?1 and c.updateTime >= ?2 and c.updateTime <= ?3")
    Set<LogOnline> findByClientId(Long client_id,Long start,Long end);

    @Query("select sum(c.incoming) from LogOnline as c where c.client.id = ?1")
    Long getIncomingByClientId(Long client_id);

    @Query("select sum(c.incoming) from LogOnline as c where c.client.id = ?1 and c.updateTime >= ?2 and c.updateTime <= ?3")
    Long getIncomingByClientId(Long client_id,Long start,Long end);

    @Query("select sum(c.outgoing) from LogOnline as c where c.client.id = ?1")
    Long getOutgoingByClientId(Long client_id);

    @Query("select sum(c.outgoing) from LogOnline as c where c.client.id = ?1 and c.updateTime >= ?2 and c.updateTime <= ?3")
    Long getOutgoingByClientId(Long client_id,Long start,Long end);

}
