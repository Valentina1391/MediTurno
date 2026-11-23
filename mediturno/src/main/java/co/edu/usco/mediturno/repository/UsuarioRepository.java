package co.edu.usco.mediturno.repository;

import co.edu.usco.mediturno.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /**
    * Busca un usuario por su correo electrónico.
    *
    * @param email correo del usuario
    * @return el usuario, si existe
    */
    Optional<Usuario> findByEmail(String email);
}