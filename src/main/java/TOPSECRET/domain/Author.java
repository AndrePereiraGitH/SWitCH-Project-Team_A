package TOPSECRET.domain;

import java.time.LocalDate;
import java.util.Objects;
/**
 * Represents an Author, defined by a Profile (name, country, date of birth) and a biography.
 * NOTE: The constructor validates inputs to prevent invalid authors from being created.
 */
public class Author {

    private final Profile _authorProfile;
    private final String _biography;

    public Author(Profile profile, String biography) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }
        if (biography == null || biography.isBlank())
            throw new IllegalArgumentException("Biography cannot be null or blank");

        this._authorProfile = profile;
        this._biography = biography;
    }

    public Author(String name, Country countries, LocalDate dateOfBirth, String bio) {
        this(new Profile(name, countries, dateOfBirth), bio);
    }

    public Profile getProfile() {
        return _authorProfile;
    }

    public String getBiography() {
        return _biography;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this == o) {     // Same reference in memory => definitely equal
            return true;
        }
        Author author = (Author) o;
        return Objects.equals(_authorProfile, author._authorProfile) && Objects.equals(_biography, author._biography);
    }
}