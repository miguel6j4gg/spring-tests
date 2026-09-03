package pe.edu.uls.tests_productos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import pe.edu.uls.tests_productos.model.Equipo;

@RestController
@RequestMapping("/equipos")
public class EquipoController {

    // =====================================================
    // GET 1 - LISTAR EQUIPOS
    // =====================================================

    @GetMapping
    public List<Equipo> listarEquipos() {

        List<Equipo> equipos = new ArrayList<>();

        equipos.add(new Equipo(
                "GE-001",
                "Grupo Electrógeno 01",
                "Caterpillar C15",
                500,
                1200,
                "Arequipa",
                "Diesel",
                "Disponible"
        ));

        equipos.add(new Equipo(
                "GE-002",
                "Grupo Electrógeno 02",
                "Perkins 1106",
                300,
                850,
                "Miraflores",
                "Diesel",
                "Alquilado"
        ));

        equipos.add(new Equipo(
                "GE-003",
                "Grupo Electrógeno 03",
                "Cummins QSB",
                200,
                600,
                "Arequipa",
                "Diesel",
                "Mantenimiento"
        ));

        return equipos;
    }


    // =====================================================
    // GET 2 - BUSCAR POR CODIGO
    // =====================================================

    @GetMapping("/{codigo}")
    public Equipo obtenerEquipo(@PathVariable String codigo) {

        return new Equipo(
                codigo,
                "Grupo Electrógeno",
                "Caterpillar C15",
                500,
                1200,
                "Arequipa",
                "Diesel",
                "Disponible"
        );
    }


    // =====================================================
    // GET 3 - BUSCAR POR UBICACION
    // =====================================================

    @GetMapping("/ubicacion/{ubicacion}")
    public List<Equipo> buscarPorUbicacion(
            @PathVariable String ubicacion) {

        List<Equipo> equipos = new ArrayList<>();

        equipos.add(new Equipo(
                "GE-001",
                "Grupo Electrógeno 01",
                "Caterpillar C15",
                500,
                1200,
                ubicacion,
                "Diesel",
                "Disponible"
        ));

        equipos.add(new Equipo(
                "GE-004",
                "Grupo Electrógeno 04",
                "Perkins 1104",
                250,
                700,
                ubicacion,
                "Diesel",
                "Disponible"
        ));

        return equipos;
    }


    // =====================================================
    // GET 4 - BUSCAR POR TIPO DE COMBUSTIBLE
    // =====================================================

    @GetMapping("/tipo/{tipo}")
    public List<Equipo> buscarPorTipo(
            @PathVariable String tipo) {

        List<Equipo> equipos = new ArrayList<>();

        equipos.add(new Equipo(
                "GE-001",
                "Grupo Electrógeno 01",
                "Caterpillar C15",
                500,
                1200,
                "Arequipa",
                tipo,
                "Disponible"
        ));

        equipos.add(new Equipo(
                "GE-002",
                "Grupo Electrógeno 02",
                "Perkins 1106",
                300,
                850,
                "Miraflores",
                tipo,
                "Alquilado"
        ));

        return equipos;
    }


    // =====================================================
    // GET 5 - EQUIPOS CON MAYOR POTENCIA
    // =====================================================

    @GetMapping("/mayor-potencia/{potencia}")
    public List<Equipo> equiposMayorPotencia(
            @PathVariable double potencia) {

        List<Equipo> equipos = new ArrayList<>();

        equipos.add(new Equipo(
                "GE-001",
                "Grupo Electrógeno 01",
                "Caterpillar C15",
                potencia + 100,
                1200,
                "Arequipa",
                "Diesel",
                "Disponible"
        ));

        equipos.add(new Equipo(
                "GE-002",
                "Grupo Electrógeno 02",
                "Perkins 1106",
                potencia + 50,
                850,
                "Miraflores",
                "Diesel",
                "Alquilado"
        ));

        return equipos;
    }


    // =====================================================
    // POST 1 - REGISTRAR EQUIPO
    // =====================================================

    @PostMapping
    public Equipo registrarEquipo(
            @RequestBody Equipo equipo) {

        return equipo;
    }


    // =====================================================
    // POST 2 - CALCULAR CONSUMO DE COMBUSTIBLE
    // =====================================================

    @PostMapping("/calcular-combustible")
    public Equipo calcularCombustible(
            @RequestBody Equipo equipo) {

        double consumo = equipo.getPotencia() * 0.20;

        equipo.setCombustible(
                "Consumo estimado: " + consumo + " L/h"
        );

        return equipo;
    }


    // =====================================================
    // POST 3 - ACTUALIZAR HOROMETRO
    // =====================================================

    @PostMapping("/actualizar-horometro")
    public Equipo actualizarHorometro(
            @RequestBody Equipo equipo) {

        equipo.setHorometro(
                equipo.getHorometro() + 100
        );

        return equipo;
    }


    // =====================================================
    // POST 4 - REGISTRAR SERVICIO
    // =====================================================

    @PostMapping("/registrar-servicio")
    public Equipo registrarServicio(
            @RequestBody Equipo equipo) {

        equipo.setEstado("En servicio");

        return equipo;
    }


    // =====================================================
    // POST 5 - CAMBIAR ESTADO
    // =====================================================

    @PostMapping("/cambiar-estado")
    public Equipo cambiarEstado(
            @RequestBody Equipo equipo) {

        equipo.setEstado("Mantenimiento");

        return equipo;
    }
}