# CC Test Project
**A project to test the CC Rates API built in Java with Springboot.**

## Setup
- Open the project via Maven.
- Update the _auth.properties_ file in src/test/resources/request/body with your API key and login ID. **DO NOT COMMIT THIS UPDATED FILE**
- Run the CucumberRunner class with JUnit to execute the tests

## Design
This project uses primarily generic components that have their values stored via properties files. Each type of request has
a number of associated properties files found in src/test/resources/request/

### Properties

#### Details
All requests have a properties file found in /details. This contains the elements required to send the request to the API

Value | Example | Usage
--- | --- | ---
endpoint  | https://devapi.currencycloud.com | API endpoint
resource | /v2/authenticate/api | Resouce of the defined API call
method | POST | Request method type, currently either POST or GET
loggingIn | true | Determines whether the request being sent is logged
loggingOut | false | Determines whether the response received is logged
Content-Type | multipart/form-data | Optional field for POST requests for the content type of that request

For logging the authentication header is always obscured by default, however for any requests or responses that contain sensitive data within the body (specifically the initial auth call) these can be disabled here.

#### Body and Query Parameters
The properties files for the body and query parameters can be found in /body and /queryParams respectively. Both of these are a direct mapping to the key/value pairs used by a request for these parameters. All of the required fields are provided and can be executed immediately, or modified per scenario as required. [See this file for a quote request for an example](https://github.com/markroberts125/cc-test-project/blob/master/src/test/resources/requests/queryParams/quote.properties)

### The Client
The client is a Rest-Assured client, however this client is used in isolation and is only referenced via a client interface. This interface contains one method: sendRequest, which takes in the generic request object and outputs the generic response output. As such the actual client is of little importance and can easily be changed or replaced as required.

### The RequestHandler
The RequestHandler functions as a middleman between the step definitions and the client. Its primary purpose is to handle the authorisation token but is also used to store the request and response for the step definitions to access. This step is necessary to allow the reuse of the retrieved key as part of an auth call, as well as providing a new token for other requests in the case that the auth call has not already been made. Under the current circumstances this is not necessary, however it is important to ensure all scenarios are functionally independant rather than entirely reliant on the auth scenario running first. Finally, this class has an additional method retireToken(). This is used as the destroy method for the RequestHandler spring bean, ensuring that the token is retired at the end of execution regardless of the outcome.

### The Scenarios
Given the number of scenarios, they have all been included in one file, the same is true of the step definitions. Focus has been given to readability in these scenarios, specifically in the initial given line. This could be made generic by feeding in the string value of the associated properties values at the step level, but given the simplicity of the project I decided against this. Most of the steps are fairly straightforward, except perhaps the last one _And the key I have is no longer valid_, which makes an additional call with the stored terminated key and confirms an unauthorised response. This is all hidden within the step definitions to keep the scenario simple, and the end result for the step is the same.

There are several generic steps (Send request, receive response, error codes) already in place. It is possible to create additional generic steps for modifying the query and body parameters by directly calling setBodyValue() and setQueryParameter() directly using values provided in the step but this would require scenario three to have an additional three lines at ultimately very little benefit.

### Extension
Additional tests for the rates API can be added using the steps provided. While there isn't a generic method to access response body values, this can easily be added via directly calling getBodyValue() in the step definition using a value provided in the step. Additional API calls can be added by providing the properties files in the aforementioned place and there are examples in the project for both a GET (quote) and POST (auth) request.

## Final Comments 
This project has been built on a few assumptions. The most notable are as follows:
1. All POST calls will use multipart/form-data. Any other type is not supported.
2. All response bodies will be in the JSON format
3. No headers other than the authorisation and Content-Type headers will be requred.

Additional commentary regarding more complex methods can be found in the code for their relevant classes.
