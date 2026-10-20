package modelo;

import java.io.Serializable;

public class Sala implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;

    public Sala(int id, String nombre) {
        this.id = Validaciones.enteroPositivo(id, "id de sala");
        this.nombre = Validaciones.textoNoVacio(nombre, "nombre de sala");
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() { return "Sala{id=" + id + ", nombre='" + nombre + "'}"; }
}
