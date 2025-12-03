package com.gestionpacientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profesionales")
public class Profesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Column(name = "nombre_usuario", unique = true, nullable = false)
    private String nombreUsuario;

    @NotBlank(message = "La profesión es obligatoria")
    @Column(nullable = false)
    private String profesion;

    @NotBlank(message = "El tipo de terapia es obligatorio")
    @Column(name = "tipo_terapia", nullable = false)
    private String tipoTerapia;

    @NotNull(message = "El valor por terapia es obligatorio")
    @Positive(message = "El valor por terapia debe ser positivo")
    @Column(name = "valor_por_terapia", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorPorTerapia;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Terapia> terapias = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructores
    public Profesional() {
    }

    public Profesional(String nombre, String nombreUsuario, String profesion, String tipoTerapia, BigDecimal valorPorTerapia) {
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
        this.profesion = profesion;
        this.tipoTerapia = tipoTerapia;
        this.valorPorTerapia = valorPorTerapia;
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

    public BigDecimal getValorPorTerapia() {
        return valorPorTerapia;
    }

    public void setValorPorTerapia(BigDecimal valorPorTerapia) {
        this.valorPorTerapia = valorPorTerapia;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Terapia> getTerapias() {
        return terapias;
    }

    public void setTerapias(List<Terapia> terapias) {
        this.terapias = terapias;
    }
}


