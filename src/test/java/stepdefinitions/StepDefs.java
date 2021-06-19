package stepdefinitions;

import config.CucumberConfig;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefs extends CucumberConfig {
    
    @Given("a request to authenticate with the API")
    public void a_request_to_authenticate_with_the_API(){
    }

    @When("the request is sent")
    public void theRequestIsSent() {
    }

    @Then("I receive a key for future authentication")
    public void iReceiveAKeyForFutureAuthentication() {
    }
}
