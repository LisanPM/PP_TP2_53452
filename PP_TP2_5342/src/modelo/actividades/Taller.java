package modelo.actividades;

import modelo.Estudiante;
import modelo.certificacion.Certificable;

public class Taller extends Actividad implements Certificable {
    private static final long serialVersionUID = 1L;

    private boolean requiereNotebook;

    public Taller(int id, String titulo, int cupoMaximo, boolean requiereNotebook) {
        super(id, titulo, cupoMaximo);
        this.requiereNotebook = requiereNotebook;
    }

    public boolean isRequiereNotebook() { return requiereNotebook; }

    /** Si el estudiante debe traer notebook, la facultad no la provee y el costo baja. */
    @Override
    public double calcularCostoMateriales() { return requiereNotebook ? 1500.0 : 2300.0; }

    @Override
    public String getTipo() { return "Taller"; }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "CERTIFICADO DE ASISTENCIA | Emisor: " + ENTIDAD_EMISORA + " | " + estudiante
                + " asistió al Taller '" + getTitulo() + "'";
    }
}
