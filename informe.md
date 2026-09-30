# Informe de Cambios y Queries

## 1. Cambios estructurales en el modelo

| Archivo | Cambio realizado | Líneas modificadas |
|---|---|---|
| `AlquilerDetalle.java` | Reemplazado el atributo `producto` por una colección `productos` (ManyToMany) y creado join table `alquiler_detalle_productos`. | 47‑53 (definición del atributo), 127‑133 (getters/setters) |
| `MantenimientoDetalle.java` | Reemplazado el atributo `producto` por una colección `productos` (ManyToMany) y creado join table `mantenimiento_detalle_productos`. | 32‑38 (definición del atributo), 72‑78 (getters/setters) |
| `RequestAlquiler.java` (DTO) | Campo `productoId` → `List<Integer> productoIds`. | (actualizado en el archivo de request, líneas aproximadas 15‑20) |
| `ResponseAlquiler.java` (DTO) | Campo `producto` → `List<ResponseProducto> productos`. | (actualizado en el archivo de response, líneas aproximadas 22‑27) |
| `RequestMantenimiento.java` y `ResponseMantenimiento.java` | Cambios análogos a los anteriores para manejar listas de productos. | (líneas equivalentes en cada DTO) |

## 2. Actualizaciones en la capa de servicio

Se modificaron `AlquilerService` y `MantenimientoService` para:
- Cargar la lista de `Producto` a partir de los IDs recibidos.
- Asignar esa lista al atributo `productos` de `AlquilerDetalle` y `MantenimientoDetalle`.
- Ajustar los mapeos de DTO ↔ entidad.

## 3. Repositorios y Queries

### 3.1. Seis consultas JPQL (más de una entidad)

| Repositorio | Método | Descripción | Línea |
|---|---|---|---|
| `AlquilerRepository` | `findAlquileresPorClienteNombreYEstado` | Busca alquileres filtrando por nombre del cliente y estado (alquiler + cliente). | 13‑15 |
| `AlquilerRepository` | `findAlquileresPorModeloEquipo` | Busca alquileres por modelo de equipo (alquiler + detalle + equipo). | 17‑19 |
| `MantenimientoRepository` | `findMantenimientosPorUbicacionEquipo` | Obtiene mantenimientos según ubicación del equipo (mantenimiento + equipo). | 13‑15 |
| `MantenimientoRepository` | `findMantenimientosPorCodigoProducto` | Obtiene mantenimientos que involucren un producto concreto (mantenimiento + detalle + producto). | 17‑19 |
| `EquipoRepository` | `findEquiposPorTipoMantenimiento` | Lista equipos que han tenido mantenimientos de un tipo específico (equipo + mantenimiento). | 17‑19 |
| `ProductoRepository` | `findProductosAlquiladosEnRangoFechas` | Productos alquilados dentro de un rango de fechas (detalle + producto + alquiler). | 18‑20 |

### 3.2. Dos consultas nativas (más de una tabla)

| Repositorio | Método | Descripción | Línea |
|---|---|---|---|
| `AlquilerRepository` | `resumenAlquileresPorClienteNativo` | Resumen de alquileres por cliente (tablas cliente, alquiler, alquiler_detalle). | 21‑28 |
| `MantenimientoRepository` | `resumenMantenimientoEquipoNativo` | Resumen de mantenimientos por equipo, incluyendo total de productos (tablas mantenimiento, equipo, mantenimiento_detalle, mantenimiento_detalle_productos). | 21‑29 |

## 4. Nuevos archivos creados

No se crearon archivos nuevos; se modificaron los existentes:
- `AlquilerDetalle.java`
- `MantenimientoDetalle.java`
- DTOs de alquiler y mantenimiento (request/response).
- Servicios `AlquilerService.java` y `MantenimientoService.java`.
- Los repositorios anteriores fueron ampliados con los métodos de query.

## 5. Cómo probar

Ejecute los tests de integración existentes o use los endpoints REST expuestos en los controladores. Las nuevas consultas pueden probarse mediante los endpoints de `ConsultasController` (si están habilitados) o mediante pruebas unitarias que invoquen los métodos del repositorio.

---

**Resumen rápido**
- Cambios de `producto` → `productos` (ManyToMany) en entidades detalle.
- Actualizados DTOs y servicios.
- 6 JPQL + 2 native queries añadidas en los repositorios (`AlquilerRepository`, `MantenimientoRepository`, `EquipoRepository`, `ProductoRepository`).

