package com.gestionpacientes.controller;

import com.gestionpacientes.dto.RegistroTerapiaRequestDTO;
import com.gestionpacientes.dto.RegistroTerapiaResponseDTO;
import com.gestionpacientes.dto.RegistroTerapiaUpdateDTO;
import com.gestionpacientes.service.RegistroTerapiaService;
import com.gestionpacientes.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros-terapia")
@RequiredArgsConstructor
public class RegistroTerapiaController {

    private final RegistroTerapiaService registroTerapiaService;

    @PostMapping
    public ResponseEntity<List<RegistroTerapiaResponseDTO>> crearRegistros(@Valid @RequestBody RegistroTerapiaRequestDTO requestDTO) {
        Long profesionalId = SecurityUtil.getProfesionalIdFromContext();
        List<RegistroTerapiaResponseDTO> responseDTOs = registroTerapiaService.crearRegistros(requestDTO, profesionalId);
        return new ResponseEntity<>(responseDTOs, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroTerapiaResponseDTO> obtenerRegistroPorId(@PathVariable Long id) {
        Long profesionalId = SecurityUtil.getProfesionalIdFromContext();
        RegistroTerapiaResponseDTO responseDTO = registroTerapiaService.obtenerRegistroPorId(id, profesionalId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<RegistroTerapiaResponseDTO>> obtenerRegistrosPorPaciente(@PathVariable Long pacienteId) {
        Long profesionalId = SecurityUtil.getProfesionalIdFromContext();
        List<RegistroTerapiaResponseDTO> registros = registroTerapiaService.obtenerRegistrosPorPaciente(pacienteId, profesionalId);
        return ResponseEntity.ok(registros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistroTerapiaResponseDTO> actualizarRegistro(
            @PathVariable Long id,
            @Valid @RequestBody RegistroTerapiaUpdateDTO updateDTO) {
        Long profesionalId = SecurityUtil.getProfesionalIdFromContext();
        RegistroTerapiaResponseDTO responseDTO = registroTerapiaService.actualizarRegistro(id, updateDTO, profesionalId);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRegistro(@PathVariable Long id) {
        Long profesionalId = SecurityUtil.getProfesionalIdFromContext();
        registroTerapiaService.eliminarRegistro(id, profesionalId);
        return ResponseEntity.noContent().build();
    }
}

