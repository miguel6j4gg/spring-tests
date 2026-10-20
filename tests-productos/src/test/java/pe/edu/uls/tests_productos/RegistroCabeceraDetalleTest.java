package pe.edu.uls.tests_productos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import tools.jackson.databind.ObjectMapper;

import pe.edu.uls.tests_productos.cu.adminAlquiler.request.RequestAlquiler;
import pe.edu.uls.tests_productos.cu.adminMantenimiento.request.RequestMantenimiento;
import pe.edu.uls.tests_productos.domain.entity.Alquiler;
import pe.edu.uls.tests_productos.domain.entity.Cliente;
import pe.edu.uls.tests_productos.domain.entity.Empleado;
import pe.edu.uls.tests_productos.domain.entity.Entidad;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Mantenimiento;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.repository.AlquilerRepository;
import pe.edu.uls.tests_productos.domain.repository.ClienteRepository;
import pe.edu.uls.tests_productos.domain.repository.EmpleadoRepository;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;
import pe.edu.uls.tests_productos.domain.repository.MantenimientoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tarea Parte B:
 * Registro de un entity que contenga items (patrón cabecera-detalle).
 * Se implementan dos métodos test de prueba con entities (cabecera) distintas:
 * 1. Alquiler (Cabecera) con AlquilerDetalle (Items/Detalle) y productos asociados.
 * 2. Mantenimiento (Cabecera) con MantenimientoDetalle (Items/Detalle) y productos asociados.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class RegistroCabeceraDetalleTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository repoCliente;

    @Autowired
    private EquipoRepository repoEquipo;

    @Autowired
    private ProductoRepository repoProducto;

    @Autowired
    private EmpleadoRepository repoEmpleado;

    @Autowired
    private AlquilerRepository repoAlquiler;

    @Autowired
    private MantenimientoRepository repoMantenimiento;

    // =========================================================================
    // CASO 1: Cabecera Alquiler -> Detalle AlquilerDetalle
    // =========================================================================
    @Test
    @Transactional
    @DisplayName("Debe registrar un Alquiler con sus ítems de detalle y productos asociados")
    void testRegistrarAlquilerConDetalles() throws Exception {
        // 1. Obtener entidades relacionadas existentes
        Cliente cliente = repoCliente.findAll().stream().findFirst().orElseThrow();
        Entidad entidad = cliente.getEntidad();
        Equipo equipo = repoEquipo.findAll().stream().findFirst().orElseThrow();
        Producto producto1 = repoProducto.findAll().stream().findFirst().orElseThrow();

        int totalAlquileresAntes = repoAlquiler.findAll().size();

        // 2. Construir ítems de detalle (patrón cabecera - detalle)
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

        RequestAlquiler requestAlquiler = new RequestAlquiler(
                LocalDate.now(),
                LocalDate.now().plusDays(15),
                1,
                entidad.getId(),
                2500.0,
                "efectivo",
                0,
                null,
                "Garantía en custodia según contrato",
                List.of(detalle)
        );

        // 3. Ejecutar POST a través de MockMvc
        String responseContent = mockMvc.perform(post("/alquileres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestAlquiler)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.entidadId").value(entidad.getId()))
                .andExpect(jsonPath("$.montoGarantia").value(2500.0))
                .andExpect(jsonPath("$.detalles").isArray())
                .andExpect(jsonPath("$.detalles[0].equipoId").value(equipo.getId()))
                .andExpect(jsonPath("$.detalles[0].observaciones").value("Equipo entregado en óptimas condiciones operativas"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 4. Validar persistencia real de cabecera y detalle en BD
        int idAlquilerGenerado = objectMapper.readTree(responseContent).get("id").asInt();
        Alquiler alquilerPersistido = repoAlquiler.findById(idAlquilerGenerado).orElseThrow();

        assertThat(alquilerPersistido.getEntidad().getId()).isEqualTo(entidad.getId());
        assertThat(alquilerPersistido.getDetalles()).isNotEmpty();
        assertThat(alquilerPersistido.getDetalles().get(0).getEquipo().getId()).isEqualTo(equipo.getId());
        assertThat(alquilerPersistido.getDetalles().get(0).getProductos()).isNotEmpty();

        int totalAlquileresDespues = repoAlquiler.findAll().size();
        assertThat(totalAlquileresDespues).isEqualTo(totalAlquileresAntes + 1);
    }

    // =========================================================================
    // CASO 2: Cabecera Mantenimiento -> Detalle MantenimientoDetalle
    // =========================================================================
    @Test
    @Transactional
    @DisplayName("Debe registrar un Mantenimiento con sus ítems de detalle y productos consumidos")
    void testRegistrarMantenimientoConDetalles() throws Exception {
        // 1. Obtener entidades existentes
        Equipo equipo = repoEquipo.findAll().stream().findFirst().orElseThrow();
        Empleado empleado = repoEmpleado.findAll().stream().findFirst().orElseThrow();
        Producto producto1 = repoProducto.findAll().stream().findFirst().orElseThrow();

        int totalMantenimientosAntes = repoMantenimiento.findAll().size();

        // 2. Construir ítems de detalle (cabecera - detalle)
        RequestMantenimiento.DetalleMantenimientoRequest detalle =
                new RequestMantenimiento.DetalleMantenimientoRequest(
                        List.of(producto1.getId()),
                        2,
                        "Cambio de componentes preventivos de rutina",
                        "Instalación conforme a especificación técnica del fabricante"
                );

        RequestMantenimiento requestMantenimiento = new RequestMantenimiento(
                LocalDate.now().minusDays(2),
                LocalDate.now(),
                LocalDate.now(),
                "Preventivo",
                "Mantenimiento de 250 horas programado",
                equipo.getId(),
                empleado.getId(),
                List.of(detalle)
        );

        // 3. Ejecutar POST a través de MockMvc
        String responseContent = mockMvc.perform(post("/mantenimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMantenimiento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.equipoId").value(equipo.getId()))
                .andExpect(jsonPath("$.tipo").value("Preventivo"))
                .andExpect(jsonPath("$.detalles").isArray())
                .andExpect(jsonPath("$.detalles[0].cantidad").value(2))
                .andExpect(jsonPath("$.detalles[0].observaciones").value("Instalación conforme a especificación técnica del fabricante"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 4. Validar persistencia real de cabecera y detalle en BD
        int idMantenimientoGenerado = objectMapper.readTree(responseContent).get("id").asInt();
        Mantenimiento mantenimientoPersistido = repoMantenimiento.findById(idMantenimientoGenerado).orElseThrow();

        assertThat(mantenimientoPersistido.getEquipo().getId()).isEqualTo(equipo.getId());
        assertThat(mantenimientoPersistido.getEmpleado().getId()).isEqualTo(empleado.getId());
        assertThat(mantenimientoPersistido.getDetalles()).isNotEmpty();
        assertThat(mantenimientoPersistido.getDetalles().get(0).getCantidad()).isEqualTo(2);
        assertThat(mantenimientoPersistido.getDetalles().get(0).getProductos()).isNotEmpty();

        int totalMantenimientosDespues = repoMantenimiento.findAll().size();
        assertThat(totalMantenimientosDespues).isEqualTo(totalMantenimientosAntes + 1);
    }
}
