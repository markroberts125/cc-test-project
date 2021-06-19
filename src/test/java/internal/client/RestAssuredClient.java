package internal.client;

import internal.Response;
import internal.request.Request;
import internal.request.RequestMethod;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import static internal.RequestHandler.AUTH_HEADER;
import static io.restassured.RestAssured.given;

public class RestAssuredClient implements Client {

    public static final Logger LOG = LoggerFactory.getLogger(RestAssuredClient.class);

    public RestAssuredClient() {
        RestAssured.config = RestAssuredConfig.newConfig().logConfig(LogConfig.logConfig().blacklistHeader(AUTH_HEADER));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Response sendRequest(Request request) {
        Map requestHeaders = request.getHeaders();
        Map queryParameters = request.getQueryParameters();

        RequestSpecification requestSpec = given().relaxedHTTPSValidation().headers(requestHeaders).queryParams(queryParameters);
        if (request.getRequestMethod()== RequestMethod.POST) {
            requestSpec.contentType("multipart/form-data");
            Set<String> keys = request.getBodyParameters().stringPropertyNames();
            for (String key : keys) {
                requestSpec.multiPart(key, request.getBodyParameters().getProperty(key));
            }
        }

        if (request.isLoggingIn()) {
            LOG.info("Rest Assured Request:");
            requestSpec.log().all();
        }

        io.restassured.response.Response restAssuredResponse = switch (request.getRequestMethod()) {
            case GET -> requestSpec.get(request.getURL());
            case POST -> requestSpec.post(request.getURL());
        };

        if (request.isLoggingOut()) {
            LOG.info("Rest Assured Response:");
            restAssuredResponse.then().log().all();
        }

        return new Response(restAssuredResponse.getStatusCode(),restAssuredResponse.body().asString());
    }
}
