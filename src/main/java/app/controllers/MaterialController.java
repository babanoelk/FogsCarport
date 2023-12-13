package app.controllers;

import app.entities.Materials;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialsMapper;
import io.javalin.http.Context;

import java.util.List;

public class MaterialController {
    public static void loadMaterials(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        try{
            List<Materials>materialsList= MaterialsMapper.getAllMaterials(connectionPool);

            ctx.attribute("materialsList",materialsList);

            ctx.render("ret-i-varer.html");



        } catch (DatabaseException e) {
        ctx.attribute("message", e.getMessage());
        ctx.render("fejlside.html");
    }

}

}
