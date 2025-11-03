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
}
