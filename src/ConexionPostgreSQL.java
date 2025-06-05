import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConexionPostgreSQL{

    public static void main(String[] args) {
        String url = "jdbc:postgresql://aws-0-us-east-2.pooler.supabase.com:6543/postgres";
        String usuario = "postgres.lhadhhdyxuiytabqmnnt";
        String contrasena = "transportesSAcontraseña";

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            System.out.println("Conexión establecida correctamente.");
            new Furgoneta().actualizarVehiculo(conexion,"FGEL");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }
}
