package app;

import app.config.ThymeleafConfig;
import app.controllers.FormController;
import app.controllers.OrderController;
import app.controllers.SystemController;
import app.controllers.UserController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {
    private static final String USER = "notactive";
    private static final String PASSWORD = "notactive";
    private static final String URL = "jdbc:postgresql://localhost:5432/notactive";
    private static final String DB = "notactive";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args)
    {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");

            JavalinThymeleaf.init(ThymeleafConfig.templateEngine());
        }).start(7070);

        // Routing

        //app.get("/", ctx ->  ctx.render("index.html"));
        app.get("/", ctx -> SystemController.load(ctx));
        app.get("/bestil-carport", ctx -> FormController.loadMeasurements(ctx, connectionPool));
        app.post("/ordre-indsendt", ctx -> FormController.formInput(ctx, connectionPool));
        app.get("/login", ctx ->  ctx.render("login.html"));
        app.post("/min-side", ctx -> UserController.login(ctx,connectionPool));
        app.get("/min-side", ctx -> OrderController.getAllOrders(ctx,connectionPool));
        app.post("/delete", ctx -> OrderController.deleteOrder(ctx,connectionPool));
        app.post("/se-order", ctx -> OrderController.getSpecificOrder(ctx,connectionPool));
        //app.get("/delete", ctx -> OrderController.deleteOrder(ctx,connectionPool));
        app.post("/opdater-ordre", ctx -> OrderController.updateOrderStatus(ctx, connectionPool));

    }

}