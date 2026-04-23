package mx.unison.database.dao;

import com.j256.ormlite.dao.Dao;
import mx.unison.models.Almacen;
import java.sql.SQLException;
import java.util.List;

public class AlmacenDao {
    private Dao<Almacen, Integer> dao;

    public AlmacenDao(Dao<Almacen, Integer> dao) {
        this.dao = dao;
    }

    public void create(Almacen almacen) throws SQLException {
        dao.create(almacen);
    }

    public Almacen getById(int id) throws SQLException {
        return dao.queryForId(id);
    }

    public List<Almacen> getAll() throws SQLException {
        return dao.queryForAll();
    }

    public void update(Almacen almacen) throws SQLException {
        dao.update(almacen);
    }

    public void delete(Almacen almacen) throws SQLException {
        dao.delete(almacen);
    }
}
