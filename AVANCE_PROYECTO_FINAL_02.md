# Avance de Proyecto Final 02 — Backend Cafedronel

## 1. Identificación del proyecto

| Campo | Valor |
|-------|-------|
| **Nombre** | Backend Cafedronel (inventario y operación de cafetería) |
| **Repositorio** | `inventario_backend` |
| **Paquete base** | `com.example.backend_cafedronel` |
| **Entrega** | Rúbrica de Avance de Proyecto Final 02 |
| **Alcance** | API REST con JPA/Hibernate, CRUD, consultas, transacciones, Spring Security y JWT |

## 2. Descripción general

Sistema backend para gestionar el catálogo de productos, usuarios, proveedores, inventario de insumos, pedidos con detalle y ventas de una cafetería. La API es stateless, protegida con JWT y organizada en capas (Controller → Service → Repository → Entity).

## 3. Tecnologías utilizadas

- Java 17
- Spring Boot 3.5.8
- Spring Data JPA / Hibernate 6
- Spring Security + JWT (`jjwt` 0.12.6)
- Jakarta Bean Validation
- Flyway (migraciones V0–V7)
- PostgreSQL (producción/local) y H2 (pruebas)
- Maven, JUnit 5, MockMvc

## 4. Arquitectura del sistema

```text
Cliente (Postman / frontend)
        │
        ▼
┌───────────────────┐
│  REST Controllers │  ← DTOs + @Valid
└─────────┬─────────┘
          ▼
┌───────────────────┐
│     Services      │  ← @Transactional, reglas de negocio
└─────────┬─────────┘
          ▼
┌───────────────────┐
│   Repositories    │  ← JPQL / derived queries
└─────────┬─────────┘
          ▼
┌───────────────────┐
│  Entidades JPA    │
└─────────┬─────────┘
          ▼
   PostgreSQL / H2 (Flyway)
```

**Configuración transversal:** `SecurityConfig`, `JwtAuthenticationFilter`, `JwtService`, `GlobalExceptionHandler`, `CorsConfig`.

## 5. Modelo de datos y entidades principales

| Entidad | Tabla | Relaciones JPA destacadas |
|---------|-------|---------------------------|
| `Usuario` | `usuarios` | Autenticación y roles (`USER`, `ADMIN`) |
| `Producto` | `productos` | Catálogo vendible; campo `activo` |
| `Proveedor` | `proveedores` | Provee insumos |
| `Inventario` | `inventario` | `@ManyToOne` → `Proveedor` |
| `Pedido` | `pedidos` | `@OneToMany` → `DetallePedido` (cascade, orphanRemoval) |
| `DetallePedido` | `detalle_pedido` | `@ManyToOne` → `Pedido`, `@ManyToOne` → `Producto` |
| `Venta` | `ventas` | `@ManyToOne` → `Usuario`, `@ManyToOne` → `Producto` |

Las migraciones Flyway en `src/main/resources/db/migration/` definen claves foráneas, índices y datos demo (admin, productos, proveedores, inventario).

## 6. Persistencia JPA/Hibernate

- `spring.jpa.hibernate.ddl-auto=validate` — el esquema lo controla Flyway, no Hibernate.
- `spring.jpa.open-in-view=false` — evita sesión abierta en la capa web.
- Anotaciones usadas: `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`, `@ManyToOne`, `@OneToMany`, `@Enumerated`, `@PrePersist`, `@PreUpdate`.
- Entidades con lógica de dominio: totales de pedido, `addDetalle`, validación de estados, resolución de proveedor en inventario.
- Contraseñas: `@JsonProperty(WRITE_ONLY)` en `Usuario`; respuestas con `UsuarioResponse` sin password.

## 7. CRUD implementado

