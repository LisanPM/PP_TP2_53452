package modelo;

import modelo.actividades.Actividad;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Inscripción de un estudiante a una actividad.
 * TicketDeAcceso es una clase anidada MIEMBRO (interna): un ticket solo existe
 * en el contexto de una inscripción concreta y accede a sus datos con Inscripcion.this.
 */
public class Inscripcion implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String PENDIENTE = "PENDIENTE";
    public static final String CONFIRMADA = "CONFIRMADA";

    private final LocalDate fecha;
    private String estado;
    private final Estudiante estudiante;
    private final Actividad actividad;
    private TicketDeAcceso ticket;

    public Inscripcion(Estudiante estudiante, Actividad actividad) {
        if (estudiante == null || actividad == null) {
            throw new IllegalArgumentException("Estudiante y actividad son obligatorios");
        }
        this.estudiante = estudiante;
        this.actividad = actividad;
        this.fecha = LocalDate.now();
        this.estado = PENDIENTE;
    }

    public void confirmar() { this.estado = CONFIRMADA; }
    public boolean estaConfirmada() { return CONFIRMADA.equals(estado); }

    /** Emite el ticket únicamente si la inscripción está confirmada. */
    public TicketDeAcceso generarTicket() {
        if (!estaConfirmada()) {
            throw new IllegalStateException("No se puede emitir ticket: la inscripción de "
                    + estudiante.getNombre() + " no está confirmada");
        }
        if (ticket == null) {
            ticket = new TicketDeAcceso();
        }
        return ticket;
    }

    public LocalDate getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public Estudiante getEstudiante() { return estudiante; }
    public Actividad getActividad() { return actividad; }
    public TicketDeAcceso getTicket() { return ticket; }

    @Override
    public String toString() {
        return "Inscripcion{" + estudiante + ", fecha=" + fecha + ", estado=" + estado + "}";
    }

    public class TicketDeAcceso implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String idTicket;
        private final LocalDate fechaEmision;

        private TicketDeAcceso() {
            this.idTicket = "TKT-" + actividad.getId() + "-" + estudiante.getLegajo();
            this.fechaEmision = LocalDate.now();
        }

        public String getIdTicket() { return idTicket; }
        public LocalDate getFechaEmision() { return fechaEmision; }

        public void enviarTicket() {
            System.out.println("   [" + Thread.currentThread().getName() + "] Ticket " + idTicket
                    + " enviado a " + estudiante.getNombre()
                    + " para '" + actividad.getTitulo() + "' (emitido " + fechaEmision + ")");
        }
    }
}
