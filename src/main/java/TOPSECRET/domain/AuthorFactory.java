package TOPSECRET.domain;

import java.time.LocalDate;

public class AuthorFactory {
    /**
     * Factory responsible for creating {@link Author} instances.
     * <p>
     * This factory centralizes the creation logic of {@code Author} objects,
     * ensuring minor input normalization before delegating validation
     * to the {@code Author} constructor.
     * <p>
     * Currently, it trims the {@code name} and {@code biography} fields
     * if they are not {@code null}.
     */
    public Author create(String name, Country country, LocalDate dateOfBirth, String biography) {
        return new Author(name == null ? null : name.trim(), country, dateOfBirth, biography == null ? null : biography.trim());
    }
    public Author createWithExistingProfile(Profile profile, String biography) {
        return new Author(profile, biography == null ? null : biography.trim());
    }
}
