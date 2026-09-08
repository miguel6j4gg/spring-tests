package pe.edu.uls.tests_productos.services;

import org.springframework.stereotype.Service;

import pe.edu.uls.tests_productos.model.Equipo;

@Service
public class EquipoService {

    public Equipo crearEquipo(Equipo equipo) {
        // Aquí iría la lógica para guardar el equipo en la base de datos
        return equipo;
    }
}
