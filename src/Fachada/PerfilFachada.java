package Fachada;

import Config.DatabaseConfig;
import Dominio.Funcionalidad;
import Dominio.Perfil;
import Config.PostgresException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerfilFachada {
    private final Connection connection;

    public PerfilFachada() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    // RF002-01 - Ingreso de Perfil
    public void crearPerfil(String nombrePerfil, String estado, String descripcion) throws PostgresException {
        String sql = "INSERT INTO Perfil (nombre_perfil, estado, descripcion) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombrePerfil);
            statement.setString(2, estado);
            statement.setString(3, descripcion);

            statement.executeUpdate();
            System.out.println("Perfil creado exitosamente.");
        } catch (SQLException e) {
            throw new PostgresException(e.getSQLState(), e.getMessage());
        }
    }

    // RF002-02 - Listado de Perfiles
    public List<Perfil> listarPerfiles() throws PostgresException {
        String sql = "SELECT * FROM Perfil";
        List<Perfil> perfiles = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Perfil perfil = new Perfil(
                        resultSet.getInt("id_perfil"),
                        resultSet.getString("nombre_perfil"),
                        resultSet.getString("estado"),
                        resultSet.getString("descripcion")
                );

                // Obtener funcionalidades asociadas
                perfil.setFuncionalidades(obtenerFuncionalidadesDePerfil(perfil.getIdPerfil()));

                perfiles.add(perfil);
            }
        } catch (SQLException e) {
            throw new PostgresException(e.getSQLState(), e.getMessage());
        }

        return perfiles;
    }

    // RF002-03 - Modificación de Perfil
    public void modificarPerfil(int idPerfil, String nombrePerfil, String estado, String descripcion) throws PostgresException {
        String sql = "UPDATE Perfil SET nombre_perfil = ?, estado = ?, descripcion = ? WHERE id_perfil = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombrePerfil);
            statement.setString(2, estado);
            statement.setString(3, descripcion);
            statement.setInt(4, idPerfil);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Perfil actualizado exitosamente.");
            } else {
                System.out.println("Perfil no encontrado.");
            }
        } catch (SQLException e) {
            throw new PostgresException(e.getSQLState(), e.getMessage());
        }
    }

    // RF002-04 - Baja de Perfil
    public void eliminarPerfil(int idPerfil) throws PostgresException {
        String sql = "DELETE FROM Perfil WHERE id_perfil = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPerfil);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Perfil eliminado exitosamente.");
            } else {
                System.out.println("Perfil no encontrado.");
            }
        } catch (SQLException e) {
            throw new PostgresException(e.getSQLState(), e.getMessage());
        }
    }

    // Asociar funcionalidades a un perfil
    public void asociarFuncionalidadAPerfil(int idPerfil, int idFuncionalidad) throws PostgresException {
        String sql = "INSERT INTO PerfilFuncionalidad (id_perfil, id_funcionalidad) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPerfil);
            statement.setInt(2, idFuncionalidad);

            statement.executeUpdate();
            System.out.println("Funcionalidad asociada exitosamente al perfil.");
        } catch (SQLException e) {
            throw new PostgresException(e.getSQLState(), e.getMessage());
        }
    }

    // Eliminar asociación de funcionalidad con un perfil
    public void eliminarFuncionalidadDePerfil(int idPerfil, int idFuncionalidad) throws PostgresException {
        String sql = "DELETE FROM PerfilFuncionalidad WHERE id_perfil = ? AND id_funcionalidad = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPerfil);
            statement.setInt(2, idFuncionalidad);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Funcionalidad eliminada del perfil exitosamente.");
            } else {
                System.out.println("No se encontró la funcionalidad asociada al perfil.");
            }
        } catch (SQLException e) {
            throw new PostgresException(e.getSQLState(), e.getMessage());
        }
    }

    // Obtener funcionalidades asociadas a un perfil
    private List<Funcionalidad> obtenerFuncionalidadesDePerfil(int idPerfil) throws PostgresException {
        String sql = "SELECT f.id_funcionalidad, f.nombre_funcionalidad, f.descripcion_funcionalidad " +
                "FROM Funcionalidad f " +
                "INNER JOIN PerfilFuncionalidad pf ON f.id_funcionalidad = pf.id_funcionalidad " +
                "WHERE pf.id_perfil = ?";
        List<Funcionalidad> funcionalidades = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPerfil);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Funcionalidad funcionalidad = new Funcionalidad(
                            resultSet.getInt("id_funcionalidad"),
                            resultSet.getString("nombre_funcionalidad"),
                            resultSet.getString("descripcion_funcionalidad")
                    );
                    funcionalidades.add(funcionalidad);
                }
            }
        } catch (SQLException e) {
            throw new PostgresException(e.getSQLState(), e.getMessage());
        }

        return funcionalidades;
    }
}
