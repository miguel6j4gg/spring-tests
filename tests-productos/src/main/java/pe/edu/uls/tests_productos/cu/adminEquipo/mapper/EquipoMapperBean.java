package pe.edu.uls.tests_productos.cu.adminEquipo.mapper;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import pe.edu.uls.tests_productos.cu.adminEquipo.request.RequestEquipo;
import pe.edu.uls.tests_productos.cu.adminEquipo.response.ResponseEquipo;
import pe.edu.uls.tests_productos.domain.entity.Equipo;

@Component
@Primary
public class EquipoMapperBean implements EquipoMapper {

    @Override
    public Equipo toEquipo(RequestEquipo requestEquipo) {
        if (requestEquipo == null) return null;
        Equipo equipo = new Equipo();
        equipo.setCodigo(requestEquipo.codigo());
        equipo.setNombre(requestEquipo.nombre());
        equipo.setModelo(requestEquipo.modelo());
        equipo.setPotencia(requestEquipo.potencia());
        equipo.setHorometro(requestEquipo.horometro());
        equipo.setUbicacion(requestEquipo.ubicacion());
        equipo.setCombustible(requestEquipo.combustible());
        equipo.setEstado(requestEquipo.estado());
        return equipo;
    }

    @Override
    public ResponseEquipo toResponse(Equipo equipo) {
        if (equipo == null) return null;
        return new ResponseEquipo(
                equipo.getId(),
                equipo.getCodigo(),
                equipo.getNombre(),
                equipo.getModelo(),
                equipo.getPotencia(),
                equipo.getHorometro(),
                equipo.getUbicacion(),
                equipo.getCombustible(),
                equipo.getEstado()
        );
    }
}
