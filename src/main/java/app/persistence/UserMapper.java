package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;

public class UserMapper {
    public static User addUser(User newUser, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO public.USER (name, email, password, address, mobile, zipcode, role) values (?,?,?,?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, newUser.getName());
                ps.setString(2, newUser.getEmail());
                ps.setString(3, newUser.getPassword());
                ps.setString(4, newUser.getAddress());
                ps.setInt(5, newUser.getMobile());
                ps.setInt(6, newUser.getZipcode());
                //ps.setBoolean(7, newUser.getConsent());
                ps.setInt(7, newUser.getRole());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Fejl i data leveret ved oprettelse af bruger i databasen");
                }
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int id = rs.getInt(1);
                newUser.setId(id);
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("violates foreign key constraint \"fk_user_zipcode\"")) {
                throw new DatabaseException("Det indtastede postnummer eksisterer ikke. Prøv igen");
            } else if (e.getMessage().contains("\"unique_mail\"")) {
                throw new DatabaseException("Den indtastede e-mail eksisterer allerede. Prøv igen");
            } else {
                throw new DatabaseException("Følgende fejl opstod: " + e.getMessage());
            }
        }
        return newUser;
    }


    public static User getUserByEmail(String email, ConnectionPool connectionPool) throws DatabaseException {

        User user = null;

        String sql = "select * from public.USER where email = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                rs.next();

                int id = rs.getInt(1);
                String name = rs.getString(2);
                String userEmail = rs.getString(3);
                String password = rs.getString(4);
                String address = rs.getString(5);
                int mobile = rs.getInt(6);
                int role = rs.getInt(7);
                int zipcode = rs.getInt(8);

                user = new User(id, name, userEmail, password, address, mobile, role, zipcode);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return user;
    }


    public static boolean loginValidator(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        boolean isLoggedIn = false;
        String sql = "select * from public.User where email = ? and password = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    isLoggedIn = true;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Login fejl, email eller password er forkert " + e);
        }
        return isLoggedIn;
    }

    public static void updateUser(User user, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE public.user SET name = ?, email = ?, address = ?, mobile = ?, zipcode = ? WHERE id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getAddress());
                ps.setInt(4, user.getMobile());
                ps.setInt(5, user.getZipcode());
                ps.setInt(6, user.getId());

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

