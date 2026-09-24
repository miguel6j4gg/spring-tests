package pe.edu.uls.tests_productos.cu.adminCliente.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pe.edu.uls.tests_productos.cu.adminCliente.request.RequestCliente;
import pe.edu.uls.tests_productos.cu.adminCliente.response.ResponseCliente;
import pe.edu.uls.tests_productos.domain.entity.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    Cliente toCliente(RequestCliente requestCliente);

    ResponseCliente toResponse(Cliente cliente);

}
