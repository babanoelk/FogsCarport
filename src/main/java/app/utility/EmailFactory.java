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

    public static void sendOrderQuestion(String customerName, String orderId, String customerEmail, String message) throws IOException {

        Email from = new Email(companyMail);
        from.setName(customerName);

        Mail mail = new Mail();
        mail.setFrom(from);

        String API_KEY = System.getenv("SENDGRID_API_KEY");

        Personalization personalization = new Personalization();

        // data som skal til sendgrid skabelonen
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

    public static void sendBill(String customerName, String orderId, String price, String employeeName) throws IOException {

        Email from = new Email(companyMail);

        from.setName(employeeName);
        Mail mail = new Mail();
        mail.setFrom(from);

        String API_KEY = System.getenv("SENDGRID_API_KEY");

        Personalization personalization = new Personalization();

        personalization.addTo(new Email("badranyoussef@gmail.com"));
        personalization.addDynamicTemplateData("customerName", customerName);
        personalization.addDynamicTemplateData("orderId", orderId);
        personalization.addDynamicTemplateData("employeeName", employeeName);
        personalization.addDynamicTemplateData("price", price);
        mail.addPersonalization(personalization);

        mail.addCategory("carportapp");

        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

            // indsæt dit skabelonid herunder
            mail.templateId = "d-7bcb5fff6e634676829f5cf87d8d7eac";
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

    public static void sendOrderToSalesTeam(String customerName, String length, String width, String height, int id) throws IOException {

        Email from = new Email(companyMail);

        from.setName("bestillings formular");
        Mail mail = new Mail();
        mail.setFrom(from);

        String API_KEY = System.getenv("SENDGRID_API_KEY");

        Personalization personalization = new Personalization();

        personalization.addTo(new Email("badranyoussef@gmail.com"));
        personalization.addDynamicTemplateData("customerName", customerName);
        personalization.addDynamicTemplateData("orderId", id);
        personalization.addDynamicTemplateData("length", length);
        personalization.addDynamicTemplateData("width", width);
        personalization.addDynamicTemplateData("height", height);
        mail.addPersonalization(personalization);

        mail.addCategory("carportapp");

        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

            // indsæt dit skabelonid herunder
            mail.templateId = "d-395a3ae738494b1e982e394f63fe0ca0";
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

    public static void sendMessageToSalesTeam(String customerName, String customerPhone, String customerEmail, String message) throws IOException {

        Email from = new Email(companyMail);
        from.setName(customerName);

        Mail mail = new Mail();
        mail.setFrom(from);

        String API_KEY = System.getenv("SENDGRID_API_KEY");

        Personalization personalization = new Personalization();

        personalization.addTo(new Email("badranyoussef@gmail.com"));
        personalization.addDynamicTemplateData("name", customerName);
        personalization.addDynamicTemplateData("message", message);
        personalization.addDynamicTemplateData("email", customerEmail);
        personalization.addDynamicTemplateData("phone", customerPhone);

        mail.addPersonalization(personalization);

        mail.addCategory("carportapp");

        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

            // indsæt dit skabelonid herunder
            mail.templateId = "d-cf710838dff94472b1297944af04adff";
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
