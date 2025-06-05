import java.sql.*;
import java.util.ArrayList;
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
        Scanner sc = new Scanner(System.in);
        System.out.println("Digame entre que campo desea actualizar: ");
        System.out.println("1.Largo\n2.Ancho\n3.Peso\n4.Modelo\n5.Numero de plazas");
        var campoNumero = sc.nextInt();
        String campo = switch (campoNumero) {
            case 1 -> "largo";
            case 2 -> "ancho";
            case 3 -> "peso";
            case 4 -> "modelo";
            case 5 -> "nPlazas";
            default -> "";
        };
        if (campo.isEmpty()){
            return -2;
        }
        String consulta = "update furgonetas set " + campo + " = ? where matricula = ?"; // La paremtrizacion de los Prepared no sirven para los campos, así que lo hice así aunque no me gusta mucho
        System.out.println(consulta);
        try (PreparedStatement stm = connection.prepareStatement(consulta)) {
            switch (campo) {
                case "modelo" -> {
                    System.out.println("Digame el nuevo valor para el modelo:");
                    var nuevoModelo = sc.nextLine();
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
