package com.alonso.vipera.training.springboot_apirest.persistence;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.user.User;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryAdapter {

    private UserRepository userRepository;

    public UserRepositoryAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        User fetched = userRepository.findById(id).orElse(null);
        userRepository.delete(fetched);
    }
}
