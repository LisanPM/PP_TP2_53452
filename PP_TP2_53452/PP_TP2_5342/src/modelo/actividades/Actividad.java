package modelo.actividades;

import modelo.Estudiante;
import modelo.Inscripcion;
import modelo.Validaciones;
import modelo.excepciones.CupoExcedidoException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Actividad implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int CUPO_MINIMO = 2;

    private int id;
    private String titulo;
    private int cupoMaximo;
    private final List<Inscripcion> inscripciones = new ArrayList<>();

    protected Actividad(int id, String titulo, int cupoMaximo) {
        this.id = Validaciones.enteroPositivo(id, "id de actividad");
        this.titulo = Validaciones.textoNoVacio(titulo, "título de actividad");
        this.cupoMaximo = Validaciones.enteroPositivo(cupoMaximo, "cupo máximo");
    }

    /** Lanza CupoExcedidoException (chequeada) si no hay lugar disponible. */
    public Inscripcion inscribir(Estudiante estudiante) throws CupoExcedidoException {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo");
        }
        for (Inscripcion i : inscripciones) {
            if (i.getEstudiante().getLegajo().equals(estudiante.getLegajo())) {
                throw new IllegalArgumentException(estudiante.getNombre() + " ya está inscripto en '" + titulo + "'");
            }
        }
        if (inscripciones.size() >= cupoMaximo) {
            throw new CupoExcedidoException("Cupo agotado en '" + titulo + "' (máximo " + cupoMaximo
                    + "): no se pudo inscribir a " + estudiante.getNombre());
        }
        Inscripcion nueva = new Inscripcion(estudiante, this);
        inscripciones.add(nueva);
        return nueva;
    }

    public boolean alcanzaCupoMinimo() { return inscripciones.size() >= CUPO_MINIMO; }

    public void mostrarInscripciones() {
        if (inscripciones.isEmpty()) {
            System.out.println("      (sin inscripciones)");
        }
        for (Inscripcion i : inscripciones) {
            System.out.println("      - " + i);
        }
    }

    public final void mostrarIdentificacion() {
        System.out.println("   [" + getTipo() + " #" + id + "] " + titulo
                + " | cupo " + inscripciones.size() + "/" + cupoMaximo);
    }

    public abstract double calcularCostoMateriales();
    public abstract String getTipo();

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getCupoMaximo() { return cupoMaximo; }
    public List<Inscripcion> getInscripciones() { return Collections.unmodifiableList(inscripciones); }
}
