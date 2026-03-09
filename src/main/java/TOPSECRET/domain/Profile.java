package TOPSECRET.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Holds the personal data of a user: name, country, and date of birth.
 * All three fields are required and cannot change after the profile is created.
 */
public class Profile {

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
        if (o == null || getClass() != o.getClass()) return false;
        if (this == o) return true;
        Profile profile = (Profile) o;
        return Objects.equals(_name, profile._name)
                && Objects.equals(_country, profile._country)
                && Objects.equals(_birthday, profile._birthday);
    }
}