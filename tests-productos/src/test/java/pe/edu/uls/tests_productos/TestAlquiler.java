package pe.edu.uls.tests_productos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.databind.ObjectMapper;

import pe.edu.uls.tests_productos.cu.adminAlquiler.request.RequestAlquiler;
import pe.edu.uls.tests_productos.domain.entity.Alquiler;
import pe.edu.uls.tests_productos.domain.entity.AlquilerDetalle;
import pe.edu.uls.tests_productos.domain.entity.Cliente;
import pe.edu.uls.tests_productos.domain.entity.Entidad;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.repository.AlquilerDetalleRepository;
import pe.edu.uls.tests_productos.domain.repository.AlquilerRepository;
import pe.edu.uls.tests_productos.domain.repository.ClienteRepository;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TestAlquiler {

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
    private AlquilerRepository repoAlquiler;

    @Autowired
    private AlquilerDetalleRepository repoAlquilerDetalle;

    @Test
    @DisplayName("9. Consulta por ID de Entity Alquiler (Cabecera)")
    void testConsultaPorIdAlquiler() {
        Alquiler muestra = repoAlquiler.findAll().stream().findFirst().orElseThrow();
        Optional<Alquiler> encontrado = repoAlquiler.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getEntidad()).isNotNull();
    }

    @Test
    @DisplayName("10. Consulta por ID de Entity AlquilerDetalle (Detalle)")
    void testConsultaPorIdAlquilerDetalle() {
        AlquilerDetalle muestra = repoAlquilerDetalle.findAll().stream().findFirst().orElseThrow();
        Optional<AlquilerDetalle> encontrado = repoAlquilerDetalle.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getEquipo()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("Debe registrar un Alquiler con sus ítems de detalle y productos asociados")
    void testRegistrarAlquilerConDetalles() throws Exception {
        Cliente cliente = repoCliente.findAll().stream().findFirst().orElseThrow();
        Entidad entidad = cliente.getEntidad();
        Equipo equipo = repoEquipo.findAll().stream().findFirst().orElseThrow();
        Producto producto1 = repoProducto.findAll().stream().findFirst().orElseThrow();

        int totalAlquileresAntes = repoAlquiler.findAll().size();

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

        int idAlquilerGenerado = objectMapper.readTree(responseContent).get("id").asInt();
        Alquiler alquilerPersistido = repoAlquiler.findById(idAlquilerGenerado).orElseThrow();

        assertThat(alquilerPersistido.getEntidad().getId()).isEqualTo(entidad.getId());
        assertThat(alquilerPersistido.getDetalles()).isNotEmpty();
        assertThat(alquilerPersistido.getDetalles().get(0).getEquipo().getId()).isEqualTo(equipo.getId());
        assertThat(alquilerPersistido.getDetalles().get(0).getProductos()).isNotEmpty();

        int totalAlquileresDespues = repoAlquiler.findAll().size();
        assertThat(totalAlquileresDespues).isEqualTo(totalAlquileresAntes + 1);
    }
}
