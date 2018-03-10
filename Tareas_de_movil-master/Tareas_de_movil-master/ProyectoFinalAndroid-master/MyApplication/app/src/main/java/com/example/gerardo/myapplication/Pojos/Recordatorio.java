package com.example.gerardo.myapplication.Pojos;

/**
 * Created by Gerardo on 29/10/2017.
 */

public class Recordatorio {
    private int id;
    private String fecha;
    private String hora;
    private int Nota;

    public Recordatorio(){};
    public Recordatorio(String fecha, String hora, int nota) {
        this.fecha = fecha;
        this.hora = hora;
        Nota = nota;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setNota(int nota) {
        Nota = nota;
    }

    public int getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public int getNota() {
        return Nota;
    }
}
