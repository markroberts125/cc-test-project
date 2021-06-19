Feature: Using the Payments API

  Scenario: Authenticating with the API
    Given a request to authenticate with the API
    When the request is sent
    Then I receive a key for future authentication

  Scenario: Closing the session with the API
    Given a request to close the session with the API
    When the request is sent
    Then I receive confirmation the session has been closed