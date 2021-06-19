package internal;

import internal.client.Client;
import internal.request.Request;
import org.springframework.beans.factory.annotation.Autowired;

public class RequestHandler {

    public static final String AUTH_HEADER = "X-Auth-Token";

    Client client;
    String authToken;
    Request request;
    Response response;

    public RequestHandler(Client client){
        this.client = client;
    }

    public Request getRequest() {
        return request;
    }

    public void buildRequest(String requestName) {
        if (requestName.equals("auth")) {
            request = new Request(requestName);
        } else if (authToken == null) {
            request = new Request("auth");
            sendRequest();
            request = new Request(requestName,authToken);
        } else {
            request = new Request(requestName, authToken);
        }
    }

    public void sendRequest(){
        response = client.sendRequest(request);
        if(request.getName().equals("auth")){
            authToken = response.getBodyValue("auth_token");
        }
        else if(request.getName().equals("closeSession")){
            authToken = null;
        }
    }

    public void retireToken(){
        if(authToken != null)
        request = new Request("closeSession", authToken);
        sendRequest();
    }

}
