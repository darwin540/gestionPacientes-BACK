package com.gestionpacientes.service;

import com.gestionpacientes.dto.PacienteDTO;
import com.gestionpacientes.dto.ProfesionalPacientesDTO;
import com.gestionpacientes.exception.DuplicateResourceException;
import com.gestionpacientes.exception.ResourceNotFoundException;
import com.gestionpacientes.model.Paciente;
import com.gestionpacientes.model.Profesional;
import com.gestionpacientes.model.Terapia;
import com.gestionpacientes.model.TipoDocumento;
import com.gestionpacientes.repository.PacienteRepository;
import com.gestionpacientes.repository.ProfesionalRepository;
import com.gestionpacientes.repository.TerapiaRepository;
import com.gestionpacientes.repository.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final TerapiaRepository terapiaRepository;
    private final ProfesionalRepository profesionalRepository;

    @Autowired
    public PacienteService(PacienteRepository pacienteRepository, 
                          TipoDocumentoRepository tipoDocumentoRepository,
                          TerapiaRepository terapiaRepository,
                          ProfesionalRepository profesionalRepository) {
        this.pacienteRepository = pacienteRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.terapiaRepository = terapiaRepository;
        this.profesionalRepository = profesionalRepository;
    }

    public List<PacienteDTO> findAll() {
        return pacienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PacienteDTO findById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        return convertToDTO(paciente);
    }

    public PacienteDTO findByNumeroDocumento(String numeroDocumento) {
        Paciente paciente = pacienteRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente con número de documento: " + numeroDocumento + " no encontrado"));
        return convertToDTO(paciente);
    }

    public Optional<PacienteDTO> findByNumeroDocumentoOptional(String numeroDocumento) {
        return pacienteRepository.findByNumeroDocumento(numeroDocumento)
                .map(this::convertToDTO);
    }

    public List<PacienteDTO> searchByNombreAndApellido(String nombre, String apellido) {
        if (nombre != null && !nombre.trim().isEmpty() && apellido != null && !apellido.trim().isEmpty()) {
            return pacienteRepository.findByNombreAndApellido(nombre.trim(), apellido.trim()).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else if (nombre != null && !nombre.trim().isEmpty()) {
            return pacienteRepository.findByNombreContainingIgnoreCase(nombre.trim()).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else if (apellido != null && !apellido.trim().isEmpty()) {
            return pacienteRepository.findByApellidoContainingIgnoreCase(apellido.trim()).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public PacienteDTO create(PacienteDTO pacienteDTO) {
        // Verificar si el número de documento ya existe
        if (pacienteRepository.existsByNumeroDocumento(pacienteDTO.getNumeroDocumento())) {
            throw new DuplicateResourceException("El número de documento ya existe: " + pacienteDTO.getNumeroDocumento());
        }

        // Verificar que el tipo de documento existe
        Long tipoDocumentoId = pacienteDTO.getTipoDocumentoId();
        Objects.requireNonNull(tipoDocumentoId, "El ID del tipo de documento no puede ser nulo");
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(tipoDocumentoId)
                .orElseThrow(() -> new ResourceNotFoundException("TipoDocumento", tipoDocumentoId));

        Paciente paciente = convertToEntity(pacienteDTO, tipoDocumento);
        @SuppressWarnings("null") // JPA save() siempre devuelve un objeto no nulo
        Paciente savedPaciente = pacienteRepository.save(paciente);
        return convertToDTO(savedPaciente);
    }

    public PacienteDTO update(Long id, PacienteDTO pacienteDTO) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));

        // Verificar si el número de documento ya existe en otro paciente
        if (!paciente.getNumeroDocumento().equals(pacienteDTO.getNumeroDocumento())) {
            if (pacienteRepository.existsByNumeroDocumento(pacienteDTO.getNumeroDocumento())) {
                throw new DuplicateResourceException("El número de documento ya existe: " + pacienteDTO.getNumeroDocumento());
            }
        }

        paciente.setNombre(pacienteDTO.getNombre());
        paciente.setApellido(pacienteDTO.getApellido());
        paciente.setNumeroDocumento(pacienteDTO.getNumeroDocumento());
        
        // Actualizar tipo de documento si cambió
        Long tipoDocumentoId = pacienteDTO.getTipoDocumentoId();
        if (tipoDocumentoId != null && !paciente.getTipoDocumento().getId().equals(tipoDocumentoId)) {
            TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(tipoDocumentoId)
                    .orElseThrow(() -> new ResourceNotFoundException("TipoDocumento", tipoDocumentoId));
            paciente.setTipoDocumento(tipoDocumento);
        }

        Paciente updatedPaciente = pacienteRepository.save(paciente);
        return convertToDTO(updatedPaciente);
    }

    public void delete(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        if (!pacienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente", id);
        }
        pacienteRepository.deleteById(id);
    }

    // Métodos de conversión
    private PacienteDTO convertToDTO(Paciente paciente) {
        Objects.requireNonNull(paciente, "El paciente no puede ser nulo");
        Objects.requireNonNull(paciente.getTipoDocumento(), "El tipo de documento del paciente no puede ser nulo");
        return new PacienteDTO(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getTipoDocumento().getId(),
                paciente.getTipoDocumento().getNombre(),
                paciente.getNumeroDocumento()
        );
    }

    private Paciente convertToEntity(PacienteDTO pacienteDTO, TipoDocumento tipoDocumento) {
        Paciente paciente = new Paciente();
        paciente.setNombre(pacienteDTO.getNombre());
        paciente.setApellido(pacienteDTO.getApellido());
        paciente.setTipoDocumento(tipoDocumento);
        paciente.setNumeroDocumento(pacienteDTO.getNumeroDocumento());
        return paciente;
    }

    public List<ProfesionalPacientesDTO> getPacientesPorProfesional() {
        // Obtener todos los profesionales activos
        List<Profesional> profesionales = profesionalRepository.findByActivoTrue();
        
        List<ProfesionalPacientesDTO> resultado = new ArrayList<>();
        
        // Para cada profesional, obtener sus pacientes únicos a través de las terapias
        for (Profesional profesional : profesionales) {
            // Obtener terapias del profesional
            List<Terapia> terapiasDelProfesional = terapiaRepository.findByProfesionalId(profesional.getId());
            
            // Crear un Set para almacenar IDs de pacientes únicos
            Set<Long> pacienteIdsUnicos = new HashSet<>();
            List<PacienteDTO> pacientesUnicos = new ArrayList<>();
            
            // Recorrer las terapias y agregar pacientes únicos
            for (Terapia terapia : terapiasDelProfesional) {
                Paciente paciente = terapia.getPaciente();
                if (paciente != null && pacienteIdsUnicos.add(paciente.getId())) {
                    pacientesUnicos.add(convertToDTO(paciente));
                }
            }
            
            // Solo agregar el profesional si tiene pacientes
            if (!pacientesUnicos.isEmpty()) {
                ProfesionalPacientesDTO dto = new ProfesionalPacientesDTO(
                    profesional.getId(),
                    profesional.getNombre(),
                    profesional.getApellido(),
                    profesional.getNombreUsuario(),
                    profesional.getProfesion(),
                    profesional.getTipoTerapia()
                );
                dto.setPacientes(pacientesUnicos);
                resultado.add(dto);
            }
        }
        
        return resultado;
    }
}

