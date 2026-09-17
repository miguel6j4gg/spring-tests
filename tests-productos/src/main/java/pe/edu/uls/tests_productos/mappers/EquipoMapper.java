package pe.edu.uls.tests_productos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pe.edu.uls.tests_productos.model.Equipo;
import pe.edu.uls.tests_productos.dto.RequestEquipo;
import pe.edu.uls.tests_productos.dto.ResponseEquipo;

@Mapper(componentModel = "spring")
public interface EquipoMapper {

    @Mapping(target = "id", ignore = true)
    Equipo toEquipo(RequestEquipo requestEquipo);

    ResponseEquipo toResponse(Equipo equipo);
}
