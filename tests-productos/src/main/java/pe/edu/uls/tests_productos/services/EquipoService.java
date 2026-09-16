package pe.edu.uls.tests_productos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.uls.tests_productos.model.Equipo;
import pe.edu.uls.tests_productos.repository.EquipoRepository;

@Service
public class EquipoService {

    @Autowired
    EquipoRepository repoEquipo;

    public Equipo registrarEquipo(Equipo equipo) {
        Equipo savedEquipo = repoEquipo.save(equipo);
        return savedEquipo;
    }

    public Equipo obtenerEquipo(int id) {
        return repoEquipo.findById(id).orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
    }

    public List<Equipo> buscarPorUbicacion(String ubicacion) {

        return repoEquipo.findByUbicacion(ubicacion);
    }

}