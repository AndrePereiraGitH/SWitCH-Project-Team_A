package TOPSECRET.domain.BibliographicalData;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.GenreId;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;

/**
 * Contract for managing {@link BibliographicalData} objects.
 * <p>
 * Defines operations for creating, storing, and retrieving bibliographical
 * data associated with publications, including authors, genres, language,
 * and publication date.
 * </p>
 */

public interface IBibliographicalDataRepository {

    /**
     * Saves a created bibliographical data record to the repository
     * if it does not already exist.
     *
     * @param bibliographicalDataCreated The bibliographical data objects created by the factory
     * @throws InstanceAlreadyExistsException throws exception when bibliographical data already exists
     * @return the created and stored bibliographical data record
     */
    BibliographicalData save(BibliographicalData bibliographicalDataCreated) throws InstanceAlreadyExistsException;

    /**
     * Returns the complete list of bibliographical data records stored in the repository.
     *
     * <p>The returned list reflects the repository's current internal state and
     * contains all bibliographical data records that have been previously added.
     * Modifications to the returned list will affect the repository's internal
     * collection, as no defensive copy is created.</p>
     *
     * @return the internal list of all stored bibliographical data records; never {@code null}
     */

    List<BibliographicalData> findAll();

    /**
     * Finds bibliographical data in the repository by its unique identifier.
     *
     * <p>Iterates through all stored bibliographical data entries and returns
     * the first one whose ID matches the provided {@link BibliographicalDataId}.</p>
     *
     * @param bibliographicalDataId the unique identifier of the bibliographical data
     *                              to search for; must not be {@code null}
     * @return an {@link Optional} containing the matching {@link BibliographicalData}
     *         if found, or an empty {@link Optional} if no entry with the given ID exists
     * @throws NullPointerException if {@code bibliographicalDataId} is {@code null}
     */
    Optional<BibliographicalData> findById(BibliographicalDataId bibliographicalDataId);

    /**
     * Retrieves all {@link BibliographicalDataId} associated with a given {@link GenreId}.
     *
     * <p>This method searches the repository for all {@link BibliographicalData}
     * instances whose list of genres contains the provided genre identifier.</p>
     *
     * @param genreId the identifier of the genre used to filter bibliographical data;
     *                must not be {@code null}
     * @return a list of {@link BibliographicalDataId} that are associated with the given genre;
     *         may be empty if no matches are found
     *
     * @throws IllegalArgumentException if {@code genreId} is {@code null}
     */
    List<BibliographicalDataId> findAllByGenreId(GenreId genreId);

    /**
     * Finds the ID of a bibliographical data entry by its title, using case-insensitive
     * and whitespace-trimmed matching.
     *
     * @param bdTitle the title to search for; must not be {@code null} or blank
     * @return an {@link Optional} containing the {@link BibliographicalDataId} if a match
     *         is found, or an empty {@link Optional} if no match exists
     * @throws NullPointerException     if {@code bdTitle} is {@code null}
     * @throws IllegalArgumentException if {@code bdTitle} is blank
     */
    Optional<BibliographicalDataId> findIdByTitle(String bdTitle);

    /**
     * Retrieves the identifiers of all bibliographical data entries associated with a given author.
     *
     * @param authorId the identifier of the author whose bibliographical data identifiers are to be retrieved
     * @return a list of {@link BibliographicalDataId} associated with the given author
     * @throws IllegalArgumentException if {@code authorId} is null
     */
    List<BibliographicalDataId> findIdByAuthorId(AuthorId authorId);

    /**
     * Retrieves the titles of all bibliographical entries in the list.
     *
     * @return a {@link List} of {@link String} containing all titles from {@code _bdList},
     *         or an empty list if no entries are present.
     */
    List<String> getAllTitles();




}
