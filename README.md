# Backend Cafedronel

## Descripción

API REST desarrollada con Spring Boot para gestionar productos, inventario, pedidos, ventas, proveedores y usuarios. Los datos se mantienen **en memoria** (sin base de datos), pensado para demostración académica y pruebas rápidas.

## Objetivo

Entregar un backend funcional con arquitectura por capas (controlador, servicio, modelo, DTO y manejo centralizado de errores), convenciones REST coherentes bajo el prefijo `/api`, validación de entrada y pruebas automatizadas mínimas pero reales.

## Tecnologías

- Java 17
- Spring Boot 3.5.x
- Maven 3.9+
- Jakarta Validation (Bean Validation)
- JUnit 5 y Spring Boot Test (MockMvc)

## Estructura del proyecto

```
src/main/java/com/example/backend_cafedronel/
├── BackendCafedronelApplication.java
├── config/           # CORS
├── controller/       # API REST
├── dto/              # Cuerpos de petición validados
├── exception/        # Excepciones y GlobalExceptionHandler
├── mapper/           # Conversión DTO → modelo (pedidos)
├── model/            # Entidades de dominio
└── service/          # Lógica de negocio e implementaciones
```

## Requisitos

- JDK 17
- Maven 3.9+ (o usar el wrapper `mvnw` / `mvnw.cmd` del proyecto)

## Cómo ejecutar

```bash
mvn -version
mvn clean test
mvn spring-boot:run
```

Por defecto la API escucha en **http://localhost:8081** (configurable con `PORT` en despliegues como Render).

Comprobación rápida en navegador o cliente HTTP:

- `GET http://localhost:8081/api/estado` — mensaje de salud de la aplicación
- `GET http://localhost:8081/api/productos` — listado de productos semilla

## Convenciones HTTP

| Situación              | Código |
|------------------------|--------|
| Lectura exitosa        | 200    |
| Creación exitosa       | 201    |
| Borrado exitoso        | 204    |
| Recurso no encontrado  | 404    |
| Datos inválidos        | 400    |
| Credenciales inválidas | 401 |
| Email duplicado        | 409    |

Los errores devuelven un cuerpo JSON uniforme (`timestamp`, `status`, `error`, `message`, `fieldErrors` en validación).

## Tabla de endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/estado` | Comprueba que el backend responde |
| GET | `/api/productos` | Lista productos |
| GET | `/api/productos/{id}` | Obtiene un producto |
| GET | `/api/productos/categoria/{categoria}` | Filtra por categoría |
| POST | `/api/productos` | Crea producto (cuerpo `ProductoRequest`, **201**) |
| PUT | `/api/productos/{id}` | Actualiza producto |
| DELETE | `/api/productos/{id}` | Elimina producto (**204**) |
| GET | `/api/pedidos` | Lista pedidos |
| GET | `/api/pedidos/{id}` | Obtiene un pedido |
| GET | `/api/pedidos/{id}/detalles` | Líneas del pedido |
| POST | `/api/pedidos` | Crea pedido (`PedidoCreateRequest`, **201**) |
| PUT | `/api/pedidos/{id}` | Actualiza pedido |
| PUT | `/api/pedidos/{id}/estado?estado=en_proceso` | Cambia estado (`pendiente`, `en_proceso`, `completado`, `cancelado`) |
| DELETE | `/api/pedidos/{id}` | Elimina pedido (**204**) |
| GET | `/api/ventas` | Lista ventas |
| POST | `/api/ventas` | Registra venta (`VentaRequest`, **201**) |
| PUT | `/api/ventas/{id}` | Actualiza venta |
| DELETE | `/api/ventas/{id}` | Elimina venta (**204**) |
| GET | `/api/proveedores` | Lista proveedores |
| POST | `/api/proveedores` | Crea proveedor (`ProveedorRequest`, **201**) |
| PUT | `/api/proveedores/{id}` | Actualiza proveedor |
| DELETE | `/api/proveedores/{id}` | Elimina proveedor (**204**) |
| GET | `/api/inventario` | Lista insumos |
| POST | `/api/inventario` | Alta de insumo (`InventarioRequest`, **201**) |
| PUT | `/api/inventario/{id}` | Actualiza insumo |
| DELETE | `/api/inventario/{id}` | Elimina insumo (**204**) |
| POST | `/api/inventario/{id}/deducciones` | Descuenta stock (`{"unidades":n}`) |
| GET | `/api/usuarios` | Lista usuarios |
| POST | `/api/usuarios` | Registro (`UsuarioRegistroRequest`, **201**) |
| PUT | `/api/usuarios/{id}` | Actualiza usuario (`UsuarioUpdateRequest`) |
| DELETE | `/api/usuarios/{id}` | Elimina usuario (**204**) |
| POST | `/api/auth/sesiones` | Inicio de sesión (`LoginRequest` → `LoginResponse`) |

### Ejemplos de cuerpo (JSON)

**Crear pedido** (`POST /api/pedidos`):

```json
{
  "cliente": "María",
  "detalles": [
    { "productoId": 1, "cantidad": 2 },
    { "productoId": 2, "cantidad": 1 }
  ]
}
```

**Registrar usuario** (`POST /api/usuarios`):

```json
{
  "nombre": "Nuevo",
  "email": "nuevo@cafedronel.com",
  "password": "secreto",
  "rol": "usuario"
}
```

**Sesión** (`POST /api/auth/sesiones`):

```json
{
  "email": "admin@cafedronel.com",
  "password": "admin123"
}
```

Usuario semilla: `admin@cafedronel.com` / `admin123`.

## Evidencia sugerida para la entrega

1. **Build y pruebas**: captura de consola con `mvn clean test` finalizando en **BUILD SUCCESS**.
2. **Servidor en ejecución**: captura de `mvn spring-boot:run` y log de arranque en puerto **8081**.
3. **Endpoints**: capturas de Postman, Thunder Client o navegador contra `GET /api/estado` y `GET /api/productos` (u otros de la tabla).

## Pruebas automatizadas

- `BackendCafedronelApplicationTests`: carga del contexto Spring.
- `ProductoApiTest`: MockMvc sobre listado, detalle **404**, creación **201** y validación **400**.
- `AuthApiTest`: login inválido devuelve **401**.
- `PedidoServiceImplTest`: cálculo de total al crear un pedido (catálogo mockeado).
- `UsuarioServiceImplTest`: registro con email duplicado lanza `DuplicateEmailException`.

Ejecutar todas con:

```bash
mvn clean test
```

## Notas técnicas

- Los precios y totales de **pedidos** y **ventas** se resuelven siempre desde el mismo **catálogo** (`ProductoService`), evitando listas duplicadas inconsistentes.
- Las contraseñas no se serializan en las respuestas JSON de usuario (`@JsonProperty(access = WRITE_ONLY)`).
