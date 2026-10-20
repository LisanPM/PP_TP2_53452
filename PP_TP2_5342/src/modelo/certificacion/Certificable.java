package modelo.certificacion;

import modelo.Estudiante;

/** Capacidad de emitir certificados: solo algunas actividades la tienen (Taller, Curso). */
public interface Certificable {
    String ENTIDAD_EMISORA = "UTN - Facultad Regional Mendoza";

    String generarCertificado(Estudiante estudiante);
}
