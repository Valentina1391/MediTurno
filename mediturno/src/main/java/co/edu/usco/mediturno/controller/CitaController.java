package co.edu.usco.mediturno.controller;

import co.edu.usco.mediturno.model.Cita;
import co.edu.usco.mediturno.repository.CitaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.usco.mediturno.repository.MedicoRepository;
import org.springframework.security.core.Authentication;
import java.util.List;

    /**
    * Controlador REST para gestionar las citas médicas.
    */

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaRepository repository;
    private final MedicoRepository medicos;

    public CitaController(CitaRepository repository, MedicoRepository medicos) {
        this.repository = repository;
        this.medicos = medicos;
    }
    /** Lista todas las citas. */                   
    @GetMapping
    public List<Cita> listar() {
        return repository.findAll();
    }

    /**
    * Lista las citas del médico que inició sesión.
    * Se identifica por el correo guardado en el token JWT.
    *
    * @param auth datos del usuario autenticado
    * @return las citas de ese médico
    */
    @GetMapping("/mi-agenda")
    public List<Cita> miAgenda(Authentication auth) {
        return medicos.findByUsuarioEmail(auth.getName())
            .map(m -> repository.findByMedicoId(m.getId()))
            .orElse(List.of());
    }

    /** Lista las citas de un paciente. */ 
    @GetMapping("/paciente/{id}")
    public List<Cita> porPaciente(@PathVariable Long id) {
        return repository.findByPacienteId(id);
    }
    /** Lista las citas (agenda) de un médico. */         
    @GetMapping("/medico/{id}")
    public List<Cita> porMedico(@PathVariable Long id) {
        return repository.findByMedicoId(id);
    }

    /**
    * Reserva una cita médica.
    * Si el médico ya tiene una cita PROGRAMADA a esa misma hora,
    * responde 409 (Conflict).
    *
    * @param cita datos de la cita (médico, paciente y fecha/hora)
    * @return Un mensaje de conflicto
    */

    @PostMapping
    public ResponseEntity<?> reservar(@RequestBody Cita cita) {
        boolean ocupado = repository.existsByMedicoIdAndFechaHoraAndEstado(
                cita.getMedico().getId(), cita.getFechaHora(), "PROGRAMADA");
        if (ocupado) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El médico ya tiene una cita en ese horario");
        }
        cita.setEstado("PROGRAMADA");
        return ResponseEntity.ok(repository.save(cita));
    }
    /**
    * Cancela una cita cambiando su estado a CANCELADA.
    *
    * @param id id de la cita
    * @return la cita actualizada o 404 si no existe
    */    
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Cita> cancelar(@PathVariable Long id) {
        return repository.findById(id)
                .map(c -> {
                    c.setEstado("CANCELADA");
                    return ResponseEntity.ok(repository.save(c));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    /**
    * Reprograma una cita a una nueva fecha y hora.
    * Si el médico ya está ocupado a esa hora, responde 409 (Conflict).
    *
    * @param id id de la cita
    * @param datos objeto con la nueva fechaHora
    * @return la cita actualizada o un mensaje de conflicto
    */
    @PutMapping("/{id}/reprogramar")
    public ResponseEntity<?> reprogramar(@PathVariable Long id, @RequestBody Cita datos) {
        return repository.findById(id).map(c -> {
            boolean ocupado = repository.existsByMedicoIdAndFechaHoraAndEstado(
                    c.getMedico().getId(), datos.getFechaHora(), "PROGRAMADA");
            if (ocupado) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body((Object) "El médico ya tiene una cita en ese horario");
            }
            c.setFechaHora(datos.getFechaHora());
            return ResponseEntity.ok((Object) repository.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }
    /**
    * Cambia el estado de una cita (por ejemplo, a ATENDIDA).
    *
    * @param id id de la cita
    * @param estado nuevo estado
    * @return la cita actualizada o 404 si no existe
    */ 
    @PutMapping("/{id}/estado")
    public ResponseEntity<Cita> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        return repository.findById(id)
                .map(c -> {
                    c.setEstado(estado);
                    return ResponseEntity.ok(repository.save(c));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}