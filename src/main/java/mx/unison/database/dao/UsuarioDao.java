package mx.unison.database.dao;

import com.j256.ormlite.dao.Dao;
import mx.unison.models.Usuario;
import java.sql.SQLException;
import java.util.List;

public class UsuarioDao {
    private Dao<Usuario, Integer> dao;

    public UsuarioDao(Dao<Usuario, Integer> dao) {
        this.dao = dao;
    }

     public void create(Usuario usuario) throws SQLException {
        dao.create(usuario);
    }

    public Usuario getByNombre(String nombre) throws SQLException {
        List<Usuario> usuarios = dao.queryForEq("nombre", nombre);
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    public List<Usuario> getAll() throws SQLException {
        return dao.queryForAll();
    }

    public void update(Usuario usuario) throws SQLException {
        dao.update(usuario);
    }

    public void delete(Usuario usuario) throws SQLException {
        dao.delete(usuario);
    }
}
