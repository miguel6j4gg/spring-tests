package pe.edu.uls.tests_productos;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pe.edu.uls.tests_productos.domain.entity.Almacen;
import pe.edu.uls.tests_productos.domain.entity.Alquiler;
import pe.edu.uls.tests_productos.domain.entity.AlquilerDetalle;
import pe.edu.uls.tests_productos.domain.entity.Cliente;
import pe.edu.uls.tests_productos.domain.entity.Empleado;
import pe.edu.uls.tests_productos.domain.entity.Empresa;
import pe.edu.uls.tests_productos.domain.entity.Entidad;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Mantenimiento;
import pe.edu.uls.tests_productos.domain.entity.MantenimientoDetalle;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.entity.Proveedor;
import pe.edu.uls.tests_productos.domain.repository.AlmacenRepository;
import pe.edu.uls.tests_productos.domain.repository.AlquilerDetalleRepository;
import pe.edu.uls.tests_productos.domain.repository.AlquilerRepository;
import pe.edu.uls.tests_productos.domain.repository.ClienteRepository;
import pe.edu.uls.tests_productos.domain.repository.EmpleadoRepository;
import pe.edu.uls.tests_productos.domain.repository.EmpresaRepository;
import pe.edu.uls.tests_productos.domain.repository.EntidadRepository;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;
import pe.edu.uls.tests_productos.domain.repository.MantenimientoDetalleRepository;
import pe.edu.uls.tests_productos.domain.repository.MantenimientoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProveedorRepository;

/**
 * Tarea Parte A:
 * Consulta por ID de cada Entity del modelo de datos.
 * Se prueban 12 entidades del modelo relacional.
 */
@SpringBootTest
public class ConsultaPorIdTest {

    @Autowired
    private AlmacenRepository repoAlmacen;

    @Autowired
    private EmpleadoRepository repoEmpleado;

    @Autowired
    private ProveedorRepository repoProveedor;

    @Autowired
    private ClienteRepository repoCliente;

    @Autowired
    private EmpresaRepository repoEmpresa;

    @Autowired
    private EntidadRepository repoEntidad;

    @Autowired
    private EquipoRepository repoEquipo;

    @Autowired
    private ProductoRepository repoProducto;

    @Autowired
    private AlquilerRepository repoAlquiler;

    @Autowired
    private AlquilerDetalleRepository repoAlquilerDetalle;

    @Autowired
    private MantenimientoRepository repoMantenimiento;

    @Autowired
    private MantenimientoDetalleRepository repoMantenimientoDetalle;

    @Test
    @DisplayName("1. Consulta por ID de Entity Almacen")
    void testConsultaPorIdAlmacen() {
        Almacen muestra = repoAlmacen.findAll().stream().findFirst().orElseThrow();
        Optional<Almacen> encontrado = repoAlmacen.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getCodigo()).isEqualTo(muestra.getCodigo());
        assertThat(encontrado.get().getNombre()).isNotBlank();
    }

    @Test
    @DisplayName("2. Consulta por ID de Entity Empleado")
    void testConsultaPorIdEmpleado() {
        Empleado muestra = repoEmpleado.findAll().stream().findFirst().orElseThrow();
        Optional<Empleado> encontrado = repoEmpleado.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getDni()).isEqualTo(muestra.getDni());
        assertThat(encontrado.get().getNombre()).isNotBlank();
    }

    @Test
    @DisplayName("3. Consulta por ID de Entity Proveedor")
    void testConsultaPorIdProveedor() {
        Proveedor muestra = repoProveedor.findAll().stream().findFirst().orElseThrow();
        Optional<Proveedor> encontrado = repoProveedor.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getRuc()).isEqualTo(muestra.getRuc());
        assertThat(encontrado.get().getRazonSocial()).isNotBlank();
    }

    @Test
    @DisplayName("4. Consulta por ID de Entity Cliente")
    void testConsultaPorIdCliente() {
        Cliente muestra = repoCliente.findAll().stream().findFirst().orElseThrow();
        Optional<Cliente> encontrado = repoCliente.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getDocumento()).isEqualTo(muestra.getDocumento());
        assertThat(encontrado.get().getNombre()).isNotBlank();
    }

    @Test
    @DisplayName("5. Consulta por ID de Entity Empresa")
    void testConsultaPorIdEmpresa() {
        // Obtenemos una empresa existente o persistimos una controlada si no hay registradas
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

    @Test
    @DisplayName("6. Consulta por ID de Entity Entidad")
    void testConsultaPorIdEntidad() {
        Entidad muestra = repoEntidad.findAll().stream().findFirst().orElseThrow();
        Optional<Entidad> encontrado = repoEntidad.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getTelefono()).isNotBlank();
    }

    @Test
    @DisplayName("7. Consulta por ID de Entity Equipo")
    void testConsultaPorIdEquipo() {
        Equipo muestra = repoEquipo.findAll().stream().findFirst().orElseThrow();
        Optional<Equipo> encontrado = repoEquipo.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getCodigo()).isEqualTo(muestra.getCodigo());
        assertThat(encontrado.get().getModelo()).isNotBlank();
    }

    @Test
    @DisplayName("8. Consulta por ID de Entity Producto")
    void testConsultaPorIdProducto() {
        Producto muestra = repoProducto.findAll().stream().findFirst().orElseThrow();
        Optional<Producto> encontrado = repoProducto.findById(muestra.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getId()).isEqualTo(muestra.getId());
        assertThat(encontrado.get().getCodigo()).isEqualTo(muestra.getCodigo());
        assertThat(encontrado.get().getPrecio()).isPositive();
    }

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
}
