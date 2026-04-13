package com.ejemplo.model;

public class biblioteca {
    private String nombre;
    private String direccion;
    private String telefono_contacto;

    public  biblioteca(String nombre, String direccion, String telefono_contacto) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono_contacto = telefono_contacto;
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

    public String getTelefono_contacto() {
        return telefono_contacto;
    }

    public void setTelefono_contacto(String telefono_contacto) {
        this.telefono_contacto = telefono_contacto;
    }
}
