package com.fliaping.wifi.auth.domain.repository;

import com.fliaping.wifi.auth.domain.model.LogOnline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.List;

/**
 * Created by Payne on 8/5/16.
 */
@Repository
public interface LogOnlineRepository extends JpaRepository<LogOnline,Long> {

    LogOnline findByToken(String token);

    @Query("select c from LogOnline as c where c.client.id = ?1 and c.updateTime = (select max(l.updateTime) from LogOnline as l where l.client.id=?1) ")
    LogOnline getLastByClient(Long client_id);

    @Query("select c from LogOnline as c where c.client.id = ?1")
    List<LogOnline> findByClientId(Long client_id);

    @Query("select c from LogOnline as c where c.client.id = ?1 and c.updateTime >= ?2 and c.updateTime <= ?3")
    List<LogOnline> findByClientId(Long client_id,Long start,Long end);

    //根据客户端查询
    @Query("select sum(c.incoming) from LogOnline as c where c.client.id = ?1")
    Long getIncomingByClientId(Long client_id);

    @Query("select sum(c.incoming) from LogOnline as c where c.client.id = ?1 and c.updateTime >= ?2 and c.updateTime <= ?3")
    Long getIncomingByClientId(Long client_id,Long start,Long end);

    @Query("select sum(c.outgoing) from LogOnline as c where c.client.id = ?1")
    Long getOutgoingByClientId(Long client_id);

    @Query("select sum(c.outgoing) from LogOnline as c where c.client.id = ?1 and c.updateTime >= ?2 and c.updateTime <= ?3")
    Long getOutgoingByClientId(Long client_id,Long start,Long end);

    //根据路由查询
    @Query("select sum(c.incoming) from LogOnline as c where c.router.id = ?1")
    Long getIncomingByRouterId(Long router_id);

    @Query("select sum(c.incoming) from LogOnline as c where c.router.id = ?1 and c.updateTime >= ?2 and c.updateTime <= ?3")
    Long getIncomingByRouterId(Long router_id,Long start,Long end);

    @Query("select sum(c.outgoing) from LogOnline as c where c.router.id = ?1")
    Long getOutgoingByRouterId(Long client_id);

    @Query("select sum(c.outgoing) from LogOnline as c where c.router.id = ?1 and c.updateTime >= ?2 and c.updateTime <= ?3")
    Long getOutgoingByRouterId(Long client_id,Long start,Long end);

}
