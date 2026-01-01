# Guía de Pruebas y Solución de Problemas

## Problema: Error 403 Forbidden

Si estás recibiendo un error 403 al hacer peticiones, sigue estos pasos:

### 1. Verificar que hayas hecho Login primero

**IMPORTANTE**: Antes de hacer cualquier petición a endpoints protegidos, debes hacer login y obtener un token.

**Pasos:**
1. Ejecuta el request "Login Profesional" en Postman
2. Verifica que recibas un código 200 y un token en la respuesta
3. El token se guarda automáticamente en la variable `authToken`

### 2. Verificar el Header de Authorization

En Postman, para los endpoints que requieren autenticación:
- Ve a la pestaña "Headers"
- Asegúrate de que exista el header:
  - **Key**: `Authorization`
  - **Value**: `Bearer {{authToken}}`

**NOTA**: El espacio después de "Bearer" es importante.

### 3. Verificar el Token en Postman

1. Ve a la colección → Variables
2. Verifica que `authToken` tenga un valor (no esté vacío)
3. Si está vacío, ejecuta el login nuevamente

### 4. Flujo Correcto de Pruebas

```
1. POST /api/profesionales (crear profesional)
   {
     "nombre": "Dr. Test",
     "profesion": "Médico",
     "numeroCuentaBanco": "123",
     "nombreBanco": "Test Bank",
     "email": "test@email.com",
     "username": "testuser",
     "password": "password123"
   }

2. POST /api/auth/login
   {
     "username": "testuser",
     "password": "password123"
   }
   → Guarda el token automáticamente

3. POST /api/pacientes (ahora con token)
   Headers: Authorization: Bearer {token}
   {
     "nombre": "Juan",
     "apellido": "Pérez",
     "tipoDocumento": "CC",
     "documento": "1234567890"
   }
```

### 5. Endpoints que NO requieren autenticación

- `POST /api/auth/login`
- `POST /api/profesionales` (crear profesional)
- `GET /api/tipos-terapia/**` (todos los endpoints)
- `GET /api/servicios/**` (todos los endpoints)

### 6. Endpoints que SÍ requieren autenticación

- `GET /api/profesionales/{id}`
- `PUT /api/profesionales/{id}`
- `DELETE /api/profesionales/{id}`
- `POST /api/profesionales/{id}/tipos-terapia`
- **TODOS** los endpoints de `/api/pacientes/**`

### 7. Verificar Logs del Servidor

Si sigues teniendo problemas, revisa los logs del servidor:
- Busca mensajes de error relacionados con JWT
- Verifica que el token no esté expirado (válido por 24 horas por defecto)

### 8. Errores Comunes

**Error 403 después de hacer login:**
- El token no se guardó correctamente
- El header Authorization no está bien formado
- El token expiró (haz login nuevamente)

**Error 401 Unauthorized:**
- No enviaste el header Authorization
- El token es inválido o corrupto
- El token expiró

**Token inválido:**
- El token puede haber expirado (24 horas)
- El token puede estar mal formado
- Solución: Haz login nuevamente

## Debugging

### Habilitar Logs Detallados

Agrega esto a tu `application.properties`:

```properties
logging.level.com.gestionpacientes.security=DEBUG
logging.level.org.springframework.security=DEBUG
```

Esto te mostrará información detallada sobre el proceso de autenticación.

### Verificar Token Manualmente

Si quieres verificar el token manualmente, puedes usar JWT.io:
1. Copia el token de la respuesta del login
2. Ve a https://jwt.io
3. Pega el token
4. Verifica que tenga la estructura correcta

## Solución Rápida

Si nada funciona:

1. **Reinicia la aplicación**
2. **Crea un nuevo profesional**
3. **Haz login con ese profesional**
4. **Usa el token inmediatamente** (no esperes mucho tiempo)

El token se guarda automáticamente en Postman después del login, pero si cierras Postman o cambias de entorno, necesitarás hacer login nuevamente.

