package app.persistence;

import app.dtos.GetOrderWithIdDateCustomerNoteConsentStatus;
import app.dtos.UserCarportOrderDTO;
import app.entities.Carport;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static List<GetOrderWithIdDateCustomerNoteConsentStatus> getAllOrders(User user, ConnectionPool connectionPool) throws DatabaseException {
        List<GetOrderWithIdDateCustomerNoteConsentStatus> orderList = new ArrayList<>();

        String sql = "select id, date, customer_note, order_status from public.ORDER where user_id = ?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, user.getId());
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    Date date = rs.getDate("date");
                    String customerNote = rs.getString("customer_note");
                    int orderID = rs.getInt("order_status");
                    String orderStatus = getStatusByID(orderID,connectionPool);
                    orderList.add(new GetOrderWithIdDateCustomerNoteConsentStatus(id, date, customerNote, orderStatus));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indlæsning af kundeordre " + e.getMessage());
        }
        return orderList;
    }

    public static String getStatusByID(int statusID, ConnectionPool connectionPool) throws DatabaseException, SQLException {
        String sql = "select * from public.status where id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, statusID);

                ResultSet rs = ps.executeQuery();
                rs.next();

                return rs.getString("status");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void deleteOrder(Order order, User user, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "DELETE FROM public.order WHERE user_id = ? AND id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                // Set the user ID and order ID parameters in the prepared statement
                ps.setInt(1, user.getId());
                ps.setInt(2, order.getId());

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected != 1) {
                    throw new DatabaseException("Ingen ordre slettet. Fejl i databasen.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ingen ordre slettet. " + e.getMessage());
        }
    }
}

