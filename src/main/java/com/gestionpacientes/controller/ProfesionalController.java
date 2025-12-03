package com.gestionpacientes.controller;

import com.gestionpacientes.dto.ProfesionalDTO;
import com.gestionpacientes.service.ProfesionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profesionales")
@CrossOrigin(origins = "*")
public class ProfesionalController {

    private final ProfesionalService profesionalService;

    @Autowired
    public ProfesionalController(ProfesionalService profesionalService) {
        this.profesionalService = profesionalService;
    }

    @GetMapping
    public ResponseEntity<List<ProfesionalDTO>> getAllProfesionales() {
        List<ProfesionalDTO> profesionales = profesionalService.findAll();
        return ResponseEntity.ok(profesionales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalDTO> getProfesionalById(@PathVariable Long id) {
        ProfesionalDTO profesional = profesionalService.findById(id);
        return ResponseEntity.ok(profesional);
    }

    @GetMapping("/usuario/{nombreUsuario}")
    public ResponseEntity<ProfesionalDTO> getProfesionalByNombreUsuario(@PathVariable String nombreUsuario) {
        ProfesionalDTO profesional = profesionalService.findByNombreUsuario(nombreUsuario);
        return ResponseEntity.ok(profesional);
    }

    @PostMapping
    public ResponseEntity<ProfesionalDTO> createProfesional(@Valid @RequestBody ProfesionalDTO profesionalDTO) {
        ProfesionalDTO createdProfesional = profesionalService.create(profesionalDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfesional);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesionalDTO> updateProfesional(@PathVariable Long id, @Valid @RequestBody ProfesionalDTO profesionalDTO) {
        ProfesionalDTO updatedProfesional = profesionalService.update(id, profesionalDTO);
        return ResponseEntity.ok(updatedProfesional);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesional(@PathVariable Long id) {
        profesionalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newPassword = request.get("password");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        profesionalService.updatePassword(id, newPassword);
        return ResponseEntity.ok().build();
    }
}


