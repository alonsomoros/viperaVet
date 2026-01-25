package com.alonso.vipera.training.springboot_apirest.persistence.repository;

import java.util.Optional;

import com.alonso.vipera.training.springboot_apirest.model.user.ConfirmationToken;

public interface ConfirmationTokenRepository {
    ConfirmationToken save(ConfirmationToken confirmationToken);

    Optional<ConfirmationToken> findByToken(String token);

    void delete(ConfirmationToken confirmationToken);
}
