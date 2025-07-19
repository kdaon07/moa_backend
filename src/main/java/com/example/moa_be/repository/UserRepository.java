package com.example.moa_be.repository;

import com.example.moa_be.db.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserid(String userid);
}
