package co.edu.usco.mediturno.repository;

import co.edu.usco.mediturno.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Acceso a datos de las citas médicas.
 */
public interface CitaRepository extends JpaRepository<Cita, Long> {
    /**
     * Verifica si un médico ya tiene una cita en una fecha y hora con un estado dado.
     *
     * @param medicoId id del médico
     * @param fechaHora fecha y hora de la cita
     * @param estado estado de la cita (por ejemplo, PROGRAMADA)
     * @return true si existe un conflicto de horario
     */
    boolean existsByMedicoIdAndFechaHoraAndEstado(Long medicoId, LocalDateTime fechaHora, String estado);

    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByMedicoId(Long medicoId);
}