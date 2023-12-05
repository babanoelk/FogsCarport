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
        String sql = "INSERT INTO public.ORDER (customer_note, user_id, carport_id) values (?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1,dto.getOrder().getCustomerNote());
                ps.setInt(2, userAdded.getId());
                ps.setInt(3, carportAdded.getId());
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
