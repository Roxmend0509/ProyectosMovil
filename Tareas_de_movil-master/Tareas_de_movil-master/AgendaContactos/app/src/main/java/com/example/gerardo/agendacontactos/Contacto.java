package com.example.gerardo.agendacontactos;

import java.io.Serializable;

/**
 * Created by Gerardo on 02/09/2017.
 */

public class Contacto implements Serializable {
    private static final long serialVersionUID=1L;

    private String _nombre;
    private String _email;
    private String _twiter;
    private String _tel;
    private String _fec;

    public void setNombre(String nombre){
        this._nombre=nombre;
    }
    public String getNombre(){
        return this._nombre;
    }

    public void setemail(String email){
        this._email=email;
    }
    public String getemail(){
        return this._email;
    }

    public void settwiter(String twiter){
        this._twiter=twiter;
    }
    public String gettwiter(){
        return this._twiter;
    }

    public void settel(String tel){
        this._tel=tel;
    }
    public String gettel(){
        return this._tel;
    }

    public void setfec(String fec){
        this._fec=fec;
    }
    public String getfec(){
        return this._fec;
    }




}
