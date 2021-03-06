package com.fliaping.wifi.auth.domain.repository;

import com.fliaping.wifi.auth.domain.model.MpShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Payne on 8/16/16.
 */
@Repository
public interface MpShopRepository extends JpaRepository<MpShop,String> {

    List<MpShop> findByMpApp_AppId(String appId);
}
