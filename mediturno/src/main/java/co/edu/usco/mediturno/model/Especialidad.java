package co.edu.usco.mediturno.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
    /**
    * Representa una especialidad médica (por ejemplo, Pediatría).
    */
@Entity
@Table(name = "especialidades")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nombre;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
