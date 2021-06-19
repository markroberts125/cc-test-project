package stepdefinitions;

import config.CucumberConfig;
import internal.RequestHandler;

import javax.ws.rs.core.Response.Status;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static internal.RequestHandler.AUTH_TOKEN;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StepDefs extends CucumberConfig {

    @Autowired
    private RequestHandler requestHandler;

    @Given("a request to authenticate with the API")
    public void aRequestToAuthenticateWithTheAPI() {
        requestHandler.buildRequest("auth");
    }

    @Given("a request to close the session with the API")
    public void aRequestToCloseTheSessionWithTheAPI() {
        requestHandler.buildRequest("closeSession");
    }

    @When("the request is sent")
    public void theRequestIsSent() {
        requestHandler.sendRequest();
    }

    @Then("I receive a(n) {status} response/error")
    public void iReceiveAResponse(Status status) {
        assertEquals(status, requestHandler.getResponse().getStatus());
    }

    @Then("I receive a key for future authentication")
    public void iReceiveAKeyForFutureAuthentication() {
        assertNotNull(requestHandler.getResponse().getBodyValue(AUTH_TOKEN));
    }

    @Then("the key I have is no longer valid")
    public void theKeyIHaveIsNoLongerValid() {
        requestHandler.sendUnauthorisedRequest();
        assertEquals(UNAUTHORIZED, requestHandler.getResponse().getStatus());
    }
}
