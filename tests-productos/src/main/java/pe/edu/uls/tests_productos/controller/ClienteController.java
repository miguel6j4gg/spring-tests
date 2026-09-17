package pe.edu.uls.tests_productos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.uls.tests_productos.dto.RequestCliente;
import pe.edu.uls.tests_productos.dto.ResponseCliente;
import pe.edu.uls.tests_productos.mappers.ClienteMapper;
import pe.edu.uls.tests_productos.model.Cliente;
import pe.edu.uls.tests_productos.services.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    // =====================================================
    // POST 1 - REGISTRAR CLIENTE
    // =====================================================

    @PostMapping
    public ResponseCliente registrarCliente(@RequestBody RequestCliente request) {
        Cliente cliente = clienteMapper.toCliente(request);
        cliente = clienteService.registrarCliente(cliente);
        return clienteMapper.toResponse(cliente);
    }

    // =====================================================
    // GET 2 - BUSCAR POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseCliente obtenerCliente(@PathVariable int id) {
        Cliente cliente = clienteService.obtenerCliente(id);
        return clienteMapper.toResponse(cliente);
    }

    // =====================================================
    // GET 3 - BUSCAR POR ESTADO
    // =====================================================

    @GetMapping("/estado/{estado}")
    public List<ResponseCliente> buscarPorEstado(@PathVariable String estado) {
        return clienteService.buscarPorEstado(estado)
                .stream()
                .map(clienteMapper::toResponse)
                .toList();
    }

    // =====================================================
    // GET 4 - LISTAR TODOS
    // =====================================================

    @GetMapping
    public List<ResponseCliente> listarClientes() {
        return clienteService.listarClientes()
                .stream()
                .map(clienteMapper::toResponse)
                .toList();
    }

}
