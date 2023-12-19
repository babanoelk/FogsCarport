package app.controllers;

import app.dtos.DTOPartsByMaterials;
import app.entities.Materials;
import app.entities.Order;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;
import app.persistence.MaterialsMapper;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

import java.util.List;

public class MaterialController {
    public static void loadMaterials(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Materials> materialsList = MaterialsMapper.getAllMaterials(connectionPool);

            ctx.attribute("materialsList", materialsList);

            ctx.render("ret-i-varer.html");


        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("fejlside.html");
        }

    }

    public static void loadParts(Context ctx, ConnectionPool connectionPool) {

        int orderId = Integer.parseInt(ctx.formParam("order_id"));

        try {
            Order order = OrderMapper.getOrderById(orderId, connectionPool);
            List<DTOPartsByMaterials> partsList = MaterialMapper.getPartsList(order, connectionPool);

            ctx.attribute("partsList", partsList);
            ctx.render("kunde-ordre.html");
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

            Materials newMaterial = new Materials(0, name, length, description, itemNumber, width, height, price);
            MaterialsMapper.addMaterial(newMaterial, connectionPool);

            ctx.redirect("/AdminSide");
        } catch (Exception e) {
            ctx.result("An error occurred: " + e.getMessage());
        }
    }


    public static void deleteMaterial (Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int materialId = Integer.parseInt(ctx.formParam("materialId"));
        MaterialsMapper.deleteMaterial(materialId, connectionPool);
        ctx.redirect("/AdminSide");
    }

    public static void updateMaterial (Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Materials material = new Materials(Integer.parseInt(ctx.formParam("id")),
                ctx.formParam("name"),
                Integer.parseInt(ctx.formParam("length")),
                ctx.formParam("description"),
                Long.parseLong(ctx.formParam("item_number")),
                Integer.parseInt(ctx.formParam("width_cm")),
                Integer.parseInt(ctx.formParam("height_cm")),
                Integer.parseInt(ctx.formParam("price"))
            );
        MaterialsMapper.updateMaterial(material, connectionPool);
        ctx.redirect("/AdminSide");
    }


}

