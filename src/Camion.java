import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

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

    /**
     * Obtener un vehiculo en este caso un camion
     * @param connection la conexion a la base de datos
     * @param matricula la matricula del vehiculo a obtener
     * @return un objeto Camion que contiene todos los datos requeridos
     */
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

    /**
     * Inserta un camion teniendo en cuenta los datos de objetos ya instanciados
     * @param connection la conexion a la base de datos
     * @return la cantidad de filas insertadas
     */
    @Override
    public int insertarVehiculo(Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement("insert into camion values(?, ?, ?, ?, ?)")) {
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

    /**
     * Actualiza un campo de un Camion
     * @param connection la conexion a la base de datos
     * @param matricula la matricula del Camion
     * @return la cantidad de filas que fueron actualizadas
     */
    @Override
    public int actualizarVehiculo(Connection connection, String matricula) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digame entre que campo desea actualizar: ");
        System.out.println("1.Largo\n2.Peso\n3.Modelo\n4.Capacidad de Carga\n5.Salir");
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
            case 4 -> "capacidadCarga";
            default -> "";
        };
        if (campo.isEmpty()){
            return -2;
        }
        String consulta = "update camion set " + campo + " = ? where matricula = ?"; // La paremtrizacion de los Prepared no sirven para los campos, así que lo hice así aunque no me gusta mucho
        if (campo.equals("capacidadCarga")) { // Este campo esta en camelCase necesito
            consulta = "update camion set \"" + campo + "\" = ? where matricula = ?";
        }
        try (PreparedStatement stm = connection.prepareStatement(consulta)) {
            if (campo.equals("modelo")) {
                System.out.println("Digame el nuevo valor para el modelo:");
                var nuevoModelo = sc.nextLine();
                stm.setString(1, nuevoModelo);
            } else {
                System.out.println("Digame el nuevo valor para " + campo + ":");
                var otrosValores = sc.nextDouble();
                stm.setDouble(1, otrosValores);
            }
            stm.setString(2,matricula);
            return stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en el proceso de actualizacion " + e);
            return -2;
        }
    }

    /**
     * Elimina un vehiculo en especifico pasandole la matricula por parametro
     * @param connection la conexion a la base de datos
     * @param matricula la matricula
     */
    @Override
    public void eliminarVehiculo(Connection connection, String matricula) {
        try (PreparedStatement statement = connection.prepareStatement("delete from camion where matricula=?")) {
            statement.setString(1,matricula);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error en eliminar vehiculo " + e);
        }
    }

    /**
     * Obtiene todos los camiones
     * @param connection la conexion a la base de datos
     * @return un arrayList con todos los camiones que hay en la base de datos
     */
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


