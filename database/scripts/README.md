# Scripts SQL de mantenimiento (PostgreSQL)

Scripts opcionales para **después** de pruebas con Postman o pgAdmin. No reemplazan a Flyway (`src/main/resources/db/migration/`).

## Cuándo usarlos

| Situación | Script |
|-----------|--------|
| Mismo producto repetido (mismo `nombre`, distinto `id`) | `limpiar_productos_duplicados_y_catalogo.sql` |
| IDs con huecos (2, 5, 7, 11…) y quieres 1, 2, 3… | `reordenar_ids_productos.sql` |
| Solo reordenar IDs sin limpiar duplicados | `reordenar_ids_productos.sql` |

## `limpiar_productos_duplicados_y_catalogo.sql`

1. Muestra duplicados por `nombre`.
2. Conserva el **id más bajo** por nombre y elimina el resto.
3. Reasigna `detalle_pedido` y `ventas` si hubiera referencias.
4. Inserta productos del menú (Cappuccino, Latte, Croissant, etc.) solo si no existen.
5. Reordena IDs a 1, 2, 3… (orden: `categoria`, `nombre`).
6. Reinicia la secuencia `productos_id_seq` para que el próximo alta por API sea `MAX(id)+1`.

**Opcional:** descomenta el bloque `DELETE ... Mocaccino Demo APF02%` para quitar productos de prueba de Postman.

## `reordenar_ids_productos.sql`

Solo renumeración:

- Orden final: `categoria`, `nombre`, `id` antiguo.
- Actualiza FK en `detalle_pedido` y `ventas`.
- Usa IDs temporales negativos para evitar choques de clave primaria.
- Deja la secuencia lista para nuevos inserts.

## Ejecución

**pgAdmin:** abrir el archivo → ejecutar (F5).

**PowerShell** (ajusta usuario y base):

```powershell
$env:PGPASSWORD="tu_password"
psql -h localhost -U postgres -d inventario_backenddatabase -f database/scripts/limpiar_productos_duplicados_y_catalogo.sql
psql -h localhost -U postgres -d inventario_backenddatabase -f database/scripts/reordenar_ids_productos.sql
```

## Consulta recomendada en pgAdmin

Para ver el catálogo del 1 al N sin confusión por orden de categoría:

```sql
SELECT id, nombre, precio, categoria
FROM productos
ORDER BY id;
```

## Nota Postman

Tras reordenar, las URLs `PUT /api/productos/{id}` deben usar los **ids nuevos**. Si vuelves a crear muchos POST con el mismo nombre, ejecuta de nuevo el script de limpieza.
