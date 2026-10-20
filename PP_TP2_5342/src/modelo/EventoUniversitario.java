package modelo;

import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventoUniversitario implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DIRECTORIO = "datos";
    private static final int CUPO_POR_DEFECTO = 30;

    private static int cantidadEventos = 0; // static: no se serializa

    private final String id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;
    private Sala sala;
    private final List<Actividad> actividades = new ArrayList<>();

    public EventoUniversitario(String id, String titulo, double costoBase, boolean gratuito) {
        this.id = Validaciones.textoNoVacio(id, "id de evento");
        this.titulo = Validaciones.textoNoVacio(titulo, "título de evento");
        this.costoBase = Validaciones.noNegativo(costoBase, "costo base");
        this.gratuito = gratuito;
        cantidadEventos++;
    }

    /** Constructor de copia: el id es final y único, por eso la copia lleva sufijo. */
    public EventoUniversitario(EventoUniversitario otro) {
        this(otro.id + "-copia", otro.titulo, otro.costoBase, otro.gratuito);
        this.sala = otro.sala;
        this.actividades.addAll(otro.actividades);
    }

    // ---------- Ejercicio 3: métodos parametrizados acotados y wildcards ----------

    public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo) {
        List<T> resultado = new ArrayList<>();
        for (Actividad a : actividades) {
            if (tipo.isInstance(a)) {
                resultado.add(tipo.cast(a));
            }
        }
        return resultado;
    }

    public double calcularCostoMateriales(List<? extends Actividad> actividades) {
        double total = 0;
        for (Actividad a : actividades) {
            total += a.calcularCostoMateriales();
        }
        return total;
    }

    public double calcularCostoEstimado() {
        return gratuito ? 0.0 : costoBase + calcularCostoMateriales(actividades);
    }

    // ---------- Gestión del evento ----------

    public void asignarSala(Sala sala) {
        if (sala == null) {
            throw new IllegalArgumentException("La sala no puede ser nula");
        }
        this.sala = sala;
    }

    public void agregarActividad(Actividad actividad) {
        if (actividad == null) {
            throw new IllegalArgumentException("La actividad no puede ser nula");
        }
        actividades.add(actividad);
    }

    public void crearActividad(int id, String titulo, String tipo) {
        crearActividad(id, titulo, tipo, CUPO_POR_DEFECTO);
    }

    public void crearActividad(int id, String titulo, String tipo, int cupo) {
        Validaciones.textoNoVacio(tipo, "tipo de actividad");
        switch (tipo.trim().toLowerCase()) {
            case "charla" -> agregarActividad(new Charla(id, titulo, cupo, "A confirmar"));
            case "taller" -> agregarActividad(new Taller(id, titulo, cupo, true));
            case "curso" -> agregarActividad(new Curso(id, titulo, cupo, 1));
            default -> throw new IllegalArgumentException("Tipo de actividad desconocido: " + tipo);
        }
    }

    public void mostrarDatos() {
        System.out.println("EVENTO " + id + ": " + titulo
                + " | " + (gratuito ? "GRATUITO" : "costo base $" + costoBase)
                + " | " + (sala != null ? sala : "sin sala")
                + " | costo estimado $" + calcularCostoEstimado());
        for (Actividad a : actividades) {
            a.mostrarIdentificacion();
            a.mostrarInscripciones();
        }
    }

    // ---------- Ejercicio 1: persistencia con manejo granular de excepciones ----------

    private static File archivoDe(String idEvento) {
        return new File(DIRECTORIO, "evento_" + idEvento + ".ser");
    }

    public boolean persistirEvento() {
        File archivo = archivoDe(id);
        File dir = archivo.getParentFile();
        if (!dir.exists() && !dir.mkdirs()) {
            System.out.println("   [persistir] No se pudo crear el directorio '" + dir + "'");
            return false;
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivo))) {
            out.writeObject(this);
            System.out.println("   [persistir] Evento " + id + " guardado en " + archivo.getPath());
            return true;
        } catch (NotSerializableException e) {
            System.out.println("   [persistir] Un objeto del evento no es Serializable: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("   [persistir] No se pudo abrir/crear el archivo: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("   [persistir] Error de E/S al guardar: " + e.getMessage());
        }
        return false;
    }

    public EventoUniversitario recuperarEvento(String idEvento) {
        File archivo = archivoDe(idEvento);
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            EventoUniversitario recuperado = (EventoUniversitario) in.readObject();
            System.out.println("   [recuperar] Evento " + idEvento + " leído desde " + archivo.getPath());
            return recuperado;
        } catch (FileNotFoundException e) {
            System.out.println("   [recuperar] El archivo del evento '" + idEvento + "' no existe: " + archivo.getPath());
        } catch (StreamCorruptedException e) {
            System.out.println("   [recuperar] El archivo está corrupto o no es una serialización válida: " + e.getMessage());
        } catch (InvalidClassException e) {
            System.out.println("   [recuperar] Versión de clase incompatible: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("   [recuperar] El archivo está incompleto (fin de archivo inesperado)");
        } catch (ClassNotFoundException e) {
            System.out.println("   [recuperar] No se encontró la clase del objeto guardado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("   [recuperar] Error de E/S al leer: " + e.getMessage());
        }
        return null;
    }

    public static int getCantidadEventos() { return cantidadEventos; }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public Sala getSala() { return sala; }
    public List<Actividad> getActividades() { return Collections.unmodifiableList(actividades); }
}
