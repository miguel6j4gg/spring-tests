package pe.edu.uls.tests_productos.cu.adminProducto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pe.edu.uls.tests_productos.cu.adminProducto.request.RequestProducto;
import pe.edu.uls.tests_productos.cu.adminProducto.response.ResponseProducto;
import pe.edu.uls.tests_productos.domain.entity.Producto;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(target = "id", ignore = true)
    Producto toProducto(RequestProducto requestProducto);

    ResponseProducto toResponse(Producto producto);

}
