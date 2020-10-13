package sendgrid;

import model.User;

import java.io.IOException;

public interface EmailService {
    void sendEmailWithTemplateId(User user) throws IOException;
}
