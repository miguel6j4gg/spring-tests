package pe.edu.uls.tests_productos.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.uls.tests_productos.model.Producto;
import pe.edu.uls.tests_productos.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository repoProducto;

    public ProductoService(ProductoRepository repoProducto) {
        this.repoProducto = repoProducto;
    }

    // =====================================================
    // REGISTRAR PRODUCTO
    // =====================================================

    public Producto registrarProducto(Producto producto) {
        // Verificar que no exista otro producto con el mismo código
        if (repoProducto.existsByCodigo(producto.getCodigo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un producto con el código: " + producto.getCodigo()
            );
        }

        return repoProducto.save(producto);
    }

    // =====================================================
    // BUSCAR PRODUCTO POR ID
    // =====================================================

    public Producto obtenerProducto(int id) {
        return repoProducto.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado con ID: " + id
                ));
    }

    // =====================================================
    // BUSCAR PRODUCTOS POR CATEGORIA
    // =====================================================

    public List<Producto> buscarPorCategoria(String categoria) {
        return repoProducto.findByCategoria(categoria);
    }

    // =====================================================
    // LISTAR TODOS LOS PRODUCTOS
    // =====================================================

    public List<Producto> listarProductos() {
        return repoProducto.findAll();
    }

}
