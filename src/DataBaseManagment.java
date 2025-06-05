import java.sql.*;
import java.util.ArrayList;

public class DataBaseManagment {
    public static ArrayList<Camion> obtenerTodosCamiones(Connection connection) {
        ArrayList<Camion> camiones = new ArrayList<>();
        try (Statement stm = connection.createStatement();ResultSet set = stm.executeQuery("select * from camion;")) {
            while (set.next()) {
                camiones.add(new Camion(set.getString("matricula"),set.getDouble("largo"),set.getDouble("peso"),set.getString("modelo"),set.getDouble("capacidadCarga")));
            }
        } catch (SQLException e) {
            System.err.println("Error en obtencion " + e);
        }
        return camiones;
    }
}
