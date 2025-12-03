package com.gestionpacientes.config;

import com.gestionpacientes.model.Role;
import com.gestionpacientes.model.User;
import com.gestionpacientes.repository.RoleRepository;
import com.gestionpacientes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(RoleRepository roleRepository, 
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeAdminUser();
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
}

