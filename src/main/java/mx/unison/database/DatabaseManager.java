package mx.unison.database;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import mx.unison.models.Almacen;
import mx.unison.models.Producto;
import mx.unison.models.Usuario;
import mx.unison.database.dao.*;
import mx.unison.util.CryptoUtils;

import java.sql.SQLException;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:Inventario.db";
    private ConnectionSource connectionSource;

    private AlmacenDao almacenDao;
    private ProductoDao productoDao;
    private UsuarioDao usuarioDao;

    public DatabaseManager() throws SQLException {
        this.connectionSource = new JdbcConnectionSource(DATABASE_URL);
        initializeDaos();
        initializeTables();
    }

    private void initializeDaos() throws SQLException {
        this.almacenDao = new AlmacenDao(
                DaoManager.createDao(connectionSource, Almacen.class)
        );
        this.productoDao = new ProductoDao(
                DaoManager.createDao(connectionSource, Producto.class)
        );
        this.usuarioDao = new UsuarioDao(
                DaoManager.createDao(connectionSource, Usuario.class)
        );
    }

    private void initializeTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, Usuario.class);
        TableUtils.createTableIfNotExists(connectionSource, Almacen.class);
        TableUtils.createTableIfNotExists(connectionSource, Producto.class);

        // Insertar usuarios por defecto
        createDefaultUsers();
    }

    private void createDefaultUsers() throws SQLException {
        createUserIfNotExists("ADMIN", "admin23", "ADMIN");
        createUserIfNotExists("PRODUCTOS", "productos19", "PRODUCTOS");
        createUserIfNotExists("ALMACENES", "almacenes11", "ALMACENES");
    }

    private void createUserIfNotExists(String nombre, String password, String rol)
            throws SQLException {
        if (usuarioDao.getByNombre(nombre) == null) {
            String hashedPassword = CryptoUtils.hashPassword(password);
            Usuario user = new Usuario(nombre, hashedPassword, rol);
            usuarioDao.create(user);
        }
    }

    // Getters para DAOs
    public AlmacenDao getAlmacenDao() { return almacenDao; }
    public ProductoDao getProductoDao() { return productoDao; }
    public UsuarioDao getUsuarioDao() { return usuarioDao; }

    public void close() throws SQLException {
        try {
            connectionSource.close();
        } catch (Exception e) {
            throw new SQLException("Error al cerrar la conexión a la base de datos", e);
        }
    }
}
