package pe.edu.uls.tests_productos.cu.adminCliente.mapper;

import pe.edu.uls.tests_productos.cu.adminCliente.request.RequestCliente;
import pe.edu.uls.tests_productos.cu.adminCliente.response.ResponseCliente;
import pe.edu.uls.tests_productos.domain.entity.Cliente;

public interface ClienteMapper {

    Cliente toCliente(RequestCliente requestCliente);

    ResponseCliente toResponse(Cliente cliente);

}
