package net.rox.agendacontactos;

import java.io.Serializable;


public class Metos implements Serializable{
    private String user;
    private String email;
    private String twit;
    private String fon;
    private String naci;

    public Metos(String user, String email, String twit, String fon, String naci) {
        this.user = user;
        this.email = email;
        this.twit = twit;
        this.fon = fon;
        this.naci = naci;
    }

    @Override
    public String toString() {
        return user+"\n"+email;
    }

}
