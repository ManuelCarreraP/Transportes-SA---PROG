import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManagment {
    Connection connection;
    public DatabaseManagment(Connection connection) {
        this.connection = connection;
    }
    public void mostrarAlgo() { // Esto es un metodo de ejemplo
        try (Statement stm = connection.createStatement(); ResultSet set = stm.executeQuery("select * from existencias;")) {

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
