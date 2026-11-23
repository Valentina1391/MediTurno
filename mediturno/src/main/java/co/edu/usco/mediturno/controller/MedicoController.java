package co.edu.usco.mediturno.controller;

import co.edu.usco.mediturno.model.Medico;
import co.edu.usco.mediturno.repository.MedicoRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * Controlador REST para gestionar los médicos (CRUD).
 */
@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoRepository repository;

    public MedicoController(MedicoRepository repository) {
        this.repository = repository;
    }
    /** Lista todos los médicos. */
    @GetMapping
    public List<Medico> listar() {
        return repository.findAll();
    }
    /** Obtiene un médico por su id. */
    @GetMapping("/{id}")
    public ResponseEntity<Medico> obtener(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    /** Registra un nuevo médico. */
    @PostMapping
    public Medico crear(@Valid @RequestBody Medico medico) {
        return repository.save(medico);
    }
    /** Actualiza los datos de un médico. */
    @PutMapping("/{id}")
    public ResponseEntity<Medico> actualizar(@PathVariable Long id, @Valid @RequestBody Medico datos) {
        return repository.findById(id)
                .map(m -> {
                    m.setNombre(datos.getNombre());
                    m.setEspecialidad(datos.getEspecialidad());
                    return ResponseEntity.ok(repository.save(m));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    /** Elimina un médico por su id. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
