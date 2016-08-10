package com.fliaping.wifi.auth.domain.repository;

import com.fliaping.wifi.auth.domain.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Payne on 8/8/16.
 */
public interface ClientRepository extends JpaRepository<Client,Long> {

    public Client findByMac(@Param("mac") String mac);
}
