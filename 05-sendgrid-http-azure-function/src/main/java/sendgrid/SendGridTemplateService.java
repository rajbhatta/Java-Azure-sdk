package sendgrid;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import model.User;

import java.io.IOException;

public class SendGridTemplateService implements EmailService {

    @Override
    public void sendEmailWithTemplateId(User user) throws IOException {
        Mail mail = new Mail();
        mail.setFrom(new Email("bhatt11y@uwindsor.ca"));
        mail.setTemplateId("7506d8f6535e4efd919b67cc9c424465 ");

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("firstName", user.getFirstName());
        personalization.addDynamicTemplateData("lastName", user.getLastName());
        personalization.addTo(new Email("gieltsraj@gmail.com"));
        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}
