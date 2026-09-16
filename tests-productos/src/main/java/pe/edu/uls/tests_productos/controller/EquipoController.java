package pe.edu.uls.tests_productos.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import pe.edu.uls.tests_productos.dto.RequestEquipo;
import pe.edu.uls.tests_productos.dto.ResponseEquipo;
import pe.edu.uls.tests_productos.mappers.EquipoMapper;
import pe.edu.uls.tests_productos.model.Equipo;
import pe.edu.uls.tests_productos.services.EquipoService;

@RestController
@RequestMapping("/new-equipos")
public class EquipoController {

	private final EquipoService equipoService;

	private final EquipoMapper equipoMapper;

	public EquipoController(EquipoService equipoService, EquipoMapper equipoMapper) {
		this.equipoService = equipoService;
		this.equipoMapper = equipoMapper;
	}

	// =====================================================
	// POST 1 - REGISTRAR EQUIPO
	// =====================================================

	@PostMapping
	public ResponseEquipo registrarEquipo(
			@RequestBody RequestEquipo request) {
		Equipo equipo = equipoMapper.toEquipo(request);
		equipo = equipoService.registrarEquipo(equipo);
		ResponseEquipo response = equipoMapper.toResponse(equipo);
		return response;
	}

	// =====================================================
	// GET 2 - BUSCAR POR ID
	// =====================================================

	@GetMapping("/{id}")
	public ResponseEquipo obtenerEquipo(@PathVariable int id) {

		Equipo equipo = equipoService.obtenerEquipo(id);
		ResponseEquipo response = equipoMapper.toResponse(equipo);
		return response;
	}

	// =====================================================
	// GET 3 - BUSCAR POR UBICACION
	// =====================================================

	@GetMapping("/ubicacion/{ubicacion}")
	public List<ResponseEquipo> buscarPorUbicacion(
			@PathVariable String ubicacion) {

		return equipoService.buscarPorUbicacion(ubicacion)
				.stream()
				.map(equipoMapper::toResponse)
				.toList();
	}

}