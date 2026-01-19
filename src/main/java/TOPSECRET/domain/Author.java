package TOPSECRET.domain;

public class Author {

    private Profile profile;

    public Author(Profile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }
}