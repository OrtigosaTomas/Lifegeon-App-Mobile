package com.example.lifegeon;

public class Objeto {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer precio;

    public Objeto(Integer id, String nombre, String descripcion, Integer precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPrecio() { return precio; }

    public void setPrecio(Integer precio) { this.precio = precio; }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }
}
