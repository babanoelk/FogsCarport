package app.persistence;

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
                int carportId = rs.getInt(1);
                carport.setId(carportId);
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

    public static void deleteCarportById(int carportId, ConnectionPool connectionPool) throws DatabaseException {

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
                int shedId = rs.getInt(1);
                shed.setId(shedId);
            }
        } catch (SQLException e) {
                throw new DatabaseException("Fejl ved oprettelse af chef:" + e.getMessage());
            }

        return shed;
    }

    public static void updateCarport(Carport carport, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE public.carport SET width = ?, length = ?, height = ? WHERE id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, carport.getWidth());
                ps.setInt(2, carport.getLength());
                ps.setInt(3, carport.getHeight());
                ps.setInt(4, carport.getId());

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
            throw new DatabaseException(e.getMessage());
        }
    }

    public static Carport getCarportById(int carportId, ConnectionPool connectionPool) throws DatabaseException{
        Carport carport = null;

        String sql = "SELECT * FROM public.carport Where id = ?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, carportId);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    int width = rs.getInt(2);
                    int length = rs.getInt(3);
                    int height = rs.getInt(4);

                    carport = new Carport(id, width, length, height);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indlæsning af carport " + e.getMessage());
        }
        return carport;
    }
}
