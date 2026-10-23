# Informe Técnico de Tarea - Evidencia 3: Pruebas Unitarias e Integración con Spring Boot

Este documento detalla la solución implementada para la tarea de Spring Boot (Evidencia 3), explicando la arquitectura del proyecto, la estructura de las pruebas implementadas (consignas **A** y **B**), los fragmentos de código clave y las decisiones técnicas adoptadas para que cualquier integrante del equipo pueda comprender y sustentar el trabajo.

---

##  Tabla de Contenidos
1. [Objetivos de la Tarea](#-objetivos-de-la-tarea)
2. [Estructura del Proyecto y Modelo de Datos](#-estructura-del-proyecto-y-modelo-de-datos)
3. [Parte A: Consulta por ID de cada Entity (12 Tests)](#-parte-a-consulta-por-id-de-cada-entity-12-tests)
4. [Parte B: Registro de Entities Cabecera-Detalle (2 Tests)](#-parte-b-registro-de-entities-cabecera-detalle-2-tests)
5. [Ajustes Técnicos y Buenas Prácticas](#-ajustes-técnicos-y-buenas-prácticas)
6. [Cómo Ejecutar los Tests](#-cómo-ejecutar-los-tests)
7. [Resultado de la Ejecución](#-resultado-de-la-ejecución)

---

##  Objetivos de la Tarea

1. **Consigna A**: Implementar pruebas automatizadas de **consulta por ID (`findById`)** para cada entity del modelo de datos actualizado (se requerían al menos 10 tablas; nuestro modelo cuenta con **12 entidades** evaluadas).
2. **Consigna B**: Implementar dos métodos de test para el **registro de entidades que sigan el patrón Cabecera-Detalle** (un entity principal que contiene una colección de ítems asociados), verificando tanto la respuesta HTTP como el impacto y persistencia en la base de datos.
3. **Criterios de evaluación**: Dominio del código presentado, aplicación de buenas prácticas (inyección de dependencias, aserciones declarativas, transaccionalidad, manejo de excepciones) y completitud de datos del modelo.

---

##  Estructura del Proyecto y Modelo de Datos

El modelo de datos gestiona el alquiler y mantenimiento de maquinaria pesada/equipos industriales. Se compone de 12 entidades principales:

| N° | Entidad | Rol en el Dominio | Repositorio JPA |
|:--:|:---|:---|:---|
| 1 | `Almacen` | Sedes y patios de maquinaria (Lima, Arequipa, etc.) | `AlmacenRepository` |
| 2 | `Empleado` | Personal técnico que realiza o supervisa mantenimientos | `EmpleadoRepository` |
| 3 | `Proveedor` | Empresas proveedoras de insumos/repuestos | `ProveedorRepository` |
| 4 | `Cliente` | Persona natural / cliente con documento | `ClienteRepository` |
| 5 | `Empresa` | Persona jurídica / cliente corporativo con RUC | `EmpresaRepository` |
| 6 | `Entidad` | Tabla compartida de contacto (teléfono, correo, dirección) | `EntidadRepository` |
| 7 | `Equipo` | Grupos electrógenos y maquinaria pesada | `EquipoRepository` |
| 8 | `Producto` | Insumos, repuestos y accesorios (cables, filtros, etc.) | `ProductoRepository` |
| 9 | `Alquiler` | **Cabecera** del contrato de alquiler (fechas, cliente, garantía) | `AlquilerRepository` |
| 10 | `AlquilerDetalle` | **Detalle** del alquiler (equipo asignado, horómetros, precios, insumos) | `AlquilerDetalleRepository` |
| 11 | `Mantenimiento` | **Cabecera** del servicio de mantenimiento (3 fechas, tipo, equipo, empleado) | `MantenimientoRepository` |
| 12 | `MantenimientoDetalle` | **Detalle** del mantenimiento (descripción del trabajo, insumos/repuestos) | `MantenimientoDetalleRepository` |

---

##  Parte A: Consulta por ID de cada Entity 

**Archivos:** una clase por caso de uso en `pe.edu.uls.tests_productos`: `TestAlmacen`, `TestAlquiler`, `TestCliente`, `TestEmpleado`, `TestEmpresa`, `TestEntidad`, `TestEquipo`, `TestMantenimiento`, `TestProducto` y `TestProveedor`.

### ¿Cómo funciona la prueba?
Cada método de prueba obtiene un registro representativo mediante el repositorio Spring Data JPA (aprovechando los datos sembrados por `DatabaseInitializer` en PostgreSQL Neon), invoca el método estándar `repo.findById(id)` y comprueba mediante `AssertJ` (`assertThat`):
- Que el `Optional` no esté vacío (`isPresent()`).
- Que el ID recuperado coincida exactamente con el solicitado.
- Que los atributos descriptivos y claves foráneas no sean nulos y tengan coherencia de negocio.

### Fragmentos de Código Explicados

#### 1. Consulta por ID de Cabecera (`Alquiler`)
```java
@Test
@DisplayName("9. Consulta por ID de Entity Alquiler (Cabecera)")
void testConsultaPorIdAlquiler() {
    // 1. Obtenemos un alquiler existente de la base de datos
    Alquiler muestra = repoAlquiler.findAll().stream().findFirst().orElseThrow();
    
    // 2. Ejecutamos la consulta por ID requerida por la consigna
    Optional<Alquiler> encontrado = repoAlquiler.findById(muestra.getId());

    // 3. Verificamos que se haya encontrado y que mantenga su relación con Cliente
    assertThat(encontrado).isPresent();
    assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
    assertThat(encontrado.get().getCliente()).isNotNull();
}
```
> **Explicación para sustentar:** `findById(id)` retorna un `Optional<T>`. Verificamos con `assertThat(encontrado).isPresent()` para asegurar que Hibernate mapeó la fila correctamente y que la asociación `@ManyToOne Cliente` no se encuentre rota.

#### 2. Consulta por ID de Detalle (`AlquilerDetalle`)
```java
@Test
@DisplayName("10. Consulta por ID de Entity AlquilerDetalle (Detalle)")
void testConsultaPorIdAlquilerDetalle() {
    AlquilerDetalle muestra = repoAlquilerDetalle.findAll().stream().findFirst().orElseThrow();
    Optional<AlquilerDetalle> encontrado = repoAlquilerDetalle.findById(muestra.getId());

    assertThat(encontrado).isPresent();
    assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
    assertThat(encontrado.get().getEquipo()).isNotNull();
}
```

#### 3. Consulta por ID de Entity con Herencia / Composición (`Empresa` y `Entidad`)
```java
@Test
@DisplayName("5. Consulta por ID de Entity Empresa")
void testConsultaPorIdEmpresa() {
    // Obtenemos una empresa existente o generamos una controlada si la BD está limpia
    Empresa muestra = repoEmpresa.findAll().stream().findFirst().orElseGet(() -> {
        Empresa nueva = new Empresa();
        nueva.setRuc("20999888771");
        nueva.setRazonSocial("Empresa Demo Test S.A.C.");
        nueva.setContacto("Gerente General");
        Entidad ent = new Entidad();
        ent.setTelefono("911223344");
        ent.setCorreo("contacto@empresademo.pe");
        ent.setDireccion("Av. Test 100");
        ent.setEstado(1);
        nueva.setEntidad(ent);
        return repoEmpresa.save(nueva);
    });

    Optional<Empresa> encontrado = repoEmpresa.findById(muestra.getId());

    assertThat(encontrado).isPresent();
    assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
    assertThat(encontrado.get().getRuc()).isEqualTo(muestra.getRuc());
}
```

---

##  Parte B: Registro de Entities Cabecera-Detalle (2 Tests)

**Archivos:** `pe.edu.uls.tests_productos.TestAlquiler` (caso Alquiler) y `pe.edu.uls.tests_productos.TestMantenimiento` (caso Mantenimiento).

Para la consigna B se siguió el patrón del ejemplo del profesor:
1. Pruebas de integración con `@SpringBootTest` y `@AutoConfigureMockMvc`.
2. Simulación de peticiones HTTP `POST` con payloads JSON vía `MockMvc`.
3. Verificación de respuesta JSON (`jsonPath`) y estado HTTP `200 OK`.
4. Comprobación directa en la base de datos de que **tanto la cabecera como sus ítems de detalle** hayan sido persistidos y que el conteo global se haya incrementado en `+1`.

---

### Caso 1: Cabecera `Alquiler` con Detalles `AlquilerDetalle`

Un alquiler contiene una lista de detalles de alquiler, donde cada detalle especifica:
- Equipo alquilado (`equipoId`).
- Horómetro de salida y retorno (`horometroSalida`, `horometroRetorno`).
- Fechas de inicio, fin y retorno estimada.
- Observaciones de estado físico del equipo al ser entregado.
- Productos/accesorios complementarios requeridos.
- Módulo de garantía (monto, tipo y estado).

#### Fragmento de Código Explicado:
```java
@Test
@Transactional
@DisplayName("Debe registrar un Alquiler con sus ítems de detalle y productos asociados")
void testRegistrarAlquilerConDetalles() throws Exception {
    // 1. Obtener entidades relacionadas previamente existentes en la BD
    Cliente cliente = repoCliente.findAll().stream().findFirst().orElseThrow();
    Equipo equipo = repoEquipo.findAll().stream().findFirst().orElseThrow();
    Producto producto1 = repoProducto.findAll().stream().findFirst().orElseThrow();

    int totalAlquileresAntes = repoAlquiler.findAll().size();

    // 2. Construir el detalle del alquiler (ítems que van dentro de la cabecera)
    RequestAlquiler.DetalleAlquilerRequest.ProductoAlquilerRequest prodItem =
            new RequestAlquiler.DetalleAlquilerRequest.ProductoAlquilerRequest(producto1.getId(), producto1.getPrecio());

    RequestAlquiler.DetalleAlquilerRequest detalle = new RequestAlquiler.DetalleAlquilerRequest(
            equipo.getId(),
            List.of(producto1.getId()),
            List.of(prodItem),
            1,
            1500.0,
            100.0,
            150.0,
            "Mina Antamina - Sector 4",
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(15),
            null,
            "Equipo entregado en óptimas condiciones operativas"
    );

    // 3. Construir la cabecera (Alquiler) que contiene la lista de detalles
    RequestAlquiler requestAlquiler = new RequestAlquiler(
            LocalDate.now(),
            LocalDate.now().plusDays(15),
            1,
            cliente.getId(),
            2500.0,
            "efectivo",
            0,
            null,
            "Garantía en custodia según contrato",
            List.of(detalle) // <- PATRÓN CABECERA-DETALLE
    );

    // 4. Enviar petición HTTP POST al endpoint /alquileres
    String responseContent = mockMvc.perform(post("/alquileres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestAlquiler)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.clienteId").value(cliente.getId()))
            .andExpect(jsonPath("$.montoGarantia").value(2500.0))
            .andExpect(jsonPath("$.detalles").isArray())
            .andExpect(jsonPath("$.detalles[0].equipoId").value(equipo.getId()))
            .andReturn()
            .getResponse()
            .getContentAsString();

    // 5. Validar persistencia real en la base de datos
    int idAlquilerGenerado = objectMapper.readTree(responseContent).get("id").asInt();
    Alquiler alquilerPersistido = repoAlquiler.findById(idAlquilerGenerado).orElseThrow();

    // Aserciones sobre la cabecera y el detalle guardado en cascada
    assertThat(alquilerPersistido.getCliente().getId()).isEqualTo(cliente.getId());
    assertThat(alquilerPersistido.getDetalles()).isNotEmpty();
    assertThat(alquilerPersistido.getDetalles().get(0).getEquipo().getId()).isEqualTo(equipo.getId());

    int totalAlquileresDespues = repoAlquiler.findAll().size();
    assertThat(totalAlquileresDespues).isEqualTo(totalAlquileresAntes + 1);
}
```

---

### Caso 2: Cabecera `Mantenimiento` con Detalles `MantenimientoDetalle`

Un mantenimiento contiene una lista de actividades de mantenimiento (detalles), donde cada una registra:
- Tipo de labor realizada y cantidad.
- Insumos y repuestos consumidos (`productoIds`).
- Observaciones de ejecución y ciclo de las 3 fechas (fecha de solicitud, fecha programada y fecha realizada).
- Asignación opcional del técnico responsable (`empleadoId`).

#### Fragmento de Código Explicado:
```java
@Test
@Transactional
@DisplayName("Debe registrar un Mantenimiento con sus ítems de detalle y productos consumidos")
void testRegistrarMantenimientoConDetalles() throws Exception {
    // 1. Obtener entidades requeridas
    Equipo equipo = repoEquipo.findAll().stream().findFirst().orElseThrow();
    Empleado empleado = repoEmpleado.findAll().stream().findFirst().orElseThrow();
    Producto producto1 = repoProducto.findAll().stream().findFirst().orElseThrow();

    int totalMantenimientosAntes = repoMantenimiento.findAll().size();

    // 2. Construir el detalle del mantenimiento
    RequestMantenimiento.DetalleMantenimientoRequest detalle =
            new RequestMantenimiento.DetalleMantenimientoRequest(
                    List.of(producto1.getId()),
                    2,
                    "Cambio de componentes preventivos de rutina",
                    "Instalación conforme a especificación técnica del fabricante"
            );

    // 3. Construir la cabecera con el ciclo de 3 fechas y la lista de detalles
    RequestMantenimiento requestMantenimiento = new RequestMantenimiento(
            LocalDate.now().minusDays(2), // fechaSolicitud
            LocalDate.now(),              // fechaProgramada
            LocalDate.now(),              // fechaRealizado
            "Preventivo",
            "Mantenimiento de 250 horas programado",
            equipo.getId(),
            empleado.getId(),
            List.of(detalle)              // <- PATRÓN CABECERA-DETALLE
    );

    // 4. Invocar el endpoint POST /mantenimientos
    String responseContent = mockMvc.perform(post("/mantenimientos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestMantenimiento)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.equipoId").value(equipo.getId()))
            .andExpect(jsonPath("$.tipo").value("Preventivo"))
            .andExpect(jsonPath("$.detalles").isArray())
            .andExpect(jsonPath("$.detalles[0].cantidad").value(2))
            .andReturn()
            .getResponse()
            .getContentAsString();

    // 5. Validar persistencia en base de datos
    int idMantenimientoGenerado = objectMapper.readTree(responseContent).get("id").asInt();
    Mantenimiento mantenimientoPersistido = repoMantenimiento.findById(idMantenimientoGenerado).orElseThrow();

    assertThat(mantenimientoPersistido.getEquipo().getId()).isEqualTo(equipo.getId());
    assertThat(mantenimientoPersistido.getEmpleado().getId()).isEqualTo(empleado.getId());
    assertThat(mantenimientoPersistido.getDetalles()).isNotEmpty();
    assertThat(mantenimientoPersistido.getDetalles().get(0).getCantidad()).isEqualTo(2);

    int totalMantenimientosDespues = repoMantenimiento.findAll().size();
    assertThat(totalMantenimientosDespues).isEqualTo(totalMantenimientosAntes + 1);
}
```

---

##  Ajustes Técnicos y Buenas Prácticas Aplicadas

1. **Compatibilidad con Jackson en Spring Boot**:
   - En el proyecto se utiliza `tools.jackson.databind.ObjectMapper`, idéntico a la plantilla del profesor, garantizando serialización y deserialización sin errores de classpath.

2. **Anotación `@Transactional` en Métodos de Test**:
   - Dado que las colecciones `detalles` están configuradas con carga perezosa (`FetchType.LAZY`), al consultar `alquiler.getDetalles()` después de la llamada `mockMvc`, Hibernate necesita mantener abierta la sesión del `EntityManager`. Colocar `@Transactional` evita la excepción `LazyInitializationException` y garantiza aislamiento en las pruebas.

3. **Mapeo Cascada Bidireccional (`CascadeType.ALL`)**:
   - Las entidades `Alquiler` y `Mantenimiento` usan `cascade = CascadeType.ALL` y `orphanRemoval = true`.
   - Cuando el controlador guarda la cabecera mediante `save(alquiler)`, JPA automáticamente persiste los registros hijos en `alquiler_detalle` y `alquiler_detalle_productos` dentro de la misma transacción.

4. **Sincronización DDL en Neon DB**:
   - En `DatabaseInitializer.java` se incorporó la creación condicional (`ALTER TABLE ... ADD COLUMN IF NOT EXISTS`) para que columnas recién incorporadas (las 3 fechas del ciclo de mantenimiento y los datos de garantía) existan en la base de datos remota sin alterar registros históricos existentes.

---

## Cómo Ejecutar los Tests

Desde la terminal en el directorio raíz del proyecto:

### 1. Ejecutar solo las pruebas de esta tarea (Consignas A y B):
```bash
./mvnw test -Dtest='Test*'
```

### 2. Ejecutar individualmente la Parte A (Consultas por ID):
```bash
./mvnw test -Dtest=TestAlmacen,TestAlquiler,TestCliente,TestEmpleado,TestEmpresa,TestEntidad,TestEquipo,TestMantenimiento,TestProducto,TestProveedor
```
