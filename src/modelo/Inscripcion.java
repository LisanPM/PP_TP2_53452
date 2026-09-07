import java.time.LocalDate; // fechas
public class Inscripcion {
    private LocalDate fecha;
    private  String estado;
    private Estudiante estudiante;
public Inscripcion(LocalDate fecha, String estado, Estudiante estudiante){
    this.estado = estado;
    this.fecha = fecha;
    this.estudiante = estudiante;
    }
    public String getestado() {
        return this.estado;
    }
    public LocalDate getfecha() {
          return this.fecha; 
    
    }
    public Estudiante getestudiante() {
        return this.estudiante;
    }
    
}// llave final
