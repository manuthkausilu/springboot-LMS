package com.example.Sekai_Academy.repo;

import com.example.Sekai_Academy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u")
    long countAllUsers();

}
