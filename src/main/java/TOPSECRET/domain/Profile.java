package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;

import java.time.LocalDate;

public class Profile {

    // Attributes

    private final String name;
    private final Countries country;
    private final LocalDate birthday;

    // Constructor

    public Profile(String name, Countries country, LocalDate birthday) {
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

    public Countries getCountry() {
        return country;
    }

    public LocalDate getBirthday() {
        return birthday;
    }
}

