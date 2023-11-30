package app.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserMapper {
    public static void addUser(User newUser, ConnectionPool connectionPool) {
        String sql = "INSERT INTO USER (name, email, password, address, zipcode, mobile) values (?,?,?,?,?,?)";
        try (Connection connection = connectionPool.getConnection) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                
            }
        }
    }
}
