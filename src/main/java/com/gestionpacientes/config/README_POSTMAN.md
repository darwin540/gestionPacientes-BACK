# Colección de Postman - Gestión de Pacientes API

## Configuración Inicial

### 1. Importar la Colección
1. Abre Postman
2. Haz clic en **Import**
3. Selecciona el archivo `Gestion_Pacientes.postman_collection.json`
4. La colección se importará con todas las carpetas y requests

### 2. Configurar Variables de Entorno
La colección incluye variables predefinidas que puedes configurar:

- **baseUrl**: URL base de tu API (por defecto: `http://localhost:8080`)
- **authToken**: Token JWT que se guarda automáticamente después del login
- **profesionalId**: ID del profesional autenticado

**Para cambiar las variables:**
1. Selecciona la colección en el panel lateral
2. Haz clic en la pestaña **Variables**
3. Modifica los valores según tu entorno

## Estructura de la Colección

### 📁 Autenticación
- **Login Profesional**: Inicia sesión y guarda automáticamente el token JWT

### 📁 Profesionales
- Crear Profesional
- Obtener Todos los Profesionales
- Obtener Profesional por ID
- Actualizar Profesional
- Eliminar Profesional
- Asignar Tipos de Terapia a Profesional
- Quitar Tipos de Terapia de Profesional

### 📁 Pacientes (Requiere Autenticación)
- Crear Paciente
- Obtener Todos los Pacientes (solo del profesional autenticado)
- Obtener Paciente por ID (solo si pertenece al profesional)
- Actualizar Paciente (solo el creador)
- Eliminar Paciente (solo el creador)

### 📁 Tipos de Terapia
- Crear Tipo de Terapia
- Obtener Todos los Tipos de Terapia
- Obtener Tipo de Terapia por ID
- Actualizar Tipo de Terapia
- Eliminar Tipo de Terapia

### 📁 Servicios
- Crear Servicio
- Obtener Todos los Servicios
- Obtener Servicio por ID
- Actualizar Servicio
- Eliminar Servicio

## Flujo de Pruebas Recomendado

### 1. Preparación
```bash
# 1. Asegúrate de que la aplicación esté corriendo
# 2. La base de datos esté configurada y las tablas creadas
```

### 2. Crear Datos Base (sin autenticación)
1. **Crear Tipos de Terapia** - Crea al menos 2-3 tipos de terapia
2. **Crear Servicios** - Crea algunos servicios
3. **Crear Profesionales** - Crea al menos 2 profesionales diferentes

### 3. Autenticación
1. **Login Profesional** - Haz login con uno de los profesionales creados
   - El token se guardará automáticamente en la variable `authToken`
   - El ID del profesional se guardará en `profesionalId`

### 4. Trabajar con Pacientes (Requiere Token)
1. **Crear Pacientes** - Crea pacientes con el profesional autenticado
2. **Obtener Pacientes** - Verifica que solo veas tus pacientes
3. **Actualizar/Eliminar** - Prueba actualizar y eliminar tus pacientes

### 5. Asignar Tipos de Terapia
1. **Asignar Tipos de Terapia** - Asigna tipos de terapia a un profesional
2. **Verificar** - Obtén el profesional para ver los tipos asignados

## Ejemplos de Uso

### Crear un Profesional
```json
POST /api/profesionales
{
    "nombre": "Dr. Carlos Rodríguez",
    "profesion": "Médico General",
    "numeroCuentaBanco": "1234567890",
    "nombreBanco": "Banco de Bogotá",
    "email": "carlos@email.com",
    "username": "carlosrodriguez",
    "password": "password123",
    "tiposTerapiaIds": []
}
```

### Login
```json
POST /api/auth/login
{
    "username": "carlosrodriguez",
    "password": "password123"
}
```

**Respuesta:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tipoToken": "Bearer",
    "profesionalId": 1,
    "nombreProfesional": "Dr. Carlos Rodríguez",
    "email": "carlos@email.com"
}
```

### Crear un Paciente (con token)
```
Headers:
Authorization: Bearer {token}

POST /api/pacientes
{
    "nombre": "Juan",
    "apellido": "Pérez",
    "tipoDocumento": "CC",
    "documento": "1234567890"
}
```

### Crear Tipo de Terapia
```json
POST /api/tipos-terapia
{
    "nombre": "Fisioterapia",
    "valorUnitario": 50000.00
}
```

### Asignar Tipos de Terapia a un Profesional
```json
POST /api/profesionales/1/tipos-terapia
[1, 2, 3]
```

## Notas Importantes

1. **Token JWT**: El token expira después de 24 horas (configurable en `application.properties`)
2. **Aislamiento de Pacientes**: Cada profesional solo puede ver/editar sus propios pacientes
3. **Duplicados**: No se puede crear el mismo paciente dos veces para el mismo profesional
4. **Permisos**: Solo el creador de un paciente puede actualizarlo o eliminarlo

## Solución de Problemas

### Error 401 Unauthorized
- Verifica que hayas hecho login y el token esté guardado
- Revisa que el header `Authorization` tenga el formato correcto: `Bearer {token}`

### Error 403 Forbidden
- Verifica que el paciente pertenezca al profesional autenticado
- Solo el creador puede actualizar/eliminar pacientes

### Error 409 Conflict
- Puede ser un duplicado (email ya existe, paciente ya creado por el profesional, etc.)

### Error 404 Not Found
- Verifica que el ID del recurso exista
- Para pacientes, verifica que pertenezca al profesional autenticado

