# Checklist APF02 — Sacar 20/20

Marca cada item antes de entregar al profesor.

## Codigo y pruebas

- [ ] `mvn test` → BUILD SUCCESS (46 pruebas)
- [ ] `mvn -DskipTests package` genera el JAR
- [ ] App arranca con variables `DB_*` y `JWT_SECRET`

## Rúbrica tecnica

- [ ] Entidades JPA completas con relaciones
- [ ] CRUD completo (incluye GET por id en todos los modulos)
- [ ] Validaciones Bean Validation en DTOs
- [ ] Consultas JPQL/derivadas expuestas en API
- [ ] `@Transactional` en operaciones criticas
- [ ] Spring Security activo (401 sin token, 403 sin rol)
- [ ] DELETE solo ADMIN
- [ ] Login JWT funcional
- [ ] BCrypt en contrasenas
- [ ] Sin secretos en Git

## Documentacion

- [ ] `AVANCE_PROYECTO_FINAL_02.md` revisado
- [ ] `README.md` actualizado
- [ ] Coleccion Postman importada (`docs/postman/Cafedronel-APF02.postman_collection.json`)

## Evidencias (capturas)

- [ ] Terminal `mvn test`
- [ ] Postman login + token
- [ ] CRUD producto o pedido completo
- [ ] 401 sin token y 403 rol incorrecto
- [ ] Consulta stock bajo o precio minimo
- [ ] IDE con estructura de paquetes
- [ ] Base de datos con tablas y datos demo (V7)
