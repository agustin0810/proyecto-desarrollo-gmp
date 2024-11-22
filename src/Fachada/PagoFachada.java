package Fachada;

import Config.DatabaseConfig;
import Dominio.Cliente;
import Dominio.Cuota;
import Config.PostgresException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoFachada {
    private final Connection connection;

    public PagoFachada() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    // RF007-01 - Ingreso de Pagos
    public void registrarPago(Cuota cuota) throws PostgresException {
        String sql = "INSERT INTO Cuota (id_cuota, numero_cuota, fecha_cuota, valor_cuota, id_cliente) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cuota.getIdCuota());
            statement.setInt(2, cuota.getNumeroCuota());
            statement.setDate(3, new Date(cuota.getFechaCuota().getTime()));
            statement.setDouble(4, cuota.getValorCuota());
            statement.setInt(5, cuota.getCliente().getIdUsuario());

            statement.executeUpdate();
            System.out.println("Pago registrado exitosamente.");
        } catch (SQLException e) {
            throw new PostgresException(e.getSQLState(), e.getMessage());
        }
    }

    // RF007-02 - Modificación de Pagos
    public void modificarPago(int idCuota, double nuevoValor, Date nuevaFecha) throws PostgresException {
        String sql = "UPDATE Cuota SET valor_cuota = ?, fecha_cuota = ? WHERE id_cuota = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, nuevoValor);
            statement.setDate(2, new Date(nuevaFecha.getTime()));
            statement.setInt(3, idCuota);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Pago modificado exitosamente.");
            } else {
                System.out.println("No se encontró el pago con el ID especificado.");
            }
        } catch (SQLException e) {
            throw new PostgresException(e.getSQLState(), e.getMessage());
        }
    }

    // Listar pagos de un cliente (opcional para facilitar consultas)
    public List<Cuota> listarPagosPorCliente(int idCliente) throws PostgresException {
        String sql = "SELECT id_cuota, numero_cuota, fecha_cuota, valor_cuota FROM Cuota WHERE id_cliente = ?";
        List<Cuota> pagos = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCliente);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Cuota cuota = new Cuota(
                        resultSet.getInt("id_cuota"),
                        resultSet.getInt("numero_cuota"),
                        resultSet.getDate("fecha_cuota"),
                        resultSet.getDouble("valor_cuota"),
                        null // Se puede llenar con un objeto Cliente si es necesario
                );
                pagos.add(cuota);
            }
        } catch (SQLException e) {
            throw new PostgresException(e.getSQLState(), e.getMessage());
        }

        return pagos;
    }
}
