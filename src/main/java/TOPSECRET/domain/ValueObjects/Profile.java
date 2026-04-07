package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.ValueObject;
import TOPSECRET.domain.Country.Country;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Value Object representing the personal profile of a user,
 * composed of a name, country, and date of birth.
 * <p>
 * A {@code Profile} is immutable — all fields are set at construction time
 * and cannot be changed. Two profiles are considered equal if all their
 * fields have the same values.
 * </p>
 */
public class Profile implements ValueObject{

    private final String _name;
    private final Contact _contact;
    private final LocalDate _birthday;

    /**
     * Creates a Profile with the given data.
     *
     * @param name     the user's name; must not be null or blank
     * @param contact  the user's contact; must not be null
     * @param birthday the user's date of birth; must not be null
     * @throws IllegalArgumentException if any field is null, or if name is blank
     */
    public Profile(String name, Contact contact, LocalDate birthday) {
        if (name == null || name.isBlank() || contact == null || birthday == null) {
            throw new IllegalArgumentException("Profile attributes cannot be null or blank");
        }
        this._name = name;
        this._contact = contact;
        this._birthday = birthday;
    }

    public String getName() {
        return _name;
    }

    public Contact getContact() {
        return _contact;
    }

    public LocalDate getBirthday() {
        return _birthday;
    }

    /**
     * Two profiles are equal if they have the same name, country, and birthday.
     * Overridden because Java's default compares memory addresses, not field values.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(_name, profile._name)
                && Objects.equals(_contact, profile._contact)
                && Objects.equals(_birthday, profile._birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, _contact, _birthday);
    }
}