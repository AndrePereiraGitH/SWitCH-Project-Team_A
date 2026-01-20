package TOPSECRET.domain;

import java.util.Objects;

public class Author {

    private Profile _authorProfile;
    private String _biography;

    public Author(Profile profile, String biography) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }
        if ( biography == null || biography.isBlank() )
            throw new IllegalArgumentException("Biography cannot be null or blank");

        this._authorProfile = profile;
        this._biography = biography;
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

    @Override
    public int hashCode() {
        return Objects.hash(_authorProfile, _biography);
    }
}