package com.alonso.vipera.training.springboot_apirest.persistence.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alonso.vipera.training.springboot_apirest.model.user.Role;
import com.alonso.vipera.training.springboot_apirest.model.user.User;

/**
 * Repositorio para la gestión de usuarios.
 */
public interface UserRepository {

    /**
     * Busca todos los usuarios con paginación.
     * 
     * @param pageable Información de paginación
     * @return Página de usuarios
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Busca un usuario por su ID.
     * 
     * @param id ID del usuario a buscar
     * @return Optional que contiene el usuario encontrado o vacío si no existe
     */
    Optional<User> findById(Long id);


    /**
     * Busca un usuario por su correo electrónico.
     * 
     * @param email Correo electrónico a buscar
     * @return Optional que contiene el usuario encontrado o vacío si no existe
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca usuarios que coincidan con los filtros proporcionados.
     * 
     * @param id       ID del usuario a buscar (opcional)
     * @param name     Nombre a buscar (opcional)
     * @param surnames Apellidos a buscar (opcional)
     * @param email    Correo electrónico a buscar (opcional)
     * @param role     Rol del usuario a buscar (opcional)
     * @param pageable Información de paginación
     * @return Página de usuarios que coinciden con los filtros
     */
    Page<User> findByFilters(Long id, String name, String surnames, String email, Role role, Pageable pageable);

    /**
     * Verifica si existe un usuario con el ID especificado.
     * 
     * @param id ID del usuario a verificar
     * @return true si existe un usuario con ese ID, false en caso contrario
     */
    boolean existsById(Long id);

    /**
     * Verifica si existe un usuario con el correo electrónico especificado.
     * 
     * @param email Correo electrónico del usuario a verificar
     * @return true si existe un usuario con ese correo electrónico, false en caso contrario
     */
    boolean existsByEmail(String email);


    /**
     * Verifica si existe un usuario con el teléfono especificado.
     * 
     * @param phone Teléfono del usuario a verificar
     * @return true si existe un usuario con ese teléfono, false en caso contrario
     */
    boolean existsByPhone(String phone);

    /**
     * Guarda un usuario en el repositorio.
     * 
     * @param user Usuario a guardar
     * @return Usuario guardado
     */
    User save(User user);

    /**
     * Elimina un usuario por su ID.
     * 
     * @param id ID del usuario a eliminar
     */
    void delete(Long id);

}