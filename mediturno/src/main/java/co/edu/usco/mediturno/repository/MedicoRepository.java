package co.edu.usco.mediturno.repository;

import co.edu.usco.mediturno.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/** Acceso a datos de los médicos. */
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByUsuarioEmail(String email);
}