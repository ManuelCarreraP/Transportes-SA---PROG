import java.sql.Connection;

public class DataBaseManagment {
    Connection connection;

    public DataBaseManagment(Connection connection) {
        this.connection = connection;
    }
    
}
