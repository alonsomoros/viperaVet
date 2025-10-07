package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.model.User;
import com.alonso.vipera.training.springboot_apirest.persistence.UserRepositoryAdapter;

@Service
public class UserService {

    private UserRepositoryAdapter userRepositoryAdapter;

    @Autowired
    public UserService(UserRepositoryAdapter userRepositoryAdapter) {
        this.userRepositoryAdapter = userRepositoryAdapter;
    }

    public List<User> getAll(){
        return userRepositoryAdapter.findAll();
    }

    public User getById(Long id){
        return userRepositoryAdapter.findById(id).orElse(null);
    }

    public User getByEmail(String email){
        return userRepositoryAdapter.findByEmail(email).orElse(null);
    }

    public User getByUsername(String username){
        return userRepositoryAdapter.findByUsername(username).orElse(null);
    }

    public User create(User user){
        return userRepositoryAdapter.save(user);
    }

    public void delete(Long id){
        userRepositoryAdapter.delete(id);
    }
}
