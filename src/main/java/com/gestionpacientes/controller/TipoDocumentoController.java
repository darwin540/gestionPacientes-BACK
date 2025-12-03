package com.gestionpacientes.controller;

import com.gestionpacientes.dto.TipoDocumentoDTO;
import com.gestionpacientes.service.TipoDocumentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-documento")
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    @Autowired
    public TipoDocumentoController(TipoDocumentoService tipoDocumentoService) {
        this.tipoDocumentoService = tipoDocumentoService;
    }

    @GetMapping
    public ResponseEntity<List<TipoDocumentoDTO>> getAllTiposDocumento() {
        List<TipoDocumentoDTO> tipos = tipoDocumentoService.findAll();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<TipoDocumentoDTO>> getTiposDocumentoActivos() {
        List<TipoDocumentoDTO> tipos = tipoDocumentoService.findAllActivos();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDocumentoDTO> getTipoDocumentoById(@PathVariable Long id) {
        TipoDocumentoDTO tipo = tipoDocumentoService.findById(id);
        return ResponseEntity.ok(tipo);
    }

    @PostMapping
    public ResponseEntity<TipoDocumentoDTO> createTipoDocumento(@Valid @RequestBody TipoDocumentoDTO tipoDocumentoDTO) {
        TipoDocumentoDTO createdTipo = tipoDocumentoService.create(tipoDocumentoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTipo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDocumentoDTO> updateTipoDocumento(@PathVariable Long id, @Valid @RequestBody TipoDocumentoDTO tipoDocumentoDTO) {
        TipoDocumentoDTO updatedTipo = tipoDocumentoService.update(id, tipoDocumentoDTO);
        return ResponseEntity.ok(updatedTipo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoDocumento(@PathVariable Long id) {
        tipoDocumentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

