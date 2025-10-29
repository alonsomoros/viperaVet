package com.alonso.vipera.training.springboot_apirest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alonso.vipera.training.springboot_apirest.model.user.User.Role;
import com.alonso.vipera.training.springboot_apirest.model.user.dto.out.UserOutDTO;

/**
 * Servicio para la gestión de usuarios del sistema.
 * Proporciona operaciones de consulta y eliminación para entidades User.
 */
public interface UserService {

    /**
     * Obtiene todos los usuarios de forma paginada.
     *
     * @param pageable Información de paginación (página, tamaño, ordenamiento)
     * @return Página con los usuarios encontrados
     */
    Page<UserOutDTO> getAll(Pageable pageable);

    /**
     * Busca un usuario por su ID único.
     *
     * @param id ID del usuario a buscar
     * @return DTO del usuario encontrado
     * @throws IdNotFoundException Si no se encuentra un usuario con el ID especificado
     */
    UserOutDTO getById(Long id);

    /**
     * Busca un usuario por su dirección de correo electrónico.
     *
     * @param email Dirección de email del usuario a buscar
     * @return DTO del usuario encontrado
     * @throws IdNotFoundException Si no se encuentra un usuario con el email especificado
     */
    UserOutDTO getByEmail(String email);

    /**
     * Busca un usuario por su nombre de usuario único.
     *
     * @param username Nombre de usuario a buscar
     * @return DTO del usuario encontrado
     * @throws IdNotFoundException Si no se encuentra un usuario con el username especificado
     */
    UserOutDTO getByUsername(String username);

    /**
     * Busca usuarios aplicando múltiples filtros opcionales de forma paginada.
     *
     * @param id       ID del usuario (opcional)
     * @param username Nombre de usuario (opcional)
     * @param email    Dirección de correo electrónico (opcional)
     * @param role     Rol del usuario (opcional)
     * @param pageable Información de paginación (página, tamaño, ordenamiento)
     * @return Página con los usuarios que coinciden con los filtros aplicados
     */
    Page<UserOutDTO> getUserByFilters(Long id, String username, String email, Role role, Pageable pageable);

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar
     * @throws IdNotFoundException Si no se encuentra un usuario con el ID especificado
     */
    void delete(Long id);
    
}