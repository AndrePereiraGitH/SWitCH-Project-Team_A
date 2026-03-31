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
    private final Country _country;
    private final LocalDate _birthday;

    /**
     * Creates a Profile with the given data.
     *
     * @param name     the user's name; must not be null or blank
     * @param country  the user's country; must not be null
     * @param birthday the user's date of birth; must not be null
     * @throws IllegalArgumentException if any field is null, or if name is blank
     */
    public Profile(String name, Country country, LocalDate birthday) {
        if (name == null || name.isBlank() || country == null || birthday == null) {
            throw new IllegalArgumentException("Profile attributes cannot be null or blank");
        }
        this._name = name;
        this._country = country;
        this._birthday = birthday;
    }

    /** Returns the user's name. */
    public String getName() {
        return _name;
    }

    /** Returns the user's country. */
    public Country getCountry() {
        return _country;
    }

    /** Returns the user's date of birth. */
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
                && Objects.equals(_country, profile._country)
                && Objects.equals(_birthday, profile._birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, _country, _birthday);
    }
}