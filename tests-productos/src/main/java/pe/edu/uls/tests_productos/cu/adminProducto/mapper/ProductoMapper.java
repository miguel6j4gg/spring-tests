package pe.edu.uls.tests_productos.cu.adminProducto.mapper;

import pe.edu.uls.tests_productos.cu.adminProducto.request.RequestProducto;
import pe.edu.uls.tests_productos.cu.adminProducto.response.ResponseProducto;
import pe.edu.uls.tests_productos.domain.entity.Producto;

public interface ProductoMapper {

    Producto toProducto(RequestProducto requestProducto);

    ResponseProducto toResponse(Producto producto);

}
