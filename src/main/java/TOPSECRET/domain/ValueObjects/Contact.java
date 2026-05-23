package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import jakarta.persistence.Embedded;

import java.util.Objects;

/**
 * Value Object representing the contact information of a user,
 * composed of a phone number, extension, email, and address.
 *
 * <p>
 * A {@code Contact} is considered equal to another if all its fields match.
 * The phone number and extension are mandatory and validated during
 * construction:
 * </p>
 *
 * <ul>
 *     <li>Phone number: must contain only digits and have between
 *     7 and 10 digits</li>
 *
 *     <li>Extension: must contain only digits and have between
 *     1 and 3 digits, following international calling code standards</li>
 * </ul>
 *
 * <p>
 * The email and physical address are optional and may be {@code null}.
 * </p>
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contact implements ValueObject {

    // validation
    private static final String PHONE_REGEX = "^\\d{7,10}$";
    private static final String EXTENSION_REGEX = "^\\d{1,3}$";

    private String _phoneNum;
    private String _extension;

    @Embedded
    private Email _email;

    @Embedded
    private Address _location;


    /**
     * Constructs a new {@code Contact} with the given attributes.
     *
     * @param phoneNum the phone number; must contain only digits and have between 7 and 10 digits
     * @param extension the country/area extension code; must contain only digits and have between 1 and 3 digits
     * @param email the email address; may be {@code null}
     * @param location the physical address; may be {@code null}
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>{@code phoneNum} is {@code null}</li>
     *     <li>{@code phoneNum} does not contain between 7 and 10 digits</li>
     *     <li>{@code extension} is {@code null}</li>
     *     <li>{@code extension} does not contain between 1 and 3 digits</li>
     * </ul>
     */
    // Constructor
    public Contact(String phoneNum, String extension, Email email, Address location) {

        if (phoneNum == null || !phoneNum.matches(PHONE_REGEX)) {
            throw new IllegalArgumentException("Phone number must contain 7 to 10 digits");
        }

        if (extension == null || !extension.matches(EXTENSION_REGEX)) {
            throw new IllegalArgumentException("Extension must contain 1 to 3 digits");
        }

        this._phoneNum = phoneNum;
        this._extension = extension;
        this._email = email;
        this._location = location;
    }

    // Getters
    public String getPhoneNum() {
        return _phoneNum;
    }

    public String getExtension() {
        return _extension;
    }

    public Email getEmail() {
        return _email;
    }

    public Address getAddress() {
        return _location;
    }

    public String getFullPhone() {
        return "+" + _extension + _phoneNum;
    }

    public String getFullContactInfo() {

        return "Full Contact - " +
                "Phone number: " + "+" + _extension + _phoneNum +
                ", e-mail: " + _email +
                ", address: " + _location;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        return Objects.equals(_phoneNum, contact._phoneNum) &&
                Objects.equals(_extension, contact._extension) &&
                Objects.equals(_email, contact._email) &&
                Objects.equals(_location, contact._location);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(_phoneNum, _extension, _email, _location);
    }
}
