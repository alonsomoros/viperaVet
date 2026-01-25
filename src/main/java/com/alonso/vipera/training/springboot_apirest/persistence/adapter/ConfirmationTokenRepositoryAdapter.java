package com.alonso.vipera.training.springboot_apirest.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.alonso.vipera.training.springboot_apirest.model.user.ConfirmationToken;
import com.alonso.vipera.training.springboot_apirest.persistence.jpa.ConfirmationTokenJpaRepository;
import com.alonso.vipera.training.springboot_apirest.persistence.repository.ConfirmationTokenRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ConfirmationTokenRepositoryAdapter implements ConfirmationTokenRepository {

    private final ConfirmationTokenJpaRepository jpaRepository;

    @Override
    public ConfirmationToken save(ConfirmationToken confirmationToken) {
        return jpaRepository.save(confirmationToken);
    }

    @Override
    public Optional<ConfirmationToken> findByToken(String token) {
        return jpaRepository.findByToken(token);
    }

    @Override
    public void delete(ConfirmationToken confirmationToken) {
        jpaRepository.delete(confirmationToken);
    }
}
