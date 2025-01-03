package com.aditya.habittracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aditya.habittracker.entity.User;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
