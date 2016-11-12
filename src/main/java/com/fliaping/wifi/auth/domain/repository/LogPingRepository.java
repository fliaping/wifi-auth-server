package com.fliaping.wifi.auth.domain.repository;

import com.fliaping.wifi.auth.domain.model.LogPing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Payne on 8/5/16.
 */
@Repository
public interface LogPingRepository extends JpaRepository<LogPing,Long>{

    List<LogPing> findByUpdateTimeBefore(long before);

    @Transactional
    @Modifying
    @Query("delete from LogPing where updateTime < ?1")
    void deleteBeforeUpdateTime(long before);
}
