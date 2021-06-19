package internal;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.core.Response.Status;

public class Response {

    public static final Logger LOG = LoggerFactory.getLogger(Response.class);

    private final DocumentContext context;
    private final Status status;

    public Response(int statusCode, String body){
        context = JsonPath.parse(body);
        this.status = Status.fromStatusCode(statusCode);
    }

    public Status getStatus(){
        return status;
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
