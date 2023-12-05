package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;

public class UserMapper {
    public static User addUser(User newUser, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "INSERT INTO public.USER (name, email, password, address, mobile, zipcode, consent) values (?,?,?,?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                //Number 0 is serialized ID
                ps.setString(1, newUser.getName());
                ps.setString(2, newUser.getEmail());
                ps.setString(3, newUser.getPassword());
                ps.setString(4, newUser.getAddress());
                ps.setInt(5, newUser.getMobile());
                ps.setInt(6, newUser.getZipcode());
                ps.setBoolean(7, newUser.getConsent());

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

    /*
    public static User getUserIdByEmail(int userId, ConnectionPool connectionPool) throws DatabaseException{
        User user = null;

               String sql = "select * from USER where id = ?";

               try(Connection connection = connectionPool.getConnection()){
                   try(PreparedStatement ps = connection.prepareStatement(sql)){
                       ResultSet rs = ps.executeQuery();
                       int id = rs.getInt(1);
                       String name = rs.getString(2);
                       String email = rs.getString(3);
                       String email = rs.getString(3);
                       String email = rs.getString(3);


                   }

               }


        return user;

    }

     */

}