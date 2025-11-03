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

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true, unique = true)
    private String phone;

    @Column(nullable = true, unique = false)
    private String address;

    public enum Role {
        OWNER, VET
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // OneToMany (User 1 -> N Pets)

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Pet> pets = new ArrayList<>();

    // UserDetails methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

}
