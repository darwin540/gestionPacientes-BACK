package com.gestionpacientes.controller;

import com.gestionpacientes.dto.ServicioRequestDTO;
import com.gestionpacientes.dto.ServicioResponseDTO;
import com.gestionpacientes.dto.ServicioUpdateDTO;
import com.gestionpacientes.service.ServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    @PostMapping
    public ResponseEntity<ServicioResponseDTO> crearServicio(@Valid @RequestBody ServicioRequestDTO requestDTO) {
        ServicioResponseDTO responseDTO = servicioService.crearServicio(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponseDTO> obtenerServicioPorId(@PathVariable Long id) {
        ServicioResponseDTO responseDTO = servicioService.obtenerServicioPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ServicioResponseDTO>> obtenerTodosLosServicios() {
        List<ServicioResponseDTO> servicios = servicioService.obtenerTodosLosServicios();
        return ResponseEntity.ok(servicios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioResponseDTO> actualizarServicio(
            @PathVariable Long id,
            @Valid @RequestBody ServicioUpdateDTO updateDTO) {
        ServicioResponseDTO responseDTO = servicioService.actualizarServicio(id, updateDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
        return ResponseEntity.noContent().build();
    }
}

