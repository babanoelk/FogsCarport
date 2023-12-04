package app.persistence;

import app.entities.Shed;
import app.exceptions.DatabaseException;

import java.sql.*;

public class ShedMapper {
    public static Shed addShed(Shed shed, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO shed (carportId, width, length, height) values (?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, shed.getCarportID());
                ps.setInt(2, shed.getWidth());
                ps.setInt(3, shed.getLength());
                ps.setInt(4, shed.getHeight());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Shed was not added to database");
                }
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int id = rs.getInt(1);
                shed.setId(id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved oprettelse af shed" + e.getMessage());
        }
        return shed;
    }
}
