package co.edu.usco.mediturno.repository;

import co.edu.usco.mediturno.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
/** Acceso a datos de las especialidades. */
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
}
