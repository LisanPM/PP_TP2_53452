public class Charla extends Actividad {
    private String disertante;

    // Constructor que encadena la inicializacion con la clase madre
    public Charla(int id, String titulo, String disertante, int cupo) {
        super(id, titulo, cupo);
        this.disertante = disertante;
    }

    @Override
    public double calcularCostoMateriales() {
        return 0.0; // Las charlas no tienen costo de materiales
    }

    @Override
    public String getTipo() {
        return "Charla";
    }

    public String getDisertante() {
        return this.disertante;
    }
}
