package pe.edu.uls.tests_productos.cu.adminCliente.mapper;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import pe.edu.uls.tests_productos.cu.adminCliente.request.RequestCliente;
import pe.edu.uls.tests_productos.cu.adminCliente.response.ResponseCliente;
import pe.edu.uls.tests_productos.domain.entity.Cliente;
import pe.edu.uls.tests_productos.domain.entity.Entidad;

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

        Entidad entidad = new Entidad();
        entidad.setTelefono(requestCliente.telefono());
        entidad.setDireccion(requestCliente.direccion());
        entidad.setCorreo(requestCliente.email());
        entidad.setEstado(requestCliente.estado());
        cliente.setEntidad(entidad);

        return cliente;
    }

    @Override
    public ResponseCliente toResponse(Cliente cliente) {
        if (cliente == null) return null;
        Entidad entidad = cliente.getEntidad();
        return new ResponseCliente(
                cliente.getId(),
                entidad != null ? entidad.getId() : 0,
                cliente.getDocumento(),
                cliente.getNombre(),
                cliente.getApellido(),
                entidad != null ? entidad.getCorreo() : null,
                entidad != null ? entidad.getTelefono() : null,
                entidad != null ? entidad.getDireccion() : null,
                entidad != null ? entidad.getEstado() : null
        );
    }
}
