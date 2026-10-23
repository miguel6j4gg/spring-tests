package pe.edu.uls.tests_productos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import pe.edu.uls.tests_productos.cu.adminAlquiler.request.RequestActualizarAlquiler;
import pe.edu.uls.tests_productos.cu.adminAlquiler.request.RequestAlquiler;
import pe.edu.uls.tests_productos.cu.adminAlquiler.response.DetalleEquiposProductosResponse;
import pe.edu.uls.tests_productos.cu.adminAlquiler.response.ResponseAlquiler;
import pe.edu.uls.tests_productos.domain.entity.Cliente;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.repository.ClienteRepository;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;

/**
 * =========================================================================
 * CASO DE USO 2: REGISTRAR ALQUILER DE EQUIPO (PROCESO DE NEGOCIO COMPLETO)
 * =========================================================================
 * Evaluación Parcial - Pruebas de integración HTTP con TestRestTemplate.
 * 
 * Cumple con los requerimientos:
 * 1. Operaciones REST que cubren todo el proceso de alquiler (cabecera-detalle).
 * 2. Involucra al menos dos Queries multi-entidad/tabla (Query 1 y Query 2).
 * 3. Al menos 2 tests (1 exitoso y 1 fallido) por CADA operación REST (10 tests en total).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlquilerProcesoIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private pe.edu.uls.tests_productos.domain.repository.EntidadRepository entidadRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    private Cliente clienteTest;
    private pe.edu.uls.tests_productos.domain.entity.Entidad entidadTest;
    private static Equipo equipoDisponible;
    private static Integer equipoIdDisponible;
    private Producto productoTest;
    private static Integer alquilerIdCreado;

    @BeforeEach
    void setUp() {
        // Garantizar entidad y cliente existente
        List<Cliente> clientes = clienteRepository.findAll();
        for (Cliente c : clientes) {
            if (c.getEntidad() != null) {
                clienteTest = c;
                entidadTest = c.getEntidad();
                break;
            }
        }

        if (clienteTest == null) {
            pe.edu.uls.tests_productos.domain.entity.Entidad entidad = new pe.edu.uls.tests_productos.domain.entity.Entidad();
            entidad.setCorreo("constructora@test.com");
            entidad.setTelefono("987654321");
            entidad.setDireccion("Av. Principal 123");
            entidad.setEstado(1);
            entidadTest = entidadRepository.save(entidad);

            Cliente nuevo = new Cliente();
            nuevo.setNombre("Constructora");
            nuevo.setApellido("Nacional SAC");
            nuevo.setDocumento("20485963" + (System.currentTimeMillis() % 1000));
            nuevo.setEntidad(entidadTest);
            clienteTest = clienteRepository.save(nuevo);
        }

        // Garantizar producto existente
        List<Producto> productos = productoRepository.findAll();
        if (!productos.isEmpty()) {
            productoTest = productos.get(0);
        } else {
            Producto nuevoProd = new Producto();
            nuevoProd.setCodigo("PROD-TEST-" + System.currentTimeMillis());
            nuevoProd.setNombre("Filtro de Aceite de Alto Rendimiento");
            nuevoProd.setCategoria("Repuestos");
            nuevoProd.setPrecio(180.0);
            nuevoProd.setStock(50);
            nuevoProd.setEstado(1);
            productoTest = productoRepository.save(nuevoProd);
        }
    }

    private Equipo crearEquipo(String prefijo, String nombre, String modelo, int estado) {
        Equipo equipo = new Equipo();
        equipo.setCodigo(prefijo + System.currentTimeMillis());
        equipo.setNombre(nombre);
        equipo.setModelo(modelo);
        equipo.setPotencia(500.0);
        equipo.setHorometro(120.0);
        equipo.setUbicacion("Arequipa");
        equipo.setCombustible("Diesel");
        equipo.setEstado(estado); // 1 = Disponible, 2 = Alquilado, 3 = Mantenimiento
        return equipoRepository.save(equipo);
    }

    // =========================================================================
    // OPERACIÓN REST 1: POST /alquileres
    // =========================================================================

    @Test
    @Order(1)
    @DisplayName("1.1 POST /alquileres [EXITOSO] - Debe registrar el alquiler con cabecera y detalles")
    void test01_RegistrarAlquiler_Exitoso() {
        equipoDisponible = crearEquipo("EQ-DISP-", "Excavadora Hidráulica CAT 320D", "CAT-320D-HD", 1);

        RequestAlquiler.DetalleAlquilerRequest.ProductoAlquilerRequest prodReq =
                new RequestAlquiler.DetalleAlquilerRequest.ProductoAlquilerRequest(
                        productoTest.getId(),
                        productoTest.getPrecio()
                );

        RequestAlquiler.DetalleAlquilerRequest detalle = new RequestAlquiler.DetalleAlquilerRequest(
                equipoDisponible.getId(),
                List.of(productoTest.getId()),
                List.of(prodReq),
                1,
                2500.0,
                120.0,
                0.0,
                "Proyecto Minero Cerro Verde",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                null,
                "Equipo entregado operativo y con tanque lleno"
        );

        RequestAlquiler request = new RequestAlquiler(
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                1,
                entidadTest.getId(),
                5000.0,
                "carta_fianza",
                0,
                null,
                "Garantía bancaria en custodia",
                List.of(detalle)
        );

        ResponseEntity<ResponseAlquiler> response = restTemplate.postForEntity(
                "/alquileres",
                request,
                ResponseAlquiler.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertThat(response.getBody().id()).isGreaterThan(0);
        assertThat(response.getBody().entidadId()).isEqualTo(entidadTest.getId());
        assertThat(response.getBody().detalles()).isNotEmpty();
        assertThat(response.getBody().detalles().get(0).equipoId()).isEqualTo(equipoDisponible.getId());

        alquilerIdCreado = response.getBody().id();
        equipoIdDisponible = equipoDisponible.getId();

        // Verificar que el equipo pasó a estado 2 (Alquilado)
        Equipo equipoBd = equipoRepository.findById(equipoIdDisponible).orElseThrow();
        assertEquals(2, equipoBd.getEstado(), "El equipo debe pasar a estado 2 (Alquilado)");
    }

    @Test
    @Order(2)
    @DisplayName("1.2 POST /alquileres [FALLIDO] - Debe rechazar (409 Conflict) si el equipo ya no está disponible")
    void test02_RegistrarAlquiler_EquipoNoDisponible_Conflict() {
        // Crear equipo ya alquilado (estado 2)
        Equipo equipoOcupado = crearEquipo("EQ-OCUP-", "Grupo Electrógeno Cummins C150", "Cummins-C150", 2);

        RequestAlquiler.DetalleAlquilerRequest detalle = new RequestAlquiler.DetalleAlquilerRequest(
                equipoOcupado.getId(),
                List.of(),
                List.of(),
                1,
                1200.0,
                50.0,
                0.0,
                "Obra Lima Norte",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                null,
                "Intento de alquiler con equipo ocupado"
        );

        RequestAlquiler request = new RequestAlquiler(
                LocalDate.now(),
                LocalDate.now().plusDays(10),
                1,
                entidadTest.getId(),
                2000.0,
                "efectivo",
                0,
                null,
                null,
                List.of(detalle)
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/alquileres",
                request,
                String.class
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode(),
                "Debe rechazar con 409 CONFLICT cuando el equipo no se encuentra disponible");
    }

    @Test
    @Order(3)
    @DisplayName("1.3 POST /alquileres [FLUJO ALTERNO - FALLIDO] - Cliente/Entidad no encontrado (404 Not Found)")
    void test02b_RegistrarAlquiler_ClienteNoEncontrado_NotFound() {
        Equipo eq = crearEquipo("EQ-CLI-NF-", "Generador Test", "GEN-NF", 1);

        RequestAlquiler.DetalleAlquilerRequest detalle = new RequestAlquiler.DetalleAlquilerRequest(
                eq.getId(),
                List.of(),
                List.of(),
                1, 1000.0, 0.0, 0.0,
                "Sede Central",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                null,
                "Prueba cliente no existe"
        );

        RequestAlquiler request = new RequestAlquiler(
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                1,
                999999, // Entidad ID inexistente
                1000.0,
                "efectivo",
                0,
                null,
                null,
                List.of(detalle)
        );

        ResponseEntity<String> response = restTemplate.postForEntity("/alquileres", request, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Debe responder 404 cuando el cliente/entidad no existe");
    }

    @Test
    @Order(4)
    @DisplayName("1.4 POST /alquileres [FLUJO ALTERNO - FALLIDO] - Fecha fin anterior a fecha inicio (400 Bad Request)")
    void test02c_RegistrarAlquiler_FechaFinAnteriorAFechaInicio_BadRequest() {
        Equipo eq = crearEquipo("EQ-FECHA-", "Generador Fechas", "GEN-FECHA", 1);

        RequestAlquiler.DetalleAlquilerRequest detalle = new RequestAlquiler.DetalleAlquilerRequest(
                eq.getId(),
                List.of(),
                List.of(),
                1, 1000.0, 0.0, 0.0,
                "Sede Central",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                null,
                "Prueba fecha inválida"
        );

        RequestAlquiler request = new RequestAlquiler(
                LocalDate.now().plusDays(10), // Inicio: dentro de 10 días
                LocalDate.now().plusDays(2),  // Fin: dentro de 2 días (ANTERIOR AL INICIO)
                1,
                entidadTest.getId(),
                1000.0,
                "efectivo",
                0,
                null,
                null,
                List.of(detalle)
        );

        ResponseEntity<String> response = restTemplate.postForEntity("/alquileres", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Debe responder 400 cuando fecha fin es anterior a fecha inicio");
    }

    @Test
    @Order(5)
    @DisplayName("1.5 POST /alquileres [FLUJO ALTERNO - FALLIDO] - Cantidad de productos insuficiente / sin stock (400 Bad Request)")
    void test02d_RegistrarAlquiler_StockProductoInsuficiente_BadRequest() {
        Equipo eq = crearEquipo("EQ-STOCK-", "Compresor Atlas", "ATLAS-100", 1);

        // Crear producto sin stock (stock = 0)
        Producto prodSinStock = new Producto();
        prodSinStock.setCodigo("PROD-NOSTOCK-" + System.currentTimeMillis());
        prodSinStock.setNombre("Aceite Sintético Especial");
        prodSinStock.setStock(0);
        prodSinStock.setPrecio(150.0);
        prodSinStock.setEstado(1);
        prodSinStock = productoRepository.save(prodSinStock);

        RequestAlquiler.DetalleAlquilerRequest.ProductoAlquilerRequest prodReq =
                new RequestAlquiler.DetalleAlquilerRequest.ProductoAlquilerRequest(prodSinStock.getId(), prodSinStock.getPrecio());

        RequestAlquiler.DetalleAlquilerRequest detalle = new RequestAlquiler.DetalleAlquilerRequest(
                eq.getId(),
                List.of(prodSinStock.getId()),
                List.of(prodReq),
                1, 1000.0, 0.0, 0.0,
                "Sede Central",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                null,
                "Prueba stock insuficiente"
        );

        RequestAlquiler request = new RequestAlquiler(
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                1,
                entidadTest.getId(),
                1000.0,
                "efectivo",
                0,
                null,
                null,
                List.of(detalle)
        );

        ResponseEntity<String> response = restTemplate.postForEntity("/alquileres", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Debe responder 400 cuando el producto no cuenta con stock");
    }

    @Test
    @Order(6)
    @DisplayName("1.6 POST /alquileres [FLUJO ALTERNO - FALLIDO] - Producto inválido/inexistente en el detalle (404 Not Found)")
    void test02e_RegistrarAlquiler_ProductoInexistente_NotFound() {
        Equipo eq = crearEquipo("EQ-PROD-NF-", "Torre Iluminación", "TORRE-10", 1);

        RequestAlquiler.DetalleAlquilerRequest.ProductoAlquilerRequest prodReq =
                new RequestAlquiler.DetalleAlquilerRequest.ProductoAlquilerRequest(999999, 100.0);

        RequestAlquiler.DetalleAlquilerRequest detalle = new RequestAlquiler.DetalleAlquilerRequest(
                eq.getId(),
                List.of(999999),
                List.of(prodReq),
                1, 1000.0, 0.0, 0.0,
                "Sede Central",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                null,
                "Prueba producto inexistente"
        );

        RequestAlquiler request = new RequestAlquiler(
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                1,
                entidadTest.getId(),
                1000.0,
                "efectivo",
                0,
                null,
                null,
                List.of(detalle)
        );

        ResponseEntity<String> response = restTemplate.postForEntity("/alquileres", request, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Debe responder 404 cuando el producto del detalle no existe");
    }

    // =========================================================================
    // OPERACIÓN REST 2: GET /alquileres/{id}
    // =========================================================================

    @Test
    @Order(7)
    @DisplayName("2.1 GET /alquileres/{id} [EXITOSO] - Devuelve un Alquiler existente con cabecera y detalles")
    void test03_ObtenerAlquilerPorId_Exitoso() {
        assertNotNull(alquilerIdCreado, "Debe existir un alquiler creado en el test 1");

        ResponseEntity<ResponseAlquiler> response = restTemplate.getForEntity(
                "/alquileres/" + alquilerIdCreado,
                ResponseAlquiler.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(alquilerIdCreado.intValue(), response.getBody().id());
        assertNotNull(response.getBody().detalles());
        assertFalse(response.getBody().detalles().isEmpty());
    }

    @Test
    @Order(8)
    @DisplayName("2.2 GET /alquileres/{id} [FALLIDO] - Debe responder (404 Not Found) con ID inexistente")
    void test04_ObtenerAlquilerPorId_Inexistente_NotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/alquileres/999999",
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // =========================================================================
    // OPERACIÓN REST 3: PATCH /alquileres/{id}
    // =========================================================================

    @Test
    @Order(9)
    @DisplayName("3.1 PATCH /alquileres/{id} [EXITOSO] - Debe actualizar la fecha de retorno y estado (devolución)")
    void test05_ActualizarAlquiler_DevolucionYFinalizacion_Exitoso() {
        assertNotNull(alquilerIdCreado, "Debe existir un alquiler creado en el test 1");

        RequestActualizarAlquiler requestActualizar = new RequestActualizarAlquiler(
                LocalDate.now(),
                LocalDateTime.now(),
                185.5,
                2, // 2 = Finalizado
                1, // 1 = Garantía devuelta
                LocalDate.now(),
                "Garantía liberada sin penalidades. Equipo retornado en excelente estado."
        );

        HttpEntity<RequestActualizarAlquiler> requestEntity = new HttpEntity<>(requestActualizar);

        ResponseEntity<ResponseAlquiler> response = restTemplate.exchange(
                "/alquileres/" + alquilerIdCreado,
                HttpMethod.PATCH,
                requestEntity,
                ResponseAlquiler.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Integer.valueOf(2), response.getBody().estado());
        assertEquals(Integer.valueOf(1), response.getBody().estadoGarantia());

        // Verificar que el equipo del alquiler fue liberado y volvió a estado 1 (Disponible)
        Equipo equipoLiberado = equipoRepository.findById(equipoIdDisponible).orElseThrow();
        assertEquals(1, equipoLiberado.getEstado(), "Al finalizar el alquiler, el equipo debe volver a estado 1 (Disponible)");
    }

    @Test
    @Order(10)
    @DisplayName("3.2 PATCH /alquileres/{id} [FALLIDO] - Debe responder (404 Not Found) si el alquiler no existe")
    void test06_ActualizarAlquiler_Inexistente_NotFound() {
        RequestActualizarAlquiler requestActualizar = new RequestActualizarAlquiler(
                LocalDate.now(),
                LocalDateTime.now(),
                200.0,
                2,
                1,
                LocalDate.now(),
                "Intento en alquiler inexistente"
        );

        HttpEntity<RequestActualizarAlquiler> requestEntity = new HttpEntity<>(requestActualizar);

        ResponseEntity<String> response = restTemplate.exchange(
                "/alquileres/999999",
                HttpMethod.PATCH,
                requestEntity,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(11)
    @DisplayName("3.3 PATCH /alquileres/{id} [FLUJO ALTERNO - FALLIDO] - Fecha de retorno inválida/anterior a fecha inicio (400 Bad Request)")
    void test06b_ActualizarAlquiler_FechaRetornoInvalida_BadRequest() {
        assertNotNull(alquilerIdCreado, "Debe existir un alquiler creado en el test 1");

        // Intentar registrar retorno con fecha de hace 1 año (anterior al inicio)
        RequestActualizarAlquiler requestActualizar = new RequestActualizarAlquiler(
                null,
                LocalDateTime.now().minusYears(1),
                null,
                null,
                null,
                null,
                "Fecha de retorno errónea"
        );

        HttpEntity<RequestActualizarAlquiler> requestEntity = new HttpEntity<>(requestActualizar);

        ResponseEntity<String> response = restTemplate.exchange(
                "/alquileres/" + alquilerIdCreado,
                HttpMethod.PATCH,
                requestEntity,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Debe responder 400 cuando la fecha de retorno es anterior al inicio del alquiler");
    }

    // =========================================================================
    // QUERY 1: GET /alquileres/por-cliente-equipo
    // =========================================================================

    @Test
    @Order(12)
    @DisplayName("QUERY 1.1 GET /alquileres/por-cliente-equipo [EXITOSO] - Listar alquileres por cliente y equipo")
    void test07_BuscarAlquileresPorClienteYEquipo_Exitoso() {
        assertNotNull(alquilerIdCreado, "Debe existir un alquiler creado en el test 1");

        ResponseEntity<List<ResponseAlquiler>> response = restTemplate.exchange(
                "/alquileres/por-cliente-equipo?entidadId=" + entidadTest.getId() + "&equipoId=" + equipoIdDisponible,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResponseAlquiler>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty(), "Debe encontrar al menos un alquiler para el cliente y equipo");
    }

    @Test
    @Order(13)
    @DisplayName("QUERY 1.2 GET /alquileres/por-cliente-equipo [FALLIDO] - Entidad/Equipo inexistente (404 Not Found)")
    void test07b_BuscarAlquileresPorClienteYEquipo_Inexistente_NotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/alquileres/por-cliente-equipo?entidadId=999999&equipoId=999999",
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // =========================================================================
    // OPERACIÓN REST 4: GET /alquileres/por-modelo?modelo={modelo}
    // =========================================================================

    @Test
    @Order(14)
    @DisplayName("4.1 GET /alquileres/por-modelo [EXITOSO] - Debe listar alquileres asociados al modelo de equipo")
    void test08_BuscarAlquileresPorModelo_Exitoso() {
        ResponseEntity<List<ResponseAlquiler>> response = restTemplate.exchange(
                "/alquileres/por-modelo?modelo=CAT-320D-HD",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResponseAlquiler>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty(), "Debe encontrar al menos un alquiler para el modelo CAT-320D-HD");
    }

    @Test
    @Order(15)
    @DisplayName("4.2 GET /alquileres/por-modelo [FALLIDO] - Debe responder (404 Not Found) al buscar modelo inexistente")
    void test08b_BuscarAlquileresPorModelo_Inexistente_NotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/alquileres/por-modelo?modelo=MODELO_INEXISTENTE_XYZ",
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // =========================================================================
    // OPERACIÓN REST 5: GET /alquileres/{id}/detalle-equipos-productos (QUERY 2)
    // =========================================================================

    @Test
    @Order(16)
    @DisplayName("5.1 GET /alquileres/{id}/detalle-equipos-productos [EXITOSO] - Devuelve detalle consolidado de equipos y productos")
    void test09_ObtenerDetalleEquiposProductos_Exitoso() {
        assertNotNull(alquilerIdCreado, "Debe existir un alquiler creado en el test 1");

        ResponseEntity<DetalleEquiposProductosResponse> response = restTemplate.getForEntity(
                "/alquileres/" + alquilerIdCreado + "/detalle-equipos-productos",
                DetalleEquiposProductosResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(alquilerIdCreado.intValue(), response.getBody().alquilerId());
        assertNotNull(response.getBody().detalles());
        assertFalse(response.getBody().detalles().isEmpty());

        DetalleEquiposProductosResponse.ItemDetalleResponse primerItem = response.getBody().detalles().get(0);
        assertThat(primerItem.equipoCodigo()).isNotEmpty();
        assertThat(primerItem.equipoModelo()).isEqualTo("CAT-320D-HD");
        assertThat(primerItem.productos()).isNotEmpty();
        assertEquals(productoTest.getId(), primerItem.productos().get(0).productoId());
    }

    @Test
    @Order(17)
    @DisplayName("5.2 GET /alquileres/{id}/detalle-equipos-productos [FALLIDO] - Debe responder (404 Not Found) con ID inexistente")
    void test10_ObtenerDetalleEquiposProductos_Inexistente_NotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/alquileres/999999/detalle-equipos-productos",
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
