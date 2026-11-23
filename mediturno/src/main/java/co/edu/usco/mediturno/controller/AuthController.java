package co.edu.usco.mediturno.controller;

import co.edu.usco.mediturno.model.Usuario;
import co.edu.usco.mediturno.repository.UsuarioRepository;
import co.edu.usco.mediturno.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import java.util.Collections;

import java.util.Map;

/**
 * Registro de pacientes y login con JWT.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarios, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.usuarios = usuarios;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registra un nuevo usuario con rol PACIENTE y contraseña cifrada
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody Usuario usuario) {
        if (usuarios.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
        }
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        usuario.setRol("PACIENTE"); 
        usuarios.save(usuario);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado"));
    }

    /**
     * Inicia sesión y devuelve un token JWT.
    */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> datos) {
        return usuarios.findByEmail(datos.get("email"))
                .filter(u -> encoder.matches(datos.get("password"), u.getPassword()))
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(Map.of(
                        "token", jwtUtil.generarToken(u.getEmail(), u.getRol()),
                        "rol", u.getRol(),
                        "nombre", u.getNombre(),
                        "id", u.getId())))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas"));
    }

    @Value("${google.client-id}")
    private String googleClientId;

    /**
 * Inicio de sesión con Google (OAuth 2.0 / OpenID Connect).
 */
@PostMapping("/google")
public ResponseEntity<?> google(@RequestBody Map<String, String> datos) {
    try {
        GoogleIdTokenVerifier verificador = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verificador.verify(datos.get("credential"));
        if (idToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de Google inválido");
        }

        String email = idToken.getPayload().getEmail();
        String nombre = (String) idToken.getPayload().get("name");

        Usuario u = usuarios.findByEmail(email).orElseGet(() -> {
            Usuario nuevo = new Usuario();
            nuevo.setEmail(email);
            nuevo.setNombre(nombre != null ? nombre : email);
            nuevo.setPassword(encoder.encode(java.util.UUID.randomUUID().toString()));
            nuevo.setRol("PACIENTE");
            return usuarios.save(nuevo);
        });

        return ResponseEntity.ok(Map.of(
                "token", jwtUtil.generarToken(u.getEmail(), u.getRol()),
                "rol", u.getRol(),
                "nombre", u.getNombre(),
                "id", u.getId()));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se pudo validar el token de Google");
    }
}

}