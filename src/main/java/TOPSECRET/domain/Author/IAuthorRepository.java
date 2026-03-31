package TOPSECRET.domain.Author;

import TOPSECRET.domain.ValueObjects.CountryID;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface responsible for managing {@link Author} entities.
 *
 * <p>This interface defines operations for creating and storing authors,
 * as well as retrieving all authors available in the repository.</p>
 *
 * <p>Implementations of this interface encapsulate the persistence mechanism
 * used to manage {@code Author} instances.</p>
 */

public interface IAuthorRepository {

    /**
     * Creates a new {@link Author} with the given data and adds it to the repository.
     *
     * <p>The created author is expected to satisfy all domain invariants defined
     * by {@link Author}, including a non-null and non-blank name, a non-null
     * {@link CountryID}, a non-null date of birth, and a non-null and non-blank
     * biography.</p>
     *
     * @param name the name of the author
     * @param countryID the identifier of the author's country of origin
     * @param dateOfBirth the author's date of birth
     * @param biography the author's biography
     * @return the newly created and stored {@link Author}
     * @throws IllegalArgumentException if any argument violates the rules defined
     *                                  by the {@link Author} constructor
     */
    Author createAndAddAuthor(String name, CountryID countryID, LocalDate dateOfBirth, String biography);

    /**
     * Retrieves all authors currently stored in the repository.
     *
     * @return a list containing all stored {@link Author} instances
     */
    List<Author> getAllAuthors();
}