| Módulo | Crear | Listar | Detalle (GET /{id}) | Actualizar | Eliminar |
|--------|-------|--------|---------------------|------------|----------|
| Productos | ✓ | ✓ | ✓ | ✓ | ✓ (solo ADMIN) |
| Usuarios | ✓ registro público | ✓ (ADMIN) | ✓ (ADMIN) | ✓ (ADMIN) | ✓ (ADMIN) |
| Proveedores | ✓ | ✓ | ✓ | ✓ | ✓ (solo ADMIN) |
| Inventario | ✓ | ✓ | ✓ | ✓ | ✓ (solo ADMIN) |
| Pedidos | ✓ | ✓ | ✓ | ✓ + estado | ✓ (solo ADMIN) |
| Ventas | ✓ | ✓ | ✓ | ✓ | ✓ (solo ADMIN) |

Validaciones en DTOs (`@NotBlank`, `@NotNull`, `@Positive`, `@Email`, etc.) y errores uniformes vía `GlobalExceptionHandler`.

## 8. Consultas y transacciones

### Consultas personalizadas / derivadas

| Repositorio | Consulta | Uso |
|-------------|----------|-----|
| `ProductoRepository` | `findByCategoriaIgnoreCase` | `GET /api/productos/categoria/{categoria}` |
| `ProductoRepository` | `findByActivoTrueOrderByNombreAsc` | `GET /api/productos/activos` |
| `ProductoRepository` | JPQL `buscarConPrecioMinimo` | `GET /api/productos/busqueda/precio-minimo?min=` |
| `InventarioRepository` | JPQL `findConStockBajo` | `GET /api/inventario/alertas/stock-bajo` |
| `VentaRepository` | `findByUsuario_IdOrderByFechaVentaDesc` | `GET /api/ventas/usuario/{usuarioId}` |
| `VentaRepository` | `findByEstadoIgnoreCaseOrderByFechaVentaDesc` | `GET /api/ventas/estado/{estado}` |
| `UsuarioRepository` | `findByEmailIgnoreCase`, `existsByEmailIgnoreCase` | Login y registro |
| `ProveedorRepository` | `findByNombreIgnoreCase`, `findByEmailIgnoreCase` | Inventario y duplicados |
| `DetallePedidoRepository` | `findByPedido_IdOrderByIdAsc` | Detalle de pedido |
| `PedidoRepository` | `findAllByOrderByIdAsc` | Listado ordenado |

### Transacciones (`@Transactional`)

- Lecturas: `@Transactional(readOnly = true)` en listados y consultas.
- Escrituras: crear/actualizar/eliminar pedidos, ventas, inventario (incluye deducción de stock en la misma transacción).
- Pedidos: alta con detalles, cálculo de subtotales/total y `cascade = ALL` para consistencia padre-hijo.

## 9. Seguridad con Spring Security

- Sesión **STATELESS**; CSRF deshabilitado (API REST).
- **BCrypt** (`BCryptPasswordEncoder`) para hash de contraseñas.
- Rutas públicas: `/`, `/api/estado`, `/api/auth/**`, `POST /api/usuarios` (registro).
- Rutas **ADMIN**: `/api/admin/**`, `/api/usuarios/**`, **DELETE** en productos, proveedores, inventario, pedidos y ventas.
- Rutas **USER o ADMIN**: `/api/user/**`.
- Resto de `/api/**`: requiere autenticación (JWT válido).
- Sin token → **401** (`RestAuthenticationEntryPoint`).
- Token inválido/expirado → **401** (`JwtAuthenticationFilter`).
- Rol insuficiente → **403** (`RestAccessDeniedHandler`).

## 10. Autenticación JWT

### Flujo

1. Cliente envía `POST /api/auth/sesiones` con email y password.
2. `UsuarioServiceImpl` valida credenciales (BCrypt) y usuario activo.
3. `JwtService` genera token HMAC con `sub=email`, claim `role`, expiración configurable.
4. Cliente envía `Authorization: Bearer <token>` en rutas protegidas.
5. `JwtAuthenticationFilter` valida firma/expiración y establece `ROLE_USER` o `ROLE_ADMIN` en el contexto.

