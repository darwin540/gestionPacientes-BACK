package com.gestionpacientes.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TerapiaDTO {

    private Long id;

    @NotNull(message = "El paciente es obligatorio")
    private Long pacienteId;

    @NotNull(message = "El profesional es obligatorio")
    private Long profesionalId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;

    @NotNull(message = "El servicio/departamento es obligatorio")
    private Long servicioDepartamentoId;
    
    // Información adicional para respuestas
    private String servicioDepartamentoNombre;
    private String servicioDepartamentoAbreviacion;

    // Información adicional para respuestas
    private String pacienteNombre;
    private String pacienteApellido;
    private String profesionalNombre;

    // Constructores
    public TerapiaDTO() {
    }

    public TerapiaDTO(Long id, Long pacienteId, Long profesionalId, LocalDateTime fecha, Long servicioDepartamentoId) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.profesionalId = profesionalId;
        this.fecha = fecha;
        this.servicioDepartamentoId = servicioDepartamentoId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getProfesionalId() {
        return profesionalId;
    }

    public void setProfesionalId(Long profesionalId) {
        this.profesionalId = profesionalId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Long getServicioDepartamentoId() {
        return servicioDepartamentoId;
    }

    public void setServicioDepartamentoId(Long servicioDepartamentoId) {
        this.servicioDepartamentoId = servicioDepartamentoId;
    }

    public String getServicioDepartamentoNombre() {
        return servicioDepartamentoNombre;
    }

    public void setServicioDepartamentoNombre(String servicioDepartamentoNombre) {
        this.servicioDepartamentoNombre = servicioDepartamentoNombre;
    }

    public String getServicioDepartamentoAbreviacion() {
        return servicioDepartamentoAbreviacion;
    }

    public void setServicioDepartamentoAbreviacion(String servicioDepartamentoAbreviacion) {
        this.servicioDepartamentoAbreviacion = servicioDepartamentoAbreviacion;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public String getPacienteApellido() {
        return pacienteApellido;
    }

    public void setPacienteApellido(String pacienteApellido) {
        this.pacienteApellido = pacienteApellido;
    }

    public String getProfesionalNombre() {
        return profesionalNombre;
    }

    public void setProfesionalNombre(String profesionalNombre) {
        this.profesionalNombre = profesionalNombre;
    }
}


