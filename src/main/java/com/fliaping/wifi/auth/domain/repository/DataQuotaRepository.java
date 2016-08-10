package com.fliaping.wifi.auth.domain.repository;

import com.fliaping.wifi.auth.domain.model.DataQuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Payne on 8/10/16.
 */
public interface DataQuotaRepository extends JpaRepository<DataQuota,Long> {

    @Query("select d from DataQuota as d where d.user.id = ?1")
    DataQuota findByUserId(Long user_id);
}
