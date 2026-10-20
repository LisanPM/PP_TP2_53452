package modelo.actividades;

import modelo.Estudiante;
import modelo.Validaciones;
import modelo.certificacion.Certificable;

public class Curso extends Actividad implements Certificable {
    private static final long serialVersionUID = 1L;

    private int nivel;

    public Curso(int id, String titulo, int cupoMaximo, int nivel) {
        super(id, titulo, cupoMaximo);
        this.nivel = Validaciones.enteroPositivo(nivel, "nivel");
    }

    public int getNivel() { return nivel; }

    @Override
    public double calcularCostoMateriales() { return 1000.0 + nivel * 300.0; }

    @Override
    public String getTipo() { return "Curso"; }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "CERTIFICADO DE ASISTENCIA | Emisor: " + ENTIDAD_EMISORA + " | " + estudiante
                + " asistió al Curso '" + getTitulo() + "' (nivel " + nivel + ")";
    }
}
