package pe.edu.uls.tests_productos.domain.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sec_almacen")
    @SequenceGenerator(name = "sec_almacen", sequenceName = "sec_almacen", allocationSize = 1)
    private int id;

    @Column(unique = true, nullable = false)
    private String codigo;

    private String nombre;
    private String direccion;
    private String ciudad;
    private double capacidad;
    private Integer estado;

    @CreationTimestamp
    @Column(name = "CREATED_DATE", updatable = false)
    private LocalDate createdDate;

    @UpdateTimestamp
    @Column(name = "UPDATED_DATE")
    private LocalDate updatedDate;

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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public double getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(double capacidad) {
        this.capacidad = capacidad;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }
}
