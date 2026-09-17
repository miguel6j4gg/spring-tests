package pe.edu.uls.tests_productos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.uls.tests_productos.dto.RequestProducto;
import pe.edu.uls.tests_productos.dto.ResponseProducto;
import pe.edu.uls.tests_productos.mappers.ProductoMapper;
import pe.edu.uls.tests_productos.model.Producto;
import pe.edu.uls.tests_productos.services.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoMapper productoMapper;

    public ProductoController(ProductoService productoService, ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    // =====================================================
    // POST 1 - REGISTRAR PRODUCTO
    // =====================================================

    @PostMapping
    public ResponseProducto registrarProducto(@RequestBody RequestProducto request) {
        Producto producto = productoMapper.toProducto(request);
        producto = productoService.registrarProducto(producto);
        return productoMapper.toResponse(producto);
    }

    // =====================================================
    // GET 2 - BUSCAR POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseProducto obtenerProducto(@PathVariable int id) {
        Producto producto = productoService.obtenerProducto(id);
        return productoMapper.toResponse(producto);
    }

    // =====================================================
    // GET 3 - BUSCAR POR CATEGORIA
    // =====================================================

    @GetMapping("/categoria/{categoria}")
    public List<ResponseProducto> buscarPorCategoria(@PathVariable String categoria) {
        return productoService.buscarPorCategoria(categoria)
                .stream()
                .map(productoMapper::toResponse)
                .toList();
    }

    // =====================================================
    // GET 4 - LISTAR TODOS
    // =====================================================

    @GetMapping
    public List<ResponseProducto> listarProductos() {
        return productoService.listarProductos()
                .stream()
                .map(productoMapper::toResponse)
                .toList();
    }

}
