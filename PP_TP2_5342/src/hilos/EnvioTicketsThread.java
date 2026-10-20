package hilos;

import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.actividades.Actividad;

/** Envía, en un hilo separado, los tickets de las inscripciones confirmadas de un evento. */
public class EnvioTicketsThread extends Thread {
    private final EventoUniversitario evento;

    public EnvioTicketsThread(EventoUniversitario evento) {
        super("Hilo-Envio-" + evento.getId());
        this.evento = evento;
    }

    @Override
    public void run() {
        System.out.println("   [" + getName() + "] INICIO del envío de tickets del evento " + evento.getId());
        int enviados = 0;
        for (Actividad actividad : evento.getActividades()) {
            for (Inscripcion inscripcion : actividad.getInscripciones()) {
                if (inscripcion.estaConfirmada() && inscripcion.getTicket() != null) {
                    inscripcion.getTicket().enviarTicket();
                    enviados++;
                    try {
                        Thread.sleep(300); // simula la latencia del envío
                    } catch (InterruptedException e) {
                        System.out.println("   [" + getName() + "] Envío interrumpido");
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
        System.out.println("   [" + getName() + "] FIN: " + enviados + " ticket(s) enviados");
    }
}
