package internal.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static internal.RequestHandler.AUTH_HEADER;


public class Request {

    public static final Logger LOG = LoggerFactory.getLogger(Request.class);

    private static final String REQUEST_PATH = "src/test/resources/requests/";
    private static final String DETAILS_PATH = "details/";
    private static final String QUERY_PATH = "queryParams/";
    private static final String BODY_PATH = "body/";

    private final String name;
    private String endpoint;
    private String resource;
    private RequestMethod requestMethod;
    private Properties headers = new Properties();
    private Properties queryParameters;
    private Properties bodyParameters;
    private boolean loggingIn;
    private boolean loggingOut;

    public Request(String name){
        this.name = name;
        buildRequest(name);
    }

    public Request(String name, String authKey){
        this.name = name;
        headers.put(AUTH_HEADER, authKey);
        buildRequest(name);
    }

    public String getName(){
        return name;
    }

    public String getURL() {
        return endpoint + resource;
    }

    public RequestMethod getRequestMethod(){
        return requestMethod;
    }

    public Properties getHeaders(){
            return headers;
    }

    public Properties getQueryParameters(){
        if(queryParameters != null) {
            return queryParameters;
        }
        return new Properties();
    }

    public Properties getBodyParameters(){
        if(bodyParameters != null) {
            return bodyParameters;
        }
        return new Properties();
    }

    public boolean isLoggingIn() {
        return loggingIn;
    }

    public boolean isLoggingOut() {
        return loggingOut;
    }

    public void setBodyValue(String key, String value){
        bodyParameters.setProperty(key, value);
    }

    public void setQueryParameter(String key, String value){
        queryParameters.setProperty(key, value);
    }

    private void buildRequest(String name){
        Properties details = getProperties(String.join("",REQUEST_PATH,DETAILS_PATH,name,".properties"));
        endpoint = details.getProperty("endpoint");
        resource = details.getProperty("resource");
        loggingIn = Boolean.parseBoolean(details.getProperty("loggingIn"));
        loggingOut = Boolean.parseBoolean(details.getProperty("loggingOut"));
        requestMethod = RequestMethod.valueOf(details.getProperty("method"));
        if(details.containsKey("Content-Type")){
            headers.put("Content-Type", details.getProperty("Content-Type"));
        }
        if(requestMethod == RequestMethod.GET){
            queryParameters = getProperties(String.join("",REQUEST_PATH,QUERY_PATH,name,".properties"));
        }
        if(requestMethod == RequestMethod.POST){
            bodyParameters = getProperties(String.join("",REQUEST_PATH,BODY_PATH,name,".properties"));
        }
    };

    private Properties getProperties(String fileName){
        Properties properties = new Properties();
        File propertiesFile = new File(fileName);
        if(propertiesFile.isFile()){
            try (FileInputStream fis = new FileInputStream(propertiesFile)){
                properties.load(fis);
            }
            catch (IOException e){
                LOG.error("Exception reading properties file - {}", fileName, e);
            }
        }
        return properties;
    }

}
