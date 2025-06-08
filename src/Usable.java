import java.sql.Connection;
import java.util.ArrayList;

public interface Usable<T extends Vehiculo> {
    T getVehiculo(Connection connection, String matricula);
    int insertarVehiculo(Connection connection);
    int actualizarVehiculo(Connection connection,String matricula);
    void eliminarVehiculo(Connection connection,String matricula);
    void eliminarVehiculo(Connection connection);
    ArrayList<T> obtenerTodosVehiculosCategoria(Connection connection);
}
