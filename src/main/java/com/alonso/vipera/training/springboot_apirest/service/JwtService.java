package com.alonso.vipera.training.springboot_apirest.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Servicio para la gestión de tokens JWT (JSON Web Tokens).
 * Proporciona funcionalidades para generar, validar y extraer información de tokens JWT
 * utilizados en la autenticación y autorización del sistema.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private Long EXPIRATION_TIME;

    /**
     * Genera un token JWT para un usuario autenticado.
     *
     * @param userDetails Detalles del usuario para el cual generar el token
     * @return Token JWT firmado con el username como subject y tiempo de expiración configurado
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el nombre de usuario (subject) de un token JWT.
     *
     * @param token Token JWT del cual extraer el username
     * @return Nombre de usuario contenido en el token
     * @throws JwtException Si el token es inválido o no se puede parsear
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Valida si un token JWT es válido para un usuario específico.
     * Verifica que el username coincida y que el token no haya expirado.
     *
     * @param token       Token JWT a validar
     * @param userDetails Detalles del usuario para comparar con el token
     * @return true si el token es válido, false en caso contrario
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si un token JWT ha expirado.
     *
     * @param token Token JWT a verificar
     * @return true si el token ha expirado, false en caso contrario
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Extrae un claim específico de un token JWT.
     *
     * @param <T>            Tipo del claim a extraer
     * @param token          Token JWT del cual extraer el claim
     * @param claimsResolver Función para resolver el claim específico
     * @return Valor del claim extraído
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims de un token JWT.
     *
     * @param token Token JWT a parsear
     * @return Claims contenidos en el token
     * @throws JwtException Si el token es inválido o no se puede parsear
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene la clave de firma utilizada para firmar y validar tokens JWT.
     *
     * @return Clave criptográfica basada en la clave secreta configurada
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
}