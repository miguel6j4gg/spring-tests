# Informe de Cambios y Queries

## 1. Cambios estructurales en el modelo

### 1.1. Entidad asociativa `AlquilerDetalleProducto`

La relación `ManyToMany` entre `AlquilerDetalle` y `Producto` se sustituyó por una entidad asociativa que permite guardar un precio propio por producto.

| Elemento | Detalle |
|---|---|
| Archivo nuevo | `domain/entity/AlquilerDetalleProducto.java` |
| Tabla | `alquiler_detalle_productos` (reaprovecha la join table anterior) |
| Secuencia | `sec_alquiler_detalle_producto` |
| Relaciones | `@ManyToOne` → `AlquilerDetalle` (`alquiler_detalle_id`) y `@ManyToOne` → `Producto` (`producto_id`) |
| Campo propio | `double precio` |

| Archivo | Cambio realizado |
|---|---|
| `AlquilerDetalle.java` | `@ManyToMany List<Producto>` → `@OneToMany(mappedBy = "alquilerDetalle", cascade = ALL, orphanRemoval = true) List<AlquilerDetalleProducto> productos`, con `@JsonManagedReference` y el helper `agregarProducto(...)`. |
| `ProductoRepository.java` | La JPQL de productos alquilados ahora recorre `JOIN ad.productos adp JOIN adp.producto p`. |
| `MantenimientoDetalle.java` | Sin cambios: mantiene su `ManyToMany` con `Producto`. |

### 1.2. Unificación del campo `estado` a `Integer`

Todas las entidades usan `Integer estado`. Se eliminó el uso de `LOWER()` sobre ese campo porque PostgreSQL no puede aplicarlo a un entero (`function lower(integer) does not exist`).

| Tipo | Elementos |
|---|---|
| Entidades con `estado` propio | `Almacen`, `Producto`, `Equipo`, `Alquiler`, `Proveedor`, `Empleado` |
| Entidades con `estado` heredado de `Entidad` | `Cliente`, `Empresa` |
| DTOs actualizados | `RequestAlquiler`, `ResponseAlquiler`, `RequestProducto`, `ResponseProducto`, `RequestEquipo`, `ResponseEquipo`, `RequestCliente`, `ResponseCliente`, `ResumenAlquilerClienteDTO` |

### 1.3. Entidad `Entidad` con los campos comunes

`Entidad` concentró los atributos compartidos y pasó a ser una entidad real.

| Elemento | Detalle |
|---|---|
| Tabla / secuencia | `entidad` / `sec_entidad` (antes compartía `sec_cliente` con `Cliente`, lo que generaba colisiones de PK) |
| Campos | `telefono`, `direccion`, `correo`, `Integer estado` |

- `Cliente`: eliminados `telefono`, `direccion`, `estado` y `email`; conserva `documento`, `nombre` y `apellido`.
- `Empresa`: eliminados `telefono`, `direccion`, `correo` y `estado`; conserva `ruc`, `razonSocial`, `contacto`, la relación y las fechas de auditoría.

`Cliente.email` y `Entidad.correo` eran el mismo dato con distinto nombre, así que se unificó: el campo `email` de `RequestCliente` y `ResponseCliente` se mantiene para no romper el contrato JSON de `/clientes`, pero se persiste y se lee desde `Entidad.correo`.

### 1.4. Relaciones agregadas

| Entidad | Relación | Mapeo |
|---|---|---|
| `Cliente` | `@OneToOne` → `Entidad` | `@JoinColumn(name = "entidad_id", unique = true)`, `cascade = ALL`, `orphanRemoval = true` |
| `Empresa` | `@OneToOne` → `Entidad` | `@JoinColumn(name = "entidad_id", unique = true)`, `cascade = ALL`, `orphanRemoval = true` |
| `Proveedor` | `@ManyToOne` → `Empresa` | `@JoinColumn(name = "empresa_id")`, sin cascade (varios proveedores pueden apuntar a la misma empresa) |

### 1.5. Campos de negocio de `Empresa`

`Empresa` pasó a tener su propia identidad fiscal y de contacto comercial, además de los datos comunes que aporta la `Entidad`.

| Campo | Tipo | Restricción |
|---|---|---|
| `ruc` | `String` | `@Column(unique = true, nullable = false)` |
| `razonSocial` | `String` | — |
| `contacto` | `String` | — |

