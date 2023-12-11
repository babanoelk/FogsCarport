package app.persistence;

import app.dtos.DTOShedIdLengthWidth;
import app.dtos.DTOShedLengthWidth;
import app.entities.Shed;
import app.exceptions.DatabaseException;
import java.sql.*;

public class ShedMapper {
    public static void updateShed(DTOShedIdLengthWidth dtoUser, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE public.shed SET width = ?, length = ? WHERE id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, dtoUser.getWidth());
                ps.setInt(2, dtoUser.getLength());
                ps.setInt(3, dtoUser.getId());

                int rowsAffected = ps.executeUpdate();

                // Check the number of rows affected
                if (rowsAffected > 0) {
                    System.out.println("Update successful. Rows affected: " + rowsAffected);

                } else {
                    System.out.println("No rows were updated. Check your update query or conditions.");
                    throw new DatabaseException("Der opstod en fejl under rettele af kontaktoplysninger");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Shed addShed(DTOShedLengthWidth shed, ConnectionPool connectionPool) throws DatabaseException {

        Shed newShed;
        String sql = "INSERT INTO shed (width, length) values (?,?)";

            try (Connection connection = connectionPool.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, shed.getWidth());
                    ps.setInt(2, shed.getLength());

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected != 1) {
                        throw new DatabaseException("Shed was not added to database");
                    }
                    ResultSet rs = ps.getGeneratedKeys();
                    rs.next();
                    int id = rs.getInt(1);
                    newShed = new Shed(id,shed.getWidth(),shed.getLength());
                }
            } catch (SQLException e) {
                throw new DatabaseException("Fejl ved oprettelse af chef:" + e.getMessage());
            }

            return newShed;
        }
    }


