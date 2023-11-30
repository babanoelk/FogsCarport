package app.controllers;

import app.persistence.ConnectionPool;
import app.persistence.MeasurementMapper;
import io.javalin.http.Context;
import app.exceptions.DatabaseException;
import java.util.List;

public class CarportController {

    public static void loadMeasurements(Context ctx, ConnectionPool connectionPool){

        try {
            List<Integer> lengthList = MeasurementMapper.getAllLengths(connectionPool);
            List<Integer> widthList = MeasurementMapper.getAllWidths(connectionPool);
            List<Integer> heightList = MeasurementMapper.getAllHeights(connectionPool);

            ctx.attribute("lengthList", lengthList);
            ctx.attribute("widthList", widthList);
            ctx.attribute("heightList", heightList);

            ctx.render("bestilling.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("fejlside.html");
        }

    }

}
