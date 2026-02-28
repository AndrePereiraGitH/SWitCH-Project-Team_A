package TOPSECRET.domain;

import java.time.LocalDate;
import java.util.Objects;
/**
 * Represents an Author in the system.
 * <p>
 * An Author is defined by:
 * <ul>
 *     <li>A {@link Profile} containing identification data (name, country, date of birth)</li>
 *     <li>A textual biography describing the author</li>
 * </ul>
 *
 * <p><b>Invariant:</b> An Author must always have a valid Profile and a non-blank biography.</p>
 *
 * <p>
 * The constructor performs validation to guarantee that no invalid Author
 * instances are created, preserving domain integrity.
 * </p>
 */
public class  Author {

    private final Profile _authorProfile;
    private final String _biography;

    protected Author(Profile profile, String biography) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }
        if (biography == null || biography.isBlank())
            throw new IllegalArgumentException("Biography cannot be null or blank");

        this._authorProfile = profile;
        this._biography = biography;
    }

    protected Author(String name, Country countries, LocalDate dateOfBirth, String bio) {
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