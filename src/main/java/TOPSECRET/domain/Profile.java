package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Country;

import java.time.LocalDate;

public class Profile {

    // Attributes

    private final String name;
    private final Country country;
    private final LocalDate birthday;

    // Constructor

    public Profile(String name, Country country, LocalDate birthday) {
        if (name == null || name.isBlank() || country == null || birthday == null) {
            throw new IllegalArgumentException("Profile attributes cannot be null or blank");
        }
        this.name = name;
        this.country = country;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public Country getCountry() {
        return country;
    }

    public LocalDate getBirthday() {
        return birthday;
    }
}

