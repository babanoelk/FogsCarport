package app;

import app.config.ThymeleafConfig;
import app.controllers.CarportController;
import app.controllers.FormController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "dxjYEnBgMQYWC6ZyL6wk";
    private static final String URL = "jdbc:postgresql://164.90.190.168:5432/fogs_carport";
    private static final String DB = "fogs_carport";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args)
    {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");

            JavalinThymeleaf.init(ThymeleafConfig.templateEngine());
        }).start(7070);

        // Routing

        app.get("/", ctx ->  ctx.render("index.html"));
        app.get("/bestil-carport", ctx -> FormController.loadMeasurements(ctx, connectionPool));
        app.post("/ordre-indsendt", ctx -> FormController.formInput(ctx, connectionPool));
    }

}