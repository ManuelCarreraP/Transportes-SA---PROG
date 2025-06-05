import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionPostgreSQL {
    public static void main(String[] args) {
        // URL de conexión con el formato correcto para JDBC
        String url = "jdbc:postgresql://db.sufyrkjlqdgsaiamhimv.supabase.co:5432/postgres"; // Cambia "postgres" por el nombre de tu base de datos
        String usuario = "postgres";  // Tu usuario de PostgreSQL
        String contrasena = "+McQRq2V%2b_VVJ"; // Tu contraseña

        try (Connection conn = DriverManager.getConnection(url, usuario, contrasena)) {
            System.out.println("Conexión exitosa a PostgreSQL");
        } catch (SQLException e) {
            System.out.println("Error al conectar");
            e.printStackTrace();
        }
    }
}
