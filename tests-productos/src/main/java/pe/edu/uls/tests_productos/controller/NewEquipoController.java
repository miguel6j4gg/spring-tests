package pe.edu.uls.tests_productos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import pe.edu.uls.tests_productos.dto.RequestEquipo;
import pe.edu.uls.tests_productos.dto.ResponseEquipo;
import pe.edu.uls.tests_productos.mappers.EquipoMapper;
import pe.edu.uls.tests_productos.model.Equipo;
import pe.edu.uls.tests_productos.services.NewEquipoService;

@RestController
@RequestMapping("/new-equipos")
public class NewEquipoController {

	private final NewEquipoService newEquipoService;

	private final EquipoMapper equipoMapper;

	public NewEquipoController(NewEquipoService newEquipoService, EquipoMapper equipoMapper) {
		this.newEquipoService = newEquipoService;
		this.equipoMapper = equipoMapper;
	}

	// =====================================================
	// GET 2 - BUSCAR POR CODIGO
	// =====================================================

	@GetMapping("/{codigo}")
	public ResponseEquipo obtenerEquipo(@PathVariable String codigo) {

		Equipo equipo = newEquipoService.obtenerEquipo(codigo);

		return equipoMapper.toResponseEquipo(equipo);
	}

	// =====================================================
	// GET 3 - BUSCAR POR UBICACION
	// =====================================================

	@GetMapping("/ubicacion/{ubicacion}")
	public List<ResponseEquipo> buscarPorUbicacion(
			@PathVariable String ubicacion) {

		return newEquipoService.buscarPorUbicacion(ubicacion)
				.stream()
				.map(equipoMapper::toResponseEquipo)
				.toList();
	}

	// =====================================================
	// GET 4 - BUSCAR POR TIPO DE COMBUSTIBLE
	// =====================================================

	@GetMapping("/tipo/{tipo}")
	public List<ResponseEquipo> buscarPorTipo(
			@PathVariable String tipo) {

		return newEquipoService.buscarPorTipo(tipo)
				.stream()
				.map(equipoMapper::toResponseEquipo)
				.toList();
	}

	// =====================================================
	// GET 5 - EQUIPOS CON MAYOR POTENCIA
	// =====================================================

	@GetMapping("/mayor-potencia/{potencia}")
	public List<ResponseEquipo> equiposMayorPotencia(
			@PathVariable double potencia) {

		return newEquipoService.equiposMayorPotencia(potencia)
				.stream()
				.map(equipoMapper::toResponseEquipo)
				.toList();
	}

	// =====================================================
	// POST 1 - REGISTRAR EQUIPO
	// =====================================================

	@PostMapping
	public ResponseEquipo registrarEquipo(
			@RequestBody RequestEquipo request) {

		Equipo equipo = equipoMapper.toEquipo(request);

		return equipoMapper.toResponseEquipo(
				newEquipoService.registrarEquipo(equipo));
	}

	// =====================================================
	// POST 2 - CALCULAR CONSUMO DE COMBUSTIBLE
	// =====================================================

	@PostMapping("/calcular-combustible")
	public ResponseEquipo calcularCombustible(
			@RequestBody RequestEquipo request) {

		Equipo equipo = equipoMapper.toEquipo(request);

		return equipoMapper.toResponseEquipo(
				newEquipoService.calcularCombustible(equipo));
	}

	// =====================================================
	// POST 3 - ACTUALIZAR HOROMETRO
	// =====================================================

	@PostMapping("/actualizar-horometro")
	public ResponseEquipo actualizarHorometro(
			@RequestBody RequestEquipo request) {

		Equipo equipo = equipoMapper.toEquipo(request);

		return equipoMapper.toResponseEquipo(
				newEquipoService.actualizarHorometro(equipo));
	}

	// =====================================================
	// POST 4 - REGISTRAR SERVICIO
	// =====================================================

	@PostMapping("/registrar-servicio")
	public ResponseEquipo registrarServicio(
			@RequestBody RequestEquipo request) {

		Equipo equipo = equipoMapper.toEquipo(request);

		return equipoMapper.toResponseEquipo(
				newEquipoService.registrarServicio(equipo));
	}

}