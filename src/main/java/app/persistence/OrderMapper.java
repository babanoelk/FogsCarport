package app.persistence;

import app.dtos.*;
import app.entities.Carport;
import app.entities.Order;
import app.entities.Shed;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {


    public static void addOrder(DTOUserCarportOrder dto, float carportPrice, ConnectionPool connectionPool) throws DatabaseException {
        User userAdded = UserMapper.addUser(dto.getUser(), connectionPool);
        Carport carportAdded = CarportMapper.addCarport(dto.getCarport(), connectionPool);

        String sql = "INSERT INTO public.ORDER (customer_note, user_id, carport_id, price) values (?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, dto.getOrder().getCustomerNote());
                ps.setInt(2, userAdded.getId());
                ps.setInt(3, carportAdded.getId());
                ps.setFloat(4, carportPrice);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Ordre ikke oprettet. Fejl i data sendt til databasen.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ordren blev ikke oprettet." + e.getMessage());
        }
    }

    public static void addOrderToExistingUser(DTOUserCarportOrder dto, float carportPrice, ConnectionPool connectionPool) throws DatabaseException {
        User user = UserMapper.getUserByEmail(dto.getUser().getEmail(), connectionPool);
        Carport carportAdded = CarportMapper.addCarport(dto.getCarport(), connectionPool);
        String sql = "INSERT INTO public.ORDER (customer_note, user_id, carport_id, price) values (?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, dto.getOrder().getCustomerNote());
                ps.setInt(2, user.getId());
                ps.setInt(3, carportAdded.getId());
                ps.setFloat(4, carportPrice);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Ordre ikke oprettet. Fejl i data sendt til databasen.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ordren blev ikke oprettet." + e.getMessage());
        }
    }

    public static List<DTOOrderCustomer> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        List<DTOOrderCustomer> allOrders = new ArrayList<>();
        String sql = "select public.order.id, date, customer_note, order_status, name, email, mobile, price FROM public.order JOIN public.user ON public.order.user_id = public.user.id";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    Date date = rs.getDate("date");
                    String customerNote = rs.getString("customer_note");
                    int statusId = rs.getInt("order_status");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    int mobile = rs.getInt("mobile");
                    String orderStatus = getStatusByID(statusId, connectionPool);
                    float price = rs.getFloat("price");
                    allOrders.add(new DTOOrderCustomer(id, date, customerNote, statusId, name, email, mobile, orderStatus, price));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indlæsning af kundeordre " + e.getMessage());
        }
        return allOrders;
    }

    public static List<Order> getAllOrdersByUser(User user, ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();

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
                    orderList.add(new Order(id, date, customerNote, orderStatus));
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


    public static void deleteOrderById(int orderId, ConnectionPool connectionPool) throws DatabaseException {
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
                CarportMapper.deleteCarportById(carportId, connectionPool);
            } else {
                throw new DatabaseException("Ingen ordre slettet. Fejl i databasen.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved sletning af ordre. " + e.getMessage());
        }
    }

    public static User getOrderDetails(int orderId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "SELECT public.order.user_id, public.user.name, public.user.address, public.user.zipcode, public.user.mobile, public.user.email, public.order.carport_id, public.carport.width, public.carport.length, public.carport.height, public.carport.shed_id, public.shed.width AS shed_width, public.shed.length AS shed_length, public.order.customer_note FROM public.order JOIN public.user ON public.order.user_id = public.user.id JOIN public.carport ON public.order.carport_id = public.carport.id LEFT JOIN public.shed ON public.carport.shed_id = public.shed.id WHERE public.order.id = ?";

        DTOOrderDetails orderDetails;
        User user = null;

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
                    user = new User(userId, name, email, address, mobile, zipcode);
                    orderDetails = new DTOOrderDetails(userId, name, address, zipcode, mobile, email, carportId, carportWidth, carportLength, carportHeight, shedId, shedWidth, shedLength, customerNote);
                } else {
                    // Handle the case where no rows are returned
                    orderDetails = null; // or handle it as needed
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public static Carport getCarportByOrderId(int orderId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "SELECT carport.id, carport.width, carport.length, carport.height FROM public.order JOIN carport ON public.order.carport_id = carport.id WHERE public.order.id = ?";

        Carport carport;

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, orderId);
                ResultSet rs = ps.executeQuery();

                // Check if there are any rows in the result set
                if (rs.next()) {
                    int carportId = rs.getInt("id");
                    int carportWidth = rs.getInt("width");
                    int carportLength = rs.getInt("length");
                    int carportHeight = rs.getInt("height");
                    carport = new Carport(carportId, carportWidth, carportLength, carportHeight);
                } else {
                    // Handle the case where no rows are returned
                    carport = null; // or handle it as needed
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return carport;
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

    public static Shed getShedByOrderId(int orderId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "SELECT shed.id, shed.width, shed.length FROM public.order JOIN carport ON public.order.carport_id = carport.id LEFT JOIN shed ON carport.shed_id = shed.id WHERE public.order.id = ?";

        Shed shed = null;

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, orderId);
                ResultSet rs = ps.executeQuery();

                if (!rs.wasNull()) {
                    while (rs.next()) {
                        int shedId = rs.getInt("id");
                        int shedWidth = rs.getInt("width");
                        int shedLength = rs.getInt("length");
                        shed = new Shed(shedId, shedWidth, shedLength);
                    }
                } else {
                    // Handle the case where no rows are returned
                    shed = null; // or handle it as needed
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return shed;
    }


    public static void addShedToOrder(Carport carport, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "UPDATE public.carport SET shed_id = ? WHERE shed_id IS NULL AND id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, carport.getShed().getId());
                ps.setInt(2, carport.getId());

                int shedAdded = ps.executeUpdate();

                if (shedAdded > 0) {
                    System.out.println("Shed added successfully");
                } else {
                    System.out.println("No rows updated. Order ID not found or shed ID already set.");
                }

            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static void updateStatusBillSent(int orderId, ConnectionPool connectionPool) throws DatabaseException{

        String sql ="UPDATE public.order SET order_status = 2 WHERE id = ?;";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1,orderId);

                int statusChanged = ps.executeUpdate();
                if (statusChanged > 0){
                    System.out.println("Status changed!");
                } else {
                    System.out.println("Status wasn't changed");
                }
            }
        } catch (SQLException e){
            throw new DatabaseException(e.getMessage());
        }
    }

    public static void updateOrderPrice(int orderID, float price, ConnectionPool connectionPool) throws DatabaseException{

        String sql ="UPDATE public.order SET price = ? WHERE id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setFloat(1,price);
                ps.setInt(2,orderID);

                int priceChanged = ps.executeUpdate();

                if (priceChanged > 0){
                    System.out.println("Price changed!");
                } else {
                    System.out.println("Price wasn't changed");
                }
            }
        } catch (SQLException e){
            throw new DatabaseException(e.getMessage());
        }
    }

    public static Order getOrderById(int id, ConnectionPool connectionPool) throws DatabaseException {
        Order order = null;
        String sql = "select * from public.order where id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);

                ResultSet rs = ps.executeQuery();

                while(rs.next()) {
                    int orderId = rs.getInt("id");
                    Date date = rs.getDate("date");
                    String note = rs.getString("customer_note");
                    int userId = rs.getInt("user_id");
                    int status = rs.getInt("order_status");
                    int carportId = rs.getInt("carport_id");
                    order = new Order(orderId, date, note, getStatusByID(status, connectionPool));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return order;
    }
}
