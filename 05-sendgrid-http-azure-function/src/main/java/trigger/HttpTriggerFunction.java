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
