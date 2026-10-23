package pe.edu.uls.tests_productos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.core.ParameterizedTypeReference;

import pe.edu.uls.tests_productos.cu.adminMantenimiento.request.RequestActualizarFechaRealizado;
import pe.edu.uls.tests_productos.cu.adminMantenimiento.request.RequestMantenimiento;
import pe.edu.uls.tests_productos.cu.adminMantenimiento.response.ResponseMantenimiento;
import pe.edu.uls.tests_productos.cu.adminMantenimiento.response.ResponseProductoConsumido;
import pe.edu.uls.tests_productos.domain.entity.Empleado;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Mantenimiento;
import pe.edu.uls.tests_productos.domain.entity.MantenimientoDetalle;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.repository.EmpleadoRepository;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;
import pe.edu.uls.tests_productos.domain.repository.MantenimientoDetalleRepository;
import pe.edu.uls.tests_productos.domain.repository.MantenimientoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public class TestMantenimiento {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MantenimientoRepository repoMantenimiento;

    @Autowired
    private MantenimientoDetalleRepository repoMantenimientoDetalle;

    @Autowired
    private EmpleadoRepository repoEmpleado;

    @Autowired
    private EquipoRepository repoEquipo;

    @Autowired
    private ProductoRepository repoProducto;

    @Test
    @DisplayName("11. Consulta por ID de Entity Mantenimiento (Cabecera)")
    void testConsultaPorIdMantenimiento() {
        Mantenimiento muestra = repoMantenimiento.findAll().stream().findFirst().orElseThrow();
        Optional<Mantenimiento> encontrado = repoMantenimiento.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getTipo()).isNotBlank();
        assertThat(encontrado.get().getEquipo()).isNotNull();
    }

    @Test
    @DisplayName("12. Consulta por ID de Entity MantenimientoDetalle (Detalle)")
    void testConsultaPorIdMantenimientoDetalle() {
        MantenimientoDetalle muestra = repoMantenimientoDetalle.findAll().stream().findFirst().orElseThrow();
        Optional<MantenimientoDetalle> encontrado = repoMantenimientoDetalle.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getDescripcion()).isNotBlank();
        assertThat(encontrado.get().getCantidad()).isPositive();
    }

    @Test
    @DisplayName("Debe registrar un Mantenimiento con sus ítems de detalle y productos consumidos")
    void testRegistrarMantenimiento() throws Exception {
        Equipo equipo = repoEquipo.findAll().stream().findFirst().orElseThrow();
        Empleado empleado = repoEmpleado.findAll().stream().findFirst().orElseThrow();
        Producto producto1 = repoProducto.findAll().stream().findFirst().orElseThrow();

        int totalMantenimientosAntes = repoMantenimiento.findAll().size();

        RequestMantenimiento.DetalleMantenimientoRequest detalle = new RequestMantenimiento.DetalleMantenimientoRequest(
                List.of(producto1.getId()),
                2,
                "Cambio de componentes preventivos de rutina",
                "Instalación conforme a especificación técnica del fabricante");

        LocalDate fechaSolicitudEnviada = LocalDate.now().minusDays(2);
        LocalDate fechaProgramada = LocalDate.now().plusDays(1);

        RequestMantenimiento requestMantenimiento = new RequestMantenimiento(
                fechaSolicitudEnviada,
                fechaProgramada,
                LocalDate.now(),
                "Preventivo",
                "Mantenimiento de 250 horas programado",
                equipo.getId(),
                empleado.getId(),
                List.of(detalle));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestMantenimiento> requestEntity = new HttpEntity<>(requestMantenimiento, headers);

        ResponseEntity<String> respuestaStr = restTemplate.postForEntity("/mantenimientos", requestEntity,
                String.class);
        assertThat(respuestaStr.getStatusCode()).isEqualTo(HttpStatus.OK);
        String bodyStr = respuestaStr.getBody();
        assertThat(bodyStr).isNotNull();
        tools.jackson.databind.ObjectMapper om = new tools.jackson.databind.ObjectMapper();
        tools.jackson.databind.JsonNode root = om.readTree(bodyStr);

        int idMantenimientoGenerado = root.get("id").asInt();
        assertThat(root.get("equipoId").asInt()).isEqualTo(equipo.getId());
        assertThat(root.get("tipo").asText()).isEqualTo("Preventivo");
        assertThat(root.get("detalles").isArray()).isTrue();
        assertThat(root.get("detalles").size()).isGreaterThanOrEqualTo(1);
        assertThat(root.get("detalles").get(0).get("cantidad").asInt()).isEqualTo(2);
        assertThat(root.get("detalles").get(0).get("productos").isArray()).isTrue();
        assertThat(root.get("detalles").get(0).get("productos").size()).isGreaterThanOrEqualTo(1);
        assertThat(root.get("detalles").get(0).get("observaciones").asText())
                .isEqualTo("Instalación conforme a especificación técnica del fabricante");

        Mantenimiento mantenimientoPersistido = repoMantenimiento.findByIdWithDetalles(idMantenimientoGenerado)
                .orElseThrow();

        assertThat(mantenimientoPersistido.getEquipo().getId()).isEqualTo(equipo.getId());
        assertThat(mantenimientoPersistido.getEmpleado().getId()).isEqualTo(empleado.getId());
        assertThat(mantenimientoPersistido.getFechaSolicitud()).isNotNull();
        assertThat(mantenimientoPersistido.getFechaSolicitud()).isEqualTo(LocalDate.now());
        assertThat(mantenimientoPersistido.getFechaProgramada()).isEqualTo(fechaProgramada);
        assertThat(mantenimientoPersistido.getDetalles()).isNotEmpty();
        assertThat(mantenimientoPersistido.getDetalles().get(0).getCantidad()).isEqualTo(2);

        int totalMantenimientosDespues = repoMantenimiento.findAll().size();
        assertThat(totalMantenimientosDespues).isEqualTo(totalMantenimientosAntes + 1);
    }

    @Test
    @DisplayName("Debe actualizar la fechaRealizado de un mantenimiento existente")
    void testActualizarFechaRealizado() throws Exception {
        Mantenimiento mantenimiento = repoMantenimiento.findAll().stream().findFirst().orElseThrow();
        LocalDate nuevaFecha = LocalDate.of(2026, 10, 20);

        RequestActualizarFechaRealizado request = new RequestActualizarFechaRealizado(nuevaFecha);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestActualizarFechaRealizado> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<ResponseMantenimiento> respuesta = restTemplate.exchange(
                "/mantenimientos/{id}", HttpMethod.PATCH, requestEntity,
                ResponseMantenimiento.class, mantenimiento.getId());

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseMantenimiento actualizadoBody = respuesta.getBody();
        assertThat(actualizadoBody).isNotNull();
        assertThat(actualizadoBody.id()).isEqualTo(mantenimiento.getId());
        assertThat(actualizadoBody.fechaRealizado()).isEqualTo(nuevaFecha);

        Mantenimiento actualizado = repoMantenimiento.findById(mantenimiento.getId()).orElseThrow();
        assertThat(actualizado.getFechaRealizado()).isEqualTo(nuevaFecha);
    }

    @Test
    @DisplayName("Debe listar los mantenimientos por técnico y tipo")
    public void testMantenimientosPorTecnicoYTipo() {
        ResponseEntity<List<ResponseMantenimiento>> respuesta = restTemplate.exchange(
                "/mantenimientos/por-tecnico?dni=45871234&tipo=Preventivo",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResponseMantenimiento>>() {
                });

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        List<ResponseMantenimiento> resultados = respuesta.getBody();
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
        resultados.forEach(m -> {
            assertTrue("Preventivo".equalsIgnoreCase(m.tipo()));
        });
    }

    @Test
    @DisplayName("Debe listar el resumen de productos consumidos por equipo")
    public void testResumenProductosConsumidosPorEquipo() {
        ResponseEntity<List<ResponseProductoConsumido>> respuesta = restTemplate.exchange(
                "/mantenimientos/resumen-productos/GE-001",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResponseProductoConsumido>>() {
                });

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        List<ResponseProductoConsumido> resultados = respuesta.getBody();
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
        resultados.forEach(r -> {
            assertNotNull(r.codigoProducto());
            assertNotNull(r.nombreProducto());
            assertTrue(r.totalUsos() > 0);
        });
    }
}
