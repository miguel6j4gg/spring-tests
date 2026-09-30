package pe.edu.uls.tests_productos.cu.adminCliente.mapper;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import pe.edu.uls.tests_productos.cu.adminCliente.request.RequestCliente;
import pe.edu.uls.tests_productos.cu.adminCliente.response.ResponseCliente;
import pe.edu.uls.tests_productos.domain.entity.Cliente;

@Component
@Primary
public class ClienteMapperBean implements ClienteMapper {

    @Override
    public Cliente toCliente(RequestCliente requestCliente) {
        if (requestCliente == null) return null;
        Cliente cliente = new Cliente();
        cliente.setDocumento(requestCliente.documento());
        cliente.setNombre(requestCliente.nombre());
        cliente.setApellido(requestCliente.apellido());
        cliente.setEmail(requestCliente.email());
        cliente.setTelefono(requestCliente.telefono());
        cliente.setDireccion(requestCliente.direccion());
        cliente.setEstado(requestCliente.estado());
        return cliente;
    }

    @Override
    public ResponseCliente toResponse(Cliente cliente) {
        if (cliente == null) return null;
        return new ResponseCliente(
                cliente.getId(),
                cliente.getDocumento(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getDireccion(),
                cliente.getEstado()
        );
    }
}
