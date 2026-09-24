package pe.edu.uls.tests_productos.cu.adminEquipo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.uls.tests_productos.domain.entity.Equipo;
import pe.edu.uls.tests_productos.domain.repository.EquipoRepository;

@Service
public class EquipoService {

    private final EquipoRepository repoEquipo;

    public EquipoService(EquipoRepository repoEquipo) {
        this.repoEquipo = repoEquipo;
    }

    // =====================================================
    // REGISTRAR EQUIPO
    // =====================================================

    public Equipo registrarEquipo(Equipo equipo) {

        // Verificar que no exista otro equipo con el mismo código
        if (repoEquipo.existsByCodigo(equipo.getCodigo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un equipo con el código: " + equipo.getCodigo());
        }

        return repoEquipo.save(equipo);
    }

    // =====================================================
    // BUSCAR EQUIPO POR ID
    // =====================================================

    public Equipo obtenerEquipo(int id) {

        return repoEquipo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Equipo no encontrado con ID: " + id));
    }

    // =====================================================
    // BUSCAR EQUIPOS POR UBICACION
    // =====================================================

    public List<Equipo> buscarPorUbicacion(String ubicacion) {

        return repoEquipo.findByUbicacion(ubicacion);
    }

}