Se añadió `EmpresaRepository.existsByRuc(String)` y la validación de unicidad en `EmpresaService.registrarEmpresa`, que responde `409 CONFLICT` si el RUC ya existe, igual que el resto de casos de uso con clave de negocio.

### 1.6. Correcciones de mapeo

| Archivo | Problema | Corrección |
|---|---|---|
| 10 entidades | `@UpdateTimestamp` combinado con `@Column(insertable = false)` excluía la columna del `INSERT`, provocando `null value in column "updated_date" violates not-null constraint`. | Se eliminó `insertable = false`; Hibernate ahora emite `current_timestamp` en el INSERT y en el UPDATE. |
| `Empresa.java` | Usaba la secuencia `sec_almacen`, compartida con `Almacen`. | Secuencia propia `sec_empresa`. |
| `Entidad.java` | No tenía `@Entity` y usaba la secuencia `sec_cliente`. | `@Entity` + `@Table(name = "entidad")` + `sec_entidad`. |
| `Mantenimiento.java` | El campo `fecha` se había renombrado a `fechaMantenimientoRealizado` sin actualizar las 3 llamadas que lo usaban. | `MantenimientoService` y `MantenimientoController` usan el nombre nuevo. |

## 2. Actualizaciones en la capa de servicio

- `AlquilerService`: crea un `AlquilerDetalleProducto` por producto mediante los helpers `crearDetalleProductos(...)` y `detalleProducto(...)`. Acepta la forma nueva `productos: [{productoId, precio}]` y, si no viene, usa `productoIds` heredando `Producto.precio`.
- `ClienteService.buscarPorEstado` y `ClienteController.buscarPorEstado`: reciben `Integer`.
- `ConsultasService.buscarAlquileresPorClienteYEstado(String, Integer)`: la validación pasó de `trim().isEmpty()` a `null`.
- `ConsultasService.resumenAlquileresPorCliente`: el estado de la query nativa se castea con `((Number) row[2]).intValue()`.
- `ClienteMapperBean`: crea la `Entidad` al registrar y lee de ella al responder.
- `DatabaseInitializer`: siembra la `Entidad` de los clientes vía el helper `entidadCliente(...)` y los productos alquilados vía `productoAlquilado(...)`; todos los `estado` son numéricos.

## 3. Casos de uso

### 3.1. Nuevos

| Caso de uso | Endpoints |
|---|---|
| `10 Empresa` | `POST /empresas`, `GET /empresas/{id}`, `GET /empresas/estado/{estado}`, `GET /empresas` |
| Consultar proveedores de una empresa | `GET /proveedores/empresa/{empresaId}` |
| Buscar por estado (Almacen, Empleado, Proveedor) | `GET /almacenes/estado/{estado}`, `GET /empleados/estado/{estado}`, `GET /proveedores/estado/{estado}` |

Archivos nuevos: `EmpresaRepository`, `RequestEmpresa`, `ResponseEmpresa`, `EmpresaService`, `EmpresaController`.

### 3.2. Cambios de contrato

- `POST /alquileres`: el detalle ahora acepta `productos: [{productoId, precio}]` además de `productoIds`.
- `GET /alquileres`: `detalles[].productos` cambió de `List<ResponseProducto>` a `List<{producto, precio}>`. Es un cambio incompatible para consumidores externos.
- `POST /empresas`: `RequestEmpresa` ahora exige `ruc` (único), `razonSocial` y `telefono`; `ResponseEmpresa` incluye `ruc`, `razonSocial` y `contacto`.

## 4. Repositorios y Queries

### 4.1. Seis consultas JPQL (más de una entidad)

| Repositorio | Método | Descripción |
|---|---|---|
| `AlquilerRepository` | `findAlquileresPorClienteNombreYEstado` | Alquileres por nombre de cliente y estado (alquiler + cliente). Compara `a.estado = :estado` sin `LOWER()`. |
| `AlquilerRepository` | `findAlquileresPorModeloEquipo` | Alquileres por modelo de equipo (alquiler + detalle + equipo). |
| `MantenimientoRepository` | `findMantenimientosPorUbicacionEquipo` | Mantenimientos según ubicación del equipo (mantenimiento + equipo). |
| `MantenimientoRepository` | `findMantenimientosPorCodigoProducto` | Mantenimientos que involucren un producto concreto. |
| `EquipoRepository` | `findEquiposPorTipoMantenimiento` | Equipos con mantenimientos de un tipo específico. |
| `ProductoRepository` | `findProductosAlquiladosEnRangoFechas` | Productos alquilados en un rango de fechas. Atraviesa `AlquilerDetalleProducto`. |

