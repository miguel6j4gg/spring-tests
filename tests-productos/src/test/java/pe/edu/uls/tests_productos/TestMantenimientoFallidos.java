package pe.edu.uls.tests_productos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import pe.edu.uls.tests_productos.cu.adminMantenimiento.request.RequestActualizarFechaRealizado;
import pe.edu.uls.tests_productos.cu.adminMantenimiento.request.RequestMantenimiento;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Mantenimiento;
import pe.edu.uls.tests_productos.domain.entity.MantenimientoDetalle;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;
import pe.edu.uls.tests_productos.domain.repository.MantenimientoDetalleRepository;
import pe.edu.uls.tests_productos.domain.repository.MantenimientoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public class TestMantenimientoFallidos {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MantenimientoRepository repoMantenimiento;

    @Autowired
    private MantenimientoDetalleRepository repoMantenimientoDetalle;

    @Autowired
    private EquipoRepository repoEquipo;

    @Autowired
    private ProductoRepository repoProducto;

    // =========================================================================
    // CASO FALLIDO 1: (Contraparte de testConsultaPorIdMantenimiento)
    // Consulta por ID de Mantenimiento inexistente (404 / Optional vacío)
    // =========================================================================
    @Test
    @DisplayName("F1. Debe responder 404 al consultar un Mantenimiento con ID inexistente")
    void testConsultaPorIdMantenimientoInexistente() {
        int idInexistente = 999999;

        // 1. Verificación vía Repository JPA (Optional vacío)
        Optional<Mantenimiento> encontrado = repoMantenimiento.findById(idInexistente);
        assertThat(encontrado).isEmpty();

        // 2. Verificación vía TestRestTemplate (HTTP 404 Not Found)
        ResponseEntity<String> respuesta = restTemplate.getForEntity("/mantenimientos/" + idInexistente, String.class);
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(respuesta.getBody()).contains("Mantenimiento no encontrado con ID: " + idInexistente);
    }

    // =========================================================================
    // CASO FALLIDO 2: (Contraparte de testConsultaPorIdMantenimientoDetalle)
    // Consulta por ID de MantenimientoDetalle inexistente (404 / Optional vacío)
    // =========================================================================
    @Test
    @DisplayName("F2. Debe responder 404 al consultar un MantenimientoDetalle con ID inexistente")
    void testConsultaPorIdMantenimientoDetalleInexistente() {
        int idInexistente = 999999;

        // 1. Verificación vía Repository JPA (Optional vacío)
        Optional<MantenimientoDetalle> encontrado = repoMantenimientoDetalle.findById(idInexistente);
        assertThat(encontrado).isEmpty();

        // 2. Verificación vía TestRestTemplate (HTTP 404 Not Found)
        ResponseEntity<String> respuesta = restTemplate.getForEntity("/mantenimiento-detalles/" + idInexistente,
                String.class);
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(respuesta.getBody()).contains("Detalle de mantenimiento no encontrado con ID: " + idInexistente);
    }

    // =========================================================================
    // CASO FALLIDO 3: (Contraparte de testRegistrarMantenimiento)
    // Registro fallido por producto inexistente y por fecha programada inválida
    // =========================================================================
    @Test
    @DisplayName("F3.1. Debe rechazar el registro si un producto del detalle no existe (404)")
    void testRegistrarMantenimientoConProductoInexistente() throws Exception {
        Equipo equipo = repoEquipo.findAll().stream().findFirst().orElseThrow();
        int productoInexistenteId = 999999;

        RequestMantenimiento.DetalleMantenimientoRequest detalle = new RequestMantenimiento.DetalleMantenimientoRequest(
                List.of(productoInexistenteId),
                1,
                "Cambio de componentes",
                "Producto inexistente");

        RequestMantenimiento requestMantenimiento = new RequestMantenimiento(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now(),
                "Preventivo",
                "Mantenimiento con producto inexistente",
                equipo.getId(),
                null,
                List.of(detalle));

        int totalMantenimientosAntes = repoMantenimiento.findAll().size();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestMantenimiento> requestEntity = new HttpEntity<>(requestMantenimiento, headers);

        ResponseEntity<String> respuesta = restTemplate.postForEntity("/mantenimientos", requestEntity, String.class);

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(respuesta.getBody()).isEqualTo("El producto con ID " + productoInexistenteId + " no existe");
        assertThat(repoMantenimiento.findAll().size()).isEqualTo(totalMantenimientosAntes);
    }

    @Test
    @DisplayName("F3.2. Debe rechazar el registro con fecha programada de hoy o anterior (400)")
    void testRegistrarMantenimientoConFechaProgramadaInvalida() throws Exception {
        Equipo equipo = repoEquipo.findAll().stream().findFirst().orElseThrow();
        Producto producto = repoProducto.findAll().stream().findFirst().orElseThrow();

        RequestMantenimiento.DetalleMantenimientoRequest detalle = new RequestMantenimiento.DetalleMantenimientoRequest(
                List.of(producto.getId()),
                1,
                "Cambio de componentes",
                "Fecha programada inválida");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        for (LocalDate fechaProgramada : List.of(LocalDate.now(), LocalDate.now().minusDays(1))) {
            RequestMantenimiento requestMantenimiento = new RequestMantenimiento(
                    LocalDate.now(),
                    fechaProgramada,
                    LocalDate.now(),
                    "Preventivo",
                    "Mantenimiento con fecha programada inválida",
                    equipo.getId(),
                    null,
                    List.of(detalle));

            HttpEntity<RequestMantenimiento> requestEntity = new HttpEntity<>(requestMantenimiento, headers);
            ResponseEntity<String> respuesta = restTemplate.postForEntity("/mantenimientos", requestEntity,
                    String.class);

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(respuesta.getBody()).isEqualTo("La fecha programada debe ser posterior al día de hoy");
        }
    }

    // =========================================================================
    // CASO FALLIDO 4: (Contraparte de testActualizarFechaRealizado)
    // Actualización fallida por mantenimiento inexistente y por fecha nula
    // =========================================================================
    @Test
    @DisplayName("F4.1. Debe responder 404 si el mantenimiento a actualizar no existe")
    void testActualizarFechaRealizadoMantenimientoInexistente() throws Exception {
        RequestActualizarFechaRealizado request = new RequestActualizarFechaRealizado(LocalDate.now());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestActualizarFechaRealizado> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> respuesta = restTemplate.exchange(
                "/mantenimientos/{id}", HttpMethod.PATCH, requestEntity,
                String.class, 999999);

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(respuesta.getBody()).isEqualTo("Mantenimiento no encontrado con ID: 999999");
    }

    @Test
    @DisplayName("F4.2. Debe responder 400 si la fechaRealizado enviada es nula")
    void testActualizarFechaRealizadoNula() throws Exception {
        Mantenimiento mantenimiento = repoMantenimiento.findAll().stream().findFirst().orElseThrow();

        RequestActualizarFechaRealizado request = new RequestActualizarFechaRealizado(null);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestActualizarFechaRealizado> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> respuesta = restTemplate.exchange(
                "/mantenimientos/{id}", HttpMethod.PATCH, requestEntity,
                String.class, mantenimiento.getId());

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(respuesta.getBody()).isEqualTo("La fecha realizada no puede ser nula");
    }

    // =========================================================================
    // CASO FALLIDO 5: (Contraparte de testMantenimientosPorTecnicoYTipo)
    // Búsqueda fallida por técnico inexistente y parámetros inválidos
    // =========================================================================
    @Test
    @DisplayName("F5.1. Debe responder 404 al buscar por un DNI de técnico que no existe")
    void testMantenimientosPorTecnicoInexistente() {
        ResponseEntity<String> res = restTemplate.getForEntity(
                "/mantenimientos/por-tecnico?dni=00000000&tipo=Preventivo", String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        assertThat(res.getBody()).contains("Técnico no encontrado con DNI: 00000000");
    }

    @Test
    @DisplayName("F5.2. Debe responder 400 si los parámetros dni o tipo están vacíos")
    void testMantenimientosPorTecnicoParametrosInvalidos() {
        ResponseEntity<String> res1 = restTemplate.getForEntity(
                "/mantenimientos/por-tecnico?dni=&tipo=Preventivo", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res1.getStatusCode());

        ResponseEntity<String> res2 = restTemplate.getForEntity(
                "/mantenimientos/por-tecnico?dni=45871234&tipo= ", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, res2.getStatusCode());
    }

    // =========================================================================
    // CASO FALLIDO 6: (Contraparte de testResumenProductosConsumidosPorEquipo)
    // Resumen fallido por código de equipo inexistente y parámetro inválido
    // =========================================================================
    @Test
    @DisplayName("F6.1. Debe responder 404 al consultar resumen de productos de un equipo inexistente")
    void testResumenProductosConsumidosEquipoInexistente() {
        ResponseEntity<String> res = restTemplate.getForEntity(
                "/mantenimientos/resumen-productos/EQUIPO_NO_EXISTE_999", String.class);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        assertThat(res.getBody()).contains("Equipo no encontrado con código: EQUIPO_NO_EXISTE_999");
    }

    @Test
    @DisplayName("F6.2. Debe rechazar con error de cliente al consultar resumen con código inválido o vacío")
    void testResumenProductosConsumidosParametroInvalido() {
        ResponseEntity<String> res1 = restTemplate.getForEntity(
                "/mantenimientos/resumen-productos/ ", String.class);
        assertTrue(res1.getStatusCode().is4xxClientError());
    }
}
