package modelo.actividades;

import modelo.Validaciones;

public class Charla extends Actividad {
    private static final long serialVersionUID = 1L;

    private String disertante;

    public Charla(int id, String titulo, int cupoMaximo, String disertante) {
        super(id, titulo, cupoMaximo);
        this.disertante = Validaciones.textoNoVacio(disertante, "disertante");
    }

    public String getDisertante() { return disertante; }

    @Override
    public double calcularCostoMateriales() { return 200.0; }

    @Override
    public String getTipo() { return "Charla"; }
}
