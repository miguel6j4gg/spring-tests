package pe.edu.uls.tests_productos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_equipo")
    @SequenceGenerator(
        name = "sec_equipo",
        sequenceName = "sec_equipo",
        allocationSize = 1
    )
    private int id;

    @Column(unique = true, nullable = false)
    private String codigo;

    private String nombre;
    private String modelo;
    private double potencia;
    private double horometro;
    private String ubicacion;
    private String combustible;
    private String estado;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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