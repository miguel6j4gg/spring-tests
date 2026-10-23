package pe.edu.uls.tests_productos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import pe.edu.uls.tests_productos.cu.adminAlquiler.request.RequestAlquiler;
import pe.edu.uls.tests_productos.domain.entity.Cliente;
import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.entity.Producto;
import pe.edu.uls.tests_productos.domain.repository.AlquilerDetalleRepository;
import pe.edu.uls.tests_productos.domain.repository.ClienteRepository;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;
import pe.edu.uls.tests_productos.domain.repository.ProductoRepository;

/**
 * Pruebas de integración HTTP real mediante TestRestTemplate.
 * 
 * =========================================================================
 * DEMOSTRACIÓN DE CONCURRENCIA PARA LABORATORIO:
 * Caso A: Registro CON control de bloqueo (@Lock PESSIMISTIC_WRITE) ->
 * COMPORTAMIENTO CORRECTO
 * Caso B: Registro SIN control de bloqueo (sin @Lock) -> COMPORTAMIENTO
 * INCORRECTO (Race Condition)
 * =========================================================================
 * 
 * ¿Por qué TestRestTemplate y no MockMvc?
 * - MockMvc simula peticiones en memoria dentro del mismo hilo del test y bajo
 * una sola transacción compartida.
 * - TestRestTemplate se comunica con Tomcat embebido en un puerto real HTTP;
 * cada petición viaja sobre TCP
 * y corre en su propio hilo del servidor y en su propia transacción de base de
 * datos aislada, lo que
 * permite evaluar el comportamiento de concurrencia y transacciones
 * concurrentes reales en PostgreSQL.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@org.junit.jupiter.api.TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class AlquilerEquipoRestTemplateTest {

    private static final Logger log = LoggerFactory.getLogger(AlquilerEquipoRestTemplateTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private AlquilerDetalleRepository alquilerDetalleRepository;

    private Cliente cliente1;
    private Cliente cliente2;
    private Producto producto;

    @BeforeEach
    void setUp() {
        // Calentar DispatcherServlet y pool de conexiones para evitar latencia de
        // arranque frío
        try {
            restTemplate.getForEntity("/equipos", String.class);
        } catch (Exception ignored) {
        }

        List<Cliente> clientes = clienteRepository.findAll();
        if (clientes.size() >= 2) {
            cliente1 = clientes.get(0);
            cliente2 = clientes.get(1);
        } else {
            Cliente c1 = new Cliente();
            c1.setNombre("Carlos");
            c1.setApellido("Mendoza");
            c1.setDocumento("10293847");
            cliente1 = clienteRepository.save(c1);

            Cliente c2 = new Cliente();
            c2.setNombre("Lucia");
            c2.setApellido("Paredes");
            c2.setDocumento("87654321");
            cliente2 = clienteRepository.save(c2);
        }

        producto = productoRepository.findAll().stream().findFirst().orElse(null);
    }

    private Equipo crearGrupoElectrogenoDisponible(String prefijo, String nombre) {
        Equipo equipo = new Equipo();
        equipo.setCodigo(prefijo + System.currentTimeMillis());
        equipo.setNombre(nombre);
        equipo.setModelo("Cummins C100");
        equipo.setPotencia(450.0);
        equipo.setHorometro(350.0);
        equipo.setUbicacion("Lima");
        equipo.setCombustible("Diesel");
        equipo.setEstado(1); // 1 = Disponible
        return equipoRepository.save(equipo);
    }

    private RequestAlquiler construirRequestAlquiler(int clienteId, int equipoId) {
        List<Integer> prodIds = producto != null ? List.of(producto.getId()) : List.of();
        List<RequestAlquiler.DetalleAlquilerRequest.ProductoAlquilerRequest> prodReq = producto != null
                ? List.of(new RequestAlquiler.DetalleAlquilerRequest.ProductoAlquilerRequest(producto.getId(),
                        producto.getPrecio()))
                : List.of();

        RequestAlquiler.DetalleAlquilerRequest detalle = new RequestAlquiler.DetalleAlquilerRequest(
                equipoId,
                prodIds,
                prodReq,
                1,
                1800.0,
                100.0,
                150.0,
                "Proyecto Minero Central",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                null,
                "Operativo para obra");

        return new RequestAlquiler(
                LocalDate.now(),
                LocalDate.now().plusDays(10),
                1,
                clienteId,
                3000.0,
                "transferencia",
                1,
                null,
                "Garantía en custodia",
                List.of(detalle));
    }

    // =========================================================================
    // CASO A: CON CONTROL DE BLOQUEO (@Lock PESSIMISTIC_WRITE) -> CORRECTO
    // =========================================================================
    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("CASO A: Registro CON control de bloqueo (@Lock) -> COMPORTAMIENTO CORRECTO")
    void testA_RegistroConBloqueoLock_ConcurrenciaCorrecta() throws InterruptedException {

        // 1. Arrange: Crear un grupo electrógeno disponible
        Equipo equipo = crearGrupoElectrogenoDisponible("GE-LOCK-", "Grupo Electrógeno Caterpillar C15 (CON LOCK)");
        long detallesIniciales = alquilerDetalleRepository.countByEquipoId(equipo.getId());

        System.out.printf("[ESTADO INICIAL] Equipo ID: %d | Código: %s | Nombre: %s | Estado: %d (DISPONIBLE)%n",
                equipo.getId(), equipo.getCodigo(), equipo.getNombre(), equipo.getEstado());

        RequestAlquiler requestCliente1 = construirRequestAlquiler(cliente1.getId(), equipo.getId());
        RequestAlquiler requestCliente2 = construirRequestAlquiler(cliente2.getId(), equipo.getId());

        int numHilos = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numHilos);
        CountDownLatch readyLatch = new CountDownLatch(numHilos);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numHilos);

        List<ResponseEntity<String>> respuestas = Collections.synchronizedList(new ArrayList<>());

        executor.submit(() -> {
            try {
                readyLatch.countDown();
                startLatch.await();
                System.out.println(" Hilo 1 disparando petición de alquiler para Cliente: " + cliente1.getNombre()
                        + " (ID: " + cliente1.getId() + ")");
                ResponseEntity<String> r = restTemplate.postForEntity("/alquileres/con-lock", requestCliente1,
                        String.class);
                respuestas.add(r);
                System.out.println(
                        " para hilo 1 respuesta: " + r.getStatusCode().value() + " " + r.getStatusCode());
            } catch (Exception e) {
                log.error("Error en Hilo 1", e);
            } finally {
                doneLatch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                readyLatch.countDown();
                startLatch.await();
                System.out.println(" Hilo 2 disparando petición de alquiler para Cliente: " + cliente2.getNombre()
                        + " (ID: " + cliente2.getId() + ")");
                ResponseEntity<String> r = restTemplate.postForEntity("/alquileres/con-lock", requestCliente2,
                        String.class);
                respuestas.add(r);
                System.out.println(
                        " para hilo 2 respuesta: " + r.getStatusCode().value() + " " + r.getStatusCode());
            } catch (Exception e) {
                log.error("Error en Hilo 2", e);
            } finally {
                doneLatch.countDown();
            }
        });

        // simulacion de espera
        assertTrue(readyLatch.await(5, TimeUnit.SECONDS));
        startLatch.countDown();
        assertTrue(doneLatch.await(15, TimeUnit.SECONDS));
        executor.shutdown();

        long exitosos = respuestas.stream().filter(r -> r.getStatusCode() == HttpStatus.OK).count();
        long conflictos = respuestas.stream().filter(r -> r.getStatusCode() == HttpStatus.CONFLICT).count();

        System.out.println(" Resultado prueba A:");
        System.out.printf("  * Total 200:       %d%n", exitosos);
        System.out.printf("  * Total 409:     %d%n", conflictos);

        Equipo equipoBd = equipoRepository.findById(equipo.getId()).orElseThrow();
        long detallesFinales = alquilerDetalleRepository.countByEquipoId(equipo.getId());
        System.out.printf("  * Estado del equipo en la Base de Datos:                      %d alquilado %n",
                equipoBd.getEstado());
        System.out.printf("  * Registros de alquiler persistidos para este equipo:         %d%n",
                detallesFinales - detallesIniciales);
    }

    // test de prueba B
    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("Caso B")
    void testB_RegistroSinBloqueoLock_ConcurrenciaIncorrecta() throws InterruptedException {

        Equipo equipo = crearGrupoElectrogenoDisponible("GE-NOLOCK-", "Grupo Electrógeno Perkins 1106 ");
        long detallesIniciales = alquilerDetalleRepository.countByEquipoId(equipo.getId());

        System.out.printf(" Equipo ID: %d | Código: %s | Nombre: %s | Estado: %d Disponible%n",
                equipo.getId(), equipo.getCodigo(), equipo.getNombre(), equipo.getEstado());

        RequestAlquiler requestCliente1 = construirRequestAlquiler(cliente1.getId(), equipo.getId());
        RequestAlquiler requestCliente2 = construirRequestAlquiler(cliente2.getId(), equipo.getId());

        int numHilos = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numHilos);
        CountDownLatch readyLatch = new CountDownLatch(numHilos);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numHilos);

        List<ResponseEntity<String>> respuestas = Collections.synchronizedList(new ArrayList<>());

        executor.submit(() -> {
            try {
                readyLatch.countDown();
                startLatch.await();
                System.out.println("hilo 1 Disparando petición para Cliente: " + cliente1.getNombre());
                ResponseEntity<String> r = restTemplate.postForEntity("/alquileres/sin-lock", requestCliente1,
                        String.class);
                respuestas.add(r);
                System.out.println(
                        "Hilo 1 HTTP " + r.getStatusCode().value() + " " + r.getStatusCode());
            } catch (Exception e) {
                log.error("Error en Hilo 1", e);
            } finally {
                doneLatch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                readyLatch.countDown();
                startLatch.await();
                System.out.println("hilo 2 Disparando petición para Cliente: " + cliente2.getNombre());
                ResponseEntity<String> r = restTemplate.postForEntity("/alquileres/sin-lock", requestCliente2,
                        String.class);
                respuestas.add(r);
                System.out.println(
                        "Hilo 2 HTTP " + r.getStatusCode().value() + " " + r.getStatusCode());
            } catch (Exception e) {
                log.error("Error en Hilo 2", e);
            } finally {
                doneLatch.countDown();
            }
        });

        // Sincronización y disparo concurrente simultáneo
        assertTrue(readyLatch.await(5, TimeUnit.SECONDS));
        startLatch.countDown();
        assertTrue(doneLatch.await(15, TimeUnit.SECONDS));
        executor.shutdown();

        // 2. Traza de salida descriptiva
        long exitosos = respuestas.stream().filter(r -> r.getStatusCode() == HttpStatus.OK).count();
        long alquileresCreados = alquilerDetalleRepository.countByEquipoId(equipo.getId()) - detallesIniciales;

        System.out.println(" Resultado de la prueba:");

        System.out.printf(" * Total de peticiones que respondieron HTTP 200 OK:           %d%n", exitosos);
        System.out.printf(" * Total de alquileres registrados para este MISMO equipo:     %d%n", alquileresCreados);

        // 3. Aserciones formales (Asserts)
        assertNotNull(respuestas);
        assertEquals(2, respuestas.size(), "Deben haberse recibido 2 respuestas HTTP");
        assertThat(exitosos).as("Al no haber @Lock, ambas peticiones obtienen HTTP 200 OK sin advertir la colisión")
                .isEqualTo(2);
        assertThat(alquileresCreados)
                .as("Se demuestra la anomalía de concurrencia: se crearon 2 alquileres para el mismo equipo")
                .isEqualTo(2);
    }
}
