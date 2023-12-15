package app.controllers;

import app.dtos.DTOPartsByMaterials;
import app.entities.Materials;
import app.entities.Order;
import app.entities.Part;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;
import app.persistence.OrderMapper;
import io.javalin.http.Context;

import java.util.List;

public class MaterialController {
    public static void loadMaterials(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        try {
            List<Materials> materialsList = MaterialMapper.getAllMaterials(connectionPool);
            ctx.attribute("materialsList", materialsList);
            ctx.render("lagervare.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("fejlside.html");
        }
    }


    public static void loadParts(Context ctx, ConnectionPool connectionPool) throws DatabaseException{

        int orderID = Integer.parseInt(ctx.formParam("order_id"));
        Order order = OrderMapper.getOrderById(orderID, connectionPool);

        try{
            List<DTOPartsByMaterials> partsList = MaterialMapper.getPartsList(order, connectionPool);

            ctx.attribute("partsList", partsList);
            ctx.render("kunde-ordre.html");
        } catch(DatabaseException e){
            ctx.attribute("message", e.getMessage());
            ctx.render("fejlside.html");
        }
    }
}