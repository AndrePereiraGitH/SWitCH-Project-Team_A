package TOPSECRET.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents the personal profile of a user.
 *
 * <p>A profile groups the personal data that belongs to a user:
 * their display name, the country they are from, and their date of birth.
 *
 * <p>All three fields are required and cannot be changed after the profile is created.
 * This makes the class immutable — once built, it always holds the same data.
 */
public class Profile {

    // all fields are final — they are set once in the constructor and never change
    private final String _name;        // the display name of the user
    private final Country _country;    // the country the user is from
    private final LocalDate _birthday; // the user's date of birth

    /**
     * Creates a new Profile with the given personal data.
     *
     * <p>All three parameters are required. A profile without a name, country,
     * or birthday is not valid and will not be created.
     *
     * @param name     the display name of the user; must not be null or blank
     * @param country  the country the user is from; must not be null
     * @param birthday the user's date of birth; must not be null
     * @throws IllegalArgumentException if any parameter is null, or if name is blank
     */
    public Profile(String name, Country country, LocalDate birthday) {
        // all three must be present — a profile with missing data makes no sense
        if (name == null || name.isBlank() || country == null || birthday == null) {
            throw new IllegalArgumentException("Profile attributes cannot be null or blank");
        }
        this._name = name;
        this._country = country;
        this._birthday = birthday;
    }

    // ============ Getters ============

    /** Returns the display name of the user. */
    public String getName() {
        return _name;
    }

    /** Returns the country the user is from. */
    public Country getCountry() {
        return _country;
    }

    /** Returns the user's date of birth. */
    public LocalDate getBirthday() {
        return _birthday;
    }

    // ============ equals ============

    /**
     * Two profiles are equal if they have the same name, country, and birthday.
     *
     * <p>By default, Java compares objects by memory address — two different objects
     * with the same data would not be equal. By overriding equals, we make sure
     * that two profiles with the same data are treated as the same profile.
     *
     * <p>{@code Objects.equals} is used for safe null comparison on each field.
     */
    @Override
    public boolean equals(Object o) {
        // null or wrong type — cannot be equal
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        // same object in memory — definitely equal, no need to check fields
        if (this == o) {
            return true;
        }
        Profile profile = (Profile) o;
        return Objects.equals(_name, profile._name)
                && Objects.equals(_country, profile._country)
                && Objects.equals(_birthday, profile._birthday);
    }
}