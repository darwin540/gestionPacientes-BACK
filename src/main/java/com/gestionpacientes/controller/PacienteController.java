package com.gestionpacientes.controller;

import com.gestionpacientes.dto.PacienteDTO;
import com.gestionpacientes.dto.ProfesionalPacientesDTO;
import com.gestionpacientes.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    private final PacienteService pacienteService;

    @Autowired
    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public ResponseEntity<List<PacienteDTO>> getAllPacientes() {
        List<PacienteDTO> pacientes = pacienteService.findAll();
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/por-profesional")
    public ResponseEntity<List<ProfesionalPacientesDTO>> getPacientesPorProfesional() {
        List<ProfesionalPacientesDTO> pacientesPorProfesional = pacienteService.getPacientesPorProfesional();
        return ResponseEntity.ok(pacientesPorProfesional);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> getPacienteById(@PathVariable Long id) {
        PacienteDTO paciente = pacienteService.findById(id);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/documento/{numeroDocumento}")
    public ResponseEntity<PacienteDTO> getPacienteByNumeroDocumento(@PathVariable String numeroDocumento) {
        PacienteDTO paciente = pacienteService.findByNumeroDocumento(numeroDocumento);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> searchPacientes(
            @RequestParam(required = false) String documento,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido) {
        
        // Buscar por documento (búsqueda parcial) si se proporciona
        if (documento != null && !documento.trim().isEmpty()) {
            List<PacienteDTO> pacientes = pacienteService.searchByNumeroDocumentoContaining(documento.trim());
            return ResponseEntity.ok(pacientes);
        }
        
        // Buscar por nombre y apellido
        if ((nombre != null && !nombre.trim().isEmpty()) || (apellido != null && !apellido.trim().isEmpty())) {
            List<PacienteDTO> pacientes = pacienteService.searchByNombreAndApellido(nombre, apellido);
            return ResponseEntity.ok(pacientes);
        }
        
        return ResponseEntity.badRequest().body("Debe proporcionar documento, nombre o apellido para buscar");
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<PacienteDTO>> getSuggestions(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        List<PacienteDTO> suggestions = pacienteService.getSuggestions(query, limit);
        return ResponseEntity.ok(suggestions);
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> createPaciente(@Valid @RequestBody PacienteDTO pacienteDTO) {
        PacienteDTO createdPaciente = pacienteService.create(pacienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPaciente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> updatePaciente(@PathVariable Long id, @Valid @RequestBody PacienteDTO pacienteDTO) {
        PacienteDTO updatedPaciente = pacienteService.update(id, pacienteDTO);
        return ResponseEntity.ok(updatedPaciente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        pacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


