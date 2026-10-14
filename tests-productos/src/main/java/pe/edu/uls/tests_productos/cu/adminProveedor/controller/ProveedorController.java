package pe.edu.uls.tests_productos.cu.adminProveedor.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.uls.tests_productos.cu.adminProveedor.service.ProveedorService;
import pe.edu.uls.tests_productos.domain.entity.Proveedor;

@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping("/{id}")
    public Proveedor obtenerProveedor(@PathVariable int id) {
        return proveedorService.obtenerProveedor(id);
    }

    @GetMapping
    public List<Proveedor> listarProveedores() {
        return proveedorService.listarProveedores();
    }

    @GetMapping("/estado/{estado}")
    public List<Proveedor> buscarPorEstado(@PathVariable Integer estado) {
        return proveedorService.buscarPorEstado(estado);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<Proveedor> listarPorEmpresa(@PathVariable Integer empresaId) {
        return proveedorService.listarPorEmpresa(empresaId);
    }

    @PostMapping
    public Proveedor registrarProveedor(@RequestBody Proveedor proveedor) {
        return proveedorService.registrarProveedor(proveedor);
    }
}
