import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

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

    /**
     * Obtenemos un vehiculo en este caso un Trailer
     * @param connection la conexion a la base de datos
     * @param matricula la matricula del vehiculo
     * @return un objeto Trailer
     */
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

    /**
     * Insertar un vehiculo en este caso de tipo Trailer
     * @param connection la conexion a la base de datos
     * @return el numero de filas insertadas
     */
    @Override
    public int insertarVehiculo(Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement("insert into trailers values(?, ?, ?, ?, ?)")) {
            statement.setString(1,this.matricula);
            statement.setDouble(2,this.largo);
            statement.setDouble(3,this.peso);
            statement.setString(4,this.modelo);
            statement.setInt(5,this.numRemolques);
            return statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en preparar el statement " + e);
            return -2;
        }
    }

    @Override
    public int actualizarVehiculo(Connection connection, String matricula) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digame entre que campo desea actualizar: ");
        System.out.println("1.Largo\n2.Peso\n3.Modelo\n4.Numero de remolques");
        int campoNumero = 0;
        try {
            System.out.print("Digame la opcion:");
            campoNumero = sc.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Error introduciendo el numero ");
        }
        String campo = switch (campoNumero) {
            case 1 -> "largo";
            case 2 -> "peso";
            case 3 -> "modelo";
            case 4 -> "numRemolques";
            default -> "";
        };
        if (campo.isEmpty()){
            return -2;
        }
        String consulta = "update furgonetas set " + campo + " = ? where matricula = ?"; // La paremtrizacion de los Prepared no sirven para los campos, así que lo hice así aunque no me gusta mucho
        if (campo.equals("numRemolques")) {
            consulta = "update furgonetas set \"" + campo + "\" = ? where matricula = ?";
        }
        try (PreparedStatement stm = connection.prepareStatement(consulta)) {
            switch (campo) {
                case "modelo" -> {
                    System.out.println("Digame el nuevo valor para el modelo:");
                    var nuevoModelo = sc.nextLine();
                    stm.setString(1,nuevoModelo);
                }
                case "numRemolques" -> {
                    System.out.println("Digame el nuevo valor para el numero de remolques:");
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
        try (PreparedStatement statement = connection.prepareStatement("delete from trailers where matricula=?")) {
            statement.setString(1,matricula);
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
