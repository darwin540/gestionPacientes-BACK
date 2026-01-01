package com.gestionpacientes.controller;

import com.gestionpacientes.dto.PacienteRequestDTO;
import com.gestionpacientes.dto.PacienteResponseDTO;
import com.gestionpacientes.dto.PacienteUpdateDTO;
import com.gestionpacientes.service.PacienteService;
import com.gestionpacientes.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> crearPaciente(@Valid @RequestBody PacienteRequestDTO requestDTO) {
        Long profesionalId = SecurityUtil.getProfesionalIdFromContext();
        PacienteResponseDTO responseDTO = pacienteService.crearPaciente(requestDTO, profesionalId);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> obtenerPacientePorId(@PathVariable Long id) {
        Long profesionalId = SecurityUtil.getProfesionalIdFromContext();
        PacienteResponseDTO responseDTO = pacienteService.obtenerPacientePorId(id, profesionalId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> obtenerTodosLosPacientes() {
        Long profesionalId = SecurityUtil.getProfesionalIdFromContext();
        List<PacienteResponseDTO> pacientes = pacienteService.obtenerTodosLosPacientes(profesionalId);
        return ResponseEntity.ok(pacientes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> actualizarPaciente(
            @PathVariable Long id,
            @Valid @RequestBody PacienteUpdateDTO updateDTO) {
        Long profesionalId = SecurityUtil.getProfesionalIdFromContext();
        PacienteResponseDTO responseDTO = pacienteService.actualizarPaciente(id, updateDTO, profesionalId);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        Long profesionalId = SecurityUtil.getProfesionalIdFromContext();
        pacienteService.eliminarPaciente(id, profesionalId);
        return ResponseEntity.noContent().build();
    }
}

