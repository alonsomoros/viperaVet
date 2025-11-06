package com.alonso.vipera.training.springboot_apirest.model.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.alonso.vipera.training.springboot_apirest.model.BaseEntity;
import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa a un usuario en el sistema.
 */
@Data
@Builder
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW(), modified_at = NOW() WHERE id = ?") // Borrado lógico
@SQLRestriction("deleted_at IS NULL") // Para entidades que no han sido borradas
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {

    /**
     * Nombre de usuario único en el sistema.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Contraseña del usuario en el sistema.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Email del usuario en el sistema.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Número de teléfono del usuario en el sistema.
     */
    @Column(nullable = true, unique = true)
    private String phone;

    /**
     * Dirección del usuario en el sistema.
     */
    @Column(nullable = true, unique = false)
    private String address;

    /**
     * Roles disponibles para un usuario en el sistema.
     */
    public enum Role {
        /**
         * Propietario de mascotas.
         */
        OWNER,
        /**
         * Veterinario.
         */
        VET
    }

    /**
     * Rol del usuario en el sistema (OWNER o VET).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // OneToMany (User 1 -> N Pets)

    /**
     * Lista de mascotas asociadas al usuario.
     */
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Pet> pets = new ArrayList<>();

    // UserDetails methods

    /**
     * Obtiene las autoridades/granted authorities del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

}
