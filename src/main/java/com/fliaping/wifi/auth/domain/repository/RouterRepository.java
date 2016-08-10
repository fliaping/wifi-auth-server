package com.fliaping.wifi.auth.domain.repository;

import com.fliaping.wifi.auth.domain.model.Router;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Payne on 8/5/16.
 */
public interface RouterRepository extends JpaRepository<Router,Long>{

    Router findByGwId(String gwId);
}
