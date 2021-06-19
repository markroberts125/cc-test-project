Feature: Using the Payments API

  Scenario: Authenticating with the API
    Given a request to authenticate with the API
    When the request is sent
    Then I receive a key for future authentication