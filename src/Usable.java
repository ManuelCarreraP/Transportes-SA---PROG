import java.sql.Connection;

public interface Usable<T extends Vehiculo> {
    T getVehiculo(Connection connection, String matricula);
    public int insertarVehiculo(Connection connection);
    int actualizarVehiculo(Connection connection,String matricula);
    void eliminarVehiculo(Connection connection,String matricula);
}
