package TOPSECRET.repository;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Country.Country;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface responsible for managing {@link Author} repositories.
 * <p>
 * Defines operations for creating and retrieving authors in the system.
 */

public interface IAuthorRepository {
    Author createAndAddAuthor(String name, Country country, LocalDate dateOfBirth, String biography);
    List<Author> getAllAuthors();
}
