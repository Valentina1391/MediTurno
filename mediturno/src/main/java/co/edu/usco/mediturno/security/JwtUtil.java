package co.edu.usco.mediturno.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Crear y validar tokens JWT.
 */
@Component
public class JwtUtil {

    private final SecretKey clave;
    private final long expiracion;

    public JwtUtil(@Value("${jwt.secret}") String secreto,
                   @Value("${jwt.expiration}") long expiracion) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracion = expiracion;
    }

    /**
     * Genera un token con el correo y el rol del usuario.
     *
     * @param email correo del usuario
     * @param rol rol del usuario (PACIENTE, MEDICO o ADMIN)
     * @return el token JWT firmado
     */
    public String generarToken(String email, String rol) {
        return Jwts.builder()
                .subject(email)
                .claim("rol", rol)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiracion))
                .signWith(clave)
                .compact();
    }

    /**
     * Lee y valida un token. Lanza excepción si es inválido o expiró.
     *
     * @param token token JWT
     * @return los datos (claims) del token
     */
    public Claims leerToken(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}