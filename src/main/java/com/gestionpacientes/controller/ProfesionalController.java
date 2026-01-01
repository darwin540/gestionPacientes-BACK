package com.gestionpacientes.controller;

import com.gestionpacientes.dto.ProfesionalRequestDTO;
import com.gestionpacientes.dto.ProfesionalResponseDTO;
import com.gestionpacientes.dto.ProfesionalUpdateDTO;
import com.gestionpacientes.service.ProfesionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesionales")
@RequiredArgsConstructor
public class ProfesionalController {

    private final ProfesionalService profesionalService;

    @PostMapping
    public ResponseEntity<ProfesionalResponseDTO> crearProfesional(@Valid @RequestBody ProfesionalRequestDTO requestDTO) {
        ProfesionalResponseDTO responseDTO = profesionalService.crearProfesional(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalResponseDTO> obtenerProfesionalPorId(@PathVariable Long id) {
        ProfesionalResponseDTO responseDTO = profesionalService.obtenerProfesionalPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProfesionalResponseDTO>> obtenerTodosLosProfesionales() {
        List<ProfesionalResponseDTO> profesionales = profesionalService.obtenerTodosLosProfesionales();
        return ResponseEntity.ok(profesionales);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesionalResponseDTO> actualizarProfesional(
            @PathVariable Long id,
            @Valid @RequestBody ProfesionalUpdateDTO updateDTO) {
        ProfesionalResponseDTO responseDTO = profesionalService.actualizarProfesional(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProfesional(@PathVariable Long id) {
        profesionalService.eliminarProfesional(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/tipos-terapia")
    public ResponseEntity<ProfesionalResponseDTO> asignarTiposTerapia(
            @PathVariable Long id,
            @RequestBody java.util.Set<Long> tiposTerapiaIds) {
        ProfesionalResponseDTO responseDTO = profesionalService.asignarTiposTerapia(id, tiposTerapiaIds);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}/tipos-terapia")
    public ResponseEntity<ProfesionalResponseDTO> quitarTiposTerapia(
            @PathVariable Long id,
            @RequestBody java.util.Set<Long> tiposTerapiaIds) {
        ProfesionalResponseDTO responseDTO = profesionalService.quitarTiposTerapia(id, tiposTerapiaIds);
        return ResponseEntity.ok(responseDTO);
    }
}

