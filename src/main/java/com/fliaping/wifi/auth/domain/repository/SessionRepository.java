package com.fliaping.wifi.auth.domain.repository;

import com.fliaping.wifi.auth.domain.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Payne on 8/5/16.
 */
@Repository
public interface SessionRepository extends JpaRepository<Session,Long>{

    Session findByToken(String token);


    @Query("select s from Session as s where s.client.id = ?1")
    Session findByClientId(Long id);



}
