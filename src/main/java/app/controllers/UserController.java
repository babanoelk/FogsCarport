package app.controllers;

import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;

public class UserController {
    public static User createUser(Context ctx) {
        String name = ctx.formParam("name");
        String address = ctx.formParam("address");
        int zip = Integer.parseInt(ctx.formParam("zip"));
        int mobile = Integer.parseInt(ctx.formParam("phone"));
        String email = ctx.formParam("email");
        String password = (ctx.formParam("pass"));
        boolean consent = Boolean.parseBoolean(ctx.formParam("consent"));
        int role = 1; //Standard role 'Kunde'

        //Create User instance from input data
        User user = new User(name, email, password, address, mobile, zip, consent, role);
        return user;
    }

    public static void login(Context ctx, ConnectionPool connectionPool){
        try {
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");
            //checking if email and password exists
            boolean isLoggedIn = UserMapper.loginValidator(email, password, connectionPool);
            if (isLoggedIn) {
                User user = UserMapper.getUserByEmail(email, connectionPool);
                ctx.sessionAttribute("currentUser", user);
                ctx.render("dashboard.html");
            } else {
                ctx.attribute("message", "Forkert email eller password");
                ctx.render("login.html");
            }
        } catch (Exception e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("login.html");
        }
    }


    public static void home(Context ctx){
        ctx.sessionAttribute("currentUser");
        ctx.render("index.html");
    }


    public static void dashboardMenu(Context ctx){
        ctx.sessionAttribute("currentUser");
        ctx.render("dashboard.html");
    }

    public static void logout(Context ctx){
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    public static void addAdminUser(Context ctx, ConnectionPool connectionPool) {
        try {
            String name = ctx.formParam("name");
            String address = ctx.formParam("address");
            int zip = Integer.parseInt(ctx.formParam("zip"));
            int phone = Integer.parseInt(ctx.formParam("phone"));
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");
            boolean consent = Boolean.parseBoolean(ctx.formParam("consent"));
            int role = 2; //Sælger rolle

            User user = new User(name, email, password, address, phone, zip, consent, role);

            User userAdded = UserMapper.addUser(user, connectionPool);
            String message = userAdded.getName() + " er nu tilfjøet til systemet som Sælger";
            ctx.attribute("message", message);
            ctx.render("dashboard.html");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
