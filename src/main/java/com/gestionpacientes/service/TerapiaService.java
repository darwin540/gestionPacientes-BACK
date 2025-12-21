package com.gestionpacientes.service;

import com.gestionpacientes.dto.CrearTerapiasRequest;
import com.gestionpacientes.dto.TerapiaDTO;
import com.gestionpacientes.exception.ResourceNotFoundException;
import com.gestionpacientes.model.Paciente;
import com.gestionpacientes.model.Profesional;
import com.gestionpacientes.model.ServicioDepartamento;
import com.gestionpacientes.model.Terapia;
import com.gestionpacientes.repository.PacienteRepository;
import com.gestionpacientes.repository.ProfesionalRepository;
import com.gestionpacientes.repository.ServicioDepartamentoRepository;
import com.gestionpacientes.repository.TerapiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class TerapiaService {

    private final TerapiaRepository terapiaRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfesionalRepository profesionalRepository;
    private final ServicioDepartamentoRepository servicioDepartamentoRepository;

    @Autowired
    public TerapiaService(TerapiaRepository terapiaRepository, 
                         PacienteRepository pacienteRepository,
                         ProfesionalRepository profesionalRepository,
                         ServicioDepartamentoRepository servicioDepartamentoRepository) {
        this.terapiaRepository = terapiaRepository;
        this.pacienteRepository = pacienteRepository;
        this.profesionalRepository = profesionalRepository;
        this.servicioDepartamentoRepository = servicioDepartamentoRepository;
    }

    public List<TerapiaDTO> findAll() {
        return terapiaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TerapiaDTO findById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        Terapia terapia = terapiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Terapia", id));
        return convertToDTO(terapia);
    }

    public List<TerapiaDTO> findByPacienteId(Long pacienteId) {
        return terapiaRepository.findByPacienteId(pacienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Long> findDistinctProfesionalIdsByPacienteId(Long pacienteId) {
        return terapiaRepository.findDistinctProfesionalIdsByPacienteId(pacienteId);
    }

    public List<TerapiaDTO> findByProfesionalId(Long profesionalId) {
        return terapiaRepository.findByProfesionalId(profesionalId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TerapiaDTO create(TerapiaDTO terapiaDTO) {
        Long pacienteId = Objects.requireNonNull(terapiaDTO.getPacienteId(), "El ID del paciente no puede ser nulo");
        Long profesionalId = Objects.requireNonNull(terapiaDTO.getProfesionalId(), "El ID del profesional no puede ser nulo");
        
        // Verificar que el paciente existe
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", pacienteId));

        // Verificar que el profesional existe
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional", profesionalId));

        Long servicioDepartamentoId = Objects.requireNonNull(terapiaDTO.getServicioDepartamentoId(), "El ID del servicio/departamento no puede ser nulo");
        ServicioDepartamento servicioDepartamento = servicioDepartamentoRepository.findById(servicioDepartamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("ServicioDepartamento", servicioDepartamentoId));

        Terapia terapia = new Terapia();
        terapia.setPaciente(paciente);
        terapia.setProfesional(profesional);
        terapia.setFecha(terapiaDTO.getFecha());
        terapia.setServicioDepartamento(servicioDepartamento);

        Terapia savedTerapia = terapiaRepository.save(terapia);
        Objects.requireNonNull(savedTerapia, "Error al guardar la terapia");
        return convertToDTO(savedTerapia);
    }

    public TerapiaDTO update(Long id, TerapiaDTO terapiaDTO) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        Terapia terapia = terapiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Terapia", id));

        // Verificar que el paciente existe si cambió
        Long pacienteId = terapiaDTO.getPacienteId();
        if (pacienteId != null && !terapia.getPaciente().getId().equals(pacienteId)) {
            Paciente paciente = pacienteRepository.findById(pacienteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente", pacienteId));
            terapia.setPaciente(paciente);
        }

        // Verificar que el profesional existe si cambió
        Long profesionalId = terapiaDTO.getProfesionalId();
        if (profesionalId != null && !terapia.getProfesional().getId().equals(profesionalId)) {
            Profesional profesional = profesionalRepository.findById(profesionalId)
                    .orElseThrow(() -> new ResourceNotFoundException("Profesional", profesionalId));
            terapia.setProfesional(profesional);
        }

        terapia.setFecha(terapiaDTO.getFecha());
        
        // Actualizar servicio/departamento si cambió
        Long servicioDepartamentoId = terapiaDTO.getServicioDepartamentoId();
        if (servicioDepartamentoId != null && !terapia.getServicioDepartamento().getId().equals(servicioDepartamentoId)) {
            ServicioDepartamento servicioDepartamento = servicioDepartamentoRepository.findById(servicioDepartamentoId)
                    .orElseThrow(() -> new ResourceNotFoundException("ServicioDepartamento", servicioDepartamentoId));
            terapia.setServicioDepartamento(servicioDepartamento);
        }

        Terapia updatedTerapia = terapiaRepository.save(terapia);
        Objects.requireNonNull(updatedTerapia, "Error al actualizar la terapia");
        return convertToDTO(updatedTerapia);
    }

    public void delete(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo");
        if (!terapiaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Terapia", id);
        }
        terapiaRepository.deleteById(id);
    }

    public List<TerapiaDTO> createMultiple(Long pacienteId, Long profesionalId, 
                                           List<CrearTerapiasRequest.DiaAtencion> diasAtencion) {
        Objects.requireNonNull(pacienteId, "El ID del paciente no puede ser nulo");
        Objects.requireNonNull(profesionalId, "El ID del profesional no puede ser nulo");
        Objects.requireNonNull(diasAtencion, "Los días de atención no pueden ser nulos");
        
        if (diasAtencion.isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar al menos un día de atención");
        }

        // Verificar que el paciente existe
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", pacienteId));

        // Verificar que el profesional existe
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional", profesionalId));

        List<Terapia> terapias = new java.util.ArrayList<>();
        
        for (CrearTerapiasRequest.DiaAtencion dia : diasAtencion) {
            // Verificar que el servicio/departamento existe
            Long servicioId = Objects.requireNonNull(dia.getServicioDepartamentoId(), "El ID del servicio/departamento no puede ser nulo");
            ServicioDepartamento servicioDepartamento = servicioDepartamentoRepository.findById(servicioId)
                    .orElseThrow(() -> new ResourceNotFoundException("ServicioDepartamento", servicioId));

            // Crear múltiples terapias para este día según el número especificado
            for (int i = 0; i < dia.getNumeroTerapias(); i++) {
                Terapia terapia = new Terapia();
                terapia.setPaciente(paciente);
                terapia.setProfesional(profesional);
                // Convertir LocalDate a LocalDateTime (usar hora del día, por ejemplo 10:00 AM)
                terapia.setFecha(dia.getFecha().atTime(10, 0).plusHours(i));
                terapia.setServicioDepartamento(servicioDepartamento);
                terapias.add(terapia);
            }
        }

        List<Terapia> savedTerapias = terapiaRepository.saveAll(terapias);
        return savedTerapias.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Métodos de conversión
    private TerapiaDTO convertToDTO(Terapia terapia) {
        Objects.requireNonNull(terapia, "La terapia no puede ser nula");
        Objects.requireNonNull(terapia.getPaciente(), "El paciente de la terapia no puede ser nulo");
        Objects.requireNonNull(terapia.getProfesional(), "El profesional de la terapia no puede ser nulo");
        
        Objects.requireNonNull(terapia.getServicioDepartamento(), "El servicio/departamento de la terapia no puede ser nulo");
        
        TerapiaDTO dto = new TerapiaDTO();
        dto.setId(terapia.getId());
        dto.setPacienteId(terapia.getPaciente().getId());
        dto.setProfesionalId(terapia.getProfesional().getId());
        dto.setFecha(terapia.getFecha());
        dto.setServicioDepartamentoId(terapia.getServicioDepartamento().getId());
        dto.setServicioDepartamentoNombre(terapia.getServicioDepartamento().getNombre());
        dto.setServicioDepartamentoAbreviacion(terapia.getServicioDepartamento().getAbreviacion());
        
        // Información adicional
        dto.setPacienteNombre(terapia.getPaciente().getNombre());
        dto.setPacienteApellido(terapia.getPaciente().getApellido());
        dto.setProfesionalNombre(terapia.getProfesional().getNombre());
        
        return dto;
    }
}
