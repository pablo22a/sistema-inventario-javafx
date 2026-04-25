package mx.unison;

/**
 * Clase de entrada para la aplicación JavaFX.
 *
 * Necesaria para evitar el error de JavaFX runtime components
 * al ejecutar la aplicación directamente.
 */
public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}