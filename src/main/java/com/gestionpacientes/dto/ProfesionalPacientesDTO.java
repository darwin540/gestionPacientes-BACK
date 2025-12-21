package com.gestionpacientes.dto;

import java.util.ArrayList;
import java.util.List;

public class ProfesionalPacientesDTO {
    
    private Long profesionalId;
    private String profesionalNombre;
    private String profesionalApellido;
    private String profesionalNombreUsuario;
    private String profesionalProfesion;
    private String profesionalTipoTerapia;
    private List<PacienteDTO> pacientes;

    public ProfesionalPacientesDTO() {
        this.pacientes = new ArrayList<>();
    }

    public ProfesionalPacientesDTO(Long profesionalId, String profesionalNombre, String profesionalApellido, 
                                   String profesionalNombreUsuario, String profesionalProfesion, 
                                   String profesionalTipoTerapia) {
        this.profesionalId = profesionalId;
        this.profesionalNombre = profesionalNombre;
        this.profesionalApellido = profesionalApellido;
        this.profesionalNombreUsuario = profesionalNombreUsuario;
        this.profesionalProfesion = profesionalProfesion;
        this.profesionalTipoTerapia = profesionalTipoTerapia;
        this.pacientes = new ArrayList<>();
    }

    // Getters y Setters
    public Long getProfesionalId() {
        return profesionalId;
    }

    public void setProfesionalId(Long profesionalId) {
        this.profesionalId = profesionalId;
    }

    public String getProfesionalNombre() {
        return profesionalNombre;
    }

    public void setProfesionalNombre(String profesionalNombre) {
        this.profesionalNombre = profesionalNombre;
    }

    public String getProfesionalApellido() {
        return profesionalApellido;
    }

    public void setProfesionalApellido(String profesionalApellido) {
        this.profesionalApellido = profesionalApellido;
    }

    public String getProfesionalNombreUsuario() {
        return profesionalNombreUsuario;
    }

    public void setProfesionalNombreUsuario(String profesionalNombreUsuario) {
        this.profesionalNombreUsuario = profesionalNombreUsuario;
    }

    public String getProfesionalProfesion() {
        return profesionalProfesion;
    }

    public void setProfesionalProfesion(String profesionalProfesion) {
        this.profesionalProfesion = profesionalProfesion;
    }

    public String getProfesionalTipoTerapia() {
        return profesionalTipoTerapia;
    }

    public void setProfesionalTipoTerapia(String profesionalTipoTerapia) {
        this.profesionalTipoTerapia = profesionalTipoTerapia;
    }

    public List<PacienteDTO> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<PacienteDTO> pacientes) {
        this.pacientes = pacientes;
    }

    public void addPaciente(PacienteDTO paciente) {
        this.pacientes.add(paciente);
    }
}



