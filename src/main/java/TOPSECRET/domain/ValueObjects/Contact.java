package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.ValueObject;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * Value Object representing the contact information of a user,
 * composed of a phone number, extension, email, and address.
 * <p>
 * A {@code Contact} is considered equal to another if all its fields match.
 * The only required field at construction time is the extension — all other
 * fields are optional and may be {@code null}.
 * </p>
 */
@Embeddable
public class Contact implements ValueObject {

    private String _phoneNum;
    private String _extension;

    @Embedded
    private Email _email;

    @Embedded
    private Address _location;

    /**
     * Required by JPA
     */
    protected Contact() {}

    /**
     * Constructs a new {@code Contact} with the given attributes.
     *
     * @param phoneNum  the phone number; may be {@code null}
     * @param extension the country/area extension code; must not be {@code null}
     * @param email     the email address; may be {@code null}
     * @param location  the physical address; may be {@code null}
     * @throws IllegalArgumentException if {@code extension} is {@code null}
     */
    // Constructor
    public Contact(String phoneNum, String extension, Email email, Address location) {

        if (extension == null) {
            throw new IllegalArgumentException("Extension can´t be null");
        }

        this._phoneNum = phoneNum;
        this._extension = extension;
        this._email = email;
        this._location = location;
    }


    // Getters (não sei se necessário)
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

    // Method: Get full phone number (extension + number)
    public String getFullPhone() {

        return _extension + " " + _phoneNum;
    }

    // Method: Get full contact information
    public String getFullContactInfo() {

        return "Full Contact - " +
                "Phone number: " + _extension + " " + _phoneNum +
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
