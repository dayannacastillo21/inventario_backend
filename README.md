# Backend Cafedronel (Spring Boot 3 + Java 17)

## Requisitos implementados de rúbrica
- Persistencia real con PostgreSQL + JPA/Hibernate.
- CRUD REST con manejo de errores centralizado.
- Consultas JPQL y servicios transaccionales.
- Seguridad Spring Security por roles `ADMIN` y `USER`.
- Autenticación JWT con filtro por request.

## Configuración
Definir variables (o usar defaults):
- `DB_URL=jdbc:postgresql://localhost:5432/cafedronel`
- `DB_USER=postgres`
- `DB_PASSWORD=postgres`
- `JWT_SECRET=MyUltraSecretKeyForCafedronelJwtSigningKey2026`
- `JWT_EXPIRATION_MS=86400000`

## Ejecución
```bash
./mvnw spring-boot:run
```

## Endpoints clave

### Autenticación
- `POST /api/auth/sesiones`
```json
{
  "email": "admin@cafedronel.com",
  "password": "admin123"
}
```
Respuesta:
```json
{
  "userId": 1,
  "userName": "Admin",
  "email": "admin@cafedronel.com",
  "role": "ADMIN",
  "token": "eyJ..."
}
```

### CRUD Productos
- `GET /api/productos`
- `GET /api/productos/{id}`
- `POST /api/productos`
- `PUT /api/productos/{id}`
- `DELETE /api/productos/{id}`

Body ejemplo producto:
```json
{
  "nombre": "Capuccino",
  "precio": 12.5,
  "categoria": "bebidas",
  "descripcion": "Cafe con leche"
}
```

### Rutas protegidas por rol
- `GET /api/user/ping` -> USER o ADMIN
- `GET /api/admin/ping` -> solo ADMIN

Enviar header:
`Authorization: Bearer <token>`

## Evidencias (capturas sugeridas)
1. Login exitoso con token.
2. `GET /api/productos` con token válido.
3. `GET /api/user/ping` con rol USER.
4. `GET /api/admin/ping` con rol USER devolviendo 403.
5. `GET /api/admin/ping` con rol ADMIN devolviendo 200.
6. Error 404 al consultar `/api/productos/{id}` inexistente.
7. CRUD completo (POST, GET, PUT, DELETE) de productos.

