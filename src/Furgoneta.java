import java.sql.*;
import java.util.ArrayList;

public class Furgoneta extends Vehiculo implements Usable<Furgoneta> {
    private int nPlazas;

    public Furgoneta(String matricula, double largo, double peso, String modelo, int nPlazas) {
        super(matricula, largo, peso, modelo);
        this.nPlazas = nPlazas;
    }
    public Furgoneta() {}
    public int getNPlazas() {
        return nPlazas;
    }

    public void setNPlazas(int nPlazas) {
        this.nPlazas = nPlazas;
    }

    @Override
    public String toString() {
        return "Furgoneta{" +
                "matricula='" + matricula + '\'' +
                ", largo=" + largo +
                ", peso=" + peso +
                ", modelo='" + modelo + '\'' +
                ", nPlazas=" + nPlazas +
                '}';
    }

    @Override
    public Furgoneta getVehiculo(Connection connection, String matricula) {
        return null;
    }

    @Override
    public int insertarVehiculo(Connection connection) {
        try (Statement stm = connection.createStatement()) {
            return stm.executeUpdate("insert into furgonetas values ('AMPE',23.12,12.23,'Exper',3)");
        } catch (SQLException e) {
            return -2;
        }
    }

    @Override
    public int actualizarVehiculo(Connection connection, String matricula) {
        return 0;
    }


    @Override
    public void eliminarVehiculo(Connection connection, String matricula) {
        try (PreparedStatement statement = connection.prepareStatement("delete from camion where matricula=?")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error en eliminar vehiculo " + e);
        }
    }

    @Override
    public ArrayList<Furgoneta> obtenerTodosVehiculosCategoria(Connection connection) {
        ArrayList<Furgoneta> furgonetas = new ArrayList<>();
        try (Statement stm = connection.createStatement(); ResultSet set = stm.executeQuery("select * from camion;")) {
            while (set.next()) {
                furgonetas.add(new Furgoneta(set.getString("matricula"),set.getDouble("largo"),set.getDouble("peso"),set.getString("modelo"),set.getInt("nPlazas")));
            }
        } catch (SQLException e) {
            System.err.println("Error en obtencion " + e);
        }
        return furgonetas;
    }
    }
