package com.alonso.vipera.training.springboot_apirest.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendActivationEmail(String to, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // Preparar el contexto de Thymeleaf
            Context context = new Context();
            context.setVariable("token", token);

            // Procesar el template HTML
            // El nombre "email-activation" debe coincidir con el archivo .html en src/main/resources/templates/
            String htmlContent = templateEngine.process("email-activation", context);

            helper.setTo(to);
            helper.setSubject("Activa tu cuenta en ViperaVet");
            helper.setText(htmlContent, true); // true indica que es HTML
            helper.setFrom("viperavet@noreply.com");

            mailSender.send(mimeMessage);
            log.info("Correo de activación enviado exitosamente a: {}", to);

        } catch (MessagingException e) {
            log.error("Error al enviar el correo de activación a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error al enviar email", e);
        }
    }
}