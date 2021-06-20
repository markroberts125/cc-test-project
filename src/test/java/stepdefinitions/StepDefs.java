package stepdefinitions;

import config.CucumberConfig;
import internal.RequestHandler;

import javax.ws.rs.core.Response.Status;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static internal.RequestHandler.*;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.junit.Assert.*;

public class StepDefs extends CucumberConfig {

    @Autowired
    private RequestHandler requestHandler;

    @Given("a request to authenticate with the API")
    public void aRequestToAuthenticateWithTheAPI() {
        requestHandler.buildRequest(AUTH_FILENAME);
    }

    @Given("a request to get a quote from the API")
    public void aRequestToGetAQuoteFromTheAPI() {
        requestHandler.buildRequest("quote");
    }

    @Given("a request to close the session with the API")
    public void aRequestToCloseTheSessionWithTheAPI() {
        requestHandler.buildRequest(CLOSE_SESSION_FILENAME);
    }

    @Given("I want to sell {string} {string} and buy {string} using the {string} side")
    public void iWantToSellAndBuyUsingTheSide(String amount, String sellCurrency, String buyCurrency, String side) {
        requestHandler.getRequest().setQueryParameter("amount", amount);
        requestHandler.getRequest().setQueryParameter("buy_currency", sellCurrency);
        requestHandler.getRequest().setQueryParameter("sell_currency", buyCurrency);
        requestHandler.getRequest().setQueryParameter("fixed_side", side);
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

    @Then("the error codes received contains {string}")
    public void theErrorReceivedIsError(String error) {
        assertTrue(requestHandler.getResponse().getBodyValue("$..code").contains(error));
    }

    @Then("the buy amount matches the provided rate")
    public void theBuyAmountMatchesTheProvidedRate() {
        float buyAmount = Integer.getInteger(requestHandler.getResponse().getBodyValue("client_buy_amount"));
        float sellAmount = Integer.getInteger(requestHandler.getResponse().getBodyValue("client_sell_amount"));
        float rate = Integer.getInteger(requestHandler.getResponse().getBodyValue("core_rate"));
        assertEquals(buyAmount, (sellAmount * rate), 1);
    }
}
