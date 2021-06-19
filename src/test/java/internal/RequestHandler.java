package internal;

import internal.client.Client;
import internal.request.Request;

public class RequestHandler {

    public static final String AUTH_HEADER = "X-Auth-Token";

    Client client;
    String authToken;
    Request request;

    public void buildRequest(String requestName) {
        if (requestName.equals("auth")) {
            request = new Request(requestName);
        } else if (authToken == null) {

        } else {
            request = new Request(requestName, authToken);
        }
    }

    public void sendRequest(){
        client.sendRequest(request);

    }

}
