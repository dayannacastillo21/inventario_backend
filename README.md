# Backend Cafedronel

API REST con Spring Boot para gestionar productos, inventario, pedidos, ventas, proveedores y usuarios de Cafedronel.

La rama oficial es `master`. Esta version conserva la linea productiva del proyecto: persistencia con PostgreSQL, migraciones con Flyway, autenticacion JWT y autorizacion por roles.

## Tecnologias

- Java 17
- Spring Boot 3.5.x
- Maven
- Spring Web
- Spring Data JPA
- Flyway
- PostgreSQL
- Spring Security
- JWT con `jjwt`
- Jakarta Validation
- JUnit 5, MockMvc, H2 y Spring Security Test

## Estructura

```text
src/main/java/com/example/backend_cafedronel/
|-- config/           # Configuracion transversal, como CORS
|-- controller/       # Endpoints REST
|-- dto/              # Objetos de entrada y salida
|-- exception/        # Excepciones y manejador global de errores
|-- mapper/           # Conversion entre DTO y modelo
|-- model/            # Modelos y entidades de dominio
|-- repository/       # Repositorios JPA
|-- security/         # Configuracion de seguridad y JWT
`-- service/          # Logica de negocio
```

## Configuracion

La aplicacion requiere variables de entorno para valores sensibles. No se deben versionar secretos reales.

| Variable | Descripcion | Requerida | Valor por defecto |
|----------|-------------|-----------|-------------------|
| `PORT` | Puerto HTTP | No | `8081` |
| `DB_URL` | URL JDBC de PostgreSQL | Si | - |
| `DB_USER` | Usuario de base de datos | Si | - |
| `DB_PASSWORD` | Password de base de datos | Si | - |
| `JWT_SECRET` | Clave de firma JWT, minimo 32 bytes | Si | - |
| `JWT_EXPIRATION_MS` | Duracion del token | No | `86400000` |
| `JPA_DDL_AUTO` | Validacion/gestion Hibernate | No | `validate` |
| `JPA_SHOW_SQL` | Mostrar SQL en logs | No | `false` |
| `CORS_ALLOWED_ORIGINS` | Origenes permitidos separados por coma | No | localhost dev |

Flyway ejecuta las migraciones en `src/main/resources/db/migration`.

## Ejecucion local

Configura las variables requeridas y ejecuta:

```bash
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8081`, salvo que `PORT` indique otro puerto.

## Endpoints publicos

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| `GET` | `/` | Mensaje base de la API |
| `GET` | `/api/estado` | Verificacion de estado |
| `POST` | `/api/auth/sesiones` | Inicio de sesion y generacion de token JWT |
| `POST` | `/api/usuarios` | Registro publico de usuario con rol `USER` |

El resto de endpoints requiere token JWT. Las rutas `/api/admin/**` y `/api/usuarios/**` requieren rol `ADMIN`, excepto el registro publico.

## Autenticacion

Solicitud:

```http
POST /api/auth/sesiones
Content-Type: application/json
```

```json
{
  "email": "usuario@cafedronel.com",
  "password": "password-seguro"
}
```

Respuesta:

```json
{
  "userId": 1,
  "userName": "Usuario",
  "email": "usuario@cafedronel.com",
  "role": "USER",
  "token": "eyJ..."
}
```

Enviar el token en rutas protegidas:

```http
Authorization: Bearer <token>
```

## Pruebas

```bash
.\mvnw.cmd clean test
.\mvnw.cmd -DskipTests package
```

Las pruebas usan H2 en memoria y configuracion separada en `src/test/resources/application.properties`.

## Despliegue

El `Dockerfile` construye el JAR con Maven y ejecuta la aplicacion con Java 17. En plataformas como Render, define al menos:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `CORS_ALLOWED_ORIGINS`

## Notas de seguridad

- No hay secretos reales versionados.
- Las contrasenas se almacenan con BCrypt.
- El registro publico siempre crea usuarios con rol `USER`.
- La asignacion de roles privilegiados debe hacerse por un administrador o por un proceso operativo controlado.
- CORS se configura de forma centralizada; no usar `@CrossOrigin("*")` en controladores.
