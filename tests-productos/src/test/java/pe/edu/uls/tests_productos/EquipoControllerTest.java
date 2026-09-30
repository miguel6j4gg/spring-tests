package pe.edu.uls.tests_productos;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class EquipoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EquipoRepository equipoRepository;

    // =====================================================
    // TEST 1 - GET LISTAR EQUIPOS
    // =====================================================
    @Test
    public void testListarEquipos() throws Exception {
        mockMvc.perform(get("/equipos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // =====================================================
    // TEST 2 - GET BUSCAR POR ID
    // =====================================================
    @Test
    public void testObtenerEquipo() throws Exception {
        Equipo equipo = equipoRepository.findAll().get(0);
        mockMvc.perform(get("/equipos/" + equipo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equipo.getId()));
    }

    // =====================================================
    // TEST 3 - GET BUSCAR POR UBICACION
    // =====================================================
    @Test
    public void testBuscarPorUbicacion() throws Exception {
        mockMvc.perform(get("/equipos/ubicacion/Arequipa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // =====================================================
    // TEST 4 - POST REGISTRAR EQUIPO
    // =====================================================
    @Test
    public void testRegistrarEquipo() throws Exception {
        String codigoUnico = "GE-TST-" + System.currentTimeMillis();
        String json = """
                {
                    "codigo": "%s",
                    "nombre": "Grupo Electrógeno Test",
                    "modelo": "Cummins C100",
                    "potencia": 400.0,
                    "horometro": 500.0,
                    "ubicacion": "Arequipa",
                    "combustible": "Diesel",
                    "estado": "Disponible"
                }
                """.formatted(codigoUnico);

        mockMvc.perform(post("/equipos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(codigoUnico));
    }
}