package com.gestionpacientes.controller;

import com.gestionpacientes.dto.CrearTerapiasRequest;
import com.gestionpacientes.dto.TerapiaDTO;
import com.gestionpacientes.service.TerapiaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terapias")
@CrossOrigin(origins = "*")
public class TerapiaController {

    private final TerapiaService terapiaService;

    @Autowired
    public TerapiaController(TerapiaService terapiaService) {
        this.terapiaService = terapiaService;
    }

    @GetMapping
    public ResponseEntity<List<TerapiaDTO>> getAllTerapias() {
        List<TerapiaDTO> terapias = terapiaService.findAll();
        return ResponseEntity.ok(terapias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TerapiaDTO> getTerapiaById(@PathVariable Long id) {
        TerapiaDTO terapia = terapiaService.findById(id);
        return ResponseEntity.ok(terapia);
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<TerapiaDTO>> getTerapiasByPaciente(@PathVariable Long pacienteId) {
        List<TerapiaDTO> terapias = terapiaService.findByPacienteId(pacienteId);
        return ResponseEntity.ok(terapias);
    }

    @GetMapping("/profesional/{profesionalId}")
    public ResponseEntity<List<TerapiaDTO>> getTerapiasByProfesional(@PathVariable Long profesionalId) {
        List<TerapiaDTO> terapias = terapiaService.findByProfesionalId(profesionalId);
        return ResponseEntity.ok(terapias);
    }

    @GetMapping("/paciente/{pacienteId}/profesionales")
    public ResponseEntity<List<Long>> getProfesionalesByPaciente(@PathVariable Long pacienteId) {
        List<Long> profesionalIds = terapiaService.findDistinctProfesionalIdsByPacienteId(pacienteId);
        return ResponseEntity.ok(profesionalIds);
    }

    @PostMapping
    public ResponseEntity<TerapiaDTO> createTerapia(@Valid @RequestBody TerapiaDTO terapiaDTO) {
        TerapiaDTO createdTerapia = terapiaService.create(terapiaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTerapia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TerapiaDTO> updateTerapia(@PathVariable Long id, @Valid @RequestBody TerapiaDTO terapiaDTO) {
        TerapiaDTO updatedTerapia = terapiaService.update(id, terapiaDTO);
        return ResponseEntity.ok(updatedTerapia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerapia(@PathVariable Long id) {
        terapiaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<TerapiaDTO>> createMultipleTerapias(@Valid @RequestBody CrearTerapiasRequest request) {
        List<TerapiaDTO> createdTerapias = terapiaService.createMultiple(
            request.getPacienteId(),
            request.getProfesionalId(),
            request.getDiasAtencion()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTerapias);
    }
}
