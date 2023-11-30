package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserMapper {
    public static void addUser(User newUser, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO USER (name, email, password, address, zipcode, mobile) values (?,?,?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
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
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved oprettelse af bruger");
        }
    }
}
