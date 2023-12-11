package app.utility;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Personalization;

import java.io.IOException;

public class EmailFactory {

    private static String companyMail = "badranyoussef@gmail.com";

    //Der kunne evt. tilføjes ordre nr også...
    public static void sendOrderQuestion(String customerName, String orderId, String customerEmail, String message) throws IOException {

        // Erstat xyx@gmail.com med din egen email, som er afsenderen
        Email from = new Email(companyMail);
        from.setName(customerName);

        Mail mail = new Mail();
        mail.setFrom(from);

        String API_KEY = System.getenv("SENDGRID_API_KEY");

        Personalization personalization = new Personalization();

        // Erstat kunde@gmail.com, name, email og zip med egne værdier
        // I test-fasen - brug din egen email, så du kan modtage beskeden

        personalization.addTo(new Email("badranyoussef@gmail.com"));
        personalization.addDynamicTemplateData("name", customerName);
        personalization.addDynamicTemplateData("orderId", orderId);
        personalization.addDynamicTemplateData("email", customerEmail);
        personalization.addDynamicTemplateData("besked", message);
        mail.addPersonalization(personalization);

        mail.addCategory("carportapp");

        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

            // indsæt dit skabelonid herunder
            mail.templateId = "d-59e0df7527fa4bc0a3026bde7de25ace";
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            System.out.println("Error sending mail");
            throw ex;
        }
    }
}
