package app.persistence;

import app.dtos.DTOCarportWithIdLengthWidthHeight;
import app.entities.Carport;
import app.entities.Shed;
import app.exceptions.DatabaseException;

import java.sql.*;

import static java.sql.Types.NULL;

public class CarportMapper {
    public static Carport addCarport(Carport carport, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO carport (width, length, height, shed_id) values (?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, carport.getWidth());
                ps.setInt(2, carport.getLength());
                ps.setInt(3, carport.getHeight());
                ps.setNull(4, NULL);
                if (carport.getShed() != null) {
                    Shed shedAdded = addShed(carport.getShed(), connectionPool);
                    carport.setShed(shedAdded);
                    ps.setInt(4, carport.getShed().getId());
                }

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
            if (e.getMessage().contains("\"has_shed\"")) {
                throw new DatabaseException("Vælg venligst om du ønsker et skur eller ej.");
            } else {
                throw new DatabaseException("Fejl ved oprettelse af carport" + e.getMessage());
            }
        }
        return carport;
    }

    public static void deleteCarportByCarportID(int carportId, ConnectionPool connectionPool) throws DatabaseException {

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

    public static Shed addShed(Shed shed, ConnectionPool connectionPool) throws DatabaseException {
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
                shed.setId(id);
            }
        } catch (SQLException e) {
                throw new DatabaseException("Fejl ved oprettelse af chef:" + e.getMessage());
            }

        return shed;
    }

    public static void updateCarport(DTOCarportWithIdLengthWidthHeight dtoUser, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE public.carport SET width = ?, length = ?, height = ? WHERE id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, dtoUser.getWidth());
                ps.setInt(2, dtoUser.getLength());
                ps.setInt(3, dtoUser.getHeight());
                ps.setInt(4, dtoUser.getId());


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

}
