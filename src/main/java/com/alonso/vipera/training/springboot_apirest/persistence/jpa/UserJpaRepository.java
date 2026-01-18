package com.alonso.vipera.training.springboot_apirest.persistence.jpa;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alonso.vipera.training.springboot_apirest.model.user.User;
import com.alonso.vipera.training.springboot_apirest.model.user.Role;

/**
 * Repositorio JPA para la entidad User.
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas
 * relacionadas con los usuarios en la base de datos.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

        /**
         * Busca un usuario por su correo electrónico.
         * 
         * @param email Correo electrónico a buscar
         * @return Optional que contiene el usuario encontrado o vacío si no existe
         */
        Optional<User> findByEmail(String email);

        /**
         * Busca un usuario por su nombre de usuario.
         * 
         * @param username Nombre de usuario a buscar
         * @return Optional que contiene el usuario encontrado o vacío si no existe
         */
        Optional<User> findByUsername(String username);

        /**
         * Busca usuarios aplicando múltiples filtros opcionales.
         * Permite filtrar por ID, nombre de usuario, correo electrónico y rol.
         * Si un filtro es nulo, no se aplica en la consulta.
         *
         * @param id       ID del usuario (opcional)
         * @param username Nombre de usuario (opcional)
         * @param email    Correo electrónico (opcional)
         * @param role     Rol del usuario (opcional)
         * @param pageable Información de paginación
         * @return Página de usuarios que cumplen con los filtros especificados
         */
        @Query("SELECT u FROM User u WHERE " +
                        "(:id IS NULL OR u.id = :id) AND " +
                        "(:username IS NULL OR u.username LIKE %:username%) AND " +
                        "(:email IS NULL OR u.email LIKE %:email%) AND " +
                        "(:role IS NULL OR u.userRole.role = :role)")
        Page<User> findByFilters(
                        @Param("id") Long id,
                        @Param("username") String username,
                        @Param("email") String email,
                        @Param("role") Role role,
                        Pageable pageable);

        /**
         * Comprueba si un email existe físicamente en la BBDD,
         * ignorando el filtro de soft-delete.
         * Se usa para validar el registro y evitar violaciones de UNIQUE constraint.
         *
         * @param email El email a comprobar.
         * @return true si el email existe (borrado o no), false en caso contrario.
         */
        boolean existsByEmail(String email);

        /**
         * Comprueba si un nombre de usuario existe físicamente en la BBDD,
         * ignorando el filtro de soft-delete.
         * Se usa para validar el registro y evitar violaciones de UNIQUE constraint.
         *
         * @param username El nombre de usuario a comprobar.
         * @return true si el usuario existe (borrado o no), false en caso contrario.
         */
        boolean existsByUsername(String username);

        /**
         * Comprueba si un nombre de usuario existe FÍSICAMENTE en la BBDD,
         * ignorando el filtro de soft-delete.
         * Se usa para validar el registro y evitar violaciones de UNIQUE constraint.
         *
         * @param username El nombre de usuario a comprobar.
         * @return Un Optional no vacío si el usuario existe (borrado o no).
         */
        @Query(value = "SELECT 1 FROM users WHERE username = :username LIMIT 1", nativeQuery = true)
        Optional<Object> checkIfUsernameExistsNative(@Param("username") String username);

        /**
         * Comprueba si un email existe FÍSICAMENTE en la BBDD,
         * ignorando el filtro de soft-delete.
         *
         * @param email El email a comprobar.
         * @return Un Optional no vacío si el email existe (borrado o no).
         */
        @Query(value = "SELECT 1 FROM users WHERE email = :email LIMIT 1", nativeQuery = true)
        Optional<Object> checkIfEmailExistsNative(@Param("email") String email);

        /**
         * Comprueba si un teléfono existe FÍSICAMENTE en la BBDD,
         * ignorando el filtro de soft-delete.
         *
         * @param phone El teléfono a comprobar.
         * @return Un Optional no vacío si el teléfono existe (borrado o no).
         */
        @Query(value = "SELECT 1 FROM users WHERE phone = :phone LIMIT 1", nativeQuery = true)
        Optional<Object> checkIfPhoneExistsNative(@Param("phone") String phone);
}
