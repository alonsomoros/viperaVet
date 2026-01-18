package com.alonso.vipera.training.springboot_apirest.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alonso.vipera.training.springboot_apirest.model.user.Role;
import com.alonso.vipera.training.springboot_apirest.model.user.UserRole;

@Repository
public interface UserRoleJpaRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRole(Role role);
}
