package mx.unison.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDateTime;

/**
 * Clase de almacenes.
 * Contiene los atributos necesarios para guardar almacenes en la base de datos.
 */
@DatabaseTable(tableName = "almacenes")
public class Almacen {

    /**
     * ID del almacén
     */
    @DatabaseField(generatedId = true)
    private int id;

    /**
     * Nombre del almacén
     */
    @DatabaseField(canBeNull = false)
    private String nombre;

    /**
     * Ubicación del almacén
     */
    @DatabaseField
    private String ubicacion;

    /**
     * Fecha y hora en la que fue creado el almacén
     */
    @DatabaseField
    private LocalDateTime fechaHoraCreacion;

    /**
     * Fecha y hora en la que se modificó por última vez algún atributo del almacén
     */
    @DatabaseField
    private LocalDateTime fechaHoraUltimaMod;

    /**
     * Nombre de usuario que modificó el almacén la última vez
     */
    @DatabaseField
    private String ultimoUsuario;

    public Almacen() {}

    public Almacen (String nombre, String ubicacion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.fechaHoraCreacion = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public void setFechaHoraCreacion(LocalDateTime fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public LocalDateTime getFechaHoraUltimaMod() {
        return fechaHoraUltimaMod;
    }

    public void setFechaHoraUltimaMod(LocalDateTime fechaHoraUltimaMod) {
        this.fechaHoraUltimaMod = fechaHoraUltimaMod;
    }

    public String getUltimoUsuario() {
        return ultimoUsuario;
    }

    public void setUltimoUsuario(String ultimoUsuario) {
        this.ultimoUsuario = ultimoUsuario;
    }
}
