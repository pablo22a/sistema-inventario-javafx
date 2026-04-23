package mx.unison.models;

/**
 * Clase que representa a un usuario del sistema.
 *
 * Esta clase almacena la información básica de un usuario
 * autenticado dentro del sistema de inventario, incluyendo
 * su nombre y el rol que posee dentro de la aplicación.
 */
public class Usuario {

    /**
     * Nombre del usuario.
     */
    public String nombre;

    /**
     * Rol del usuario dentro del sistema.
     * Puede utilizarse para definir permisos o niveles de acceso.
     */
    public String rol;
}
