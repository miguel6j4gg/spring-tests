package pe.edu.uls.tests_productos;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import pe.edu.uls.tests_productos.controller.EquipoController;

@WebMvcTest(EquipoController.class)
public class EquipoControllerTest {

    @Autowired
    private MockMvc mockMvc;


    // =====================================================
    // TEST 1 - GET LISTAR EQUIPOS
    // =====================================================

    @Test
    public void testListarEquipos() throws Exception {

        mockMvc.perform(
                get("/equipos")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].codigo").value("GE-001"))
        .andExpect(jsonPath("$[0].nombre")
                .value("Grupo Electrógeno 01"));
    }


    // =====================================================
    // TEST 2 - GET BUSCAR POR CODIGO
    // =====================================================

    @Test
    public void testObtenerEquipo() throws Exception {

        mockMvc.perform(
                get("/equipos/GE-001")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.codigo").value("GE-001"))
        .andExpect(jsonPath("$.potencia").value(500));
    }


    // =====================================================
    // TEST 3 - GET BUSCAR POR UBICACION
    // =====================================================

    @Test
    public void testBuscarPorUbicacion() throws Exception {

        mockMvc.perform(
                get("/equipos/ubicacion/Arequipa")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].ubicacion").value("Arequipa"))
        .andExpect(jsonPath("$[1].ubicacion").value("Arequipa"));
    }


    // =====================================================
    // TEST 4 - GET BUSCAR POR TIPO
    // =====================================================

    @Test
    public void testBuscarPorTipo() throws Exception {

        mockMvc.perform(
                get("/equipos/tipo/Diesel")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].combustible").value("Diesel"))
        .andExpect(jsonPath("$[1].combustible").value("Diesel"));
    }


    // =====================================================
    // TEST 5 - GET MAYOR POTENCIA
    // =====================================================

    @Test
    public void testEquiposMayorPotencia() throws Exception {

        mockMvc.perform(
                get("/equipos/mayor-potencia/400")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].potencia").value(500))
        .andExpect(jsonPath("$[1].potencia").value(450));
    }


    // =====================================================
    // TEST 6 - POST REGISTRAR EQUIPO
    // =====================================================

    @Test
    public void testRegistrarEquipo() throws Exception {

        String json = """
                {
                    "codigo": "GE-010",
                    "nombre": "Grupo Electrógeno Nuevo",
                    "modelo": "Cummins C100",
                    "potencia": 400,
                    "horometro": 500,
                    "ubicacion": "Arequipa",
                    "combustible": "Diesel",
                    "estado": "Disponible"
                }
                """;

        mockMvc.perform(
                post("/equipos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.codigo").value("GE-010"))
        .andExpect(jsonPath("$.potencia").value(400));
    }


    // =====================================================
    // TEST 7 - POST CALCULAR COMBUSTIBLE
    // =====================================================

    @Test
    public void testCalcularCombustible() throws Exception {

        String json = """
                {
                    "codigo": "GE-001",
                    "nombre": "Grupo Electrógeno 01",
                    "modelo": "Caterpillar C15",
                    "potencia": 500,
                    "horometro": 1200,
                    "ubicacion": "Arequipa",
                    "combustible": "Diesel",
                    "estado": "Disponible"
                }
                """;

        mockMvc.perform(
                post("/equipos/calcular-combustible")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.potencia").value(500))
        .andExpect(jsonPath("$.combustible")
                .value("Consumo estimado: 100.0 L/h"));
    }


    // =====================================================
    // TEST 8 - POST ACTUALIZAR HOROMETRO
    // =====================================================

    @Test
    public void testActualizarHorometro() throws Exception {

        String json = """
                {
                    "codigo": "GE-002",
                    "nombre": "Grupo Electrógeno 02",
                    "modelo": "Perkins 1106",
                    "potencia": 300,
                    "horometro": 850,
                    "ubicacion": "Miraflores",
                    "combustible": "Diesel",
                    "estado": "Alquilado"
                }
                """;

        mockMvc.perform(
                post("/equipos/actualizar-horometro")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.horometro").value(950))
        .andExpect(jsonPath("$.codigo").value("GE-002"));
    }


    // =====================================================
    // TEST 9 - POST REGISTRAR SERVICIO
    // =====================================================

    @Test
    public void testRegistrarServicio() throws Exception {

        String json = """
                {
                    "codigo": "GE-003",
                    "nombre": "Grupo Electrógeno 03",
                    "modelo": "Cummins QSB",
                    "potencia": 200,
                    "horometro": 600,
                    "ubicacion": "Arequipa",
                    "combustible": "Diesel",
                    "estado": "Disponible"
                }
                """;

        mockMvc.perform(
                post("/equipos/registrar-servicio")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.codigo").value("GE-003"))
        .andExpect(jsonPath("$.estado").value("En servicio"));
    }


    // =====================================================
    // TEST 10 - POST CAMBIAR ESTADO
    // =====================================================

    @Test
    public void testCambiarEstado() throws Exception {

        String json = """
                {
                    "codigo": "GE-004",
                    "nombre": "Grupo Electrógeno 04",
                    "modelo": "Perkins 1104",
                    "potencia": 250,
                    "horometro": 700,
                    "ubicacion": "Arequipa",
                    "combustible": "Diesel",
                    "estado": "Disponible"
                }
                """;

        mockMvc.perform(
                post("/equipos/cambiar-estado")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.codigo").value("GE-004"))
        .andExpect(jsonPath("$.estado").value("Mantenimiento"));
    }
}