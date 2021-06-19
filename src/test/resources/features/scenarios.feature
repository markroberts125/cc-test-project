Feature: Using the Payments API

  Scenario: Authenticating with the API
    Given a request to authenticate with the API
    When the request is sent
    Then I receive an OK response
    And I receive a key for future authentication

  Scenario: Get a quote from the API
    Given a request to get a quote from the API
    And I want to sell "10000" "GBP" and buy "USD" using the "sell" side
    When the request is sent
    Then I receive an OK response
    And the buy amount matches the provided rate

  Scenario Outline: Sending invalid requests to the API
    Given a request to get a quote from the API
    And I want to sell "<amount>" "<sell>" and buy "<buy>" using the "<side>" side
    When the request is sent
    Then I receive a Bad Request response
    And the error codes received contains "<error>"

    Examples:
      | amount | sell | buy | side | error                   |
      | 10000  | GBP  | GBP | sell | unique_value_parameters |
      | -10000 | GBP  | USD | sell | amount_type_is_wrong    |
      | -10000 | GBP  | USD | sell | amount_is_too_small     |

  Scenario: Closing the session with the API
    Given a request to close the session with the API
    When the request is sent
    Then I receive an OK response
    And the key I have is no longer valid