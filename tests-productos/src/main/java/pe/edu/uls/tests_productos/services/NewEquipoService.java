package pe.edu.uls.tests_productos.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import pe.edu.uls.tests_productos.exceptions.EquipoInvalidoException;
import pe.edu.uls.tests_productos.exceptions.EquipoNoEncontradoException;
import pe.edu.uls.tests_productos.model.Equipo;

@Service
public class NewEquipoService {

	public Equipo obtenerEquipo(String codigo) {

		if (!codigo.equals("GE-001")) {
			throw new EquipoNoEncontradoException("Equipo no encontrado");
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

	public List<Equipo> buscarPorUbicacion(String ubicacion) {

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

	public List<Equipo> buscarPorTipo(String tipo) {

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

	public List<Equipo> equiposMayorPotencia(double potencia) {

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

	public Equipo registrarEquipo(Equipo equipo) {
		// Aquí iría la lógica para guardar el equipo en la base de datos

		if (!equipo.getCodigo().substring(0, 2).equals("GE")) {
			throw new EquipoInvalidoException("Código de equipo inválido");
		}

		return equipo;

	}

	public Equipo calcularCombustible(Equipo equipo) {

		double consumo = equipo.getPotencia() * 0.20;

		equipo.setCombustible(
				"Consumo estimado: " + consumo + " L/h");

		return equipo;
	}

	public Equipo actualizarHorometro(Equipo equipo) {

		equipo.setHorometro(
				equipo.getHorometro() + 100);

		return equipo;
	}

	public Equipo registrarServicio(Equipo equipo) {

		equipo.setEstado("En servicio");

		return equipo;
	}

	public Equipo cambiarEstado(Equipo equipo) {

		equipo.setEstado("Mantenimiento");

		return equipo;
	}
}