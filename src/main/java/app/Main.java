package app;

import app.config.ThymeleafConfig;
import app.controllers.FormController;
import app.controllers.OrderController;
import app.controllers.SystemController;
import app.controllers.UserController;
import app.controllers.*;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {
    private static final String USER = "notactive";
    private static final String PASSWORD = "notactive";
    private static final String URL = "jdbc:postgresql://localhost:5432/notactive";
    private static final String DB = "notactive";
    private static String API_KEY = System.getenv("SENDGRID_API_KEY");
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");

            JavalinThymeleaf.init(ThymeleafConfig.templateEngine());
        }).start(7070);

        // Routing get
        app.get("/", ctx -> SystemController.load(ctx));
        app.get("/bestil-carport", ctx -> FormController.loadMeasurements(ctx, connectionPool));
        app.get("/login", ctx -> ctx.render("login.html"));
        app.get("/ordre-side", ctx -> OrderController.getAllOrders(ctx, connectionPool));
        app.get("/lagervare", ctx -> MaterialController.loadMaterials(ctx, connectionPool));

        // Routing post
        app.post("/ordre-indsendt", ctx -> FormController.formInput(ctx, connectionPool));
        app.post("/dashboard", ctx -> UserController.login(ctx, connectionPool));
        app.post("/delete", ctx -> OrderController.deleteOrder(ctx, connectionPool));
        app.post("/se-order", ctx -> OrderController.getSpecificOrder(ctx, connectionPool));
        app.post("/slet", ctx -> OrderController.deleteOrder(ctx,connectionPool));
        app.post("/opdater-ordre", ctx -> OrderController.updateOrderStatus(ctx, connectionPool));
        app.post("/kontakt", ctx -> OrderController.orderContact(ctx));
        app.post("/besked-indsendt", ctx -> EmailController.sendOrderQuestion(ctx));
        app.post("/name-chang", ctx -> OrderController.updateOrderUser(ctx, connectionPool));
        app.post("/gem-bruger-oplysninger", ctx -> OrderController.updateOrderUser(ctx, connectionPool));
        app.post("/gem-carport-oplysninger", ctx -> OrderController.updateCarport(ctx, connectionPool));
        app.post("/gem-skur-oplysninger", ctx -> OrderController.updateShed(ctx, connectionPool));
        app.post("/tilfoej-skur", ctx -> OrderController.addShed(ctx, connectionPool));
        app.post("/send-regning", ctx -> OrderController.sendBill(ctx, connectionPool));
        app.get("/ret-i-varer", ctx -> MaterialController.loadMaterials(ctx,connectionPool));
    }

}