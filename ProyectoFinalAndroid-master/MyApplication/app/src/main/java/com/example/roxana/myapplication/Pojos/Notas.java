package com.example.roxana.myapplication.Pojos;


public class Notas {
    private int id;
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    private String estado;
    private String tipo;

    public Notas(){};


    public Notas(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Notas(String titulo, String descripcion,String estado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado=estado;
    }


    public Notas(String titulo, String descripcion, String fecha, String hora,String tipo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo=tipo;
    }

    public Notas(String titulo, String descripcion, String fecha, String hora,String estado,String tipo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.estado=estado;
        this.tipo=tipo;
    }



    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }


    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }


    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String isEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
