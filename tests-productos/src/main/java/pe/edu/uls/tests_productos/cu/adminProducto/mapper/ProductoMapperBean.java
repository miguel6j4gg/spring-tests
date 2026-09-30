package pe.edu.uls.tests_productos.cu.adminProducto.mapper;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import pe.edu.uls.tests_productos.cu.adminProducto.request.RequestProducto;
import pe.edu.uls.tests_productos.cu.adminProducto.response.ResponseProducto;
import pe.edu.uls.tests_productos.domain.entity.Producto;

@Component
@Primary
public class ProductoMapperBean implements ProductoMapper {

    @Override
    public Producto toProducto(RequestProducto requestProducto) {
        if (requestProducto == null) return null;
        Producto producto = new Producto();
        producto.setCodigo(requestProducto.codigo());
        producto.setNombre(requestProducto.nombre());
        producto.setDescripcion(requestProducto.descripcion());
        producto.setCategoria(requestProducto.categoria());
        producto.setPrecio(requestProducto.precio());
        producto.setStock(requestProducto.stock());
        producto.setEstado(requestProducto.estado());
        return producto;
    }

    @Override
    public ResponseProducto toResponse(Producto producto) {
        if (producto == null) return null;
        return new ResponseProducto(
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getCategoria(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getEstado()
        );
    }
}
