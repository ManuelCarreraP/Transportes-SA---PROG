import java.sql.*;
import java.util.ArrayList;

public class Camion extends Vehiculo implements Usable<Camion> {
    private double capacidadCarga; // en toneladas

    public Camion(String matricula, double largo, double peso, String modelo, double capacidadCarga) {
        super(matricula, largo, peso, modelo);
        this.capacidadCarga = capacidadCarga;
    }
    public Camion() {}
    public double getCapacidadCarga() {
        return capacidadCarga;
    }

    public void setCapacidadCarga(double capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }

    @Override
    public String toString() {
        return "Camion{" +
                "matricula='" + matricula + '\'' +
                ", largo=" + largo +
                ", peso=" + peso +
                ", modelo='" + modelo + '\'' +
                ", capacidadCarga=" + capacidadCarga + " toneladas" +
                '}';
    }
    @Override
    public Camion getVehiculo(Connection connection, String matricula) {
        Camion camion = null;
        try (PreparedStatement statement = connection.prepareStatement("select * from Camion where matricula=?")) {
            statement.setString(1,matricula);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    camion = new Camion(resultSet.getString("matricula"),resultSet.getDouble("largo"),resultSet.getDouble("peso"),resultSet.getString("modelo"),resultSet.getDouble("capacidadCarga"));
                }
            } catch (SQLException e) {
                System.out.println("Fallo al obtener informacion " + e);
            }
        } catch (SQLException e) {
            System.out.println("Error ejecutando consulta"+e);
        }
        return camion;
    }

    @Override
    public int insertarVehiculo(Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement("insert into Camion values(?, ?, ?, ?, ?)")) {
            statement.setString(1,this.matricula);
            statement.setDouble(2,this.largo);
            statement.setDouble(3,this.peso);
            statement.setString(4,this.modelo);
            statement.setDouble(5,this.capacidadCarga);
            return statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en preparar el statement " + e);
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
    public ArrayList<Camion> obtenerTodosVehiculosCategoria(Connection connection) {
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


