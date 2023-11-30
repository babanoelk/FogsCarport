package app.controllers;

import io.javalin.http.Context;

public class FormularController {

    public static void formularInput(Context ctx) {

        //Carport width & length
        int carportWidth = Integer.parseInt(ctx.formParam("carport_bredde"));
        int carportLength = Integer.parseInt(ctx.formParam("carport_længde"));


        //Shed width & length
        int shedWidth = Integer.parseInt(ctx.formParam("Redskabsrum_bredde"));
        int shedLength = Integer.parseInt(ctx.formParam("Redskabsrum_længde"));


        //User information
        String name = (ctx.formParams("fullname")).toString();
        String address = (ctx.formParams("address")).toString();
        int postalCode = Integer.parseInt(ctx.formParam("postnumber"));
        int mobile = Integer.parseInt(ctx.formParam("tlfnumber"));
        String eMail = (ctx.formParams("email")).toString();
        String password = (ctx.formParams("email")).toString();
        boolean permission = Boolean.parseBoolean((ctx.formParams("permission")).toString());

    }

}
