package com.alonso.vipera.training.springboot_apirest.persistence.adapter;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.User.Role;
import com.alonso.vipera.training.springboot_apirest.persistence.jpa.UserJpaRepository;
import com.alonso.vipera.training.springboot_apirest.persistence.repository.UserRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private UserJpaRepository userRepository;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Page<User> findByFilters(Long id, String username, String email, Role role, Pageable pageable) {
        return userRepository.findByFilters(id, username, email, role, pageable);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.checkIfEmailExistsNative(email).isPresent();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.checkIfUsernameExistsNative(username).isPresent();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        User fetched = userRepository.findById(id).orElse(null);
        userRepository.delete(fetched);
    }

}
