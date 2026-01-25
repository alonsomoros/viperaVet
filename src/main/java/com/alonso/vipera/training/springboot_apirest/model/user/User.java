package com.alonso.vipera.training.springboot_apirest.model.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.alonso.vipera.training.springboot_apirest.model.BaseEntity;
import com.alonso.vipera.training.springboot_apirest.model.pet.Pet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
     * Nombre del usuario.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Apellidos del usuario.
     */
    @Column(nullable = false)
    private String surnames;

    /**
     * Contraseña del usuario en el sistema.
     */
    @Column(nullable = true)
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
     * DNI del usuario en el sistema.
     */
    @Column(nullable = true, unique = true)
    private String dni;

    /**
     * Rol del usuario en el sistema (USER o VET).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private UserRole userRole;

    /**
     * Indica si el usuario está habilitado (activado por email).
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = false;

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
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.userRole.getRole()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