Consultas derivadas que consultan `estado`: `ClienteRepository` (join a `Entidad`), `EmpresaRepository` (join a `Entidad`), `AlmacenRepository`, `EmpleadoRepository` y `ProveedorRepository` (campo propio). Además `ProveedorRepository.findByEmpresa_Id`.

### 4.2. Dos consultas nativas (más de una tabla)

| Repositorio | Método | Descripción |
|---|---|---|
| `AlquilerRepository` | `resumenAlquileresPorClienteNativo` | Resumen de alquileres por cliente (cliente, alquiler, alquiler_detalle). |
| `MantenimientoRepository` | `resumenMantenimientoEquipoNativo` | Resumen de mantenimientos por equipo (mantenimiento, equipo, mantenimiento_detalle, mantenimiento_detalle_productos). |

## 5. Cómo probar

Los casos de uso se ejercitan con la colección de Postman (`postman/Tests Productos.postman_collection.json`), que cubre los 10 casos de uso y encadena los ids generados en variables de colección. También hay requests por caso en `requests/*.http` y `request.http`.

Los tests de integración están organizados por caso de uso: `TestAlmacen`, `TestAlquiler`, `TestCliente`, `TestConsultas` (6 JPQL y 2 nativas), `TestEmpleado`, `TestEmpresa`, `TestEntidad`, `TestEquipo`, `TestMantenimiento`, `TestProducto` y `TestProveedor`. Se ejecutan con:

```
./mvnw test
```

## 6. Pendiente en la base de datos

`estado` pasó de `varchar` a `int`. `spring.jpa.hibernate.ddl-auto=update` no altera el tipo de columnas existentes, así que hay que ejecutar una migración manual antes de levantar la aplicación:

```sql
ALTER TABLE producto  ALTER COLUMN estado TYPE integer USING CASE estado WHEN 'Activo' THEN 1 ELSE 0 END;
ALTER TABLE equipo    ALTER COLUMN estado TYPE integer USING CASE estado WHEN 'Disponible' THEN 1 WHEN 'Alquilado' THEN 2 WHEN 'Mantenimiento' THEN 3 ELSE 0 END;
ALTER TABLE almacen   ALTER COLUMN estado TYPE integer USING CASE estado WHEN 'Operativo' THEN 1 ELSE 0 END;
ALTER TABLE alquiler  ALTER COLUMN estado TYPE integer USING CASE estado WHEN 'Activo' THEN 1 WHEN 'Finalizado' THEN 2 ELSE 0 END;
```

Además, si quedaron filas con `UPDATED_DATE` en `NULL` en columnas `NOT NULL`:

```sql
UPDATE proveedor SET updated_date = created_date WHERE updated_date IS NULL;
```

Y sobre las columnas nuevas de `empresa`, que se agregan solas con `ddl-auto=update` pero exigen RUC en toda fila existente:

```sql
UPDATE empresa SET ruc = '00000000000' WHERE ruc IS NULL;
ALTER TABLE empresa ALTER COLUMN ruc SET NOT NULL;
CREATE UNIQUE INDEX ux_empresa_ruc ON empresa (ruc);
```

Las columnas que quedaron obsoletas no se borran solas (Hibernate no elimina columnas). Para limpiar `cliente.email`, `cliente.telefono`, `cliente.direccion` y `cliente.estado`:

```sql
ALTER TABLE cliente DROP COLUMN email;
ALTER TABLE cliente DROP COLUMN telefono;
ALTER TABLE cliente DROP COLUMN direccion;
ALTER TABLE cliente DROP COLUMN estado;
```

---

**Resumen rápido**
- `AlquilerDetalleProducto` reemplaza el `ManyToMany` y agrega `precio` por producto.
- `estado` es `Integer` en todas las entidades, DTOs, repositorios y consultas.
- `Entidad` concentra los campos comunes y se relaciona `OneToOne` con `Cliente` y `Empresa`.
- `Proveedor` se relaciona `ManyToOne` con `Empresa`.
- Corregido el `NOT NULL` de `UPDATED_DATE` y las secuencias duplicadas (`sec_entidad`, `sec_empresa`).
- Caso de uso de `Empresa` completo y endpoints "por estado" en Almacen, Empleado y Proveedor.
- 6 JPQL + 2 nativas, más las consultas derivadas por estado y por empresa.