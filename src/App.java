import java.util.List;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("       EJECUCIÓN DEL EJERCICIO 3 - UTN FRM        ");
        System.out.println("==================================================\n");

        // a. Se registren estudiantes
        List<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(new Estudiante("Lionel Messi", "101010"));
        estudiantes.add(new Estudiante("Emiliano Martinez", "232323"));
        estudiantes.add(new Estudiante("Lionel Scaloni", "181818"));

        System.out.println("[a] Lista de estudiantes cargada.");
        System.out.println();

        // b. Se construyan eventos
        EventoUniversitario seminarioSistemas = new EventoUniversitario("EVT-SEM", "Seminario de Ingenieria de Software", 4000.0, false);

        System.out.println("[b] Evento instanciado.");
        System.out.println();

        // c. Se asigne una sala a cada evento
        Sala aulaMagna = new Sala(201, "Aula Magna UTN");
        seminarioSistemas.asignarSala(aulaMagna);

        System.out.println("[c] Sala asignada.");
        System.out.println();

        // d. Se creen actividades para cada evento del tipo Charla y/o Taller
        System.out.println("[d] Creando actividades interactivas (Prestar atencion a la consola):");
        
        // Esta llamada solicitara el nombre del disertante por consola
        seminarioSistemas.crearActividad(101, "Charla de Microservicios", 3, "charla");
        System.out.println();

        // Esta llamada solicitara si requiere o no notebook por consola
        seminarioSistemas.crearActividad(102, "Taller de Git Avanzado", 2, "taller");
        System.out.println();

        // e. Se inscriban estudiantes en cada actividad
        System.out.println("[e] Procesando inscripciones:");
        
        // Inscribimos en la Charla (indice 0)
        seminarioSistemas.getActividades().get(0).inscribir(estudiantes.get(0)); // Messi
        seminarioSistemas.getActividades().get(0).inscribir(estudiantes.get(1)); // Dibu
        
        // Inscribimos en el Taller (indice 1, cupo de 2)
        seminarioSistemas.getActividades().get(1).inscribir(estudiantes.get(0)); // Messi
        seminarioSistemas.getActividades().get(1).inscribir(estudiantes.get(2)); // Scaloni
        
        // Intentamos un tercer inscripto en el taller para probar el cupo maximo
        System.out.println("   * Intento de inscripcion excediendo el limite:");
        seminarioSistemas.getActividades().get(1).inscribir(estudiantes.get(1)); // Dibu (Debe rebotar)
        System.out.println();

        // f. Se muestre el resumen de datos de cada evento y se recorran sus actividades
        System.out.println("[f] Reporte final consolidado (Ligado dinamico):");
        seminarioSistemas.mostrarDatos();
        System.out.println();

        // g. Se muestre el total de eventos creados
        System.out.println("[g] Control general de instancias:");
        System.out.println("Total de eventos universitarios registrados: " + EventoUniversitario.getCantidadEventos());
        System.out.println("==================================================");
    }
}