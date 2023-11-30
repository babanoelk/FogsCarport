package app.controllers;

import app.persistence.ConnectionPool;
import app.persistence.MeasurementMapper;
import io.javalin.http.Context;

import java.util.List;

public class CarportController {

    public static void loadMeasurements(Context ctx, ConnectionPool connectionPool){
        try {
            List<Integer> lengthList = MeasurementMapper.getAllLengths(connectionPool);
            List<Integer> widthList = MeasurementMapper.getAllWidths(connectionPool);

            ctx.attribute("lenghtList", lengthList);
            ctx.attribute("widthList", widthList);

            ctx.render("bestilling.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }

    }

}
