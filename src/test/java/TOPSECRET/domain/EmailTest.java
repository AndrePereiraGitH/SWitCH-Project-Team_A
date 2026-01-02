package TOPSECRET.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @ParameterizedTest(name = "{index} => userName={0}, domain={1}, expectedEmail={2}")
    @CsvSource({
            "john.doe, example.com, john.doe@example.com",
            "alice123, gmail.com, alice123@gmail.com",
            "user_name, my-domain.org, user_name@my-domain.org"
    })
    void testValidEmails(String userName, String domain, String expectedEmail) {
        Email email = new Email(userName, domain);
        assertEquals(expectedEmail, email.getEmail());
    }

    @ParameterizedTest
    @CsvSource({
            "invalid*name, example.com",
            "john, invalid_domain",
            "user!, gmail.com"
    })
    void testInvalidEmails(String userName, String domain) {
        assertThrows(IllegalArgumentException.class, () -> new Email(userName, domain));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testNullOrEmptyUserName(String userName) {
        assertThrows(IllegalArgumentException.class, () -> new Email(userName, "example.com"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testNullOrEmptyDomain(String domain) {
        assertThrows(IllegalArgumentException.class, () -> new Email("john", domain));
    }
}
