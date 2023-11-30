package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderMapper {
    public static void addOrder(Order order, User user, Carport carport, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO ORDER (date, customer_note, consent, user_id, carport_id) values (?,?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setDate(1, order.getDate);
                ps.setDate(2,order.get)
            }
        } catch (SQLException e) {
            throw new DatabaseException("write womthing");
        }
    }
}
