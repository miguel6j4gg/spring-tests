package pe.edu.uls.tests_productos.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.uls.tests_productos.domain.entity.Alquiler;
import pe.edu.uls.tests_productos.domain.entity.AlquilerDetalle;
import pe.edu.uls.tests_productos.domain.entity.Cliente;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Mantenimiento;
import pe.edu.uls.tests_productos.domain.entity.MantenimientoDetalle;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.repository.AlquilerRepository;
import pe.edu.uls.tests_productos.domain.repository.ClienteRepository;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;
import pe.edu.uls.tests_productos.domain.repository.MantenimientoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final EquipoRepository equipoRepository;
    private final ProductoRepository productoRepository;
    private final AlquilerRepository alquilerRepository;
    private final MantenimientoRepository mantenimientoRepository;

    public DatabaseInitializer(
            ClienteRepository clienteRepository,
            EquipoRepository equipoRepository,
            ProductoRepository productoRepository,
            AlquilerRepository alquilerRepository,
            MantenimientoRepository mantenimientoRepository) {
        this.clienteRepository = clienteRepository;
        this.equipoRepository = equipoRepository;
        this.productoRepository = productoRepository;
        this.alquilerRepository = alquilerRepository;
        this.mantenimientoRepository = mantenimientoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Inicializando/verificando datos de prueba en Neon DB...");

        // 1. Clientes
        Cliente c1 = clienteRepository.findAll().stream()
                .filter(c -> "20100012345".equals(c.getDocumento()))
                .findFirst().orElseGet(() -> {
                    Cliente c = new Cliente();
                    c.setDocumento("20100012345");
                    c.setNombre("Constructora Graña y Montero S.A.");
                    c.setApellido("Empresa");
                    c.setEmail("contacto@gym.pe");
                    c.setTelefono("987654321");
                    c.setDireccion("Av. Javier Prado 123");
                    c.setEstado("Activo");
                    return clienteRepository.save(c);
                });

        Cliente c2 = clienteRepository.findAll().stream()
                .filter(c -> "20543210987".equals(c.getDocumento()))
                .findFirst().orElseGet(() -> {
                    Cliente c = new Cliente();
                    c.setDocumento("20543210987");
                    c.setNombre("Mining Corp Perú S.A.C.");
                    c.setApellido("Empresa");
                    c.setEmail("operaciones@miningcorp.pe");
                    c.setTelefono("954321876");
                    c.setDireccion("Av. Ejército 456");
                    c.setEstado("Activo");
                    return clienteRepository.save(c);
                });

        // 2. Equipos
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
                    e.setEstado("Disponible");
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
                    e.setEstado("Alquilado");
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
                    e.setEstado("Mantenimiento");
                    return equipoRepository.save(e);
                });

        // 3. Productos (Insumos / Añadidos para funcionamiento o mantenimientos)
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
                    p.setEstado("Disponible");
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
                    p.setEstado("Disponible");
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
                    p.setEstado("Disponible");
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
                    p.setEstado("Disponible");
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
                    p.setEstado("Disponible");
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
                    p.setEstado("Disponible");
                    return productoRepository.save(p);
                });

        // 4. Alquileres
        if (alquilerRepository.count() == 0) {
            Alquiler a1 = new Alquiler();
            a1.setCliente(c1);
            a1.setFechaInicio(LocalDate.of(2026, 8, 1));
            a1.setFechaFin(LocalDate.of(2026, 8, 30));
            a1.setEstado("Activo");

            AlquilerDetalle ad1 = new AlquilerDetalle();
            ad1.setEquipo(e2);
            ad1.setProductos(List.of(p1, p2));
            ad1.setCantidad(1);
            ad1.setPrecioUnitario(1500.0);
            ad1.setHorometroSalida(800.0);
            ad1.setHorometroRetorno(850.0);
            ad1.setFechaSalida(LocalDateTime.of(2026, 8, 1, 8, 0));
            ad1.setFechaRetorno(LocalDateTime.of(2026, 8, 30, 18, 0));
            ad1.setLugar("Mina Las Bambas");
            a1.agregarDetalle(ad1);

            alquilerRepository.save(a1);

            Alquiler a2 = new Alquiler();
            a2.setCliente(c2);
            a2.setFechaInicio(LocalDate.of(2026, 9, 1));
            a2.setFechaFin(LocalDate.of(2026, 9, 25));
            a2.setEstado("Finalizado");

            AlquilerDetalle ad2 = new AlquilerDetalle();
            ad2.setEquipo(e1);
            ad2.setProductos(List.of(p1, p3));
            ad2.setCantidad(1);
            ad2.setPrecioUnitario(1200.0);
            ad2.setHorometroSalida(1200.0);
            ad2.setHorometroRetorno(1250.0);
            ad2.setFechaSalida(LocalDateTime.of(2026, 9, 1, 9, 0));
            ad2.setFechaRetorno(LocalDateTime.of(2026, 9, 25, 17, 0));
            ad2.setLugar("Planta Arequipa");
            a2.agregarDetalle(ad2);

            alquilerRepository.save(a2);
        }

        // 5. Mantenimientos
        if (mantenimientoRepository.count() == 0) {
            Mantenimiento m1 = new Mantenimiento();
            m1.setEquipo(e4);
            m1.setFecha(LocalDate.of(2026, 9, 15));
            m1.setTipo("Preventivo");
            m1.setObservaciones("Mantenimiento rutinario con reemplazo de insumos");

            MantenimientoDetalle md1 = new MantenimientoDetalle();
            md1.setCantidad(3);
            md1.setDescripcion("Reemplazo de filtros de aceite, aire y kit de o-rings");
            md1.setProductos(List.of(p4, p5, p6));
            m1.agregarDetalle(md1);

            mantenimientoRepository.save(m1);

            Mantenimiento m2 = new Mantenimiento();
            m2.setEquipo(e1);
            m2.setFecha(LocalDate.of(2026, 9, 26));
            m2.setTipo("Correctivo");
            m2.setObservaciones("Cambio de cableado deteriorado y filtro de aceite");

            MantenimientoDetalle md2 = new MantenimientoDetalle();
            md2.setCantidad(2);
            md2.setDescripcion("Cambio de cables trifásicos y filtro");
            md2.setProductos(List.of(p1, p4));
            m2.agregarDetalle(md2);

            mantenimientoRepository.save(m2);
        }

        System.out.println("Base de datos de Neon actualizada con éxito con todos los registros necesarios.");
    }
}
