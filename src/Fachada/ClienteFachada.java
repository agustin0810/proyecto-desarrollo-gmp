package Fachada;

import Config.DatabaseConfig;
import Dominio.Cliente;
import Dominio.Perfil;
import Config.PostgresException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteFachada {
    private final Connection connection;

    public ClienteFachada() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    public void crearCliente(int id, String nombres, String apellidos, String tipoDocumento, String numeroDocumento,
                             String domicilio, String correo, String contrasena, String estado, int nroSocio,
                             int totalAnualCuotas, int pagoCuotas, boolean dificultadAuditiva, boolean lenguajeSenas, int idPerfil) throws PostgresException {
        if (!tipoDocumento.equalsIgnoreCase("CEDULA") && !tipoDocumento.equalsIgnoreCase("PASAPORTE")) {
            throw new PostgresException("23514", "El tipo de documento debe ser 'CEDULA' o 'PASAPORTE'.");
        }

        String sqlUsuario = "INSERT INTO Usuario (id_usuario, nombres, apellidos, tipo_documento, numero_documento, " +
                "correo, contrasena, domicilio, estado, id_perfil) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlCliente = "INSERT INTO Cliente (id_usuario, nro_asociado, total_anual_cuotas, pago_cuotas, " +
                "dificultad_auditiva, maneja_lenguaje_senas) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statementUsuario = connection.prepareStatement(sqlUsuario);
                 PreparedStatement statementCliente = connection.prepareStatement(sqlCliente)) {

                statementUsuario.setInt(1, id);
                statementUsuario.setString(2, nombres);
                statementUsuario.setString(3, apellidos);
                statementUsuario.setString(4, tipoDocumento);
                statementUsuario.setString(5, numeroDocumento);
                statementUsuario.setString(6, correo);
                statementUsuario.setString(7, contrasena);
                statementUsuario.setString(8, domicilio);
                statementUsuario.setString(9, estado);
                statementUsuario.setInt(10, idPerfil);
                statementUsuario.executeUpdate();

                statementCliente.setInt(1, id);
                statementCliente.setInt(2, nroSocio);
                statementCliente.setInt(3, totalAnualCuotas);
                statementCliente.setInt(4, pagoCuotas);
                statementCliente.setBoolean(5, dificultadAuditiva);
                statementCliente.setBoolean(6, lenguajeSenas);
                statementCliente.executeUpdate();
            }

            connection.commit();
            System.out.println("Cliente creado exitosamente.");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new PostgresException(e.getSQLState(), e.getMessage());
            }
            throw new PostgresException(e.getSQLState(), e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new PostgresException(e.getSQLState(), e.getMessage());

            }
        }
    }

    public void mostrarClientes() throws PostgresException {
        String sql = "SELECT u.id_usuario, u.nombres, u.apellidos, u.tipo_documento, u.numero_documento, " +
                "u.correo, u.domicilio, u.estado, c.nro_asociado, c.total_anual_cuotas, c.pago_cuotas, " +
                "c.dificultad_auditiva, c.maneja_lenguaje_senas, p.nombre_perfil " +
                "FROM Usuario u " +
                "INNER JOIN Cliente c ON u.id_usuario = c.id_usuario " +
                "INNER JOIN Perfil p ON u.id_perfil = p.id_perfil";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            List<String> clientes = new ArrayList<>();
            while (resultSet.next()) {
                String cliente = "ID: " + resultSet.getInt("id_usuario") +
                        ", Nombres: " + resultSet.getString("nombres") +
                        ", Apellidos: " + resultSet.getString("apellidos") +
                        ", Documento: " + resultSet.getString("tipo_documento") + " " + resultSet.getString("numero_documento") +
                        ", Correo: " + resultSet.getString("correo") +
                        ", Domicilio: " + resultSet.getString("domicilio") +
                        ", Estado: " + resultSet.getString("estado") +
                        ", Perfil: " + resultSet.getString("nombre_perfil") +
                        ", Número de Socio: " + resultSet.getInt("nro_asociado") +
                        ", Total Cuotas Anuales: " + resultSet.getInt("total_anual_cuotas") +
                        ", Pago de Cuotas: " + resultSet.getInt("pago_cuotas") +
                        ", Dificultad Auditiva: " + resultSet.getBoolean("dificultad_auditiva") +
                        ", Lenguaje de Señas: " + resultSet.getBoolean("maneja_lenguaje_senas");
                clientes.add(cliente);
            }

            if (clientes.isEmpty()) {
                System.out.println("No hay clientes registrados.");
            } else {
                clientes.forEach(System.out::println);
            }

        } catch (SQLException e) {
            throw new PostgresException(e.getSQLState(), e.getMessage());
        }
    }

    public Cliente buscarCliente(int id) throws PostgresException {
        String sql = "SELECT u.id_usuario, u.nombres, u.apellidos, u.tipo_documento, u.numero_documento, " +
                "u.correo, u.contrasena, u.domicilio, u.estado, u.id_perfil, " +
                "p.nombre_perfil, c.nro_asociado, c.total_anual_cuotas, c.pago_cuotas, " +
                "c.dificultad_auditiva, c.maneja_lenguaje_senas " +
                "FROM Usuario u " +
                "INNER JOIN Cliente c ON u.id_usuario = c.id_usuario " +
                "LEFT JOIN Perfil p ON u.id_perfil = p.id_perfil " +
                "WHERE u.id_usuario = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Cliente(
                            resultSet.getInt("id_usuario"),
                            resultSet.getString("nombres"),
                            resultSet.getString("apellidos"),
                            resultSet.getString("tipo_documento"),
                            resultSet.getString("numero_documento"),
                            resultSet.getString("correo"),
                            resultSet.getString("contrasena"),
                            resultSet.getString("domicilio"),
                            null, null, resultSet.getString("estado"),
                            resultSet.getObject("id_perfil") != null ?
                                    new Perfil(resultSet.getInt("id_perfil"), resultSet.getString("nombre_perfil"), null, null)
                                    : null,
                            resultSet.getInt("nro_asociado"), resultSet.getInt("total_anual_cuotas"),
                            resultSet.getInt("pago_cuotas"), resultSet.getBoolean("maneja_lenguaje_senas"),
                            resultSet.getBoolean("dificultad_auditiva")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new PostgresException(e.getSQLState(), e.getMessage());
        }
    }

    public void modificarCliente(int id, String nombres, String apellidos, String domicilio, String estado,
                                 int totalAnualCuotas, int pagoCuotas, boolean dificultadAuditiva, boolean lenguajeSenas, int idPerfil) throws PostgresException {
        String sqlUsuario = "UPDATE Usuario SET nombres = ?, apellidos = ?, domicilio = ?, estado = ?, id_perfil = ? WHERE id_usuario = ?";
        String sqlCliente = "UPDATE Cliente SET total_anual_cuotas = ?, pago_cuotas = ?, dificultad_auditiva = ?, maneja_lenguaje_senas = ? WHERE id_usuario = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statementUsuario = connection.prepareStatement(sqlUsuario);
                 PreparedStatement statementCliente = connection.prepareStatement(sqlCliente)) {

                statementUsuario.setString(1, nombres);
                statementUsuario.setString(2, apellidos);
                statementUsuario.setString(3, domicilio);
                statementUsuario.setString(4, estado);
                statementUsuario.setInt(5, idPerfil);
                statementUsuario.setInt(6, id);
                statementUsuario.executeUpdate();

                statementCliente.setInt(1, totalAnualCuotas);
                statementCliente.setInt(2, pagoCuotas);
                statementCliente.setBoolean(3, dificultadAuditiva);
                statementCliente.setBoolean(4, lenguajeSenas);
                statementCliente.setInt(5, id);
                statementCliente.executeUpdate();
            }

            connection.commit();
            System.out.println("Cliente actualizado exitosamente.");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new PostgresException(e.getSQLState(), e.getMessage());

            }
            throw new PostgresException(e.getSQLState(), e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new PostgresException(e.getSQLState(), e.getMessage());

            }
        }
    }

    public void eliminarCliente(int id) throws PostgresException {
        String sqlCliente = "DELETE FROM Cliente WHERE id_usuario = ?";
        String sqlUsuario = "DELETE FROM Usuario WHERE id_usuario = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statementCliente = connection.prepareStatement(sqlCliente);
                 PreparedStatement statementUsuario = connection.prepareStatement(sqlUsuario)) {

                statementCliente.setInt(1, id);
                statementCliente.executeUpdate();

                statementUsuario.setInt(1, id);
                statementUsuario.executeUpdate();
            }

            connection.commit();
            System.out.println("Cliente eliminado exitosamente.");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new PostgresException(e.getSQLState(), e.getMessage());

            }
            throw new PostgresException(e.getSQLState(), e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new PostgresException(e.getSQLState(), e.getMessage());

            }
        }
    }

    public boolean loginUsuario(String correo, String contrasena) throws PostgresException {
        String sql = "SELECT u.id_usuario FROM Usuario u " +
                "INNER JOIN Cliente c ON u.id_usuario = c.id_usuario " +
                "WHERE u.correo = ? AND u.contrasena = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, correo);
            statement.setString(2, contrasena);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new PostgresException(e.getSQLState(), e.getMessage());
        }
    }
}
