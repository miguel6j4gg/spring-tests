package pe.edu.uls.tests_productos.mappers;

import org.mapstruct.Mapper;

import pe.edu.uls.tests_productos.model.Equipo;
import pe.edu.uls.tests_productos.dto.EquipoRequest;

@Mapper(componentModel = "spring")
public interface EquipoMapper {
    Equipo toEquipo(EquipoRequest equipoRequest);
}
