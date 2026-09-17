package pe.edu.uls.tests_productos.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.uls.tests_productos.model.Cliente;
import pe.edu.uls.tests_productos.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository repoCliente;

    public ClienteService(ClienteRepository repoCliente) {
        this.repoCliente = repoCliente;
    }

    // =====================================================
    // REGISTRAR CLIENTE
    // =====================================================

    public Cliente registrarCliente(Cliente cliente) {
        // Verificar que no exista otro cliente con el mismo documento
        if (repoCliente.existsByDocumento(cliente.getDocumento())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un cliente con el documento: " + cliente.getDocumento()
            );
        }

        return repoCliente.save(cliente);
    }

    // =====================================================
    // BUSCAR CLIENTE POR ID
    // =====================================================

    public Cliente obtenerCliente(int id) {
        return repoCliente.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con ID: " + id
                ));
    }

    // =====================================================
    // BUSCAR CLIENTES POR ESTADO
    // =====================================================

    public List<Cliente> buscarPorEstado(String estado) {
        return repoCliente.findByEstado(estado);
    }

    // =====================================================
    // LISTAR TODOS LOS CLIENTES
    // =====================================================

    public List<Cliente> listarClientes() {
        return repoCliente.findAll();
    }

}
