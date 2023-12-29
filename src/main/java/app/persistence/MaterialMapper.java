package app.persistence;

import app.dtos.DTOPartsByMaterials;
import app.entities.Material;
import app.entities.Part;
import app.entities.Order;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialMapper {
    public static List<Material> getAllMaterials(ConnectionPool connectionPool) throws DatabaseException {
        List<Material> allMaterials = new ArrayList<>();

        String sql = "SELECT id, name, length_cm, description, item_number, width_cm, height_cm, price from MATERIALS";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int length = rs.getInt("length_cm");
                    String description = rs.getString("description");
                    long itemNumber = rs.getLong("item_number");
                    int width = rs.getInt("width_cm");
                    int height = rs.getInt("height_cm");
                    int price = rs.getInt("price");
                    allMaterials.add(new Material(id, name, length, description, itemNumber, width, height, price));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indlæsning af materialer " + e.getMessage());
        }
        return allMaterials;
    }

    public static Material addMaterial(Material material, ConnectionPool connectionPool) throws DatabaseException {
        Material newMaterial;
        String sql = "INSERT INTO materials (name, length_cm, description, item_number, width_cm, height_cm, price) values(?,?,?,?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, material.getName());
                ps.setInt(2, material.getLength());
                ps.setString(3, material.getDescription());
                ps.setLong(4, material.getItemNumber());
                ps.setInt(5, material.getWidth());
                ps.setInt(6, material.getHeight());
                ps.setInt(7, material.getPrice());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("material wasn't added to the Database");
                }
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int id = rs.getInt(1);

                newMaterial = new Material(id, material.getName(), material.getLength(), material.getDescription(), material.getItemNumber(), material.getWidth(), material.getHeight(), material.getPrice());
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved oprettelse af material:" + e.getMessage());
        }
        return newMaterial;
    }

    public static void deleteMaterial(int materialId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM materials WHERE id = ?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, materialId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Material with ID " + materialId + " was not found or could not be deleted.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error when deleting material: " + e.getMessage());
        }
    }

    public static void updateMaterial(Material material, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE public.materials SET name = ?, length_cm = ?, description = ?, item_number = ?, width_cm = ?, height_cm = ?, price = ? WHERE id = ?";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, material.getName());
                ps.setInt(2, material.getLength());
                ps.setString(3, material.getDescription());
                ps.setLong(4, material.getLength());
                ps.setInt(5, material.getWidth());
                ps.setInt(6, material.getHeight());
                ps.setInt(7, material.getPrice());
                ps.setInt(8, material.getId());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Material with ID " + material.getId() + " was not found or could not be updated.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Der opstod en fejl ved oprettelse af vare: " + e.getMessage());
        }
    }

    public static void addPartsList(List<Part> partList, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "INSERT INTO parts_list (material_id, amount, order_id) values(?,?,?)";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                for (Part part : partList) {
                    ps.setInt(1, part.getMaterialId());
                    ps.setInt(2, part.getAmount());
                    ps.setInt(3, part.getOrderId());
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected != 1) {
                        throw new DatabaseException("Partslist wasn't added to the Database");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved oprettelse af material:" + e.getMessage());
        }
    }

    public static List<DTOPartsByMaterials> getPartsList(Order order, ConnectionPool connectionPool) throws DatabaseException {
        List<DTOPartsByMaterials> partsList = new ArrayList<>();
        String sql = "Select materials.name, materials.length_cm, amount, description from materials join parts_list on materials.id = parts_list.material_id where parts_list.order_id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, order.getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String name = rs.getString("name");
                    int length = rs.getInt("length_cm");
                    int amount = rs.getInt("amount");
                    String description = rs.getString("description");
                    DTOPartsByMaterials partListLine = new DTOPartsByMaterials(name, length, amount, description);
                    partsList.add(partListLine);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return partsList;
    }
}



