package mx.unison.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDateTime;

/**
 * Clase que representa a un usuario del sistema.
 *
 * Esta clase almacena la información básica de un usuario
 * autenticado dentro del sistema de inventario, incluyendo
 * su nombre y el rol que posee dentro de la aplicación.
 */
@DatabaseTable(tableName = "usuarios")
public class Usuario {

    /**
     * ID del usuario, generado automáticamente por la base de datos.
     */
    @DatabaseField(generatedId = true)
    private int id;

    /**
     * Nombre del usuario.
     */
    @DatabaseField(canBeNull = false, unique = true)
    private String nombre;

    /**
     * Contraseña del usuario.
     */
    @DatabaseField(canBeNull = false)
    private String password;

    /**
     * Fecha y hora del último inicio de sesión del usuario.
     */
    @DatabaseField
    private LocalDateTime fechaHoraUltimoInicio;

    /**
     * Rol del usuario dentro del sistema.
     * Puede utilizarse para definir permisos o niveles de acceso.
     */
    @DatabaseField(canBeNull = false)
    private String rol;

    public Usuario() {}

    public Usuario(String nombre, String password, String rol) {
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
        this.fechaHoraUltimoInicio = LocalDateTime.now();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getFechaHoraUltimoInicio() {
        return fechaHoraUltimoInicio;
    }

    public void setFechaHoraUltimoInicio(LocalDateTime fechaHoraUltimoInicio) {
        this.fechaHoraUltimoInicio = fechaHoraUltimoInicio;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
