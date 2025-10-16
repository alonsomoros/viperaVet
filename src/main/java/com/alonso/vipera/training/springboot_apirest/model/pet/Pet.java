package com.alonso.vipera.training.springboot_apirest.model.pet;

import java.sql.Date;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.alonso.vipera.training.springboot_apirest.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "pets")
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String name;

    @Column(nullable = false, unique = false)
    private Date birthDate;

    @Column(nullable = false, unique = false)
    private String specie;

    @Column(nullable = false, unique = false)
    private String breed;

    @Column(nullable = false, unique = false)
    private Double weight;

    @Column(nullable = true, unique = false)
    private String diet_info;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = true, unique = false)
    private String photo_url;

    // ManyToOne (N Pets -> 1 User)
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}