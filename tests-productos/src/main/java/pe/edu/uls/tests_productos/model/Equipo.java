package pe.edu.uls.tests_productos.model;

public class Equipo {

    private String codigo;
    private String nombre;
    private String modelo;
    private double potencia;
    private double horometro;
    private String ubicacion;
    private String combustible;
    private String estado;

    public Equipo() {
    }

    public Equipo(String codigo, String nombre, String modelo,
                  double potencia, double horometro,
                  String ubicacion, String combustible,
                  String estado) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.modelo = modelo;
        this.potencia = potencia;
        this.horometro = horometro;
        this.ubicacion = ubicacion;
        this.combustible = combustible;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getPotencia() {
        return potencia;
    }

    public void setPotencia(double potencia) {
        this.potencia = potencia;
    }

    public double getHorometro() {
        return horometro;
    }

    public void setHorometro(double horometro) {
        this.horometro = horometro;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}