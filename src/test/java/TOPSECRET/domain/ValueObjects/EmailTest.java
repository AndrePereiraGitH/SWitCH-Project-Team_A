package TOPSECRET.domain.ValueObjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

/**
 * Test class responsible for validating the behavior of the Email domain object.
 *
 * These tests ensure that:
 * - Valid usernames and domains produce a correct email address
 * - Invalid inputs result in appropriate exceptions
 */
class EmailTest {

    /**
     * Parameterized test that verifies valid combinations of
     * usernames and domains produce the expected email address.
     *
     * Each row in the CsvSource represents one valid test case.
     */
    @ParameterizedTest(name = "{index} => userName={0}, domain={1}, expectedEmail={2}")
    @CsvSource({
            "john.doe, example.com, john.doe@example.com",
            "alice123, gmail.com, alice123@gmail.com",
            "user_name, my-domain.org, user_name@my-domain.org"
    })
    void testValidEmails(String userName, String domain, String expectedEmail) {

        // Create an Email object using valid input values
        Email email = new Email(userName, domain);

        // Verify that the generated email matches the expected format
        assertEquals(expectedEmail, email.getEmail());
    }

    /**
     * Parameterized test that verifies invalid usernames or domains
     * cause the Email constructor to throw an IllegalArgumentException.
     */
    @ParameterizedTest
    @CsvSource({
            "invalid*name, example.com",
            "john, invalid_domain",
            "user!, gmail.com"
    })
    void testInvalidEmails(String userName, String domain) {

        // Verify that creating an Email with invalid input throws an exception
        assertThrows(IllegalArgumentException.class, () -> new Email(userName, domain));
    }

    /**
     * Parameterized test that verifies null or empty usernames
     * are rejected by the Email constructor.
     *
     * @NullAndEmptySource automatically provides:
     * - null
     * - empty string ("")
     */
    @ParameterizedTest
    @NullAndEmptySource
    void testNullOrEmptyUserName(String userName) {

        // Attempting to create an Email with an invalid username must fail
        assertThrows(IllegalArgumentException.class, () -> new Email(userName, "example.com"));
    }

    /**
     * Parameterized test that verifies null or empty domains
     * are rejected by the Email constructor.
     */
    @ParameterizedTest
    @NullAndEmptySource
    void testNullOrEmptyDomain(String domain) {

        // Attempting to create an Email with an invalid domain must fail
        assertThrows(IllegalArgumentException.class, () -> new Email("john", domain));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldReturnCorrectUserName(String userName) {
        Email email = new Email("john", "gmail.com");
        assertEquals("john", email.getUserName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldReturnCorrectDomain(String domain) {
        Email email = new Email("john", "gmail.com");
        assertEquals("gmail.com", email.getDomain());
    }
}
