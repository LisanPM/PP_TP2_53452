import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public abstract class Actividad {
    private int id;    
    private String titulo;
    private int cupoMaximo;
    public static final int CUPO_MINIMO = 1;
    private List<Inscripcion> inscripciones;

    // El constructor de la superclase
    public Actividad(int id, String titulo, int cupoMaximo) {
        this.id = id;
        this.titulo = titulo;
        this.cupoMaximo = cupoMaximo;
        this.inscripciones = new ArrayList<>(); 
    }

    // El metodo de inscripcion mantiene la logica de control de cupos
    public Inscripcion inscribir(Estudiante estudiante) {
        if (this.inscripciones.size() < this.cupoMaximo) {   
            Inscripcion nuevaInscripcion = new Inscripcion(LocalDate.now(), "Aceptada", estudiante);
            this.inscripciones.add(nuevaInscripcion);
            System.out.println("Inscripcion aceptada para " + estudiante.getNombre() + " en " + this.titulo);
            return nuevaInscripcion;
        } else {
            System.out.println("No se pudo inscribir a " + estudiante.getNombre() + ", no hay mas cupos disponibles en " + this.titulo);
            return null;
        }
    }

    // Mostrar los alumnos inscriptos en esta actividad especifica
    public void mostrarInscripciones() {
        System.out.println("Alumnos inscriptos en " + this.titulo + ":");
        if (this.inscripciones.isEmpty()) {
            System.out.println("   No hay alumnos inscriptos en esta actividad.");
        } else {
            for (Inscripcion ins : this.inscripciones) {
                System.out.println("   - [" + ins.getestudiante().getLegajo() + "] " + ins.getestudiante().getNombre());
            } 
        }
    }

    // Metodo calificado como FINAL: impide la redefinicion en las subclases
    public final void mostrarIdentificacion() {
        System.out.println("Actividad: " + this.titulo + " (ID: " + this.id + ") - Tipo: " + this.getTipo());
    }

    // Metodos abstractos que seran resueltos por las subclases concretas
    public abstract double calcularCostoMateriales();
    public abstract String getTipo();

    // Getters necesarios para las subclases e inscripciones
    public String getTitulo() {
        return this.titulo;
    }
}