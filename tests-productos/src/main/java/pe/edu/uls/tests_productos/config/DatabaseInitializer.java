package pe.edu.uls.tests_productos.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.uls.tests_productos.domain.entity.Almacen;
import pe.edu.uls.tests_productos.domain.entity.Alquiler;
import pe.edu.uls.tests_productos.domain.entity.AlquilerDetalle;
import pe.edu.uls.tests_productos.domain.entity.AlquilerDetalleProducto;
import pe.edu.uls.tests_productos.domain.entity.Cliente;
import pe.edu.uls.tests_productos.domain.entity.Empleado;
import pe.edu.uls.tests_productos.domain.entity.Entidad;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Mantenimiento;
import pe.edu.uls.tests_productos.domain.entity.MantenimientoDetalle;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.entity.Proveedor;
import pe.edu.uls.tests_productos.domain.repository.AlmacenRepository;
import pe.edu.uls.tests_productos.domain.repository.AlquilerRepository;
import pe.edu.uls.tests_productos.domain.repository.ClienteRepository;
import pe.edu.uls.tests_productos.domain.repository.EmpleadoRepository;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;
import pe.edu.uls.tests_productos.domain.repository.MantenimientoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProveedorRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final EquipoRepository equipoRepository;
    private final ProductoRepository productoRepository;
    private final AlquilerRepository alquilerRepository;
    private final MantenimientoRepository mantenimientoRepository;
    private final ProveedorRepository proveedorRepository;
    private final AlmacenRepository almacenRepository;
    private final EmpleadoRepository empleadoRepository;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(
            ClienteRepository clienteRepository,
            EquipoRepository equipoRepository,
            ProductoRepository productoRepository,
            AlquilerRepository alquilerRepository,
            MantenimientoRepository mantenimientoRepository,
            ProveedorRepository proveedorRepository,
            AlmacenRepository almacenRepository,
            EmpleadoRepository empleadoRepository,
            org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        this.clienteRepository = clienteRepository;
        this.equipoRepository = equipoRepository;
        this.productoRepository = productoRepository;
        this.alquilerRepository = alquilerRepository;
        this.mantenimientoRepository = mantenimientoRepository;
        this.proveedorRepository = proveedorRepository;
        this.almacenRepository = almacenRepository;
        this.empleadoRepository = empleadoRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        System.out.println("Inicializando/verificando datos de prueba en Neon DB...");

        // 1. Proveedores
        Proveedor prov1 = proveedorRepository.findAll().stream()
                .filter(p -> "20100055441".equals(p.getRuc()))
                .findFirst().orElseGet(() -> {
                    Proveedor p = new Proveedor();
                    p.setRuc("20100055441");
                    p.setRazonSocial("Ferreyros S.A. - Caterpillar Perú");
                    p.setContacto("Carlos Ramos");
                    p.setTelefono("988776655");
                    p.setEmail("contacto@ferreyros.com.pe");
                    p.setDireccion("Av. Industrial 450, Lima");
                    p.setEstado(1);
                    return proveedorRepository.save(p);
                });

        Proveedor prov2 = proveedorRepository.findAll().stream()
                .filter(p -> "20200088992".equals(p.getRuc()))
                .findFirst().orElseGet(() -> {
                    Proveedor p = new Proveedor();
                    p.setRuc("20200088992");
                    p.setRazonSocial("Distribuidora Cummins del Perú S.A.C.");
                    p.setContacto("Mariana Silva");
                    p.setTelefono("977665544");
                    p.setEmail("ventas@cummins.pe");
                    p.setDireccion("Carretera Central Km 8.5, Ate");
                    p.setEstado(1);
                    return proveedorRepository.save(p);
                });

        Proveedor prov3 = proveedorRepository.findAll().stream()
                .filter(p -> "20300099881".equals(p.getRuc()))
                .findFirst().orElseGet(() -> {
                    Proveedor p = new Proveedor();
                    p.setRuc("20300099881");
                    p.setRazonSocial("Insumos & Filtros Industriales S.A.C.");
                    p.setContacto("Pedro Gutierrez");
                    p.setTelefono("966554433");
                    p.setEmail("operaciones@filtrosperu.com");
                    p.setDireccion("Av. Argentina 2100, Callao");
                    p.setEstado(1);
                    return proveedorRepository.save(p);
                });

        // 2. Almacenes
        Almacen alm1 = almacenRepository.findAll().stream()
                .filter(a -> "ALM-01".equals(a.getCodigo()))
                .findFirst().orElseGet(() -> {
                    Almacen a = new Almacen();
                    a.setCodigo("ALM-01");
                    a.setNombre("Patio Central de Maquinaria Lima");
                    a.setDireccion("Av. Néstor Gambetta 1500");
                    a.setCiudad("Lima");
                    a.setCapacidad(50.0);
                    a.setEstado(1);
                    return almacenRepository.save(a);
                });

        Almacen alm2 = almacenRepository.findAll().stream()
                .filter(a -> "ALM-02".equals(a.getCodigo()))
                .findFirst().orElseGet(() -> {
                    Almacen a = new Almacen();
                    a.setCodigo("ALM-02");
                    a.setNombre("Base Operativa Arequipa Sur");
                    a.setDireccion("Parque Industrial Río Seco Mz. B");
                    a.setCiudad("Arequipa");
                    a.setCapacidad(30.0);
                    a.setEstado(1);
                    return almacenRepository.save(a);
                });

        Almacen alm3 = almacenRepository.findAll().stream()
                .filter(a -> "ALM-03".equals(a.getCodigo()))
                .findFirst().orElseGet(() -> {
                    Almacen a = new Almacen();
                    a.setCodigo("ALM-03");
                    a.setNombre("Sede Logística Cusco");
                    a.setDireccion("Av. Huayruropata 890");
                    a.setCiudad("Cusco");
                    a.setCapacidad(20.0);
                    a.setEstado(1);
                    return almacenRepository.save(a);
                });

        // 3. Empleados
        Empleado emp1 = empleadoRepository.findAll().stream()
                .filter(e -> "45871234".equals(e.getDni()))
                .findFirst().orElseGet(() -> {
                    Empleado e = new Empleado();
                    e.setDni("45871234");
                    e.setNombre("Carlos");
                    e.setApellido("Mendoza Rojas");
                    e.setCargo("Técnico Especialista en Motores Diésel");
                    e.setTelefono("944332211");
                    e.setEmail("cmendoza@empresa.pe");
                    e.setEstado(1);
                    return empleadoRepository.save(e);
                });

        Empleado emp2 = empleadoRepository.findAll().stream()
                .filter(e -> "48923456".equals(e.getDni()))
                .findFirst().orElseGet(() -> {
                    Empleado e = new Empleado();
                    e.setDni("48923456");
                    e.setNombre("Jorge");
                    e.setApellido("Vega Castillo");
                    e.setCargo("Técnico Electricista Industrial");
                    e.setTelefono("933221100");
                    e.setEmail("jvega@empresa.pe");
                    e.setEstado(1);
                    return empleadoRepository.save(e);
                });

        // 4. Clientes
        Cliente c1 = clienteRepository.findAll().stream()
                .filter(c -> "20100012345".equals(c.getDocumento()))
                .findFirst().orElseGet(() -> {
                    Cliente c = new Cliente();
                    c.setDocumento("20100012345");
                    c.setNombre("Constructora Graña y Montero S.A.");
                    c.setApellido("Empresa");
                    c.setEntidad(entidadCliente("987654321", "Av. Javier Prado 123", "contacto@gym.pe", 1));
                    return clienteRepository.save(c);
                });

        Cliente c2 = clienteRepository.findAll().stream()
                .filter(c -> "20543210987".equals(c.getDocumento()))
                .findFirst().orElseGet(() -> {
                    Cliente c = new Cliente();
                    c.setDocumento("20543210987");
                    c.setNombre("Mining Corp Perú S.A.C.");
                    c.setApellido("Empresa");
                    c.setEntidad(entidadCliente("954321876", "Av. Ejército 456", "operaciones@miningcorp.pe", 1));
                    return clienteRepository.save(c);
                });

        // 5. Equipos
        Equipo e1 = equipoRepository.findAll().stream()
                .filter(e -> "GE-001".equals(e.getCodigo()))
                .findFirst().orElseGet(() -> {
                    Equipo e = new Equipo();
                    e.setCodigo("GE-001");
                    e.setNombre("Grupo Electrógeno 01");
                    e.setModelo("Cummins QSB");
                    e.setPotencia(300.0);
                    e.setHorometro(1250.0);
                    e.setUbicacion("Arequipa");
                    e.setCombustible("Diesel");
                    e.setEstado(1);
                    e.setAlmacen(alm2);
                    return equipoRepository.save(e);
                });

        Equipo e2 = equipoRepository.findAll().stream()
                .filter(e -> "GE-002".equals(e.getCodigo()))
                .findFirst().orElseGet(() -> {
                    Equipo e = new Equipo();
                    e.setCodigo("GE-002");
                    e.setNombre("Grupo Electrógeno 02");
                    e.setModelo("Caterpillar C15");
                    e.setPotencia(500.0);
                    e.setHorometro(850.0);
                    e.setUbicacion("Lima");
                    e.setCombustible("Diesel");
                    e.setEstado(2);
                    e.setAlmacen(alm1);
                    return equipoRepository.save(e);
                });

        Equipo e4 = equipoRepository.findAll().stream()
                .filter(e -> "GE-004".equals(e.getCodigo()))
                .findFirst().orElseGet(() -> {
                    Equipo e = new Equipo();
                    e.setCodigo("GE-004");
                    e.setNombre("Grupo Electrógeno 04");
                    e.setModelo("Perkins 1106");
                    e.setPotencia(250.0);
                    e.setHorometro(600.0);
                    e.setUbicacion("Cusco");
                    e.setCombustible("Diesel");
                    e.setEstado(3);
                    e.setAlmacen(alm3);
                    return equipoRepository.save(e);
                });

        // 6. Productos (Insumos / Añadidos para funcionamiento o mantenimientos)
        Producto p1 = productoRepository.findAll().stream()
                .filter(p -> "PROD-001".equals(p.getCodigo()))
                .findFirst().orElseGet(() -> {
                    Producto p = new Producto();
                    p.setCodigo("PROD-001");
                    p.setNombre("Cables de Fuerza Trifásicos");
                    p.setDescripcion("Cables de potencia vulcanizados 4x50mm");
                    p.setCategoria("Accesorios");
                    p.setPrecio(150.0);
                    p.setStock(20);
                    p.setEstado(1);
                    p.setProveedor(prov1);
                    return productoRepository.save(p);
                });

        Producto p2 = productoRepository.findAll().stream()
                .filter(p -> "PROD-002".equals(p.getCodigo()))
                .findFirst().orElseGet(() -> {
                    Producto p = new Producto();
                    p.setCodigo("PROD-002");
                    p.setNombre("Tablero de Transferencia Automática ATS");
                    p.setDescripcion("Tablero ATS 400A para arranque automático");
                    p.setCategoria("Tableros");
                    p.setPrecio(500.0);
                    p.setStock(10);
                    p.setEstado(1);
                    p.setProveedor(prov2);
                    return productoRepository.save(p);
                });

        Producto p3 = productoRepository.findAll().stream()
                .filter(p -> "PROD-003".equals(p.getCodigo()))
                .findFirst().orElseGet(() -> {
                    Producto p = new Producto();
                    p.setCodigo("PROD-003");
                    p.setNombre("Tanque Auxiliar de Combustible 500L");
                    p.setDescripcion("Tanque de diésel con bomba de trasvase");
                    p.setCategoria("Tanques");
                    p.setPrecio(350.0);
                    p.setStock(8);
                    p.setEstado(1);
                    p.setProveedor(prov1);
                    return productoRepository.save(p);
                });

        Producto p4 = productoRepository.findAll().stream()
                .filter(p -> "PROD-004".equals(p.getCodigo()))
                .findFirst().orElseGet(() -> {
                    Producto p = new Producto();
                    p.setCodigo("PROD-004");
                    p.setNombre("Filtro de Aceite Heavy Duty");
                    p.setDescripcion("Filtro sintético para motor diésel");
                    p.setCategoria("Filtros");
                    p.setPrecio(45.0);
                    p.setStock(50);
                    p.setEstado(1);
                    p.setProveedor(prov3);
                    return productoRepository.save(p);
                });

        Producto p5 = productoRepository.findAll().stream()
                .filter(p -> "PROD-005".equals(p.getCodigo()))
                .findFirst().orElseGet(() -> {
                    Producto p = new Producto();
                    p.setCodigo("PROD-005");
                    p.setNombre("Filtro de Aire Industrial");
                    p.setDescripcion("Filtro primario y secundario de aire");
                    p.setCategoria("Filtros");
                    p.setPrecio(60.0);
                    p.setStock(40);
                    p.setEstado(1);
                    p.setProveedor(prov3);
                    return productoRepository.save(p);
                });

        Producto p6 = productoRepository.findAll().stream()
                .filter(p -> "PROD-006".equals(p.getCodigo()))
                .findFirst().orElseGet(() -> {
                    Producto p = new Producto();
                    p.setCodigo("PROD-006");
                    p.setNombre("Kit de O-Rings de Neopreno");
                    p.setDescripcion("Juego de empaques para sistema hidráulico y tubos");
                    p.setCategoria("Repuestos");
                    p.setPrecio(25.0);
                    p.setStock(100);
                    p.setEstado(1);
                    p.setProveedor(prov3);
                    return productoRepository.save(p);
                });

        // 7. Alquileres
        if (alquilerRepository.count() == 0) {
            Alquiler a1 = new Alquiler();
            a1.setCliente(c1);
            a1.setFechaInicio(LocalDate.of(2026, 8, 1));
            a1.setFechaFin(LocalDate.of(2026, 8, 30));
            a1.setEstado(1);

            AlquilerDetalle ad1 = new AlquilerDetalle();
            ad1.setEquipo(e2);
            ad1.agregarProducto(productoAlquilado(p1));
            ad1.agregarProducto(productoAlquilado(p2));
            ad1.setCantidad(1);
            ad1.setPrecioUnitario(1500.0);
            ad1.setHorometroSalida(800.0);
            ad1.setHorometroRetorno(850.0);
            ad1.setFechaSalida(LocalDateTime.of(2026, 8, 1, 8, 0));
            ad1.setFechaRetorno(LocalDateTime.of(2026, 8, 30, 18, 0));
            ad1.setLugar("Mina Las Bambas");
            ad1.setObservaciones("Entrega en buen estado, verificar horómetro al retorno");
            a1.agregarDetalle(ad1);

            alquilerRepository.save(a1);

            Alquiler a2 = new Alquiler();
            a2.setCliente(c2);
            a2.setFechaInicio(LocalDate.of(2026, 9, 1));
            a2.setFechaFin(LocalDate.of(2026, 9, 25));
            a2.setEstado(2);

            AlquilerDetalle ad2 = new AlquilerDetalle();
            ad2.setEquipo(e1);
            ad2.agregarProducto(productoAlquilado(p1));
            ad2.agregarProducto(productoAlquilado(p3));
            ad2.setCantidad(1);
            ad2.setPrecioUnitario(1200.0);
            ad2.setHorometroSalida(1200.0);
            ad2.setHorometroRetorno(1250.0);
            ad2.setFechaSalida(LocalDateTime.of(2026, 9, 1, 9, 0));
            ad2.setFechaRetorno(LocalDateTime.of(2026, 9, 25, 17, 0));
            ad2.setLugar("Planta Arequipa");
            ad2.setObservaciones("Entrega en buen estado, verificar horómetro al retorno");
            a2.agregarDetalle(ad2);

            alquilerRepository.save(a2);
        }

        // 8. Mantenimientos
        if (mantenimientoRepository.count() == 0) {
            Mantenimiento m1 = new Mantenimiento();
            m1.setEquipo(e4);
            m1.setEmpleado(emp1);
            m1.setFechaMantenimientoRealizado(LocalDate.of(2026, 9, 15));
            m1.setTipo("Preventivo");
            m1.setObservaciones("Mantenimiento rutinario con reemplazo de insumos");

            MantenimientoDetalle md1 = new MantenimientoDetalle();
            md1.setCantidad(3);
            md1.setDescripcion("Reemplazo de filtros de aceite, aire y kit de o-rings");
            md1.setObservaciones("Se realizó el mantenimiento siguiendo el protocolo estándar");
            md1.setProductos(List.of(p4, p5, p6));
            m1.agregarDetalle(md1);

            mantenimientoRepository.save(m1);

            Mantenimiento m2 = new Mantenimiento();
            m2.setEquipo(e1);
            m2.setEmpleado(emp2);
            m2.setFechaMantenimientoRealizado(LocalDate.of(2026, 9, 26));
            m2.setTipo("Correctivo");
            m2.setObservaciones("Cambio de cableado deteriorado y filtro de aceite");

            MantenimientoDetalle md2 = new MantenimientoDetalle();
            md2.setCantidad(2);
            md2.setDescripcion("Cambio de cables trifásicos y filtro");
            md2.setObservaciones("Se reemplazaron los componentes defectuosos y se verificó el funcionamiento");
            md2.setProductos(List.of(p1, p4));
            m2.agregarDetalle(md2);

            mantenimientoRepository.save(m2);
        }

        System.out.println("Base de datos de Neon actualizada con éxito con todos los registros necesarios.");
    }

    private AlquilerDetalleProducto productoAlquilado(Producto producto) {
        AlquilerDetalleProducto detalleProducto = new AlquilerDetalleProducto();
        detalleProducto.setProducto(producto);
        detalleProducto.setPrecio(producto.getPrecio());
        return detalleProducto;
    }

    private Entidad entidadCliente(String telefono, String direccion, String correo, Integer estado) {
        Entidad entidad = new Entidad();
        entidad.setTelefono(telefono);
        entidad.setDireccion(direccion);
        entidad.setCorreo(correo);
        entidad.setEstado(estado);
        return entidad;
    }
}
