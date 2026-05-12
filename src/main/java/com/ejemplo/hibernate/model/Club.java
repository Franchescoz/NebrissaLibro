package com.ejemplo.hibernate.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "club", schema = "nebrissabiblioteca")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_club", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "num_integrantes")
    private Integer numIntegrantes;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_fundacion")
    private LocalDate fechaFundacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idbiblioteca")
    private Biblioteca idbiblioteca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_admin_id")
    private Usuario usuarioAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_actual_id")
    private Libro libroActual;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumIntegrantes() {
        return numIntegrantes;
    }

    public void setNumIntegrantes(Integer numIntegrantes) {
        this.numIntegrantes = numIntegrantes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaFundacion() {
        return fechaFundacion;
    }

    public void setFechaFundacion(LocalDate fechaFundacion) {
        this.fechaFundacion = fechaFundacion;
    }

    public Biblioteca getIdbiblioteca() {
        return idbiblioteca;
    }

    public void setIdbiblioteca(Biblioteca idbiblioteca) {
        this.idbiblioteca = idbiblioteca;
    }

    public Usuario getUsuarioAdmin() {
        return usuarioAdmin;
    }

    public void setUsuarioAdmin(Usuario usuarioAdmin) {
        this.usuarioAdmin = usuarioAdmin;
    }

    public Libro getLibroActual() {
        return libroActual;
    }

    public void setLibroActual(Libro libroActual) {
        this.libroActual = libroActual;
    }

}