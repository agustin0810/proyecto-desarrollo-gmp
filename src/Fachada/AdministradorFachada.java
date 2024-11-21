package Fachada;

import Config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministradorFachada {
    private final Connection connection;

    public AdministradorFachada() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    // RF001-01 - Registro de Administrador
    public void crearAdministrador(int id, String nombres, String apellidos, String tipoDocumento, String numeroDocumento,
                                   String domicilio, String correo, String contrasena, String estado) {
        if (!tipoDocumento.equalsIgnoreCase("Cédula") && !tipoDocumento.equalsIgnoreCase("Pasaporte")) {
            System.out.println("El tipo de documento debe ser 'Cédula' o 'Pasaporte'.");
            return;
        }

        String sqlUsuario = "INSERT INTO Usuario (id_usuario, nombres, apellidos, tipo_documento, numero_documento, " +
                "correo, contrasena, domicilio, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlAdministrador = "INSERT INTO Administrador (id_usuario) VALUES (?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statementUsuario = connection.prepareStatement(sqlUsuario);
                 PreparedStatement statementAdministrador = connection.prepareStatement(sqlAdministrador)) {

                // Insertar en Usuario
                statementUsuario.setInt(1, id);
                statementUsuario.setString(2, nombres);
                statementUsuario.setString(3, apellidos);
                statementUsuario.setString(4, tipoDocumento);
                statementUsuario.setString(5, numeroDocumento);
                statementUsuario.setString(6, correo);
                statementUsuario.setString(7, contrasena);
                statementUsuario.setString(8, domicilio);
                statementUsuario.setString(9, estado);
                statementUsuario.executeUpdate();

                // Insertar en Administrador
                statementAdministrador.setInt(1, id);
                statementAdministrador.executeUpdate();
            }

            connection.commit();
            System.out.println("Administrador creado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear el administrador: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error al revertir la transacción: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                System.err.println("Error al restaurar el modo de autocommit: " + autoCommitEx.getMessage());
            }
        }
    }

    // RF001-02 - Listado de Administradores
    public void mostrarAdministradores() {
        String sql = "SELECT u.id_usuario, u.nombres, u.apellidos, u.tipo_documento, u.numero_documento, " +
                "u.correo, u.domicilio, u.estado FROM Usuario u " +
                "INNER JOIN Administrador a ON u.id_usuario = a.id_usuario";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            List<String> administradores = new ArrayList<>();
            while (resultSet.next()) {
                String administrador = "ID: " + resultSet.getInt("id_usuario") +
                        ", Nombres: " + resultSet.getString("nombres") +
                        ", Apellidos: " + resultSet.getString("apellidos") +
                        ", Documento: " + resultSet.getString("tipo_documento") + " " + resultSet.getString("numero_documento") +
                        ", Correo: " + resultSet.getString("correo") +
                        ", Domicilio: " + resultSet.getString("domicilio") +
                        ", Estado: " + resultSet.getString("estado");
                administradores.add(administrador);
            }

            if (administradores.isEmpty()) {
                System.out.println("No hay administradores registrados.");
            } else {
                administradores.forEach(System.out::println);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener los administradores: " + e.getMessage());
        }
    }

    // RF001-03 - Modificación de Administrador
    public void modificarAdministrador(int id, String nombres, String apellidos, String domicilio, String estado) {
        String sql = "UPDATE Usuario SET nombres = ?, apellidos = ?, domicilio = ?, estado = ? WHERE id_usuario = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombres);
            statement.setString(2, apellidos);
            statement.setString(3, domicilio);
            statement.setString(4, estado);
            statement.setInt(5, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Administrador actualizado exitosamente.");
            } else {
                System.out.println("Administrador no encontrado.");
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar el administrador: " + e.getMessage());
        }
    }

    // RF001-04 - Baja de Administrador
    public void eliminarAdministrador(int id) {
        String sqlAdministrador = "DELETE FROM Administrador WHERE id_usuario = ?";
        String sqlUsuario = "DELETE FROM Usuario WHERE id_usuario = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statementAdministrador = connection.prepareStatement(sqlAdministrador);
                 PreparedStatement statementUsuario = connection.prepareStatement(sqlUsuario)) {

                // Eliminar de Administrador
                statementAdministrador.setInt(1, id);
                statementAdministrador.executeUpdate();

                // Eliminar de Usuario
                statementUsuario.setInt(1, id);
                statementUsuario.executeUpdate();
            }

            connection.commit();
            System.out.println("Administrador eliminado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar el administrador: " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error al revertir la transacción: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                System.err.println("Error al restaurar el modo de autocommit: " + autoCommitEx.getMessage());
            }
        }
    }

    // RF001-05 - Login de Administrador
    public boolean loginUsuario(String correo, String contrasena) {
        String sql = "SELECT u.id_usuario FROM Usuario u " +
                "INNER JOIN Administrador a ON u.id_usuario = a.id_usuario " +
                "WHERE u.correo = ? AND u.contrasena = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, correo);
            statement.setString(2, contrasena);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Login exitoso para el Administrador: " + correo);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en el login: " + e.getMessage());
            return false;
        }
    }
}
