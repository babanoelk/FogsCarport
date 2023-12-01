package app.persistence;

import app.entities.Carport;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderMapper {
    public static void addOrder(Order order, User user, Carport carport, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "INSERT INTO public.ORDER (customer_note, consent, user_id, carport_id) values (?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                //Number 0 is serialized int id
                //ps.setDate(1, order.getDate());
                ps.setString(1,order.getCustomerNote());
                ps.setBoolean(2, order.getConsent());
                ps.setInt(3, user.getId());
                //Number 5 is status, which is set default in DB
                ps.setInt(4, carport.getId());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Ordre ikke oprettet. Fejl i data sendt til databasen.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ordren blev ikke oprettet." + e.getMessage());
        }
    }
}
