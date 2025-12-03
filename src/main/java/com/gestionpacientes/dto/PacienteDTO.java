package com.gestionpacientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PacienteDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotNull(message = "El tipo de documento es obligatorio")
    private Long tipoDocumentoId;
    
    private String tipoDocumentoNombre; // Para mostrar en la respuesta

    @NotBlank(message = "El número de documento es obligatorio")
    private String numeroDocumento;

    // Constructores
    public PacienteDTO() {
    }

    public PacienteDTO(Long id, String nombre, String apellido, Long tipoDocumentoId, String tipoDocumentoNombre, String numeroDocumento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumentoId = tipoDocumentoId;
        this.tipoDocumentoNombre = tipoDocumentoNombre;
        this.numeroDocumento = numeroDocumento;
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

    public Long getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    public void setTipoDocumentoId(Long tipoDocumentoId) {
        this.tipoDocumentoId = tipoDocumentoId;
    }

    public String getTipoDocumentoNombre() {
        return tipoDocumentoNombre;
    }

    public void setTipoDocumentoNombre(String tipoDocumentoNombre) {
        this.tipoDocumentoNombre = tipoDocumentoNombre;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
}


