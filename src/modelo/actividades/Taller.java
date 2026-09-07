public class Taller extends Actividad {
    private boolean requiereNotebook;

    // Constructor que encadena la inicializacion con la clase madre
    public Taller(int id, String titulo, boolean requiereNotebook, int cupo) {
        super(id, titulo, cupo);
        this.requiereNotebook = requiereNotebook;
    }

    @Override
    public double calcularCostoMateriales() {
        if (this.requiereNotebook) {
            return 5000.0; // Costo si requiere notebook
        }
        return 2000.0; // Costo estandar
    }

    @Override
    public String getTipo() {
        return "Taller";
    }

    public boolean isRequiereNotebook() {
        return this.requiereNotebook;
    }
}