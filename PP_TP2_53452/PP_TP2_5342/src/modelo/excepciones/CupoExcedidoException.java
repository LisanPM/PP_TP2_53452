package modelo.excepciones;

/** Excepción CHEQUEADA: se lanza cuando una actividad ya no tiene cupo disponible. */
public class CupoExcedidoException extends Exception {
    private static final long serialVersionUID = 1L;

    public CupoExcedidoException(String mensaje) {
        super(mensaje);
    }
}
