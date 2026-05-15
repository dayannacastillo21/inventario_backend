# Backend Cafedronel

## Descripción

API REST desarrollada con Spring Boot para gestionar productos, inventario, pedidos, ventas, proveedores y usuarios.

> Estado actual: proyecto en transición a persistencia real (JPA + PostgreSQL) y seguridad JWT.

## Objetivo

Entregar un backend funcional con arquitectura por capas (controller, service, repository, model, dto, exception), convenciones REST coherentes bajo `/api`, validación y seguridad por roles.

## Tecnologías

- Java 17
- Spring Boot 3.5.x
- Maven 3.9+
- Spring Data JPA + Hibernate
- PostgreSQL
- Spring Security
- JWT (jjwt)
- Jakarta Validation
- JUnit 5 y Spring Boot Test

## Estructura del proyecto

```
src/main/java/com/example/backend_cafedronel/
├── config/           # CORS
├── controller/       # API REST
├── dto/              # Cuerpos de petición
├── exception/        # Excepciones y GlobalExceptionHandler
├── mapper/           # Conversión DTO → modelo
├── model/            # Entidades de dominio
├── repository/       # JpaRepository
├── security/         # SecurityConfig + JWT filter/service
└── service/          # Lógica de negocio
```

## Configuración (PostgreSQL + JWT)

Variables configurables (`application.properties`):

- `DB_URL=jdbc:postgresql://localhost:5432/cafedronel`
- `DB_USER=postgres`
- `DB_PASSWORD=postgres`
- `JWT_SECRET=MyUltraSecretKeyForCafedronelJwtSigningKey2026`
- `JWT_EXPIRATION_MS=86400000`

## Cómo ejecutar

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Por defecto la API escucha en `http://localhost:8081`.

## Convenciones HTTP

| Situación              | Código |
|------------------------|--------|
| Lectura exitosa        | 200    |
| Creación exitosa       | 201    |
| Borrado exitoso        | 204    |
| Recurso no encontrado  | 404    |
| Datos inválidos        | 400    |
| Credenciales inválidas | 401    |
| Email duplicado        | 409    |
| Prohibido por rol      | 403    |

## Endpoints principales

### Públicos
- `GET /`
- `GET /api/estado`
- `POST /api/auth/sesiones`

### Productos (CRUD)
- `GET /api/productos`
- `GET /api/productos/{id}`
- `GET /api/productos/categoria/{categoria}`
- `POST /api/productos`
- `PUT /api/productos/{id}`
- `DELETE /api/productos/{id}`

### Seguridad por roles
- `GET /api/user/ping` -> USER o ADMIN
- `GET /api/admin/ping` -> ADMIN

## Ejemplos JSON

### Login
```json
{
  "email": "admin@cafedronel.com",
  "password": "admin123"
}
```

### Login response
```json
{
  "userId": 1,
  "userName": "Admin",
  "email": "admin@cafedronel.com",
  "role": "ADMIN",
  "token": "eyJ..."
}
```

### Crear producto
```json
{
  "nombre": "Capuccino",
  "precio": 12.5,
  "categoria": "bebidas",
  "descripcion": "Cafe con leche"
}
```

## Cómo probar en Postman

1. Hacer `POST /api/auth/sesiones` y copiar `token`.
2. En requests protegidos, agregar header:
   - `Authorization: Bearer <token>`
3. Probar:
   - `GET /api/productos`
   - `GET /api/user/ping`
   - `GET /api/admin/ping`

## Evidencias sugeridas para la rúbrica

1. Login exitoso con token JWT.
2. Acceso a endpoint protegido con token válido (200).
3. Acceso sin token (401).
4. Acceso con rol insuficiente (403).
5. CRUD completo de productos (POST/GET/PUT/DELETE).
6. Error controlado 404 (`ResourceNotFoundException`).

## Nota importante

Si vienes de una versión anterior en memoria, parte de los tests antiguos pueden requerir actualización para adaptarse a repositorios JPA y seguridad JWT.
