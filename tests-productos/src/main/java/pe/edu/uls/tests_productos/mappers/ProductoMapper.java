package pe.edu.uls.tests_productos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pe.edu.uls.tests_productos.dto.RequestProducto;
import pe.edu.uls.tests_productos.dto.ResponseProducto;
import pe.edu.uls.tests_productos.model.Producto;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(target = "id", ignore = true)
    Producto toProducto(RequestProducto requestProducto);

    ResponseProducto toResponse(Producto producto);

}
