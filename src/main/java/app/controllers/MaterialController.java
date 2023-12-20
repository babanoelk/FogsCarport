package app.controllers;

import app.entities.Material;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;
import io.javalin.http.Context;

import java.util.List;

public class MaterialController {
    public static void loadMaterials(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        try {
            List<Material> materialsList = MaterialMapper.getAllMaterials(connectionPool);

            ctx.attribute("materialsList", materialsList);

            ctx.render("varelager.html");


        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("fejlside.html");
        }

    }

    public static void addMaterial(Context ctx, ConnectionPool connectionPool) {
        try {
            String name = ctx.formParam("name");
            int length = Integer.parseInt(ctx.formParam("length_cm"));
            String description = ctx.formParam("description");
            long itemNumber = Long.parseLong(ctx.formParam("item_number"));
            int width = Integer.parseInt(ctx.formParam("width_cm"));
            int height = Integer.parseInt(ctx.formParam("height_cm"));
            int price = Integer.parseInt(ctx.formParam("price"));

            Material newMaterial = new Material(name, length, description, itemNumber, width, height, price);
            MaterialMapper.addMaterial(newMaterial, connectionPool);

            loadMaterials(ctx, connectionPool);
        } catch (Exception e) {
            ctx.result("An error occurred: " + e.getMessage());
        }
    }


        public static void deleteMaterial (Context ctx, ConnectionPool connectionPool) throws DatabaseException {
            int materialId = Integer.parseInt(ctx.formParam("materialId"));
            MaterialMapper.deleteMaterial(materialId, connectionPool);
            loadMaterials(ctx, connectionPool);
        }

        public static void updateMaterial (Context ctx, ConnectionPool connectionPool) throws DatabaseException {
            Material material = new Material(Integer.parseInt(ctx.formParam("id")),
                    ctx.formParam("name"),
                    Integer.parseInt(ctx.formParam("length")),
                    ctx.formParam("description"),
                    Long.parseLong(ctx.formParam("item_number")),
                    Integer.parseInt(ctx.formParam("width_cm")),
                    Integer.parseInt(ctx.formParam("height_cm")),
                    Integer.parseInt(ctx.formParam("price"))

            );
            MaterialMapper.updateMaterial(material, connectionPool);
            loadMaterials(ctx, connectionPool);
        }
        
    }

