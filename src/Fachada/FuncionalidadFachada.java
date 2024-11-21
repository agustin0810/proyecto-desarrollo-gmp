package Fachada;

import Config.DatabaseConfig;
import Dominio.Funcionalidad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionalidadFachada {
    private final Connection connection;

    public FuncionalidadFachada() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    // RF003-01 - Ingreso de Funcionalidad
    public void crearFuncionalidad(String nombreFuncionalidad, String descripcionFuncionalidad) {
        String sql = "INSERT INTO Funcionalidad (nombre_funcionalidad, descripcion_funcionalidad) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombreFuncionalidad);
            statement.setString(2, descripcionFuncionalidad);

            statement.executeUpdate();
            System.out.println("Funcionalidad creada exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear la funcionalidad: " + e.getMessage());
        }
    }

    // RF003-02 - Listado de Funcionalidades
    public List<Funcionalidad> listarFuncionalidades() {
        String sql = "SELECT * FROM Funcionalidad";
        List<Funcionalidad> funcionalidades = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Funcionalidad funcionalidad = new Funcionalidad(
                        resultSet.getInt("id_funcionalidad"),
                        resultSet.getString("nombre_funcionalidad"),
                        resultSet.getString("descripcion_funcionalidad")
                );
                funcionalidades.add(funcionalidad);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar las funcionalidades: " + e.getMessage());
        }

        return funcionalidades;
    }

    // RF003-03 - Modificación de Funcionalidad
    public void modificarFuncionalidad(int idFuncionalidad, String nombreFuncionalidad, String descripcionFuncionalidad) {
        String sql = "UPDATE Funcionalidad SET nombre_funcionalidad = ?, descripcion_funcionalidad = ? WHERE id_funcionalidad = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombreFuncionalidad);
            statement.setString(2, descripcionFuncionalidad);
            statement.setInt(3, idFuncionalidad);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Funcionalidad actualizada exitosamente.");
            } else {
                System.out.println("Funcionalidad no encontrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al modificar la funcionalidad: " + e.getMessage());
        }
    }

    // RF003-04 - Baja de Funcionalidad
    public void eliminarFuncionalidad(int idFuncionalidad) {
        String sql = "DELETE FROM Funcionalidad WHERE id_funcionalidad = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idFuncionalidad);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Funcionalidad eliminada exitosamente.");
            } else {
                System.out.println("Funcionalidad no encontrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar la funcionalidad: " + e.getMessage());
        }
    }
}
