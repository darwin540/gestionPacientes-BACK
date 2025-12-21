package com.gestionpacientes.config;

import com.gestionpacientes.constants.DocumentTypeConstants;
import com.gestionpacientes.model.*;
import com.gestionpacientes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final ServicioDepartamentoRepository servicioDepartamentoRepository;
    private final TipoTerapiaRepository tipoTerapiaRepository;
    private final ProfesionalRepository profesionalRepository;
    private final PacienteRepository pacienteRepository;
    private final TerapiaRepository terapiaRepository;

    @Autowired
    public DataInitializer(RoleRepository roleRepository, 
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          TipoDocumentoRepository tipoDocumentoRepository,
                          ServicioDepartamentoRepository servicioDepartamentoRepository,
                          TipoTerapiaRepository tipoTerapiaRepository,
                          ProfesionalRepository profesionalRepository,
                          PacienteRepository pacienteRepository,
                          TerapiaRepository terapiaRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.servicioDepartamentoRepository = servicioDepartamentoRepository;
        this.tipoTerapiaRepository = tipoTerapiaRepository;
        this.profesionalRepository = profesionalRepository;
        this.pacienteRepository = pacienteRepository;
        this.terapiaRepository = terapiaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeAdminUser();
        initializeTestData();
    }

    private void initializeRoles() {
        // Crear rol ADMIN si no existe
        if (!roleRepository.existsByName(Role.ADMIN)) {
            Role adminRole = new Role(Role.ADMIN, "Administrador del sistema con acceso completo");
            roleRepository.save(adminRole);
        }

        // Crear rol PROFESIONAL si no existe
        if (!roleRepository.existsByName(Role.PROFESIONAL)) {
            Role profesionalRole = new Role(Role.PROFESIONAL, "Profesional de la salud que atiende pacientes");
            roleRepository.save(profesionalRole);
        }
    }

    private void initializeAdminUser() {
        // Asignar rol ADMIN
        Role adminRole = roleRepository.findByName(Role.ADMIN)
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado. Asegúrate de que los roles se hayan creado primero."));
        
        // Buscar o crear usuario admin
        User adminUser = userRepository.findByUsername("admin").orElse(null);
        
        if (adminUser == null) {
            // Crear usuario admin si no existe
            adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@gestionpacientes.com");
        }
        
        // Actualizar/establecer contraseña siempre (para asegurar que esté correcta)
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setNombreCompleto("Administrador del Sistema");
        adminUser.setActivo(true);

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        adminUser.setRoles(roles);

        userRepository.save(adminUser);
    }

    private void initializeTestData() {
        initializeTiposDocumento();
        initializeServiciosDepartamentos();
        initializeTiposTerapia();
        initializeProfesionales();
        initializePacientes();
        initializeTerapias();
    }

    private void initializeTiposDocumento() {
        if (tipoDocumentoRepository.count() == 0) {
            // Usar constantes para los tipos de documento
            TipoDocumento td1 = new TipoDocumento(DocumentTypeConstants.CC, DocumentTypeConstants.CC_DESCRIPTION);
            td1.setActivo(true);
            tipoDocumentoRepository.save(td1);
            
            TipoDocumento td2 = new TipoDocumento(DocumentTypeConstants.CN, DocumentTypeConstants.CN_DESCRIPTION);
            td2.setActivo(true);
            tipoDocumentoRepository.save(td2);
            
            TipoDocumento td3 = new TipoDocumento(DocumentTypeConstants.NN, DocumentTypeConstants.NN_DESCRIPTION);
            td3.setActivo(true);
            tipoDocumentoRepository.save(td3);
            
            TipoDocumento td4 = new TipoDocumento(DocumentTypeConstants.TI, DocumentTypeConstants.TI_DESCRIPTION);
            td4.setActivo(true);
            tipoDocumentoRepository.save(td4);
            
            TipoDocumento td5 = new TipoDocumento(DocumentTypeConstants.AS, DocumentTypeConstants.AS_DESCRIPTION);
            td5.setActivo(true);
            tipoDocumentoRepository.save(td5);
        }
    }

    private void initializeServiciosDepartamentos() {
        if (servicioDepartamentoRepository.count() == 0) {
            ServicioDepartamento sd1 = new ServicioDepartamento("FISIO", "Fisioterapia");
            sd1.setActivo(true);
            servicioDepartamentoRepository.save(sd1);
            
            ServicioDepartamento sd2 = new ServicioDepartamento("PSICO", "Psicología");
            sd2.setActivo(true);
            servicioDepartamentoRepository.save(sd2);
            
            ServicioDepartamento sd3 = new ServicioDepartamento("TERAP", "Terapia Ocupacional");
            sd3.setActivo(true);
            servicioDepartamentoRepository.save(sd3);
            
            ServicioDepartamento sd4 = new ServicioDepartamento("FONO", "Fonoaudiología");
            sd4.setActivo(true);
            servicioDepartamentoRepository.save(sd4);
            
            ServicioDepartamento sd5 = new ServicioDepartamento("NUTRI", "Nutrición");
            sd5.setActivo(true);
            servicioDepartamentoRepository.save(sd5);
        }
    }

    private void initializeTiposTerapia() {
        if (tipoTerapiaRepository.count() == 0) {
            tipoTerapiaRepository.save(new TipoTerapia("Terapia Física", new BigDecimal("50000.00"), true));
            tipoTerapiaRepository.save(new TipoTerapia("Terapia Psicológica", new BigDecimal("60000.00"), true));
            tipoTerapiaRepository.save(new TipoTerapia("Terapia Ocupacional", new BigDecimal("55000.00"), true));
            tipoTerapiaRepository.save(new TipoTerapia("Terapia del Lenguaje", new BigDecimal("45000.00"), true));
            tipoTerapiaRepository.save(new TipoTerapia("Consulta Nutricional", new BigDecimal("40000.00"), true));
        }
    }

    private void initializeProfesionales() {
        if (profesionalRepository.count() == 0) {
            String password = passwordEncoder.encode("user123");
            
            profesionalRepository.save(new Profesional("María", "González", "maria.gonzalez", password, "Fisioterapeuta", "Terapia Física"));
            profesionalRepository.save(new Profesional("Carlos", "Rodríguez", "carlos.rodriguez", password, "Psicólogo", "Terapia Psicológica"));
            profesionalRepository.save(new Profesional("Ana", "Martínez", "ana.martinez", password, "Terapeuta Ocupacional", "Terapia Ocupacional"));
            profesionalRepository.save(new Profesional("Luis", "Fernández", "luis.fernandez", password, "Fonoaudiólogo", "Terapia del Lenguaje"));
            profesionalRepository.save(new Profesional("Laura", "Sánchez", "laura.sanchez", password, "Nutricionista", "Consulta Nutricional"));
        }
    }

    private void initializePacientes() {
        if (pacienteRepository.count() == 0) {
            // Usar constantes para buscar el tipo de documento
            TipoDocumento cedula = tipoDocumentoRepository.findByNombre(DocumentTypeConstants.CC)
                    .orElseThrow(() -> new RuntimeException("Tipo de documento '" + DocumentTypeConstants.CC + "' no encontrado"));
            
            pacienteRepository.save(new Paciente("Juan", "Pérez", cedula, "1234567890"));
            pacienteRepository.save(new Paciente("María", "López", cedula, "0987654321"));
            pacienteRepository.save(new Paciente("Pedro", "García", cedula, "1122334455"));
            pacienteRepository.save(new Paciente("Sofía", "Hernández", cedula, "5566778899"));
            pacienteRepository.save(new Paciente("Diego", "Torres", cedula, "9988776655"));
        }
    }

    private void initializeTerapias() {
        if (terapiaRepository.count() == 0) {
            // Obtener pacientes
            Paciente juan = pacienteRepository.findByNumeroDocumento("1234567890")
                    .orElseThrow(() -> new RuntimeException("Paciente Juan Pérez no encontrado"));
            Paciente maria = pacienteRepository.findByNumeroDocumento("0987654321")
                    .orElseThrow(() -> new RuntimeException("Paciente María López no encontrado"));
            Paciente pedro = pacienteRepository.findByNumeroDocumento("1122334455")
                    .orElseThrow(() -> new RuntimeException("Paciente Pedro García no encontrado"));
            Paciente sofia = pacienteRepository.findByNumeroDocumento("5566778899")
                    .orElseThrow(() -> new RuntimeException("Paciente Sofía Hernández no encontrado"));
            Paciente diego = pacienteRepository.findByNumeroDocumento("9988776655")
                    .orElseThrow(() -> new RuntimeException("Paciente Diego Torres no encontrado"));

            // Obtener profesionales
            Profesional mariaGonzalez = profesionalRepository.findByNombreUsuario("maria.gonzalez")
                    .orElseThrow(() -> new RuntimeException("Profesional María González no encontrado"));
            Profesional carlosRodriguez = profesionalRepository.findByNombreUsuario("carlos.rodriguez")
                    .orElseThrow(() -> new RuntimeException("Profesional Carlos Rodríguez no encontrado"));
            Profesional anaMartinez = profesionalRepository.findByNombreUsuario("ana.martinez")
                    .orElseThrow(() -> new RuntimeException("Profesional Ana Martínez no encontrado"));
            Profesional luisFernandez = profesionalRepository.findByNombreUsuario("luis.fernandez")
                    .orElseThrow(() -> new RuntimeException("Profesional Luis Fernández no encontrado"));
            Profesional lauraSanchez = profesionalRepository.findByNombreUsuario("laura.sanchez")
                    .orElseThrow(() -> new RuntimeException("Profesional Laura Sánchez no encontrado"));

            // Obtener servicios/departamentos
            ServicioDepartamento fisio = servicioDepartamentoRepository.findByAbreviacion("FISIO")
                    .orElseThrow(() -> new RuntimeException("Servicio FISIO no encontrado"));
            ServicioDepartamento psico = servicioDepartamentoRepository.findByAbreviacion("PSICO")
                    .orElseThrow(() -> new RuntimeException("Servicio PSICO no encontrado"));
            ServicioDepartamento terap = servicioDepartamentoRepository.findByAbreviacion("TERAP")
                    .orElseThrow(() -> new RuntimeException("Servicio TERAP no encontrado"));
            ServicioDepartamento fono = servicioDepartamentoRepository.findByAbreviacion("FONO")
                    .orElseThrow(() -> new RuntimeException("Servicio FONO no encontrado"));
            ServicioDepartamento nutri = servicioDepartamentoRepository.findByAbreviacion("NUTRI")
                    .orElseThrow(() -> new RuntimeException("Servicio NUTRI no encontrado"));

            // Crear terapias
            terapiaRepository.save(new Terapia(juan, mariaGonzalez, LocalDateTime.of(2024, 1, 15, 10, 0), fisio));
            terapiaRepository.save(new Terapia(maria, carlosRodriguez, LocalDateTime.of(2024, 1, 16, 14, 30), psico));
            terapiaRepository.save(new Terapia(pedro, anaMartinez, LocalDateTime.of(2024, 1, 17, 9, 0), terap));
            terapiaRepository.save(new Terapia(sofia, luisFernandez, LocalDateTime.of(2024, 1, 18, 11, 0), fono));
            terapiaRepository.save(new Terapia(diego, lauraSanchez, LocalDateTime.of(2024, 1, 19, 15, 0), nutri));
        }
    }
}

