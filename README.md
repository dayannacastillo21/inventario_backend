# Backend Cafedronel

API REST desarrollada con Spring Boot para la gestión de productos, inventario, pedidos, ventas, proveedores y usuarios de Cafedronel.

El proyecto evoluciona desde una versión académica con datos en memoria hacia una arquitectura con persistencia real, seguridad por roles y autenticación basada en JWT.

## Objetivo

Entregar un backend funcional, organizado por capas y preparado para demostrar operaciones CRUD, validación de datos, manejo centralizado de errores, persistencia con PostgreSQL y protección de endpoints mediante Spring Security.

## Tecnologías

- Java 17
- Spring Boot 3.5.x
- Maven
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Spring Security
- JWT con `jjwt`
- Jakarta Validation
- JUnit 5 y Spring Boot Test

## Estructura del proyecto

```text
src/main/java/com/example/backend_cafedronel/
|-- config/           # Configuración transversal, como CORS
|-- controller/       # Endpoints REST
|-- dto/              # Objetos de entrada y salida
|-- exception/        # Excepciones y manejador global de errores
|-- mapper/           # Conversión entre DTO y modelo
|-- model/            # Modelos y entidades de dominio
|-- repository/       # Repositorios JPA
|-- security/         # Configuración de seguridad y JWT
`-- service/          # Lógica de negocio
```

## Configuración

La aplicación toma sus valores principales desde variables de entorno. Si no se definen, usa los valores por defecto indicados en `application.properties`.

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `PORT` | Puerto HTTP de la aplicación | `8081` |
| `DB_URL` | URL JDBC de PostgreSQL | `jdbc:postgresql://localhost:5432/cafedronel` |
| `DB_USER` | Usuario de base de datos | `postgres` |
| `DB_PASSWORD` | Contraseña de base de datos | `postgres` |
| `JWT_SECRET` | Clave usada para firmar tokens JWT | `MyUltraSecretKeyForCafedronelJwtSigningKey2026` |
| `JWT_EXPIRATION_MS` | Duración del token en milisegundos | `86400000` |

## Ejecución

En Windows:

```bash
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

Por defecto, la API queda disponible en:

```text
http://localhost:8081
```

## Endpoints públicos

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/` | Mensaje base de la API |
| `GET` | `/api/estado` | Verificación de estado del backend |
| `POST` | `/api/auth/sesiones` | Inicio de sesión y generación de token JWT |

## Autenticación

Solicitud:

```http
POST /api/auth/sesiones
Content-Type: application/json
```

```json
{
  "email": "admin@cafedronel.com",
  "password": "admin123"
}
```

Respuesta esperada:

```json
{
  "userId": 1,
  "userName": "Admin",
  "email": "admin@cafedronel.com",
  "role": "ADMIN",
  "token": "eyJ..."
}
```

Para consumir rutas protegidas, enviar el token en el encabezado:

```http
Authorization: Bearer <token>
```

## Productos

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/productos` | Lista productos |
| `GET` | `/api/productos/{id}` | Obtiene un producto por id |
| `GET` | `/api/productos/categoria/{categoria}` | Filtra productos por categoría |
| `POST` | `/api/productos` | Crea un producto |
| `PUT` | `/api/productos/{id}` | Actualiza un producto |
| `DELETE` | `/api/productos/{id}` | Elimina un producto |

Ejemplo de creación:

```json
{
  "nombre": "Capuccino",
  "precio": 12.5,
  "categoria": "bebidas",
  "descripcion": "Cafe con leche"
}
```

## Seguridad por roles

| Ruta | Acceso |
|------|--------|
| `/api/user/**` | Usuarios con rol `USER` o `ADMIN` |
| `/api/admin/**` | Solo usuarios con rol `ADMIN` |

Endpoints de comprobación:

- `GET /api/user/ping`
- `GET /api/admin/ping`

## Códigos HTTP

| Situación | Código |
|-----------|--------|
| Operación exitosa | `200` |
| Recurso creado | `201` |
| Eliminación exitosa | `204` |
| Datos inválidos | `400` |
| Credenciales inválidas o token ausente | `401` |
| Acceso no permitido por rol | `403` |
| Recurso no encontrado | `404` |
| Email duplicado | `409` |

## Evidencias sugeridas

1. Login exitoso con token JWT.
2. Acceso a un endpoint protegido con token válido.
3. Acceso sin token devolviendo `401`.
4. Acceso con rol insuficiente devolviendo `403`.
5. CRUD completo de productos.
6. Respuesta `404` al consultar un recurso inexistente.
7. Manejo de validaciones con respuesta `400`.

## Nota técnica

La migración a JPA y seguridad JWT modifica la forma en que se inicializan los servicios. Si se ejecutan pruebas antiguas basadas en datos en memoria, pueden requerir actualización para inyectar repositorios, codificador de contraseñas y servicio JWT.
