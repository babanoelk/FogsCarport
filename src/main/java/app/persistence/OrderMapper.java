package app.persistence;

import app.dtos.*;
import app.entities.Carport;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    public static void addOrder(DTOUserCarportOrder dto, ConnectionPool connectionPool) throws DatabaseException {
        User userAdded = UserMapper.addUser(dto.getUser(), connectionPool);
        Carport carportAdded = CarportMapper.addCarport(dto.getCarport(), connectionPool);
        String sql = "INSERT INTO public.ORDER (customer_note, user_id, carport_id) values (?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, dto.getOrder().getCustomerNote());
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

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        List<Order> allOrders = new ArrayList<>();
        String sql = "select id, date, customer_note, order_status from public.ORDER";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    Date date = rs.getDate("date");
                    String customerNote = rs.getString("customer_note");
                    int statusId = rs.getInt("order_status");
                    String orderStatus = getStatusByID(statusId, connectionPool);
                    allOrders.add(new Order(id, date, customerNote, orderStatus));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indlæsning af kundeordre " + e.getMessage());
        }
        return allOrders;
    }

    public static List<DTOGetOrderWithIdDateCustomerNoteConsentStatus> getAllOrdersByUser(User user, ConnectionPool connectionPool) throws DatabaseException {
        List<DTOGetOrderWithIdDateCustomerNoteConsentStatus> orderList = new ArrayList<>();

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
                    String orderStatus = getStatusByID(orderID, connectionPool);
                    orderList.add(new DTOGetOrderWithIdDateCustomerNoteConsentStatus(id, date, customerNote, orderStatus));
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


    public static void deleteOrderByOrderID(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String selectOrderSQL = "SELECT carport_id FROM public.order WHERE id = ?";
        String deleteOrderSQL = "DELETE FROM public.order WHERE id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement selectOrderPs = connection.prepareStatement(selectOrderSQL);
             PreparedStatement deleteOrderPs = connection.prepareStatement(deleteOrderSQL)) {

            // Set parameters for the SELECT statement
            selectOrderPs.setInt(1, orderId);

            // Execute SELECT statement to get the carport_id
            int carportId;
            try (ResultSet rs = selectOrderPs.executeQuery()) {
                if (rs.next()) {
                    carportId = rs.getInt("carport_id");
                } else {
                    throw new DatabaseException("Ordre med ID " + orderId + " blev ikke fundet.");
                }
            }

            // Set parameters for the DELETE statement
            deleteOrderPs.setInt(1, orderId);

            // Execute DELETE statement for public.order
            int orderRowsAffected = deleteOrderPs.executeUpdate();

            // Check if at least one record was deleted from public.order
            if (orderRowsAffected > 0) {
                // Get the carport ID to delete the carport
                CarportMapper.deleteCarportByCarportID(carportId, connectionPool);
            } else {
                throw new DatabaseException("Ingen ordre slettet. Fejl i databasen.");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved sletning af ordre. " + e.getMessage());
        }
    }

    public static DTOUserWithUserIdNameAddressZipcodeMobileEmail getSpecificOrderByOrderIdWithUserAndCarportAndShed(int orderId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "SELECT public.order.user_id, public.user.name, public.user.address, public.user.zipcode, public.user.mobile, public.user.email, public.order.carport_id, public.carport.width, public.carport.length, public.carport.height, public.carport.shed_id, public.shed.width AS shed_width, public.shed.length AS shed_length, public.order.customer_note FROM public.order JOIN public.user ON public.order.user_id = public.user.id JOIN public.carport ON public.order.carport_id = public.carport.id LEFT JOIN public.shed ON public.carport.shed_id = public.shed.id WHERE public.order.id = ?";

        DTOSpecificOrderByOrderIdWithUserAndCarportAndShed specificOrderByOrderIdWithUserAndCarportAndShed;
        DTOUserWithUserIdNameAddressZipcodeMobileEmail dtoUserWithUserIdNameAddressZipcodeMobileEmail = null;

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, orderId);
                ResultSet rs = ps.executeQuery();

                // Check if there are any rows in the result set
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    int zipcode = rs.getInt("zipcode");
                    int mobile = rs.getInt("mobile");
                    String email = rs.getString("email");
                    int carportId = rs.getInt("carport_id");
                    int carportWidth = rs.getInt("width");
                    int carportLength = rs.getInt("length");
                    int carportHeight = rs.getInt("height");
                    String customerNote = rs.getString("customer_note");

                    // Declare variables outside the if-else blocks
                    int shedId = 0;
                    int shedWidth = 0;
                    int shedLength = 0;

                    // Check if shedId is not NULL to determine if there's a shed associated
                    if (!rs.wasNull()) {
                        // Handle the case where there is a shed
                        shedId = rs.getInt("shed_id");
                        shedWidth = rs.getInt("shed_width");
                        shedLength = rs.getInt("shed_length");
                    } else {
                        // Handle the case where there is no shed
                        // No need to initialize shedId, shedWidth, and shedLength here
                    }

                    dtoUserWithUserIdNameAddressZipcodeMobileEmail =
                            new DTOUserWithUserIdNameAddressZipcodeMobileEmail(userId, name, address, zipcode, mobile, email);

                    specificOrderByOrderIdWithUserAndCarportAndShed = new DTOSpecificOrderByOrderIdWithUserAndCarportAndShed(userId, name, address, zipcode, mobile, email, carportId, carportWidth, carportLength, carportHeight, shedId, shedWidth, shedLength, customerNote);
                } else {
                    // Handle the case where no rows are returned
                    specificOrderByOrderIdWithUserAndCarportAndShed = null; // or handle it as needed
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dtoUserWithUserIdNameAddressZipcodeMobileEmail;
    }

    public static DTOCarportWithIdLengthWidthHeight getSpecificCarportByOrderId(int orderId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "SELECT public.order.user_id, public.user.name, public.user.address, public.user.zipcode, public.user.mobile, public.user.email, public.order.carport_id, public.carport.width, public.carport.length, public.carport.height, public.carport.shed_id, public.shed.width AS shed_width, public.shed.length AS shed_length, public.order.customer_note FROM public.order JOIN public.user ON public.order.user_id = public.user.id JOIN public.carport ON public.order.carport_id = public.carport.id LEFT JOIN public.shed ON public.carport.shed_id = public.shed.id WHERE public.order.id = ?";

        DTOCarportWithIdLengthWidthHeight carportWithIdLengthWidthHeight;
        DTOUserWithUserIdNameAddressZipcodeMobileEmail dtoUserWithUserIdNameAddressZipcodeMobileEmail = null;

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, orderId);
                ResultSet rs = ps.executeQuery();

                // Check if there are any rows in the result set
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    int zipcode = rs.getInt("zipcode");
                    int mobile = rs.getInt("mobile");
                    String email = rs.getString("email");
                    int carportId = rs.getInt("carport_id");
                    int carportWidth = rs.getInt("width");
                    int carportLength = rs.getInt("length");
                    int carportHeight = rs.getInt("height");
                    String customerNote = rs.getString("customer_note");

                    // Declare variables outside the if-else blocks
                    int shedId = 0;
                    int shedWidth = 0;
                    int shedLength = 0;

                    // Check if shedId is not NULL to determine if there's a shed associated
                    if (!rs.wasNull()) {
                        // Handle the case where there is a shed
                        shedId = rs.getInt("shed_id");
                        shedWidth = rs.getInt("shed_width");
                        shedLength = rs.getInt("shed_length");
                    } else {
                        // Handle the case where there is no shed
                        // No need to initialize shedId, shedWidth, and shedLength here
                    }

                    carportWithIdLengthWidthHeight =
                            new DTOCarportWithIdLengthWidthHeight(carportId, carportLength, carportWidth, carportHeight);

                    //carportWithIdLengthWidthHeight = new DTOCarportWithIdLengthWidthHeight(userId, name, address, zipcode, mobile, email, carportId, carportWidth, carportLength, carportHeight, shedId, shedWidth, shedLength, customerNote);
                } else {
                    // Handle the case where no rows are returned
                    carportWithIdLengthWidthHeight = null; // or handle it as needed
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carportWithIdLengthWidthHeight;
    }


    public static void updateOrderStatus(int orderId, int statusId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "update public.order set order_status = ? where id = ?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, statusId);
                ps.setInt(2, orderId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Ordre ikke opdateret");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static boolean updateName(String newName, int orderID, ConnectionPool connectionPool) throws DatabaseException {
        boolean updatedName = false;

        String sql = "UPDATE public.user AS u SET name = ? FROM public.order AS o JOIN public.user AS u2 ON o.user_id = u2.id JOIN public.carport AS c ON o.carport_id = c.id LEFT JOIN public.shed AS s ON c.shed_id = s.id WHERE o.id = ? AND u.id = u2.id";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, orderID);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                updatedName = true;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Kunne ikke ændre navnet " + e);
        }

        return updatedName;
    }

    public static DTOShedIdLengthWidth getSpecificShedByOrderId(int orderId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "SELECT public.order.user_id, public.user.name, public.user.address, public.user.zipcode, public.user.mobile, public.user.email, public.order.carport_id, public.carport.width, public.carport.length, public.carport.height, public.carport.shed_id, public.shed.width AS shed_width, public.shed.length AS shed_length, public.order.customer_note FROM public.order JOIN public.user ON public.order.user_id = public.user.id JOIN public.carport ON public.order.carport_id = public.carport.id LEFT JOIN public.shed ON public.carport.shed_id = public.shed.id WHERE public.order.id = ?";

        DTOShedIdLengthWidth dtoShedIdLengthWidth;
        DTOUserWithUserIdNameAddressZipcodeMobileEmail dtoUserWithUserIdNameAddressZipcodeMobileEmail = null;

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, orderId);
                ResultSet rs = ps.executeQuery();

                // Check if there are any rows in the result set
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    int zipcode = rs.getInt("zipcode");
                    int mobile = rs.getInt("mobile");
                    String email = rs.getString("email");
                    int carportId = rs.getInt("carport_id");
                    int carportWidth = rs.getInt("width");
                    int carportLength = rs.getInt("length");
                    int carportHeight = rs.getInt("height");
                    String customerNote = rs.getString("customer_note");

                    // Declare variables outside the if-else blocks
                    int shedId = 0;
                    int shedWidth = 0;
                    int shedLength = 0;

                    // Check if shedId is not NULL to determine if there's a shed associated
                    if (!rs.wasNull()) {
                        // Handle the case where there is a shed
                        shedId = rs.getInt("shed_id");
                        shedWidth = rs.getInt("shed_width");
                        shedLength = rs.getInt("shed_length");
                    } else {
                        // Handle the case where there is no shed
                        // No need to initialize shedId, shedWidth, and shedLength here
                    }

                    dtoShedIdLengthWidth =
                            new DTOShedIdLengthWidth(shedId, shedLength, shedWidth);

                    //carportWithIdLengthWidthHeight = new DTOCarportWithIdLengthWidthHeight(userId, name, address, zipcode, mobile, email, carportId, carportWidth, carportLength, carportHeight, shedId, shedWidth, shedLength, customerNote);
                } else {
                    // Handle the case where no rows are returned
                    dtoShedIdLengthWidth = null; // or handle it as needed
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dtoShedIdLengthWidth;
    }


    public static void addShedToSpecificOrderId(int carportId, int shedId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "UPDATE public.carport SET shed_id = ? WHERE shed_id IS NULL AND id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, shedId);
                ps.setInt(2, carportId);

                int shedAdded = ps.executeUpdate();

                if (shedAdded > 0) {
                    System.out.println("Shed added successfully");
                } else {
                    System.out.println("No rows updated. Order ID not found or shed ID already set.");
                    // Remove the following line that throws an exception
                    // throw new DatabaseException("ikke godkendt");
                }

            }
        } catch (SQLException e) {
            // Log the exception or handle it appropriately, but avoid throwing a new exception here
            throw new RuntimeException(e);
        }
    }

    public static void sendBill(int orderID, ConnectionPool connectionPool) throws DatabaseException{

        String sql ="UPDATE public.order SET order_status = 2 WHERE id = ?;";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1,orderID);

                int statusChanged = ps.executeUpdate();
                if (statusChanged > 0){
                    System.out.println("Status changed!");
                } else {
                    System.out.println("Status wasn't changed");
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public static void updateOrderPrice(int orderID, float price, ConnectionPool connectionPool){

        String sql ="UPDATE public.order SET price = ? WHERE id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setFloat(1,price);
                ps.setInt(2,orderID);

                int priceChanged = ps.executeUpdate();

                if (priceChanged > 0){
                    System.out.println("Status changed!");
                } else {
                    System.out.println("Status wasn't changed");
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
