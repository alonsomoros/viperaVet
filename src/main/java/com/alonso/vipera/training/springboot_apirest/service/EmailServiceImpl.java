package com.alonso.vipera.training.springboot_apirest.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendActivationEmail(String to, String token) {
        // En un entorno real, aquí se configuraría JavaMailSender
        log.info("### MOCK EMAIL SERVICE ###");
        log.info("Enviando correo de activación a: {}", to);
        log.info("Token de activación: {}", token);
        log.info("Enlace sugerido: http://localhost:5173/activate?token={}", token);
        log.info("###########################");
    }
}
