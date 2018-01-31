package modelo;

import android.content.Context;

import java.util.Date;

/**
 * Created by Rox on 05/10/2017.
 */

public class Contacto {
    int id;
    String nombre;
    String correo_electronico;
    String twitter;
    String telefono;
    String fecha_nacimiento;

    public Contacto() {

    }

    public int getId() {
        return id;
    }

    public Contacto(int id, String nombre, String correo_electronico, String twitter, String telefono,String fecha_nacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.correo_electronico = correo_electronico;
        this.twitter = twitter;
        this.telefono=telefono;
        this.fecha_nacimiento=fecha_nacimiento;
    }

    public void setId(int id) {
        this.id = id;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    @Override
    public String toString() {
        return this.nombre + "\n" + this.correo_electronico + "\n" +this.twitter + "\n" +this.telefono +"\n" + this.fecha_nacimiento;
    }
}
