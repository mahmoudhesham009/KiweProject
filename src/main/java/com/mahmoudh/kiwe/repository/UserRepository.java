package com.mahmoudh.kiwe.repository;

import com.mahmoudh.kiwe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM USERS WHERE EMAIL=:email",nativeQuery = true)
    User findUserByEmail(String email);

    @Query(value = "SELECT * FROM USERS WHERE USERNAME=:username",nativeQuery = true)
    User findUserByUsername(String username);
}
