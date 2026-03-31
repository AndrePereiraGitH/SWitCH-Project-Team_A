package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.ValueObject;

/**
 * Value Object representing the contact information of a user,
 * composed of a phone number, extension, email, and address.
 * <p>
 * A {@code Contact} is considered equal to another if all its fields match.
 * The only required field at construction time is the extension — all other
 * fields are optional and may be {@code null}.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     Contact contact = new Contact("987654321", "351", email, address);
 * </pre>
 */
public class Contact implements ValueObject {

    private String _phoneNum;
    private String _extension;
    private Email _email;
    private Address _location;


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

    // Empty Constructor for framework compatibility
    public Contact () {

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

    /**
     * Indicates whether some other object is equal to this {@code Contact}.
     * <p>
     * Two {@code Contact} instances are considered equal if all their fields
     * (phone number, extension, email, and address) are equal.
     * Supports {@code null} values in any field.
     * </p>
     *
     * @param obj the object to compare with
     * @return {@code true} if the given object is a {@code Contact} with the same fields;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Contact)) return false;

        Contact other = (Contact) obj;

        if (_phoneNum == null) {
            if (other._phoneNum != null) return false;
        } else if (!_phoneNum.equals(other._phoneNum)) return false;

        if (_extension == null) {
            if (other._extension != null) return false;
        } else if (!_extension.equals(other._extension)) return false;

        if (_email == null) {
            if (other._email != null) return false;
        } else if (!_email.equals(other._email)) return false;

        if (_location == null) {
            return other._location == null;
        } else return _location.equals(other._location);
    }

    /**
     * Generates a hash code based on all Contact attributes.
     *
     * This implementation follows the standard Java pattern for hashCode:
     *
     * - Starts with a non-zero prime number (17) as an initial value.
     * - For each attribute, multiplies the current result by 31
     *   (another prime number) and adds the attribute's hash code.
     *
     * Why 17 and 31?
     * - 17 is an arbitrary non-zero prime used as a starting seed.
     * - 31 is commonly used because:
     *   • It is a prime number (reduces hash collisions)
     *   • It produces good distribution
     *   • It is computationally efficient in Java
     *
     * The formula:
     *
     *     result = 31 * result + fieldHash
     *
     * Combines all attributes into a single integer value.
     *
     * Important rule:
     * If two Contact objects are equal according to equals(),
     * they MUST return the same hashCode.
     *
     * @return integer hash code representing this Contact.
     */
    @Override
    public int hashCode() {
        int result = 17;

        result = 31 * result + (_phoneNum != null ? _phoneNum.hashCode() : 0);
        result = 31 * result + (_extension != null ? _extension.hashCode() : 0);
        result = 31 * result + (_email != null ? _email.hashCode() : 0);
        result = 31 * result + (_location != null ? _location.hashCode() : 0);

        return result;
    }
}
