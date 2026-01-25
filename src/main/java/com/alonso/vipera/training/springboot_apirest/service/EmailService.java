package com.alonso.vipera.training.springboot_apirest.service;

public interface EmailService {
    void sendActivationEmail(String to, String token);
}
