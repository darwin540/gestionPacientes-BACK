package com.gestionpacientes.controller;

import com.gestionpacientes.dto.ServicioDepartamentoDTO;
import com.gestionpacientes.service.ServicioDepartamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios-departamentos")
public class ServicioDepartamentoController {

    private final ServicioDepartamentoService servicioDepartamentoService;

    @Autowired
    public ServicioDepartamentoController(ServicioDepartamentoService servicioDepartamentoService) {
        this.servicioDepartamentoService = servicioDepartamentoService;
    }

    @GetMapping
    public ResponseEntity<List<ServicioDepartamentoDTO>> getAllServiciosDepartamentos() {
        List<ServicioDepartamentoDTO> servicios = servicioDepartamentoService.findAll();
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ServicioDepartamentoDTO>> getServiciosDepartamentosActivos() {
        List<ServicioDepartamentoDTO> servicios = servicioDepartamentoService.findAllActivos();
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDepartamentoDTO> getServicioDepartamentoById(@PathVariable Long id) {
        ServicioDepartamentoDTO servicio = servicioDepartamentoService.findById(id);
        return ResponseEntity.ok(servicio);
    }

    @PostMapping
    public ResponseEntity<ServicioDepartamentoDTO> createServicioDepartamento(@Valid @RequestBody ServicioDepartamentoDTO servicioDepartamentoDTO) {
        ServicioDepartamentoDTO createdServicio = servicioDepartamentoService.create(servicioDepartamentoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdServicio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioDepartamentoDTO> updateServicioDepartamento(@PathVariable Long id, @Valid @RequestBody ServicioDepartamentoDTO servicioDepartamentoDTO) {
        ServicioDepartamentoDTO updatedServicio = servicioDepartamentoService.update(id, servicioDepartamentoDTO);
        return ResponseEntity.ok(updatedServicio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServicioDepartamento(@PathVariable Long id) {
        servicioDepartamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

