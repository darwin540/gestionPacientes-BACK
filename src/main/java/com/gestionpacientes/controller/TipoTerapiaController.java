package com.gestionpacientes.controller;

import com.gestionpacientes.dto.TipoTerapiaDTO;
import com.gestionpacientes.service.TipoTerapiaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-terapia")
@CrossOrigin(origins = "*")
public class TipoTerapiaController {

    private final TipoTerapiaService tipoTerapiaService;

    @Autowired
    public TipoTerapiaController(TipoTerapiaService tipoTerapiaService) {
        this.tipoTerapiaService = tipoTerapiaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TipoTerapiaDTO>> getAllTiposTerapia() {
        return ResponseEntity.ok(tipoTerapiaService.findAll());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<TipoTerapiaDTO>> getTiposTerapiaActivos() {
        return ResponseEntity.ok(tipoTerapiaService.findActivos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoTerapiaDTO> getTipoTerapiaById(@PathVariable Long id) {
        return ResponseEntity.ok(tipoTerapiaService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoTerapiaDTO> createTipoTerapia(@Valid @RequestBody TipoTerapiaDTO tipoTerapiaDTO) {
        return new ResponseEntity<>(tipoTerapiaService.create(tipoTerapiaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoTerapiaDTO> updateTipoTerapia(@PathVariable Long id, @Valid @RequestBody TipoTerapiaDTO tipoTerapiaDTO) {
        return ResponseEntity.ok(tipoTerapiaService.update(id, tipoTerapiaDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTipoTerapia(@PathVariable Long id) {
        tipoTerapiaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}



