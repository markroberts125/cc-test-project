package internal.client;

import internal.Response;
import internal.request.Request;

public interface Client {

    public Response sendRequest(Request request);
}
