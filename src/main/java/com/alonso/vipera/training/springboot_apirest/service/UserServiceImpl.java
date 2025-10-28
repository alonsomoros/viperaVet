package com.alonso.vipera.training.springboot_apirest.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.exception.EmailNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameNotFoundException;
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.User.Role;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.UserRepositoryAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepositoryAdapter userRepositoryAdapter;
    private final UserMapper userMapper;

    @Override
    public List<UserOutDTO> getAll() {
        log.debug("Recuperando todos los usuarios de la base de datos...");
        List<UserOutDTO> users = userRepositoryAdapter.findAll()
                .stream()
                .map(userMapper::toOutDTO)
                .toList();
        log.debug("Se han recuperado {} usuarios en total.", users.size());
        return users;
    }

    @Override
    public UserOutDTO getById(Long id) {
        log.debug("Buscando usuario por ID: {}", id);
        User userSaved = userRepositoryAdapter.findById(id).orElseThrow(() -> new IdNotFoundException());
        log.debug("Usuario encontrado: {} (ID: {})", userSaved.getUsername(), userSaved.getId());
        return userMapper.toOutDTO(userSaved);
    }

    @Override
    public UserOutDTO getByEmail(String email) {
        log.debug("Buscando usuario por email: {}", email);
        User userSaved = userRepositoryAdapter.findByEmail(email).orElseThrow(() -> new EmailNotFoundException());
        log.debug("Usuario encontrado: {} (Email: {})", userSaved.getUsername(), userSaved.getEmail());
        return userMapper.toOutDTO(userSaved);
    }

    @Override
    @Cacheable(value = "usersByUsername", key = "#username")
    public UserOutDTO getByUsername(String username) {
        log.debug("Buscando usuario por username: {}", username);
        User userSaved = userRepositoryAdapter.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException());
        log.debug("Usuario encontrado: {} (ID: {})", userSaved.getUsername(), userSaved.getId());
        return userMapper.toOutDTO(userSaved);
    }

    @Override
    public List<UserOutDTO> getUserByFilters(Long id, String username, String email, Role role) {
        log.debug("Buscando usuarios con filtros - ID: {}, Username: {}, Email: {}, Role: {}", id, username, email,
                role);
        List<UserOutDTO> users = userRepositoryAdapter.findByFilters(id, username, email, role)
                .stream()
                .map(userMapper::toOutDTO)
                .toList();
        log.debug("Se han encontrado {} usuarios con los filtros proporcionados.", users.size());
        return users;
    }

    // Hacer un update en el futuro
    // public User update(Long id, UpdateUserDTO updateDTO) {
    // User existingUser = getById(id);
    // // Lógica de actualización sin cambiar password
    // return userRepositoryAdapter.save(updatedUser);
    // }

    @Override
    @CacheEvict(value = { "usersByUsername", "detailsByUsername" }, allEntries = true)
    public void delete(Long id) {
        log.debug("Eliminando usuario con ID: {}", id);
        if (!userRepositoryAdapter.existsById(id)) {
            log.warn("Usuario con ID: {} no encontrado para eliminar.", id);
            throw new IdNotFoundException();
        }
        userRepositoryAdapter.delete(id);
        log.debug("Usuario con ID: {} eliminado con éxito.", id);
    }

    @Override
    @Cacheable(value = "detailsByUsername", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepositoryAdapter.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException());
    }

}