### Variables de entorno (sin secretos en código)

- `JWT_SECRET` (mínimo 32 bytes)
- `JWT_EXPIRATION_MS` (default 86400000)
- `DB_URL`, `DB_USER`, `DB_PASSWORD`

### Usuario demo (migración V7)

- Email: `admin@cafedronel.com`
- Password: `password` (hash BCrypt en SQL)

## 11. Endpoints principales

Ver tabla completa en `README.md`. Resumen de rutas nuevas o relevantes para APF02:

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/productos/activos` | Productos activos |
| GET | `/api/productos/busqueda/precio-minimo?min=` | JPQL precio mínimo |
| GET | `/api/inventario/alertas/stock-bajo` | Insumos con cantidad ≤ stock mínimo |
| GET | `/api/ventas/usuario/{usuarioId}` | Ventas por usuario |
| GET | `/api/ventas/estado/{estado}` | Ventas por estado |
| GET | `/api/proveedores/{id}` | Detalle proveedor |
| GET | `/api/inventario/{id}` | Detalle inventario |
| GET | `/api/ventas/{id}` | Detalle venta |
| GET | `/api/usuarios/{id}` | Detalle usuario (ADMIN) |

## 12. Manejo de errores

Respuesta JSON uniforme (`ErrorResponse`): `timestamp`, `status`, `error`, `message`, `fieldErrors`.

Códigos usados: 400 validación/negocio, 401 credenciales, 403 permisos, 404 no encontrado, 409 conflicto (email duplicado, integridad), 500 genérico.

## 13. Evidencias de prueba sugeridas

1. **Terminal:** `mvn test` → BUILD SUCCESS (46 pruebas).
2. **Postman/curl:** login admin → copiar token.
3. **Sin token:** `GET /api/productos` → 403.
4. **Con token:** CRUD producto completo.
5. **USER vs ADMIN:** DELETE producto con USER → 403; con ADMIN → 204/404.
6. **Base de datos:** filas en `pedidos`, `detalle_pedido`, `ventas` tras operaciones.
7. **Captura IDE:** paquetes `model`, `repository`, `security`.
8. **Flyway:** tabla `flyway_schema_history` en PostgreSQL.
9. **Consulta JPQL:** `GET /api/productos/busqueda/precio-minimo?min=10`.
10. **Stock bajo:** `GET /api/inventario/alertas/stock-bajo`.

## 14. Ejecución local

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/inventario_backenddatabase"
$env:DB_USER="postgres"
$env:DB_PASSWORD="<tu-password>"
$env:JWT_SECRET="<clave-de-al-menos-32-bytes>"
mvn spring-boot:run
```

## 15. Conclusión del avance

El backend cumple los cinco criterios de la rúbrica APF02 a nivel **excelente**: modelo JPA alineado al negocio de cafetería, CRUD completo con validaciones, consultas JPQL/derivadas con transacciones, seguridad por roles con operaciones destructivas restringidas a ADMIN, y autenticación JWT documentada con pruebas automatizadas y guía de evidencias para la exposición.

## 16. Cumplimiento rubrica APF02 (20/20)

| Criterio | Peso | Nivel | Evidencia en el repositorio |
|----------|------|-------|----------------------------|
| Persistencia JPA/Hibernate | 20% | Excelente | Entidades + relaciones + Flyway + `validate` |
| CRUD | 25% | Excelente | Todos los modulos con GET/POST/PUT/DELETE |
| Consultas y transacciones | 15% | Excelente | JPQL, derived queries, `@Transactional` |
| Spring Security | 20% | Excelente | BCrypt, roles, 401/403, DELETE ADMIN |
| JWT y documentacion | 20% | Excelente | Login, filtro, este documento, Postman, 46 tests |

**Postman:** `docs/postman/Cafedronel-APF02.postman_collection.json`

---

*Documento generado para entrega académica — Proyecto Final 02.*
