## Create Sendgrid template ##
#### Find dynamic template option ####
<img src="img/img-1.png"/>

#### Once name for dynamic template is provided click on add version ####
<img src="img/img-2.png"/>

#### Either select existing template or create your new one ####
<img src="img/img-3.png"/>

#### Get template ID from created template ####
<img src="img/img-4.png"/>

## Source Code ## 
```java
package trigger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import model.User;
import sendgrid.EmailService;
import sendgrid.SendGridTemplateService;

import java.io.IOException;
import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class HttpTriggerFunction {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("sendgridtemplate")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) throws JsonProcessingException {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String userString=request.getBody().get();
       User user= new ObjectMapper().readValue(userString,User.class);

        if (user == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {

            try {
                provideTemplateService().sendEmailWithTemplateId(user);
            } catch (IOException e) {
                context.getLogger().severe("IOException occured: \t"+e.getCause());
            }

            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + user.getFirstName()).build();
        }
    }

    private EmailService provideTemplateService(){
        return new SendGridTemplateService();
    }
}

```

```java
package sendgrid;

import model.User;

import java.io.IOException;

public interface EmailService {
    void sendEmailWithTemplateId(User user) throws IOException;
}

```

```java
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

```

```java
package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

```
