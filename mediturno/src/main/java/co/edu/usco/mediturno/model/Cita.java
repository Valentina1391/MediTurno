package co.edu.usco.mediturno.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
/**
 * Representa una cita médica entre un paciente y un médico.
 * Su estado puede ser PROGRAMADA, CANCELADA o ATENDIDA.
 */
@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id")
    private Usuario paciente;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private String estado = "PROGRAMADA"; // PROGRAMADA, CANCELADA, ATENDIDA

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
    public Usuario getPaciente() { return paciente; }
    public void setPaciente(Usuario paciente) { this.paciente = paciente; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}