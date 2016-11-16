package com.fliaping.wifi.auth.domain.repository;

import com.fliaping.wifi.auth.domain.model.LogOnline;
import com.fliaping.wifi.auth.domain.model.Router;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Payne on 8/5/16.
 */
@Repository
public interface RouterRepository extends JpaRepository<Router,Long>{

    Router findByGwId(String gwId);

    @Query("select r from Router as r where r.mpShop.shopId = ?1")
    Page<Router> findByMpShopId(String shopid, Pageable pageable);

    //JPQL 的join语句为何要这样,加on产生语法错误with-clause referenced two different from-clause elements
    @Query("select r from Router r where r.mpShop.shopId in (select ms.shopId from MpShop ms left join ms.mpApp ma where ma.appId = ?1)")
    Page<Router> findByMpAppId(String appid, Pageable pageable);

}
