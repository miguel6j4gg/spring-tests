package pe.edu.uls.tests_productos.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import pe.edu.uls.tests_productos.exceptions.EquipoDuplicadoException;
import pe.edu.uls.tests_productos.exceptions.EquipoInvalidoException;
import pe.edu.uls.tests_productos.exceptions.EquipoNoEncontradoException;
import pe.edu.uls.tests_productos.model.Equipo;

@Service
public class NewEquipoService {

    // =====================================================
    // GET 1 - BUSCAR POR CODIGO
    // =====================================================

    public Equipo obtenerEquipo(String codigo) {

        if (codigo == null || codigo.isBlank()) {
            throw new EquipoInvalidoException(
                    "El código del equipo es obligatorio");
        }

        if (!codigo.equals("GE-001")) {
            throw new EquipoNoEncontradoException(
                    "Equipo no encontrado con código: " + codigo);
        }

        return new Equipo(
                codigo,
                "Grupo Electrógeno",
                "Caterpillar C15",
                500,
                1200,
                "Arequipa",
                "Diesel",
                "Disponible");
    }

    // =====================================================
    // GET 2 - BUSCAR POR UBICACION
    // =====================================================

    public List<Equipo> buscarPorUbicacion(String ubicacion) {

        if (ubicacion == null || ubicacion.isBlank()) {
            throw new EquipoInvalidoException(
                    "La ubicación es obligatoria");
        }

        if (!ubicacion.equalsIgnoreCase("Arequipa")
                && !ubicacion.equalsIgnoreCase("Miraflores")) {

            throw new EquipoNoEncontradoException(
                    "No existen equipos en la ubicación: " + ubicacion);
        }

        List<Equipo> equipos = new ArrayList<>();

        equipos.add(new Equipo(
                "GE-001",
                "Grupo Electrógeno 01",
                "Caterpillar C15",
                500,
                1200,
                ubicacion,
                "Diesel",
                "Disponible"));

        equipos.add(new Equipo(
                "GE-004",
                "Grupo Electrógeno 04",
                "Perkins 1104",
                250,
                700,
                ubicacion,
                "Diesel",
                "Disponible"));

        return equipos;
    }

    // =====================================================
    // GET 3 - BUSCAR POR TIPO DE COMBUSTIBLE
    // =====================================================

    public List<Equipo> buscarPorTipo(String tipo) {

        if (tipo == null || tipo.isBlank()) {
            throw new EquipoInvalidoException(
                    "El tipo de combustible es obligatorio");
        }

        if (!tipo.equalsIgnoreCase("Diesel")
                && !tipo.equalsIgnoreCase("Gasolina")) {

            throw new EquipoNoEncontradoException(
                    "No existen equipos con combustible: " + tipo);
        }

        List<Equipo> equipos = new ArrayList<>();

        equipos.add(new Equipo(
                "GE-001",
                "Grupo Electrógeno 01",
                "Caterpillar C15",
                500,
                1200,
                "Arequipa",
                tipo,
                "Disponible"));

        equipos.add(new Equipo(
                "GE-002",
                "Grupo Electrógeno 02",
                "Perkins 1106",
                300,
                850,
                "Miraflores",
                tipo,
                "Alquilado"));

        return equipos;
    }

    // =====================================================
    // GET 4 - EQUIPOS CON MAYOR POTENCIA
    // =====================================================

    public List<Equipo> equiposMayorPotencia(double potencia) {

        if (potencia < 0) {
            throw new EquipoInvalidoException(
                    "La potencia no puede ser negativa");
        }

        List<Equipo> equipos = new ArrayList<>();

        equipos.add(new Equipo(
                "GE-001",
                "Grupo Electrógeno 01",
                "Caterpillar C15",
                potencia + 100,
                1200,
                "Arequipa",
                "Diesel",
                "Disponible"));

        equipos.add(new Equipo(
                "GE-002",
                "Grupo Electrógeno 02",
                "Perkins 1106",
                potencia + 50,
                850,
                "Miraflores",
                "Diesel",
                "Alquilado"));

        return equipos;
    }

    // =====================================================
    // POST 1 - REGISTRAR EQUIPO
    // =====================================================

    public Equipo registrarEquipo(Equipo equipo) {

        if (equipo == null) {
            throw new EquipoInvalidoException(
                    "El equipo es obligatorio");
        }

        if (equipo.getCodigo() == null
                || equipo.getCodigo().isBlank()) {

            throw new EquipoInvalidoException(
                    "El código del equipo es obligatorio");
        }

        if (!equipo.getCodigo().startsWith("GE-")) {
            throw new EquipoInvalidoException(
                    "Código de equipo inválido. Debe comenzar con GE-");
        }

        if (equipo.getPotencia() <= 0) {
            throw new EquipoInvalidoException(
                    "La potencia debe ser mayor que cero");
        }

        if (equipo.getCodigo().equals("GE-001")) {
            throw new EquipoDuplicadoException(
                    "El equipo con código GE-001 ya existe");
        }

        return equipo;
    }

    // =====================================================
    // POST 2 - CALCULAR CONSUMO DE COMBUSTIBLE
    // =====================================================

    public Equipo calcularCombustible(Equipo equipo) {

        if (equipo == null) {
            throw new EquipoInvalidoException(
                    "El equipo es obligatorio");
        }

        if (equipo.getPotencia() <= 0) {
            throw new EquipoInvalidoException(
                    "La potencia debe ser mayor que cero para calcular el consumo");
        }

        double consumo = equipo.getPotencia() * 0.20;

        equipo.setCombustible(
                "Consumo estimado: " + consumo + " L/h");

        return equipo;
    }

    // =====================================================
    // POST 3 - ACTUALIZAR HOROMETRO
    // =====================================================

    public Equipo actualizarHorometro(Equipo equipo) {

        if (equipo == null) {
            throw new EquipoInvalidoException(
                    "El equipo es obligatorio");
        }

        if (equipo.getHorometro() < 0) {
            throw new EquipoInvalidoException(
                    "El horómetro no puede ser negativo");
        }

        equipo.setHorometro(
                equipo.getHorometro() + 100);

        return equipo;
    }

    // =====================================================
    // POST 4 - REGISTRAR SERVICIO
    // =====================================================

    public Equipo registrarServicio(Equipo equipo) {

        if (equipo == null) {
            throw new EquipoInvalidoException(
                    "El equipo es obligatorio");
        }

        if (equipo.getCodigo() == null
                || equipo.getCodigo().isBlank()) {

            throw new EquipoInvalidoException(
                    "El código del equipo es obligatorio");
        }

        if ("Mantenimiento".equalsIgnoreCase(equipo.getEstado())) {
            throw new EquipoInvalidoException(
                    "El equipo se encuentra en mantenimiento y no puede entrar en servicio");
        }

        equipo.setEstado("En servicio");

        return equipo;
    }

    // =====================================================
    // MÉTODO ADICIONAL
    // =====================================================

    public Equipo cambiarEstado(Equipo equipo) {

        if (equipo == null) {
            throw new EquipoInvalidoException(
                    "El equipo es obligatorio");
        }

        equipo.setEstado("Mantenimiento");

        return equipo;
    }
}