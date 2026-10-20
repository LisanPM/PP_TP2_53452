# PP_TP2 – Programación Orientada a Objetos en Java

**Materia:** Paradigmas de Programación – UTN FRM
**Unidad 2:** Organización, reutilización y recursos avanzados en POO
**Alumno/Legajo:** 53452

Sistema de eventos universitarios. Cubre los 4 ejercicios del TP2 sobre el modelo OO del enunciado.

## Cómo ejecutar
1. Clonar el repositorio y abrirlo en IntelliJ IDEA (JDK 17 o superior; se desarrolló con JDK 21).
2. Ejecutar la clase `App` (`src/App.java`).
3. Los eventos serializados se guardan en la carpeta `datos/` (se crea sola).

Por consola: `javac -d out $(find src -name "*.java")` y luego `java -cp out App`.

## Estructura de packages
```
src/
├── App.java                      (clase principal)
├── hilos/EnvioTicketsThread
└── modelo/
    ├── EventoUniversitario, Sala, Estudiante, Inscripcion (+ TicketDeAcceso), Validaciones
    ├── actividades/   Actividad (abstracta), Charla, Taller, Curso
    ├── certificacion/ Certificable (interfaz)
    └── excepciones/   CupoExcedidoException
```

## Qué resuelve cada ejercicio
**Ej. 1 – Packages, excepciones y serialización**
- `Actividad.inscribir()` declara `throws CupoExcedidoException` (chequeada) y la lanza con `throw`; `App` la atrapa.
- `EventoUniversitario.persistirEvento()` / `recuperarEvento()` serializan con `ObjectOutputStream`/`ObjectInputStream` (try-with-resources) y capturan de forma granular: `NotSerializableException`, `FileNotFoundException`, `StreamCorruptedException`, `InvalidClassException`, `EOFException`, `ClassNotFoundException` e `IOException`.
- `App` ejecuta un flujo `try-catch-finally` (inscribir → persistir → leer) con un caso exitoso y uno fallido. También demuestra lectura de archivo inexistente, archivo corrupto y datos inválidos (`IllegalArgumentException` desde `Validaciones`).
- Cupo mínimo: `Actividad.CUPO_MINIMO` y `alcanzaCupoMinimo()`; si no se alcanza se informa como aviso (no impide el evento).

**Ej. 2 – Interfaces**
- `Certificable` (con constante `ENTIDAD_EMISORA`) es implementada por `Taller` y `Curso`; `Charla` no la implementa, por lo que no es certificable. `App` decide con `instanceof Certificable`. Solo se certifican inscripciones confirmadas.

**Ej. 3 – Genéricos acotados y wildcards**
- `<T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T>)` usa `Class.isInstance/cast`.
- `calcularCostoMateriales(List<? extends Actividad>)` acepta `List<Charla>`, `List<Taller>`, `List<Curso>` o `List<Actividad>`.
- `App` muestra métodos exclusivos de cada subtipo sin casts, evidenciando el tipado de las listas.

**Ej. 4 – Clases anidadas e hilos**
- `Inscripcion.TicketDeAcceso` es una clase interna miembro; solo se crea con `generarTicket()` si la inscripción está confirmada (si no, `IllegalStateException`).
- `EnvioTicketsThread` (package `hilos`, extiende `Thread`) envía los tickets del evento en un hilo aparte. En consola se ven intercalados `[main]` y `[Hilo-Envio-EV-x]`.

## Decisiones de diseño
- Las validaciones están en `Validaciones` y en los constructores, no en `App`.
- Los campos `static` (`cantidadEventos`) no se serializan.
- `Inscripcion` referencia a su `Actividad` (necesario para armar el ticket); la serialización soporta el ciclo.

## Salida de ejecución
Ver `captura_consola.png` (captura de IntelliJ) y `salida_consola.txt` (misma salida en texto).
