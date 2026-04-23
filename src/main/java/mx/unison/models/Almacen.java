package mx.unison.models;

/**
 * Clase de almacenes.
 * Contiene los atributos necesarios para guardar almacenes en la base de datos.
 */
public class Almacen {

    /**
     * ID del almacén
     */
    public int id;

    /**
     * Nombre del almacén
     */
    public String nombre;

    /**
     * Ubicación del almacén
     */
    public String ubicacion;

    /**
     * Fecha y hora en la que fue creado el almacén
     */
    public String fechaHoraCreacion;

    /**
     * Fecha y hora en la que se modificó por última vez algún atributo del almacén
     */
    public String fechaHoraUltimaMod;

    /**
     * Nombre de usuario que modificó el almacén la última vez
     */
    public String ultimoUsuario;
}
