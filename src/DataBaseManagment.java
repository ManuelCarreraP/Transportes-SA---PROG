import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseManagment {
    Connection connection;

    public DataBaseManagment(Connection connection) {
        this.connection = connection;
    }
    public void setCamion(String matricula,String modelo,float largo,float peso,float capacidadCarga) {
        if (!existeVehiculo("Camion",matricula)) {
            try (PreparedStatement statement = connection.prepareStatement("insert into camion values(? ? ? ? ?);")) {
                statement.setString(1,matricula);
                statement.setFloat(2,largo);
                statement.setFloat(3,peso);
                statement.setString(4,modelo);
                statement.setFloat(5,capacidadCarga);
                System.out.println(statement.toString());
        } catch (SQLException e) {
            System.err.println("Hay un error a la hora de hacer el Statement");
        }
        } else {
            System.out.println("Ya existe ese camion");
        }
    }
    boolean existeVehiculo(String tablaVehiculo,String matricula) {
        try (PreparedStatement statement = connection.prepareStatement("select * from ? where matricula=?")) {
            statement.setString(1,tablaVehiculo);
            statement.setString(2,matricula);
            try (ResultSet set = statement.executeQuery()) {
                return set.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("No es posible de utilizar si no existe una conexion con la base de datos");
        }
    }
}
