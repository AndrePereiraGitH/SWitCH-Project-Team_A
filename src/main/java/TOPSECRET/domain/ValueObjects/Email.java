package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Represents an email address split into a username and a domain.
 * This class validates both parts before creating an Email object.
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email implements ValueObject {

    // The part before the '@' symbol (e.g. "john.doe")
    private String userName;
    // The part after the '@' symbol (e.g. "example.com")
    private String domain;

    /*
     * Regular expression that defines which characters are allowed
     * in an email username.
     * Allows letters, digits, and common email-safe symbols.
     */
    // Regex for validating username and domain
    private static final String USERNAME_REGEX = "^[A-Za-z0-9._%+-]+$";
    /*
     * Regular expression for validating a domain name.
     * - Must contain at least one dot
     * - Ends with a 2–6 letter top-level domain (e.g. .com, .net)
     */
    private static final String DOMAIN_REGEX = "^[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    // Precompiled regex patterns for better performance
    private static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);
    private static final Pattern DOMAIN_PATTERN = Pattern.compile(DOMAIN_REGEX);

    /**
     * Creates a validated Email object.
     * Throws an exception if the username or domain is invalid.
     */
    public Email(String userName, String domain) {

        // Validate username before assigning it

        if (!isValidUserName(userName)) {
            throw new IllegalArgumentException("Invalid username: " + userName);
        }

        // Validate domain before assigning it
        if (!isValidDomain(domain)) {
            throw new IllegalArgumentException("Invalid domain: " + domain);
        }

        // Assign values only after validation passes
        this.userName = userName;
        this.domain = domain;
    }

    /**
     * Checks if the username is:
     * - Not null
     * - Not empty
     * - Matches the username regex
     */
    private boolean isValidUserName(String userName) {
        return userName != null && !userName.isEmpty() && USERNAME_PATTERN.matcher(userName).matches();
    }

    /**
     * Checks if the domain is:
     * - Not null
     * - Not empty
     * - Matches the domain regex
     */
    private boolean isValidDomain(String domain) {
        return domain != null && !domain.isEmpty() && DOMAIN_PATTERN.matcher(domain).matches();
    }

    /**
     * Returns the full email address in standard format:
     * username@domain
     */
    public String getEmail() {
        return userName + "@" + domain;
    }


    // Getter for the username portion of the email
    public String getUserName() {
        return userName;
    }

    // Getter for the domain portion of the email

    public String getDomain() {
        return domain;
    }
}
