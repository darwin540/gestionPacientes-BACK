package com.gestionpacientes.controller;

import com.gestionpacientes.dto.TipoTerapiaRequestDTO;
import com.gestionpacientes.dto.TipoTerapiaResponseDTO;
import com.gestionpacientes.dto.TipoTerapiaUpdateDTO;
import com.gestionpacientes.service.TipoTerapiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-terapia")
@RequiredArgsConstructor
public class TipoTerapiaController {

    private final TipoTerapiaService tipoTerapiaService;

    @PostMapping
    public ResponseEntity<TipoTerapiaResponseDTO> crearTipoTerapia(@Valid @RequestBody TipoTerapiaRequestDTO requestDTO) {
        TipoTerapiaResponseDTO responseDTO = tipoTerapiaService.crearTipoTerapia(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoTerapiaResponseDTO> obtenerTipoTerapiaPorId(@PathVariable Long id) {
        TipoTerapiaResponseDTO responseDTO = tipoTerapiaService.obtenerTipoTerapiaPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<TipoTerapiaResponseDTO>> obtenerTodosLosTiposTerapia() {
        List<TipoTerapiaResponseDTO> tiposTerapia = tipoTerapiaService.obtenerTodosLosTiposTerapia();
        return ResponseEntity.ok(tiposTerapia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoTerapiaResponseDTO> actualizarTipoTerapia(
            @PathVariable Long id,
            @Valid @RequestBody TipoTerapiaUpdateDTO updateDTO) {
        TipoTerapiaResponseDTO responseDTO = tipoTerapiaService.actualizarTipoTerapia(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTipoTerapia(@PathVariable Long id) {
        tipoTerapiaService.eliminarTipoTerapia(id);
        return ResponseEntity.noContent().build();
    }
}

