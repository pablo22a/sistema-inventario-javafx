package mx.unison.models;

/**
 * Clase de productos.
 * Contiene los atributos necesarios para guardar productos en la base de datos.
 */
public class Producto {

    /**
     * ID del producto
     */
    public int id;

    /**
     * Nombre del producto
     */
    public String nombre;

    /**
     * Descripción del producto
     */
    public String descripcion;

    /**
     * Stock disponible del producto
     */
    public int cantidad;

    /**
     * Precio base del producto
     */
    public double precio;

    /**
     * ID del almacén donde se encuentra el producto
     */
    public int almacenId;

    /**
     * Nombre del almacen donde se encuentra el producto
     */
    public String almacenNombre;

    /**
     * Fecha de creación del producto
     */
    public String fechaCreacion;

    /**
     * Fecha de la última modificación del producto
     *
     * Se actualiza cuando uno o más atributos son modificados
     */
    public String fechaModificacion;

    /**
     * Nombre de usuario del último usuario en modificar el producto
     */
    public String ultimoUsuario;
}
