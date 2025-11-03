package com.alonso.vipera.training.springboot_apirest.persistence.jpa;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.User.Role;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

        Optional<User> findByEmail(String email);

        Optional<User> findByUsername(String username);

        @Query("SELECT u FROM User u WHERE " +
                        "(:id IS NULL OR u.id = :id) AND " +
                        "(:username IS NULL OR u.username LIKE %:username%) AND " +
                        "(:email IS NULL OR u.email LIKE %:email%) AND " +
                        "(:role IS NULL OR u.role = :role)")
        Page<User> findByFilters(
                        @Param("id") Long id,
                        @Param("username") String username,
                        @Param("email") String email,
                        @Param("role") Role role,
                        Pageable pageable);

        boolean existsByEmail(String email);

        boolean existsByUsername(String username);
}
