package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


// CLASE QUE SE ENCARGA DE REALIZAR LA CONEXION A LA BASE DE DATOS UTILIZANDO EL PATRON SINGLEOTN
public class DatabaseConfig {
    private static DatabaseConfig instance;
    private Connection connection;
    private final String url = "jdbc:postgresql://localhost:5432/PROYECTO";
    private final String user = "postgres";
    private final String password = "root";

    private DatabaseConfig() {
        try {
            connection = DriverManager.getConnection(url, user, password);
//            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
