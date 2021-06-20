package internal.client;

import internal.Response;
import internal.request.Request;

public interface Client {

    Response sendRequest(Request request);
}
