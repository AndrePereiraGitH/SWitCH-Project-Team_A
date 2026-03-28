package TOPSECRET.domain.Author;

import TOPSECRET.domain.Country;

import java.time.LocalDate;

public class AuthorFactory {
    /**
     * Factory responsible for creating {@link Author} instances.
     * <p>
     * This factory centralizes the creation logic of {@code Author} objects,
     * ensuring minor input normalization before delegating validation
     * to the {@code Author} constructor.
     */
    public Author create(String name, Country country, LocalDate dateOfBirth, String biography) {
        String trimmedName = (name != null) ? name.trim() : null;
        String trimmedBio  = (biography != null) ? biography.trim() : null;
        return new Author(trimmedName, country, dateOfBirth, trimmedBio);
    }
}
