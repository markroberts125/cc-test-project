package config;

import io.cucumber.java.ParameterType;
import javax.ws.rs.core.Response.Status;

public class ParameterTypes {
    @ParameterType(".*")
    public Status status(String statusString) {
        return Status.valueOf(statusString.toUpperCase().replace(" ", "_"));
    }
}
