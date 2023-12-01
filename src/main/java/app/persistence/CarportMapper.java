package app.persistence;

import app.entities.Carport;
import app.exceptions.DatabaseException;

import java.sql.*;

public class CarportMapper {
    public static Carport addCarport(Carport carport, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO carport (width, length, height, has_shed) values (?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, carport.getWidth());
                ps.setInt(2, carport.getLength());
                ps.setInt(3, carport.getHeight());
                ps.setBoolean(4, carport.hasShed());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Carport was not added to database");
                }
                ResultSet rs = ps.getGeneratedKeys();
                int id = rs.getInt(1);
                carport.setId(id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved oprettelse af carport");
        }
        return carport;
    }
}