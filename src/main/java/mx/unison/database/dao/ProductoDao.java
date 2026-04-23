package mx.unison.database.dao;

import com.j256.ormlite.dao.Dao;
import mx.unison.models.Producto;
import java.sql.SQLException;
import java.util.List;

public class ProductoDao {
    private Dao<Producto, Integer> dao;

    public ProductoDao(Dao<Producto, Integer> dao) {
        this.dao = dao;
    }

    public void create(Producto producto) throws SQLException {
        dao.create(producto);
    }

    public Producto getById(int id) throws SQLException {
        return dao.queryForId(id);
    }

    public List<Producto> getAll() throws SQLException {
        return dao.queryForAll();
    }

    public void update(Producto producto) throws SQLException {
        dao.update(producto);
    }

    public void delete(Producto producto) throws SQLException {
        dao.delete(producto);
    }

    public List<Producto> getByAlmacen(int almacenId) throws SQLException {
        return dao.queryForEq("almacen_id", almacenId);
    }
}
