package TOPSECRET.domain;

import java.util.regex.Pattern;

public class Email {

    private String userName;
    private String domain;

    // Regex for validating username and domain
    private static final String USERNAME_REGEX = "^[A-Za-z0-9._%+-]+$";
    private static final String DOMAIN_REGEX = "^[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    private static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);
    private static final Pattern DOMAIN_PATTERN = Pattern.compile(DOMAIN_REGEX);

    public Email(String userName, String domain) {
        if (!isValidUserName(userName)) {
            throw new IllegalArgumentException("Invalid username: " + userName);
        }
        if (!isValidDomain(domain)) {
            throw new IllegalArgumentException("Invalid domain: " + domain);
        }
        this.userName = userName;
        this.domain = domain;
    }

    private boolean isValidUserName(String userName) {
        return userName != null && !userName.isEmpty() && USERNAME_PATTERN.matcher(userName).matches();
    }

    private boolean isValidDomain(String domain) {
        return domain != null && !domain.isEmpty() && DOMAIN_PATTERN.matcher(domain).matches();
    }

    public String getEmail() {
        return userName + "@" + domain;
    }

    // Getters if needed
    public String getUserName() {
        return userName;
    }

    public String getDomain() {
        return domain;
    }
}
