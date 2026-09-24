package pe.edu.uls.tests_productos.cu.adminEquipo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pe.edu.uls.tests_productos.cu.adminEquipo.request.RequestEquipo;
import pe.edu.uls.tests_productos.cu.adminEquipo.response.ResponseEquipo;
import pe.edu.uls.tests_productos.domain.entity.Equipo;

@Mapper(componentModel = "spring")
public interface EquipoMapper {

    @Mapping(target = "id", ignore = true)
    Equipo toEquipo(RequestEquipo requestEquipo);

    ResponseEquipo toResponse(Equipo equipo);
}
