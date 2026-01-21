package com.alonso.vipera.training.springboot_apirest.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.alonso.vipera.training.springboot_apirest.exception.EmailNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.IdNotFoundException;
import com.alonso.vipera.training.springboot_apirest.exception.UsernameNotFoundException;
import com.alonso.vipera.training.springboot_apirest.mapper.UserMapper;
import com.alonso.vipera.training.springboot_apirest.model.user.Role;
import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.in.UserUpdateDTO;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;
import com.alonso.vipera.training.springboot_apirest.persistence.adapter.UserRepositoryAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de usuarios.
 * Proporciona métodos para gestionar usuarios, incluyendo operaciones CRUD y
 * consultas con filtros.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepositoryAdapter userRepositoryAdapter;
    private final UserMapper userMapper;

    @Override
    public Page<UserOutDTO> getAll(Pageable pageable) {
        log.debug("Recuperando todos los usuarios de la base de datos...");
        Page<User> users = userRepositoryAdapter.findAll(pageable);
        log.debug("Se han recuperado {} usuarios en total.", users.getSize());
        return users.map(userMapper::toOutDTO);
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
    public Page<UserOutDTO> getUserByFilters(Long id, String username, String email, Role role, Pageable pageable) {
        log.debug("Buscando usuarios con filtros - ID: {}, Username: {}, Email: {}, Role: {}", id, username, email,
                role);
        Page<User> users = userRepositoryAdapter.findByFilters(id, username, email, role, pageable);

        log.debug("Se han encontrado {} usuarios con los filtros proporcionados.", users.getContent().size());
        return users.map(userMapper::toOutDTO);
    }

    @Override
    public UserOutDTO updateUser(Long userId, UserUpdateDTO userUpdateDTO, String username) {
        log.debug("Actualizando usuario con ID: {}", userId);
        User userSaved = userRepositoryAdapter.findById(userId).orElseThrow(() -> new IdNotFoundException());
        log.debug("Usuario con ID: {} encontrado. Verificando permisos del usuario: {}", userId, username);

        if (!username.equals(userSaved.getUsername())) {
            throw new SecurityException("No tienes permiso para actualizar este usuario.");
        }
        log.debug("Permisos verificados. Actualizando información del usuario...");

        if (userUpdateDTO.getPhone() != null) {
            userSaved.setPhone(userUpdateDTO.getPhone());
            log.debug("Actualizado teléfono a: {}", userUpdateDTO.getPhone());
        }

        if (userUpdateDTO.getEmail() != null) {
            userSaved.setEmail(userUpdateDTO.getEmail());
            log.debug("Actualizado email a: {}", userUpdateDTO.getEmail());
        }

        if (userUpdateDTO.getAddress() != null) {
            userSaved.setAddress(userUpdateDTO.getAddress());
            log.debug("Actualizada dirección a: {}", userUpdateDTO.getAddress());
        }

        log.debug("Guardando cambios en la base de datos...");
        User updatedUser = userRepositoryAdapter.save(userSaved);
        log.info("Usuario con ID: {} actualizado con éxito.", userId);
        return userMapper.toOutDTO(updatedUser);
    }

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
