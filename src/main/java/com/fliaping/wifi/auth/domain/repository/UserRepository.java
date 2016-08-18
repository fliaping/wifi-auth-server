package com.fliaping.wifi.auth.domain.repository;

import com.fliaping.wifi.auth.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Payne on 8/5/16.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    User findById(long id);


    User findByUsername(String username);

    User findByWxOpenId(String wxOpenId);
}
