import hilos.EnvioTicketsThread;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.Sala;
import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;
import modelo.certificacion.Certificable;
import modelo.excepciones.CupoExcedidoException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8)); // tildes correctos en cualquier consola
        ejercicio1();
        ejercicio2();
        ejercicio3();
        ejercicio4();
    }

    private static void titulo(String texto) {
        System.out.println("\n==================== " + texto + " ====================");
    }

    // ---------------------------------------------------------------- Ejercicio 1
    private static void ejercicio1() {
        titulo("EJERCICIO 1: excepciones y persistencia");

        Estudiante ana = new Estudiante("50268", "Ana Gomez");
        Estudiante luis = new Estudiante("50311", "Luis Perez");

        System.out.println("\n--- Caso EXITOSO ---");
        EventoUniversitario ok = new EventoUniversitario("EV-1", "Semana de la Ingenieria", 5000, false);
        ok.asignarSala(new Sala(1, "Aula Magna"));
        Taller taller = new Taller(10, "Taller de Java", 3, true);
        ok.agregarActividad(taller);
        flujoInscribirPersistirLeer(ok, taller, ana, luis);

        System.out.println("\n--- Caso FALLIDO: cupo excedido (CupoExcedidoException) ---");
        EventoUniversitario fallo = new EventoUniversitario("EV-2", "Jornada de Robotica", 3000, false);
        fallo.asignarSala(new Sala(2, "Laboratorio 3"));
        Charla charla = new Charla(20, "Charla de Robotica", 1, "Dra. Lopez");
        fallo.agregarActividad(charla);
        flujoInscribirPersistirLeer(fallo, charla, ana, luis);

        System.out.println("\n--- Casos FALLIDOS de persistencia ---");
        System.out.println(" * Leer un evento que no existe:");
        ok.recuperarEvento("NO-EXISTE");
        System.out.println(" * Leer un archivo corrupto:");
        crearArchivoCorrupto();
        ok.recuperarEvento("CORRUPTO");

        System.out.println("\n--- Casos FALLIDOS de datos inválidos ---");
        try {
            new Estudiante("", "Sin legajo");
        } catch (IllegalArgumentException e) {
            System.out.println("   [datos inválidos] " + e.getMessage());
        }
        try {
            new Taller(11, "Taller sin cupo", 0, false);
        } catch (IllegalArgumentException e) {
            System.out.println("   [datos inválidos] " + e.getMessage());
        }
    }

    /** Flujo pedido: intentar inscribir, persistir y leer, con try-catch-finally. */
    private static void flujoInscribirPersistirLeer(EventoUniversitario evento, Actividad actividad,
                                                    Estudiante e1, Estudiante e2) {
        try {
            actividad.inscribir(e1);
            System.out.println("   Inscripto: " + e1.getNombre());
            actividad.inscribir(e2); // en el caso fallido lanza CupoExcedidoException
            System.out.println("   Inscripto: " + e2.getNombre());

            if (!actividad.alcanzaCupoMinimo()) {
                System.out.println("   AVISO: '" + actividad.getTitulo() + "' no alcanza el cupo mínimo de "
                        + Actividad.CUPO_MINIMO);
            }
            if (evento.persistirEvento()) {
                EventoUniversitario leido = evento.recuperarEvento(evento.getId());
                if (leido != null) {
                    System.out.println("   Datos del evento recuperado:");
                    leido.mostrarDatos();
                }
            }
        } catch (CupoExcedidoException e) {
            System.out.println("   [catch CupoExcedidoException] " + e.getMessage());
            if (!actividad.alcanzaCupoMinimo()) {
                System.out.println("   AVISO: '" + actividad.getTitulo() + "' no alcanza el cupo mínimo de "
                        + Actividad.CUPO_MINIMO + " (inscriptos: " + actividad.getInscripciones().size() + ")");
            }
        } finally {
            System.out.println("   [finally] Fin del flujo del evento " + evento.getId());
        }
    }

    private static void crearArchivoCorrupto() {
        java.io.File dir = new java.io.File("datos");
        dir.mkdirs();
        try (FileWriter w = new FileWriter(new java.io.File(dir, "evento_CORRUPTO.ser"))) {
            w.write("esto no es un objeto serializado");
        } catch (IOException e) {
            System.out.println("   No se pudo preparar el archivo de prueba: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------- Ejercicio 2
    private static void ejercicio2() {
        titulo("EJERCICIO 2: interfaces y certificados");

        Estudiante ana = new Estudiante("50268", "Ana Gomez");
        Estudiante luis = new Estudiante("50311", "Luis Perez");
        Estudiante eva = new Estudiante("50400", "Eva Torres");

        EventoUniversitario evento = new EventoUniversitario("EV-3", "Congreso de Sistemas", 4000, false);
        evento.asignarSala(new Sala(3, "Auditorio"));
        Charla charla = new Charla(30, "Charla de IA", 50, "Dr. Ruiz");
        Taller taller = new Taller(31, "Taller de Git", 20, true);
        Curso curso = new Curso(32, "Curso de SQL", 20, 2);
        evento.agregarActividad(charla);
        evento.agregarActividad(taller);
        evento.agregarActividad(curso);

        try {
            for (Actividad a : evento.getActividades()) {
                for (Estudiante e : List.of(ana, luis, eva)) {
                    a.inscribir(e).confirmar();
                }
            }
        } catch (CupoExcedidoException e) {
            System.out.println("[catch] " + e.getMessage());
        }

        System.out.println("\nCertificados emitidos (solo Taller y Curso):");
        for (Actividad a : evento.getActividades()) {
            if (a instanceof Certificable) {
                Certificable certificable = (Certificable) a;
                for (Inscripcion i : a.getInscripciones()) {
                    if (i.estaConfirmada()) {
                        System.out.println("   " + certificable.generarCertificado(i.getEstudiante()));
                    }
                }
            } else {
                System.out.println("   (" + a.getTipo() + " '" + a.getTitulo() + "': no certificable)");
            }
        }

        System.out.println("\nDatos del evento:");
        evento.mostrarDatos();
    }

    // ---------------------------------------------------------------- Ejercicio 3
    private static void ejercicio3() {
        titulo("EJERCICIO 3: genericos acotados y wildcards");

        List<Estudiante> estudiantes = List.of(
                new Estudiante("50268", "Ana Gomez"), new Estudiante("50311", "Luis Perez"),
                new Estudiante("50400", "Eva Torres"), new Estudiante("50512", "Mario Diaz"));

        EventoUniversitario evento = new EventoUniversitario("EV-4", "Expo Tecnologica", 2500, false);
        evento.asignarSala(new Sala(4, "Sala B"));
        evento.agregarActividad(new Charla(40, "Charla de Redes", 40, "Ing. Soto"));
        evento.agregarActividad(new Charla(41, "Charla de Seguridad", 40, "Ing. Vega"));
        evento.agregarActividad(new Taller(42, "Taller de Docker", 10, false));
        evento.agregarActividad(new Curso(43, "Curso de Python", 10, 1));
        evento.crearActividad(44, "Curso de Estadistica", "curso", 10);

        try {
            int n = 0;
            for (Actividad a : evento.getActividades()) {
                a.inscribir(estudiantes.get(n % estudiantes.size()));
                a.inscribir(estudiantes.get((n + 1) % estudiantes.size()));
                n++;
            }
        } catch (CupoExcedidoException e) {
            System.out.println("[catch] " + e.getMessage());
        }

        List<Charla> charlas = evento.filtrarActividadesPorTipo(Charla.class);
        List<Taller> talleres = evento.filtrarActividadesPorTipo(Taller.class);
        List<Curso> cursos = evento.filtrarActividadesPorTipo(Curso.class);

        System.out.println("\nCantidad de actividades por tipo en " + evento.getId() + ":");
        System.out.println("   Charlas: " + charlas.size());
        System.out.println("   Talleres: " + talleres.size());
        System.out.println("   Cursos: " + cursos.size());

        System.out.println("\nCosto de materiales por tipo (List<? extends Actividad>):");
        System.out.println("   Charlas:  $" + evento.calcularCostoMateriales(charlas));
        System.out.println("   Talleres: $" + evento.calcularCostoMateriales(talleres));
        System.out.println("   Cursos:   $" + evento.calcularCostoMateriales(cursos));
        System.out.println("   Total (List<Actividad>): $" + evento.calcularCostoMateriales(evento.getActividades()));

        System.out.println("\nListas correctamente tipadas (sin casts):");
        String disertante = charlas.get(0).getDisertante();      // método exclusivo de Charla
        boolean notebook = talleres.get(0).isRequiereNotebook(); // método exclusivo de Taller
        int nivel = cursos.get(0).getNivel();                    // método exclusivo de Curso
        System.out.println("   List<Charla>.get(0).getDisertante()      = " + disertante);
        System.out.println("   List<Taller>.get(0).isRequiereNotebook() = " + notebook);
        System.out.println("   List<Curso>.get(0).getNivel()            = " + nivel);
    }

    // ---------------------------------------------------------------- Ejercicio 4
    private static void ejercicio4() {
        titulo("EJERCICIO 4: clases anidadas e hilos");

        List<Estudiante> estudiantes = List.of(
                new Estudiante("50268", "Ana Gomez"), new Estudiante("50311", "Luis Perez"),
                new Estudiante("50400", "Eva Torres"), new Estudiante("50512", "Mario Diaz"));

        EventoUniversitario ev1 = new EventoUniversitario("EV-5", "Jornadas de Software", 3500, false);
        ev1.asignarSala(new Sala(5, "Sala Norte"));
        ev1.agregarActividad(new Charla(50, "Charla de Arquitectura", 10, "Ing. Mora"));
        ev1.agregarActividad(new Taller(51, "Taller de Testing", 10, true));

        EventoUniversitario ev2 = new EventoUniversitario("EV-6", "Semana de Datos", 0, true);
        ev2.asignarSala(new Sala(6, "Sala Sur"));
        ev2.agregarActividad(new Curso(60, "Curso de Big Data", 10, 3));
        ev2.agregarActividad(new Charla(61, "Charla de Data Ethics", 10, "Lic. Paz"));

        List<EventoUniversitario> eventos = List.of(ev1, ev2);

        // c) inscribir, d) confirmar algunas (las pares), e) un ticket por inscripción confirmada
        int generados = 0;
        try {
            for (EventoUniversitario ev : eventos) {
                for (Actividad a : ev.getActividades()) {
                    for (Estudiante e : estudiantes) {
                        a.inscribir(e);
                    }
                    int idx = 0;
                    for (Inscripcion i : a.getInscripciones()) {
                        if (idx++ % 2 == 0) {
                            i.confirmar();
                            i.generarTicket();
                            generados++;
                        }
                    }
                }
            }
        } catch (CupoExcedidoException e) {
            System.out.println("[catch] " + e.getMessage());
        }
        System.out.println("Tickets generados (inscripciones confirmadas): " + generados);

        // f) iniciar el proceso concurrente
        System.out.println("\n[" + Thread.currentThread().getName() + "] Inicio los hilos de envío...");
        List<EnvioTicketsThread> hilos = new ArrayList<>();
        for (EventoUniversitario ev : eventos) {
            EnvioTicketsThread hilo = new EnvioTicketsThread(ev);
            hilos.add(hilo);
            hilo.start();
        }

        // g) y h) el hilo principal sigue mostrando información mientras los otros envían
        for (EventoUniversitario ev : eventos) {
            System.out.println("[" + Thread.currentThread().getName() + "] EVENTO " + ev.getId() + ": "
                    + ev.getTitulo() + " | " + ev.getSala());
            pausa(150);
            for (Actividad a : ev.getActividades()) {
                System.out.println("[" + Thread.currentThread().getName() + "] "
                        + a.getTipo() + " '" + a.getTitulo() + "' inscriptos: " + a.getInscripciones().size());
                for (Inscripcion i : a.getInscripciones()) {
                    System.out.println("[" + Thread.currentThread().getName() + "]      - "
                            + i.getEstudiante().getNombre() + " [" + i.getEstado() + "]");
                    pausa(120);
                }
            }
        }

        for (EnvioTicketsThread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                System.out.println("[catch] Espera interrumpida");
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("[" + Thread.currentThread().getName() + "] Todos los hilos de envío finalizaron.");
    }

    private static void pausa(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
