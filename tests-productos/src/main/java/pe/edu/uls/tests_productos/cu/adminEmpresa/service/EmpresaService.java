package pe.edu.uls.tests_productos.cu.adminEmpresa.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.uls.tests_productos.cu.adminEmpresa.request.RequestEmpresa;
import pe.edu.uls.tests_productos.cu.adminEmpresa.response.ResponseEmpresa;
import pe.edu.uls.tests_productos.domain.entity.Empresa;
import pe.edu.uls.tests_productos.domain.entity.Entidad;
import pe.edu.uls.tests_productos.domain.repository.EmpresaRepository;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    // =====================================================
    // REGISTRAR EMPRESA
    // =====================================================

    public ResponseEmpresa registrarEmpresa(RequestEmpresa request) {
        if (request.ruc() == null || request.ruc().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El campo 'ruc' no puede estar vacío");
        }
        if (request.razonSocial() == null || request.razonSocial().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El campo 'razonSocial' no puede estar vacío");
        }
        if (request.telefono() == null || request.telefono().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El campo 'telefono' no puede estar vacío");
        }
        if (empresaRepository.existsByRuc(request.ruc().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una empresa con el RUC: " + request.ruc().trim());
        }

        Entidad entidad = new Entidad();
        entidad.setTelefono(request.telefono().trim());
        entidad.setDireccion(request.direccion());
        entidad.setCorreo(request.correo());
        entidad.setEstado(request.estado());

        Empresa empresa = new Empresa();
        empresa.setRuc(request.ruc().trim());
        empresa.setRazonSocial(request.razonSocial().trim());
        empresa.setContacto(request.contacto());
        empresa.setEntidad(entidad);

        return toResponse(empresaRepository.save(empresa));
    }

    // =====================================================
    // BUSCAR EMPRESA POR ID
    // =====================================================

    public ResponseEmpresa obtenerEmpresa(int id) {
        return toResponse(empresaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Empresa no encontrada con ID: " + id)));
    }

    // =====================================================
    // BUSCAR EMPRESAS POR ESTADO
    // =====================================================

    public List<ResponseEmpresa> buscarPorEstado(Integer estado) {
        return empresaRepository.findByEstado(estado).stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // LISTAR TODAS LAS EMPRESAS
    // =====================================================

    public List<ResponseEmpresa> listarEmpresas() {
        return empresaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private ResponseEmpresa toResponse(Empresa empresa) {
        Entidad entidad = empresa.getEntidad();
        return new ResponseEmpresa(
                empresa.getId(),
                empresa.getRuc(),
                empresa.getRazonSocial(),
                empresa.getContacto(),
                entidad != null ? entidad.getTelefono() : null,
                entidad != null ? entidad.getDireccion() : null,
                entidad != null ? entidad.getCorreo() : null,
                entidad != null ? entidad.getEstado() : null
        );
    }
}