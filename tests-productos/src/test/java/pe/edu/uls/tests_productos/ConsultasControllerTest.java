package pe.edu.uls.tests_productos;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.uls.tests_productos.cu.consultas.dto.ResumenAlquilerClienteDTO;
import pe.edu.uls.tests_productos.cu.consultas.dto.ResumenMantenimientoEquipoDTO;
import pe.edu.uls.tests_productos.cu.consultas.service.ConsultasService;
import pe.edu.uls.tests_productos.domain.entity.Alquiler;
import pe.edu.uls.tests_productos.domain.entity.Cliente;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Mantenimiento;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.repository.ClienteRepository;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;

@SpringBootTest
public class ConsultasControllerTest {

    @Autowired
    private ConsultasService consultasService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    // =====================================================
    // TEST 1: JPQL 1 - Alquileres por Cliente y Estado
    // =====================================================
    @Test
    public void testJpql1AlquileresPorClienteYEstado() {
        List<Alquiler> resultados = consultasService.buscarAlquileresPorClienteYEstado("Graña", "Activo");
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
        assertEquals("Activo", resultados.get(0).getEstado());
    }

    @Test
    public void testJpql1ParametrosInvalidos() {
        assertThrows(ResponseStatusException.class, () -> 
            consultasService.buscarAlquileresPorClienteYEstado("", "Activo")
        );
        assertThrows(ResponseStatusException.class, () -> 
            consultasService.buscarAlquileresPorClienteYEstado("Graña", "")
        );
    }

    // =====================================================
    // TEST 2: JPQL 2 - Alquileres por Modelo de Equipo
    // =====================================================
    @Test
    public void testJpql2AlquileresPorModeloEquipo() {
        List<Alquiler> resultados = consultasService.buscarAlquileresPorModeloEquipo("Caterpillar");
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
    }

    @Test
    public void testJpql2ParametrosInvalidos() {
        assertThrows(ResponseStatusException.class, () -> 
            consultasService.buscarAlquileresPorModeloEquipo("   ")
        );
    }

    // =====================================================
    // TEST 3: JPQL 3 - Mantenimientos por Ubicación
    // =====================================================
    @Test
    public void testJpql3MantenimientosPorUbicacion() {
        List<Mantenimiento> resultados = consultasService.buscarMantenimientosPorUbicacion("Cusco");
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
    }

    @Test
    public void testJpql3ParametrosInvalidos() {
        assertThrows(ResponseStatusException.class, () -> 
            consultasService.buscarMantenimientosPorUbicacion(null)
        );
    }

    // =====================================================
    // TEST 4: JPQL 4 - Mantenimientos por Código de Producto
    // =====================================================
    @Test
    public void testJpql4MantenimientosPorCodigoProducto() {
        List<Mantenimiento> resultados = consultasService.buscarMantenimientosPorCodigoProducto("PROD-004");
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
    }

    @Test
    public void testJpql4ParametrosInvalidos() {
        assertThrows(ResponseStatusException.class, () -> 
            consultasService.buscarMantenimientosPorCodigoProducto("")
        );
    }

    // =====================================================
    // TEST 5: JPQL 5 - Productos Alquilados en Rango de Fechas
    // =====================================================
    @Test
    public void testJpql5ProductosAlquiladosEnFechas() {
        List<Producto> resultados = consultasService.buscarProductosAlquiladosEnFechas(
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 9, 30)
        );
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
    }

    @Test
    public void testJpql5FechasInvalidas() {
        assertThrows(ResponseStatusException.class, () -> 
            consultasService.buscarProductosAlquiladosEnFechas(
                    LocalDate.of(2026, 10, 1),
                    LocalDate.of(2026, 8, 1)
            )
        );
    }

    // =====================================================
    // TEST 6: JPQL 6 - Equipos por Tipo de Mantenimiento
    // =====================================================
    @Test
    public void testJpql6EquiposPorTipoMantenimiento() {
        List<Equipo> resultados = consultasService.buscarEquiposPorTipoMantenimiento("Preventivo");
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
    }

    @Test
    public void testJpql6ParametrosInvalidos() {
        assertThrows(ResponseStatusException.class, () -> 
            consultasService.buscarEquiposPorTipoMantenimiento("")
        );
    }

    // =====================================================
    // TEST 7: NATIVO 1 - Resumen Alquileres por Cliente
    // =====================================================
    @Test
    public void testNativo1ResumenAlquileresPorCliente() {
        Cliente cliente = clienteRepository.findAll().get(0);
        List<ResumenAlquilerClienteDTO> resultados = consultasService.resumenAlquileresPorCliente(cliente.getId());
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
    }

    @Test
    public void testNativo1ClienteIdInvalido() {
        assertThrows(ResponseStatusException.class, () -> 
            consultasService.resumenAlquileresPorCliente(-1)
        );
        assertThrows(ResponseStatusException.class, () -> 
            consultasService.resumenAlquileresPorCliente(0)
        );
    }

    // =====================================================
    // TEST 8: NATIVO 2 - Resumen Mantenimientos por Equipo
    // =====================================================
    @Test
    public void testNativo2ResumenMantenimientosPorEquipo() {
        Equipo equipo = equipoRepository.findAll().stream()
                .filter(e -> "GE-004".equals(e.getCodigo()))
                .findFirst().orElseGet(() -> equipoRepository.findAll().get(0));

        List<ResumenMantenimientoEquipoDTO> resultados = consultasService.resumenMantenimientosPorEquipo(equipo.getId());
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
    }

    @Test
    public void testNativo2EquipoIdInvalido() {
        assertThrows(ResponseStatusException.class, () -> 
            consultasService.resumenMantenimientosPorEquipo(-5)
        );
    }
}
