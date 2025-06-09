import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

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
        Furgoneta furgoneta = null;
        try (PreparedStatement statement = connection.prepareStatement("select * from camion where matricula=?")) {
            statement.setString(1,matricula);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    furgoneta = new Furgoneta(resultSet.getString("matricula"),resultSet.getDouble("largo"),resultSet.getDouble("peso"),resultSet.getString("modelo"),resultSet.getInt("nPlazas"));
                }
            } catch (SQLException e) {
                System.out.println("Fallo al obtener informacion " + e);
            }
        } catch (SQLException e) {
            System.out.println("Error ejecutando consulta"+e);
        }
        return furgoneta;
    }

    @Override
    public int insertarVehiculo(Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement("insert into furgonetas values(?, ?, ?, ?, ?)")) {
            statement.setString(1,this.matricula);
            statement.setDouble(2,this.largo);
            statement.setDouble(3,this.peso);
            statement.setString(4,this.modelo);
            statement.setInt(5,this.nPlazas);
            return statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en preparar el statement " + e);
            return -2;
        }
    }

    @Override
    public int actualizarVehiculo(Connection connection, String matricula) {
        Scanner sc = new Scanner(System.in);
        System.out.println("1.Largo\n2.Peso\n4.Modelo\n5.Numero de plazas");
        int campoNumero = 0;
        try {
            System.out.print("Digame la opcion:");
            campoNumero = sc.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Error introduciendo el numero ");
        }
        String campo = switch (campoNumero) {
            case 1 -> "largo";
            case 3 -> "peso";
            case 4 -> "modelo";
            case 5 -> "nPlazas";
            default -> "";
        };
        String consulta = "update furgonetas set " + campo + " = ? where matricula = ?"; // La paremtrizacion de los Prepared no sirven para los campos, así que lo hice así aunque no me gusta mucho
        if (campo.equals("nPlazas")) {
            consulta = "update furgonetas set \"" + campo + "\" = ? where matricula = ?";
        }
        if (campo.isEmpty()){
            return -2;
        }
        try (PreparedStatement stm = connection.prepareStatement(consulta)) {
            switch (campo) {
                case "modelo" -> {
                    System.out.println("Digame el nuevo valor para el modelo:");
                    var nuevoModelo = sc.nextLine().trim();
                    stm.setString(1,nuevoModelo);
                }
                case "nPlazas" -> {
                    System.out.println("Digame el nuevo valor para el numero de plazas:");
                    var nuevoModelo = sc.nextInt();
                    stm.setInt(1,nuevoModelo);
                }
                default -> {
                    System.out.println("Digame el nuevo valor para " + campo + ":");
                    var nuevoModelo = sc.nextDouble();
                    stm.setDouble(1,nuevoModelo);
                }
            }
            stm.setString(2,matricula);
            return stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en el proceso de actualizacion " + e);
            return -2;
        }
    }


    @Override
    public void eliminarVehiculo(Connection connection, String matricula) {
        try (PreparedStatement statement = connection.prepareStatement("delete from furgonetas where matricula=?")) {
            statement.setString(1,matricula);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error en eliminar vehiculo " + e);
        }
    }

    @Override
    public void eliminarVehiculo(Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement("delete from furgonetas where matricula=?")) {
            statement.setString(1,matricula);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error en eliminar vehiculo " + e);
        }
    }

    @Override
    public ArrayList<Furgoneta> obtenerTodosVehiculosCategoria(Connection connection) {
        ArrayList<Furgoneta> furgonetas = new ArrayList<>();
        try (Statement stm = connection.createStatement(); ResultSet set = stm.executeQuery("select * from furgonetas;")) {
            while (set.next()) {
                furgonetas.add(new Furgoneta(set.getString("matricula"),set.getDouble("largo"),set.getDouble("peso"),set.getString("modelo"),set.getInt("nPlazas")));
            }
        } catch (SQLException e) {
            System.err.println("Error en obtencion " + e);
        }
        return furgonetas;
    }
}
