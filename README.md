#CC Test Project
**A project to test the CC Rates API built in Java with Springboot.**

##Setup
- Open the project as a Maven project.
- Update the _auth.properties_ file in src/test/resources/request/body with your API key and login ID. **DO NOT COMMIT THIS UPDATED FILE**
- Run the CucumberRunner class with JUnit to execute the tests

##Design
This project uses primarily generic components that have their values stored via properties files. Each type of request has
a number of associated properties files found in src/test/resources/request/
###Properties
All requests have a properties file found in /details. This contains the elements required to send the request to the API
|Value|Example|Usage|
|endpoint  |https://devapi.currencycloud.com|API endpoint|
