# Backend - Sistema de Gestión de Pacientes

## Descripción
Backend para el sistema de gestión de pacientes desarrollado con **Spring Boot**, **Maven** y **PostgreSQL**.

## Tecnologías

- **Java 21** (LTS)
- **Spring Boot 3.3.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**

## Estructura del Proyecto

```
BACKEND/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/gestionpacientes/
│   │   │       ├── model/              # Entidades JPA
│   │   │       │   ├── Profesional.java
│   │   │       │   ├── Paciente.java
│   │   │       │   └── Terapia.java
│   │   │       ├── dto/                # Data Transfer Objects
│   │   │       │   ├── ProfesionalDTO.java
│   │   │       │   ├── PacienteDTO.java
│   │   │       │   └── TerapiaDTO.java
│   │   │       ├── repository/         # Repositorios JPA
│   │   │       │   ├── ProfesionalRepository.java
│   │   │       │   ├── PacienteRepository.java
│   │   │       │   └── TerapiaRepository.java
│   │   │       ├── service/            # Capa de Servicios
│   │   │       │   ├── ProfesionalService.java
│   │   │       │   ├── PacienteService.java
│   │   │       │   └── TerapiaService.java
│   │   │       ├── controller/         # Controladores REST
│   │   │       │   ├── ProfesionalController.java
│   │   │       │   ├── PacienteController.java
│   │   │       │   └── TerapiaController.java
│   │   │       ├── exception/          # Manejo de excepciones
│   │   │       │   ├── ResourceNotFoundException.java
│   │   │       │   ├── DuplicateResourceException.java
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       ├── config/             # Configuraciones
│   │   │       │   └── CorsConfig.java
│   │   │       └── GestionPacientesApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
├── pom.xml
└── README.md
```

## Modelos de Datos

### Profesional
- **id**: Identificador único (Long, autoincremental)
- **nombre**: Nombre del profesional (obligatorio)
- **nombreUsuario**: Nombre de usuario único (obligatorio)
- **profesion**: Profesión del profesional (obligatorio)
- **tipoTerapia**: Tipo de terapia que ofrece (obligatorio)
- **valorPorTerapia**: Valor/costo por sesión de terapia (BigDecimal, obligatorio)
- **createdAt**: Fecha de creación (automático)
- **updatedAt**: Fecha de última actualización (automático)
- **terapias**: Lista de terapias realizadas (relación OneToMany)

### Paciente
- **id**: Identificador único (Long, autoincremental)
- **nombre**: Nombre del paciente (obligatorio)
- **apellido**: Apellido del paciente (obligatorio)
- **tipoDocumento**: Tipo de documento (obligatorio)
- **numeroDocumento**: Número de documento único (obligatorio)
- **createdAt**: Fecha de creación (automático)
- **updatedAt**: Fecha de última actualización (automático)
- **terapias**: Lista de terapias recibidas (relación OneToMany)

### Terapia
- **id**: Identificador único (Long, autoincremental)
- **paciente**: Referencia al paciente (relación ManyToOne, obligatorio)
- **profesional**: Referencia al profesional (relación ManyToOne, obligatorio)
- **fecha**: Fecha y hora en que se realizó la terapia (obligatorio)
- **servicio**: Servicio o departamento donde fue atendido (obligatorio)
- **createdAt**: Fecha de creación (automático)
- **updatedAt**: Fecha de última actualización (automático)

**Relaciones:**
- Una Terapia pertenece a un Paciente (ManyToOne)
- Una Terapia pertenece a un Profesional (ManyToOne)
- Un Paciente puede tener múltiples Terapias (OneToMany)
- Un Profesional puede tener múltiples Terapias (OneToMany)

## Configuración

### Requisitos Previos

- Java 21 o superior (LTS recomendado)
- Maven 3.6+
- PostgreSQL 12+

### Base de Datos

1. Crear la base de datos en PostgreSQL:
```sql
CREATE DATABASE gestion_pacientes;
```

