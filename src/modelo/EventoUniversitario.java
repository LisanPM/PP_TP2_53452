import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class EventoUniversitario {
    private static int cantidadEventos = 0;

    private final String id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;
    
    private Sala sala; 
    private List<Actividad> actividades; 

    public EventoUniversitario(String id, String titulo, double costoBase, boolean gratuito) {
        this.id = id;
        this.titulo = titulo;
        this.gratuito = gratuito;
        this.costoBase = gratuito ? 0.0 : costoBase;
        this.actividades = new ArrayList<>(); 
        cantidadEventos++;
    }

    public EventoUniversitario(EventoUniversitario otro) {
        this(otro.id + "-COPIA", otro.titulo, otro.costoBase, otro.gratuito);
    }

    // Logica de calculo financiero con ligado dinamico
    public double calcularCostoEstimado() {
        if (this.gratuito) {
            return 0.0;
        }
        double costoTotal = this.costoBase;
        for (Actividad act : this.actividades) {
            costoTotal += act.calcularCostoMateriales(); // Ligado dinamico en tiempo de ejecucion
        }
        return costoTotal * 1.21; // Costo acumulado mas el 21% de IVA
    }

    public void asignarSala(Sala sala) {
        this.sala = sala;
    }

    // Instanciacion polimorfica interactiva segun el tipo solicitado
    public void crearActividad(int id, String titulo, int cupo, String tipoActividad) {
        Scanner scanner = new Scanner(System.in);
        switch (tipoActividad.toLowerCase()) {
            case "charla":
                System.out.print("Ingrese el nombre del disertante para la charla '" + titulo + "': ");
                String disertante = scanner.nextLine();
                Actividad charla = new Charla(id, titulo, disertante, cupo);
                this.actividades.add(charla);
                System.out.println("Ok, Charla creada con exito.");
                break;
                
            case "taller":
                System.out.print("¿El taller '" + titulo + "' requiere notebook? (si/no): ");
                String respuesta = scanner.nextLine();
                boolean requiereNotebook = respuesta.equalsIgnoreCase("si") || respuesta.equalsIgnoreCase("sí");
                Actividad taller = new Taller(id, titulo, requiereNotebook, cupo);
                this.actividades.add(taller);
                System.out.println("Ok, Taller creado con exito.");
                break;
                
            default:
                System.out.println("Error: Tipo de actividad '" + tipoActividad + "' no reconocido.");
        }
    }

    public List<Actividad> getActividades() {
        return this.actividades;
    }

    public static int getCantidadEventos() {
        return cantidadEventos;
    }

    public void mostrarDatos() {
        System.out.println("==================================================");
        System.out.println("ID DEL EVENTO: " + this.id);
        System.out.println("Titulo: " + this.titulo);
        System.out.println("¿Es gratuito?: " + (this.gratuito ? "Si" : "No"));
        System.out.println("Costo Base: $" + this.costoBase);
        System.out.println("Costo Estimado (c/IVA): $" + this.calcularCostoEstimado());
        
        if (this.sala != null) {
            System.out.println("Sala asignada: " + this.sala.getNombre());
        } else {
            System.out.println("Sala asignada: Ninguna");
        }
        
        System.out.println("--------------------------------------------------");
        System.out.println("Actividades planificadas:");
        if (this.actividades.isEmpty()) {
            System.out.println("   No hay actividades registradas en este evento.");
        } else {
            for (Actividad act : this.actividades) {
                act.mostrarIdentificacion(); // Metodo final
                act.mostrarInscripciones();  
                System.out.println();
            }
        }
        System.out.println("==================================================");
    }
}