package internal;

import internal.client.Client;
import internal.request.Request;

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

    /**
     * Build a request based on the following conditions:
     * 1. If it is an auth request, do not sent an auth token
     * 2. If it is not an auth request and there is no stored auth token, get a token first, then pass that through
     * 3. If it is not an auth request and there is a stored auth token, pass that token through
     * @param requestName String name used by the properties files
     */
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

    /**
     * Pass the stored request to the client and store the resulting response
     * Additionally, if it is an auth request store the resulting auth token for future use
     * If it is a close session request remove the auth token after storing the token as terminated
     */
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

    /**
     * Send a request with a terminated token and store the response.
     */
    public void sendUnauthorisedRequest(){
        request = new Request(CLOSE_SESSION_FILENAME, terminatedToken);
        response = client.sendRequest(request);
    }

    /**
     * Send a request to close a session if there is still an active auth token
     */
    public void retireToken(){
        if(authToken != null) {
            request = new Request(CLOSE_SESSION_FILENAME, authToken);
            sendRequest();
        }
    }

}
