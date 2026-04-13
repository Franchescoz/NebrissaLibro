package com.ejemplo.model;

import java.sql.Date;

public class autor {
    private String nombre;
    private String bibliografia;
    private Date fecha_nacimiento;

    public  autor(String nombre, String bibliografia, Date fecha_nacimiento) {
        this.nombre = nombre;
        this.bibliografia = bibliografia;
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBibliografia() {
        return bibliografia;
    }

    public void setBibliografia(String bibliografia) {
        this.bibliografia = bibliografia;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
}
