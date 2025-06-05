import java.sql.*;
import java.util.ArrayList;

public class Trailer extends Vehiculo implements Usable<Trailer> {
    private int numRemolques;

    public Trailer(String matricula, double largo, double peso, String modelo, int numRemolques) {
        super(matricula, largo, peso, modelo);
        this.numRemolques = numRemolques;
    }
    public Trailer() {}

    public int getNumRemolques() {
        return numRemolques;
    }

    public void setNumRemolques(int numRemolques) {
        this.numRemolques = numRemolques;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "matricula='" + matricula + '\'' +
                ", largo=" + largo +
                ", peso=" + peso +
                ", modelo='" + modelo + '\'' +
                ", numRemolques=" + numRemolques +
                '}';
    }

    @Override
    public Trailer getVehiculo(Connection connection, String matricula) {
        Trailer trailer = null;
        try (PreparedStatement statement = connection.prepareStatement("select * from trailer where matricula=?")) {
            statement.setString(1,matricula);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    trailer = new Trailer(resultSet.getString("matricula"),resultSet.getDouble("largo"),resultSet.getDouble("peso"),resultSet.getString("modelo"),resultSet.getInt("numRemolques"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error ejecutando consulta");
        }
        return trailer;
    }

    @Override
    public int insertarVehiculo(Connection connection) {
        return 0;
    }

    @Override
    public int actualizarVehiculo(Connection connection, String matricula) {
        return 0;
    }

    @Override
    public void eliminarVehiculo(Connection connection, String matricula) {
        try (PreparedStatement statement = connection.prepareStatement("delete from trailers where matricula=?")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error en eliminar vehiculo " + e);
        }
    }

    @Override
    public ArrayList<Trailer> obtenerTodosVehiculosCategoria(Connection connection) {
        ArrayList<Trailer> trailers = new ArrayList<>();
        try (Statement stm = connection.createStatement(); ResultSet set = stm.executeQuery("select * from trailers;")) {
            while (set.next()) {
                trailers.add(new Trailer(set.getString("matricula"),set.getDouble("largo"),set.getDouble("peso"),set.getString("modelo"),set.getInt("nPlazas")));
            }
        } catch (SQLException e) {
            System.err.println("Error en obtencion " + e);
        }
        return trailers;
    }
}
