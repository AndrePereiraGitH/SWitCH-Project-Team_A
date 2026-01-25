package TOPSECRET.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Profile {

    // Attributes

    private final String _name;
    private final Country _country;
    private final LocalDate _birthday;

    // Constructor

    public Profile(String name, Country country, LocalDate birthday) {
        if (name == null || name.isBlank() || country == null || birthday == null) {
            throw new IllegalArgumentException("Profile attributes cannot be null or blank");
        }
        this._name = name;
        this._country = country;
        this._birthday = birthday;
    }

    public String getName() {
        return _name;
    }

    public Country getCountry() {
        return _country;
    }

    public LocalDate getBirthday() {
        return _birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this == o) {  // Same reference in memory => definitely equal
            return true;
        }
        Profile profile = (Profile) o;
        return Objects.equals(_name, profile._name)
                && Objects.equals(_country, profile._country)
                && Objects.equals(_birthday, profile._birthday);
    }
}