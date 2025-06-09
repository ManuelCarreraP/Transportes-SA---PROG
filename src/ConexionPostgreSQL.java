import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionPostgreSQL {
    private static final String URL = "jdbc:postgresql://aws-0-us-east-2.pooler.supabase.com:6543/postgres";
    private static final String USUARIO = "postgres.lhadhhdyxuiytabqmnnt";
    private static final String CONTRASENA = "transportesSAcontraseña";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}