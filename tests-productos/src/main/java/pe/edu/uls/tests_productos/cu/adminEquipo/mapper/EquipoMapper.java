package pe.edu.uls.tests_productos.cu.adminEquipo.mapper;

import pe.edu.uls.tests_productos.cu.adminEquipo.request.RequestEquipo;
import pe.edu.uls.tests_productos.cu.adminEquipo.response.ResponseEquipo;
import pe.edu.uls.tests_productos.domain.entity.Equipo;

public interface EquipoMapper {

    Equipo toEquipo(RequestEquipo requestEquipo);

    ResponseEquipo toResponse(Equipo equipo);
}
