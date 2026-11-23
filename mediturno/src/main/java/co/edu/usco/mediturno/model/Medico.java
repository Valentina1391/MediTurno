package co.edu.usco.mediturno.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
/**
 * Representa a un médico, asociado a una especialidad.
 */
@Entity
@Table(name = "medicos")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Usuario usuario;

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }
}