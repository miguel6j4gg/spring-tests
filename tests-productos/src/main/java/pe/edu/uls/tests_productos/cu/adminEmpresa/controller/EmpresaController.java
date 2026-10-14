package pe.edu.uls.tests_productos.cu.adminEmpresa.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.uls.tests_productos.cu.adminEmpresa.request.RequestEmpresa;
import pe.edu.uls.tests_productos.cu.adminEmpresa.response.ResponseEmpresa;
import pe.edu.uls.tests_productos.cu.adminEmpresa.service.EmpresaService;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    // POST 1 - REGISTRAR EMPRESA
    @PostMapping
    public ResponseEmpresa registrarEmpresa(@RequestBody RequestEmpresa request) {
        return empresaService.registrarEmpresa(request);
    }

    // GET 2 - BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEmpresa obtenerEmpresa(@PathVariable int id) {
        return empresaService.obtenerEmpresa(id);
    }

    // GET 3 - BUSCAR POR ESTADO
    @GetMapping("/estado/{estado}")
    public List<ResponseEmpresa> buscarPorEstado(@PathVariable Integer estado) {
        return empresaService.buscarPorEstado(estado);
    }

    // GET 4 - LISTAR TODAS
    @GetMapping
    public List<ResponseEmpresa> listarEmpresas() {
        return empresaService.listarEmpresas();
    }
}