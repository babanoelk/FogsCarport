package app.persistence;

import app.dtos.UserCarportOrderDTO;
import app.entities.Carport;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderMapper {
    public static void addOrder(UserCarportOrderDTO dto, ConnectionPool connectionPool) throws DatabaseException {

        User userAdded = UserMapper.addUser(dto.getUser(), connectionPool);
        Carport carportAdded = CarportMapper.addCarport(dto.getCarport(), connectionPool);
        String sql = "INSERT INTO public.ORDER (date, customer_note, consent, user_id, carport_id) values (current_date, ?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1,dto.getOrder().getCustomerNote());
                ps.setBoolean(2, dto.getOrder().getConsent());
                ps.setInt(3, userAdded.getId());
                ps.setInt(4, carportAdded.getId());
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
