package com.fliaping.wifi.auth.domain.repository;

import com.fliaping.wifi.auth.domain.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Payne on 8/5/16.
 */
public interface SessionRepository extends JpaRepository<Session,Long>{

    Session findByToken(String token);


    @Query("select s from Session as s where s.client.id = ?1")
    Session findByClientId(Long id);



}
