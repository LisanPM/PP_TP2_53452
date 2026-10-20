package modelo;

import java.io.Serializable;

public class Estudiante implements Serializable {
    private static final long serialVersionUID = 1L;

    private String legajo;
    private String nombre;

    public Estudiante(String legajo, String nombre) {
        this.legajo = Validaciones.textoNoVacio(legajo, "legajo");
        this.nombre = Validaciones.textoNoVacio(nombre, "nombre de estudiante");
    }

    public String getLegajo() { return legajo; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() { return nombre + " (legajo " + legajo + ")"; }
}
