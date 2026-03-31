package TOPSECRET.domain.Author;

import TOPSECRET.domain.ValueObjects.CountryID;

import java.time.LocalDate;


/**
 * Factory responsible for creating {@link Author} instances.
 *
 * <p>This factory centralizes the creation logic of {@code Author} objects,
 * performing minor input normalization before delegating validation to the
 * {@link Author} constructor.</p>
 *
 * <p>In particular, string arguments such as the author's name and biography
 * are trimmed before the {@code Author} is created.</p>
 */
public class AuthorFactory {
    /**
     * Creates a new {@link Author} instance with the given data.
     *
     * <p>Before creating the author, this method trims the {@code name} and
     * {@code biography} values when they are not {@code null}. Validation of
     * domain rules is delegated to the {@link Author} constructor.</p>
     *
     * @param name the author's name
     * @param countryID the identifier of the author's country of origin
     * @param dateOfBirth the author's date of birth
     * @param biography the author's biography
     * @return a new {@link Author} instance
     * @throws IllegalArgumentException if any argument violates the rules
     *                                  enforced by {@link Author}
     */
    public Author create(String name, CountryID countryID, LocalDate dateOfBirth, String biography) {
        String trimmedName = (name != null) ? name.trim() : null;
        String trimmedBio  = (biography != null) ? biography.trim() : null;
        return new Author(trimmedName, countryID, dateOfBirth, trimmedBio);
    }
}
