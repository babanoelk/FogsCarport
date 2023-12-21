package app.controllers;

import app.entities.User;
import io.javalin.http.Context;

public class SystemController {
    //Used to set a session User when site is loaded
    public static void load(Context ctx) {
        User user = null;
        ctx.sessionAttribute("currentUser", user);
        ctx.render("index.html");
    }
}
