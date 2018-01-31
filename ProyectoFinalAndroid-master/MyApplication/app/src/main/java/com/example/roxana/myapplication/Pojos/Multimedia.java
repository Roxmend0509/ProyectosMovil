package com.example.roxana.myapplication.Pojos;



public class Multimedia {
    private int ImageId;
    private int id;
    private String archivo;

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    private String Descripcion;
    private int Nota;

    public Multimedia(){};
    public Multimedia(String archivo, String descripcion, int nota) {
        this.archivo = archivo;
        Descripcion = descripcion;
        Nota = nota;
    }
    public Multimedia(int ImageId,String archivo, String descripcion, int nota) {
        this.ImageId=ImageId;
        this.archivo = archivo;
        Descripcion = descripcion;
        Nota = nota;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public void setNota(int nota) {
        Nota = nota;
    }

    public int getId() {
        return id;
    }

    public String getArchivo() {
        return archivo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public int getNota() {
        return Nota;
    }
}
