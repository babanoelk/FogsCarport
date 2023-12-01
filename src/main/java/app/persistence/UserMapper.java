package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;

public class UserMapper {
    public static User addUser(User newUser, ConnectionPool connectionPool) throws DatabaseException {

        User user = null;

        String sql = "INSERT INTO USER (name, email, password, address, zipcode, mobile) values (?,?,?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                //Number 0 is serialized ID
                ps.setString(1, newUser.getName());
                ps.setString(2, newUser.getEmail());
                ps.setString(3, newUser.getPassword());
                ps.setString(4, newUser.getAddress());
                ps.setInt(5, newUser.getMobile());
                //Number 6 is 'role'
                ps.setInt(7, newUser.getZipcode());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Fejl i data leveret ved oprettelse af bruger i databasen");
                }

                ResultSet rs = ps.getGeneratedKeys();

                int id = rs.getInt(1);

                newUser.setId(id);

            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved oprettelse af bruger");
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