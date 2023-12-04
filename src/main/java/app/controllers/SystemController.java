package app.controllers;

import app.entities.User;
import io.javalin.http.Context;

public class SystemController {
    public static void load(Context ctx) {
        User user = null;
        ctx.sessionAttribute("loggedinUser", user);
        ctx.render("index.html");
    }
}
