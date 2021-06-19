package internal;

import internal.client.Client;
import internal.request.Request;
import org.springframework.beans.factory.annotation.Autowired;

import static javax.ws.rs.core.Response.Status.OK;

public class RequestHandler {

    public static final String AUTH_TOKEN = "auth_token";
    public static final String AUTH_HEADER = "X-Auth-Token";
    public static final String AUTH_FILENAME = "auth";
    public static final String CLOSE_SESSION_FILENAME = "closeSession";

    private final Client client;
    private String authToken;
    private String terminatedToken;
    private Request request;
    private Response response;

    public RequestHandler(Client client){
        this.client = client;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {return response;}

    public void buildRequest(String requestName) {
        if (requestName.equals(AUTH_FILENAME)) {
            request = new Request(requestName);
        } else if (authToken == null) {
            request = new Request(AUTH_FILENAME);
            sendRequest();
            request = new Request(requestName,authToken);
        } else {
            request = new Request(requestName, authToken);
        }
    }

    public void sendRequest(){
        response = client.sendRequest(request);
        if(request.getName().equals(AUTH_FILENAME) && response.getStatus() == OK){
            authToken = response.getBodyValue(AUTH_TOKEN);
        }
        else if(request.getName().equals(CLOSE_SESSION_FILENAME) && response.getStatus() == OK){
            terminatedToken = authToken;
            authToken = null;
        }
    }

    public void sendUnauthorisedRequest(){
        request = new Request(CLOSE_SESSION_FILENAME, terminatedToken);
        response = client.sendRequest(request);
    }

    public void retireToken(){
        if(authToken != null)
        request = new Request(CLOSE_SESSION_FILENAME, authToken);
        sendRequest();
    }

}
