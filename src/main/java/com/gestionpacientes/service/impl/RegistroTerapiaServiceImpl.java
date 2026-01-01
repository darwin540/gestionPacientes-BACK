package com.gestionpacientes.service.impl;

import com.gestionpacientes.dto.RegistroTerapiaItemDTO;
import com.gestionpacientes.dto.RegistroTerapiaRequestDTO;
import com.gestionpacientes.dto.RegistroTerapiaResponseDTO;
import com.gestionpacientes.dto.RegistroTerapiaUpdateDTO;
import com.gestionpacientes.entity.*;
import com.gestionpacientes.exception.DuplicateResourceException;
import com.gestionpacientes.exception.ResourceNotFoundException;
import com.gestionpacientes.mapper.RegistroTerapiaMapper;
import com.gestionpacientes.repository.*;
import com.gestionpacientes.service.RegistroTerapiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistroTerapiaServiceImpl implements RegistroTerapiaService {

    private final RegistroTerapiaServicioRepository registroRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfesionalRepository profesionalRepository;
    private final TipoTerapiaRepository tipoTerapiaRepository;
    private final ServicioRepository servicioRepository;
    private final ProfesionalPacienteRepository profesionalPacienteRepository;
    private final RegistroTerapiaMapper registroMapper;

    @Override
    public List<RegistroTerapiaResponseDTO> crearRegistros(RegistroTerapiaRequestDTO requestDTO, Long profesionalId) {
        // Validar que el profesional existe y cargar tipos de terapia
        Profesional profesional = profesionalRepository.findById(profesionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesional", profesionalId));

        // Forzar carga de tipos de terapia
        profesional.getTiposTerapia().size();

        // Validar que el profesional tiene al menos un tipo de terapia asignado
        if (profesional.getTiposTerapia() == null || profesional.getTiposTerapia().isEmpty()) {
            throw new ResourceNotFoundException("El profesional no tiene tipos de terapia asignados");
        }

        // Obtener el primer tipo de terapia del profesional (se asigna automáticamente)
        TipoTerapia tipoTerapia = profesional.getTiposTerapia().iterator().next();

        // Validar que el paciente existe y pertenece al profesional
        ProfesionalPaciente profesionalPaciente = profesionalPacienteRepository
                .findByProfesionalIdAndPacienteId(profesionalId, requestDTO.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado o no tiene acceso"));

        Paciente paciente = profesionalPaciente.getPaciente();
        List<RegistroTerapiaServicio> registrosCreados = new ArrayList<>();

        // Validar y crear cada registro
        for (RegistroTerapiaItemDTO item : requestDTO.getRegistros()) {
            // Buscar el servicio por abreviatura
            Servicio servicio = servicioRepository.findByAbreviatura(item.getServicioAbreviatura())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Servicio no encontrado con abreviatura: " + item.getServicioAbreviatura()));

            // Validar que no exista un registro duplicado para la misma combinación
            if (registroRepository.findByPacienteAndProfesionalAndTipoTerapiaAndServicioAndFecha(
                    paciente.getId(), profesionalId, tipoTerapia.getId(), servicio.getId(), item.getFecha())
                    .isPresent()) {
                throw new DuplicateResourceException(
                        String.format("Ya existe un registro para este paciente, tipo de terapia, servicio y fecha: %s",
                                item.getFecha()));
            }

            // Crear el registro
            RegistroTerapiaServicio registro = RegistroTerapiaServicio.builder()
                    .paciente(paciente)
                    .profesional(profesional)
                    .tipoTerapia(tipoTerapia)
                    .servicio(servicio)
                    .fecha(item.getFecha())
                    .numeroSesiones(item.getNumeroSesiones())
                    .build();

            registrosCreados.add(registroRepository.save(registro));
        }

        return registrosCreados.stream()
                .map(registroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RegistroTerapiaResponseDTO obtenerRegistroPorId(Long id, Long profesionalId) {
        RegistroTerapiaServicio registro = registroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro", id));

        // Validar que el registro pertenece al profesional
        if (!registro.getProfesional().getId().equals(profesionalId)) {
            throw new ResourceNotFoundException("Registro no encontrado o no tiene acceso");
        }

        return registroMapper.toResponseDTO(registro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistroTerapiaResponseDTO> obtenerRegistrosPorPaciente(Long pacienteId, Long profesionalId) {
        // Validar que el paciente pertenece al profesional
        if (!profesionalPacienteRepository.existsByProfesionalIdAndPacienteId(profesionalId, pacienteId)) {
            throw new ResourceNotFoundException("Paciente no encontrado o no tiene acceso");
        }

        return registroRepository.findByPacienteIdAndProfesionalId(pacienteId, profesionalId)
                .stream()
                .map(registroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RegistroTerapiaResponseDTO actualizarRegistro(Long id, RegistroTerapiaUpdateDTO updateDTO, Long profesionalId) {
        RegistroTerapiaServicio registro = registroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro", id));

        // Validar que el registro pertenece al profesional
        if (!registro.getProfesional().getId().equals(profesionalId)) {
            throw new ResourceNotFoundException("Registro no encontrado o no tiene acceso");
        }

        // El tipo de terapia no se puede actualizar (se mantiene el del profesional)

        // Si se actualiza el servicio, buscar por abreviatura
        if (updateDTO.getServicioAbreviatura() != null && !updateDTO.getServicioAbreviatura().isBlank()) {
            Servicio servicio = servicioRepository.findByAbreviatura(updateDTO.getServicioAbreviatura())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Servicio no encontrado con abreviatura: " + updateDTO.getServicioAbreviatura()));
            registro.setServicio(servicio);
        }

        registroMapper.updateEntityFromDTO(updateDTO, registro);
        RegistroTerapiaServicio registroActualizado = registroRepository.save(registro);
        return registroMapper.toResponseDTO(registroActualizado);
    }

    @Override
    public void eliminarRegistro(Long id, Long profesionalId) {
        RegistroTerapiaServicio registro = registroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro", id));

        // Validar que el registro pertenece al profesional
        if (!registro.getProfesional().getId().equals(profesionalId)) {
            throw new ResourceNotFoundException("Registro no encontrado o no tiene acceso");
        }

        registroRepository.delete(registro);
    }
}

