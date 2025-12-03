package com.gestionpacientes.dto;

import jakarta.validation.constraints.NotBlank;

public class ProfesionalDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombreUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotBlank(message = "La profesión es obligatoria")
    private String profesion;

    @NotBlank(message = "El tipo de terapia es obligatorio")
    private String tipoTerapia;

    private Boolean activo = true;

    // Constructores
    public ProfesionalDTO() {
    }

    public ProfesionalDTO(Long id, String nombre, String apellido, String nombreUsuario, String password, String profesion, String tipoTerapia, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.profesion = profesion;
        this.tipoTerapia = tipoTerapia;
        this.activo = activo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getTipoTerapia() {
        return tipoTerapia;
    }

    public void setTipoTerapia(String tipoTerapia) {
        this.tipoTerapia = tipoTerapia;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}


