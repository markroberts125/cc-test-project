package internal;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import internal.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Response {

    public static final Logger LOG = LoggerFactory.getLogger(Response.class);

    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private DocumentContext context;

    public Response(int status, String body){
        context = JsonPath.parse(body);
    }

    public String getBodyValue(String key){
        try{
            return context.read(key).toString();
        }
        catch (PathNotFoundException e){
            LOG.info("Exception evaluating JsonPath expression - {}", key);
            return null;
        }
    }
}
