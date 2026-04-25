package mx.unison.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDateTime;

/**
 * Clase de productos.
 * Contiene los atributos necesarios para guardar productos en la base de datos.
 */
@DatabaseTable(tableName = "productos")
public class Producto {

    /**
     * ID del producto
     */
    @DatabaseField(generatedId = true)
    private int id;

    /**
     * Nombre del producto
     */
    @DatabaseField(canBeNull = false)
    private String nombre;

    /**
     * Descripción del producto
     */
    @DatabaseField
    private String descripcion;

    /**
     * Stock disponible del producto
     */
    @DatabaseField
    private int cantidad;

    /**
     * Precio base del producto
     */
    @DatabaseField
    private double precio;

    /**
     * Almacén al que pertenece el producto
     */
    @DatabaseField(foreign = true, foreignAutoCreate = true)
    private Almacen almacen;

    /**
     * Fecha de creación del producto
     */
    @DatabaseField
    private String fechaCreacion;

    /**
     * Fecha de la última modificación del producto
     *
     * Se actualiza cuando uno o más atributos son modificados
     */
    @DatabaseField
    private String fechaModificacion;

    /**
     * Nombre de usuario del último usuario en modificar el producto
     */
    @DatabaseField
    private String ultimoUsuario;

    public Producto() {}

    public Producto(String nombre, String descripcion, int cantidad, double precio, Almacen almacen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio = precio;
        this.almacen = almacen;
        this.fechaCreacion = String.valueOf(LocalDateTime.now());
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUltimoUsuario() {
        return ultimoUsuario;
    }

    public void setUltimoUsuario(String ultimoUsuario) {
        this.ultimoUsuario = ultimoUsuario;
    }
}
