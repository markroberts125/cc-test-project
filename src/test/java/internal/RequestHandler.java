package internal;

import internal.request.Request;

public class RequestHandler {

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

}
