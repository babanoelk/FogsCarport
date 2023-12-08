package app;

import app.config.ThymeleafConfig;
import app.controllers.*;
import app.entities.Order;
import app.persistence.ConnectionPool;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.io.IOException;

public class Main {
    private static final String USER = "notactive";
    private static final String PASSWORD = "notactive";
    private static final String URL = "jdbc:postgresql://localhost:5432/notactive";
    private static final String DB = "notactive";
    private static String API_KEY = System.getenv("SENDGRID_API_KEY");


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
        app.post("/slet", ctx -> OrderController.deleteOrder(ctx,connectionPool));
        app.post("/se-order", ctx -> OrderController.getSpecificOrder(ctx,connectionPool));
        app.post("/opdater-ordre", ctx -> OrderController.updateOrderStatus(ctx, connectionPool));
        app.post("/kontakt", ctx -> OrderController.orderContact(ctx));
        app.post("/besked-indsendt", ctx -> EmailController.sendOrderQuestion(ctx));

    }

}