2. Configurar las credenciales en `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gestion_pacientes
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### Instalación y Ejecución

1. **Clonar e instalar dependencias:**
```bash
cd BACKEND
mvn clean install
```

2. **Ejecutar la aplicación:**
```bash
mvn spring-boot:run
```

O compilar y ejecutar el JAR:
```bash
mvn clean package
java -jar target/gestion-pacientes-backend-1.0.0.jar
```

3. **Ejecutar con perfil específico:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Configuración de Hibernate

La aplicación está configurada para usar `spring.jpa.hibernate.ddl-auto=update`, lo que significa que:
- En desarrollo: Las tablas se crearán/actualizarán automáticamente
- En producción: Cambiar a `validate` o `none` para mayor seguridad

## Validaciones

Todas las entidades incluyen validaciones:
- Campos obligatorios con `@NotBlank` y `@NotNull`
- Validación de valores positivos para `valorPorTerapia`
- Campos únicos para `nombreUsuario` y `numeroDocumento`

## API REST Endpoints

La aplicación expone los siguientes endpoints REST:

### Profesionales

- `GET /api/profesionales` - Obtener todos los profesionales
- `GET /api/profesionales/{id}` - Obtener profesional por ID
- `GET /api/profesionales/usuario/{nombreUsuario}` - Obtener profesional por nombre de usuario
- `POST /api/profesionales` - Crear nuevo profesional
- `PUT /api/profesionales/{id}` - Actualizar profesional
- `DELETE /api/profesionales/{id}` - Eliminar profesional

### Pacientes

- `GET /api/pacientes` - Obtener todos los pacientes
- `GET /api/pacientes/{id}` - Obtener paciente por ID
- `GET /api/pacientes/documento/{numeroDocumento}` - Obtener paciente por número de documento
- `POST /api/pacientes` - Crear nuevo paciente
- `PUT /api/pacientes/{id}` - Actualizar paciente
- `DELETE /api/pacientes/{id}` - Eliminar paciente

### Terapias

- `GET /api/terapias` - Obtener todas las terapias
- `GET /api/terapias/{id}` - Obtener terapia por ID
- `GET /api/terapias/paciente/{pacienteId}` - Obtener terapias por paciente
- `GET /api/terapias/profesional/{profesionalId}` - Obtener terapias por profesional
- `POST /api/terapias` - Crear nueva terapia
- `PUT /api/terapias/{id}` - Actualizar terapia
- `DELETE /api/terapias/{id}` - Eliminar terapia

### Ejemplos de Uso

#### Crear un Profesional
```bash
POST /api/profesionales
Content-Type: application/json

{
  "nombre": "Dr. Juan Pérez",
  "nombreUsuario": "jperez",
  "profesion": "Psicólogo",
  "tipoTerapia": "Terapia Cognitivo-Conductual",
  "valorPorTerapia": 50000.00
}
```

#### Crear un Paciente
```bash
POST /api/pacientes
Content-Type: application/json

{
  "nombre": "María",
  "apellido": "González",
  "tipoDocumento": "DNI",
  "numeroDocumento": "12345678"
}
```

#### Crear una Terapia
```bash
POST /api/terapias
Content-Type: application/json

{
  "pacienteId": 1,
  "profesionalId": 1,
  "fecha": "2024-01-15T10:00:00",
  "servicio": "Departamento de Psicología"
}
```

## Manejo de Errores

La API devuelve respuestas de error estructuradas:

- **404 Not Found**: Recurso no encontrado
- **409 Conflict**: Recurso duplicado (ej: nombre de usuario o número de documento ya existe)
- **400 Bad Request**: Error de validación
- **500 Internal Server Error**: Error interno del servidor

Ejemplo de respuesta de error:
```json
{
  "mensaje": "Profesional con id 999 no encontrado",
  "error": "Recurso no encontrado",
  "status": 404
}
```

## CORS

La aplicación está configurada para aceptar peticiones desde:
- `http://localhost:3000` (React)
- `http://localhost:5173` (Vite)
- `http://localhost:4200` (Angular)

Para cambiar los orígenes permitidos, modificar `CorsConfig.java`.

## Próximos Pasos

- [x] Crear Repositorios (Repository layer)
- [x] Crear Servicios (Service layer)
- [x] Crear Controladores REST (Controller layer)
- [x] Implementar manejo de excepciones
- [x] Agregar DTOs (Data Transfer Objects)
- [x] Configurar CORS
- [ ] Implementar autenticación y autorización
- [ ] Agregar paginación a las listas
- [ ] Implementar filtros y búsqueda avanzada

