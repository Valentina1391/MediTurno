package co.edu.usco.mediturno.controller;

import co.edu.usco.mediturno.model.Especialidad;
import co.edu.usco.mediturno.repository.EspecialidadRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controlador REST para gestionar las especialidades médicas (CRUD).
 */
@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final EspecialidadRepository repository;

    public EspecialidadController(EspecialidadRepository repository) {
        this.repository = repository;
    }
    /** Lista todas las especialidades. */ 
    @GetMapping
    public List<Especialidad> listar() {
        return repository.findAll();
    }
    /** Obtiene una especialidad por su id. */   
    @GetMapping("/{id}")
    public ResponseEntity<Especialidad> obtener(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    /** Crea una nueva especialidad. */       
    @PostMapping
    public Especialidad crear(@Valid @RequestBody Especialidad especialidad) {
        return repository.save(especialidad);
    }
    /** Actualiza el nombre de una especialidad. */ 
    @PutMapping("/{id}")
    public ResponseEntity<Especialidad> actualizar(@PathVariable Long id,
                                                   @Valid @RequestBody Especialidad datos) {
        return repository.findById(id)
                .map(e -> {
                    e.setNombre(datos.getNombre());
                    return ResponseEntity.ok(repository.save(e));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    /** Elimina una especialidad por su id. */     
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}