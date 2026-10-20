package modelo;

/** Validaciones reutilizables: mantienen las reglas de datos fuera de la lógica principal. */
public final class Validaciones {
    private Validaciones() { }

    public static String textoNoVacio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo '" + campo + "' no puede ser nulo ni vacío");
        }
        return valor;
    }

    public static int enteroPositivo(int valor, String campo) {
        if (valor <= 0) {
            throw new IllegalArgumentException("El campo '" + campo + "' debe ser mayor a 0 (recibido: " + valor + ")");
        }
        return valor;
    }

    public static double noNegativo(double valor, String campo) {
        if (valor < 0) {
            throw new IllegalArgumentException("El campo '" + campo + "' no puede ser negativo (recibido: " + valor + ")");
        }
        return valor;
    }
}
