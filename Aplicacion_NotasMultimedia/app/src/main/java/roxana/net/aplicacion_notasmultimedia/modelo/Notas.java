package roxana.net.aplicacion_notasmultimedia.modelo;

/**
 * Created by Rox on 19/10/2017.
 */

public class Notas {
    int id;
    String titulo;
    String descripion;

    public Notas(){
    }


    public Notas(int id, String titulo, String descripion) {
        this.id = id;
        this.titulo = titulo;
        this.descripion = descripion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripion() {
        return descripion;
    }

    public void setDescripion(String descripion) {
        this.descripion = descripion;
    }

    @Override
    public String toString() {
        return this.titulo + "\n" + this.descripion;
    }


}
