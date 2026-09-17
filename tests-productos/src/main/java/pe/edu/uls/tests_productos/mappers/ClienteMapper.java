package pe.edu.uls.tests_productos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pe.edu.uls.tests_productos.dto.RequestCliente;
import pe.edu.uls.tests_productos.dto.ResponseCliente;
import pe.edu.uls.tests_productos.model.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    Cliente toCliente(RequestCliente requestCliente);

    ResponseCliente toResponse(Cliente cliente);

}
