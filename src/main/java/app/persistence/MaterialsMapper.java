package app.persistence;

import app.entities.Materials;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialsMapper {
    public static List<Materials> getAllMaterials(ConnectionPool connectionPool) throws DatabaseException {
        List<Materials> allMaterials = new ArrayList<>();

        String sql = "select id, name, length_cm, description, item_number, width_cm, height_cm, price from MATERIALS";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int length = rs.getInt("length_cm");
                    String description = rs.getString("description");
                    int itemNumber = rs.getInt("item_number");
                    int width = rs.getInt("width_cm");
                    int height = rs.getInt("height_cm");
                    int price = rs.getInt("price");
                    allMaterials.add(new Materials(id, name, length, description, itemNumber, width, height, price));

                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indlæsning af materialer " + e.getMessage());
        }
        return allMaterials;
    }

    public static Materials addMaterial(Materials materials, ConnectionPool connectionPool)throws DatabaseException
    {
        Materials newMaterial;
        String sql = "INSERT INTO materials (name, length_cm, description, item_number, width_cm, height_cm, price) values(?,?,?,?,?,?,?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                //ps.setInt(1,materials.getId());
                ps.setString(1, materials.getName());
                ps.setInt(2, materials.getLength());
                ps.setString(3, materials.getDescription());
                ps.setInt(4, materials.getItemNumber());
                ps.setInt(5, materials.getWidth());
                ps.setInt(6, materials.getHeight());
                ps.setInt(7, materials.getPrice());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("material wasn't added to the Database");
                }
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int id = rs.getInt(1);

                newMaterial = new Materials(id, materials.getName(), materials.getLength(), materials.getDescription(), materials.getItemNumber(), materials.getWidth(), materials.getHeight(), materials.getPrice());
            }
        }catch (SQLException e) {
            throw new DatabaseException("Fejl ved oprettelse af material:" + e.getMessage());
            }
        return newMaterial;
    }


    }



