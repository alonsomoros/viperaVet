package com.alonso.vipera.training.springboot_apirest.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alonso.vipera.training.springboot_apirest.model.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByAddressContaining(String address);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
