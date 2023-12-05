package app.persistence;

import app.entities.Carport;
import app.exceptions.DatabaseException;

import java.sql.*;

public class CarportMapper {
    public static Carport addCarport(Carport carport, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "insert into carport (width, length, height) values (?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, carport.getWidth());
                ps.setInt(2, carport.getLength());
                ps.setInt(3, carport.getHeight());
                //ps.setBoolean(4, carport.hasShed());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Carport was not added to database");
                }
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int id = rs.getInt(1);
                carport.setId(id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved oprettelse af carport" + e.getMessage());
        }
        return carport;
    }

    public static void deleteCarportByCarportID(int carportId, ConnectionPool connectionPool) throws DatabaseException
    {

        String sql = "DELETE from public.carport where id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, carportId);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected != 1) {
                    throw new DatabaseException("Ingen ordre slettet. Fejl i databasen.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ingen ordre slettet. " + e.getMessage());
        }
    }
}
