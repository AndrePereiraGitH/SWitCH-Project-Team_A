package TOPSECRET.domain.BibliographicalData;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
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
     * Creates a new bibliographical data record and adds it to the repository
     * if it does not already exist.
     *
     * @param title the title of the publication
     * @param authors the authors associated with the publication
     * @param originalLanguage the original language of the publication
     * @param publicationDate the publication date
     * @param genres the genres associated with the publication
     * @param chunksOfText textual content or excerpts associated with the publication
     * @return the created and stored bibliographical data record
     * @throws InstanceAlreadyExistsException if an equivalent bibliographical data record already exists
     */

    BibliographicalData createAndAdd(String title, List<AuthorId> authors, Locale originalLanguage, LocalDate publicationDate,
                                     List<GenreID> genres, String chunksOfText) throws InstanceAlreadyExistsException;


    /**
     * Finds bibliographical data in the repository by its unique identifier.
     *
     * <p>Iterates through all stored bibliographical data entries and returns
     * the first one whose ID matches the provided {@link BibliographicalDataID}.</p>
     *
     * @param bibliographicalDataId the unique identifier of the bibliographical data
     *                              to search for; must not be {@code null}
     * @return an {@link Optional} containing the matching {@link BibliographicalData}
     *         if found, or an empty {@link Optional} if no entry with the given ID exists
     * @throws NullPointerException if {@code bibliographicalDataId} is {@code null}
     */
    Optional<BibliographicalData> findBibliographicalDataById(BibliographicalDataID bibliographicalDataId);

    /**
     * Retrieves all {@link BibliographicalDataID} associated with a given {@link GenreID}.
     *
     * <p>This method searches the repository for all {@link BibliographicalData}
     * instances whose list of genres contains the provided genre identifier.</p>
     *
     * @param genreId the identifier of the genre used to filter bibliographical data;
     *                must not be {@code null}
     * @return a list of {@link BibliographicalDataID} that are associated with the given genre;
     *         may be empty if no matches are found
     *
     * @throws IllegalArgumentException if {@code genreId} is {@code null}
     */
    List<BibliographicalDataID> findAllBibliographicalDataByGenreId(GenreID genreId);

    /**
     * Finds the ID of a bibliographical data entry by its title, using case-insensitive
     * and whitespace-trimmed matching.
     *
     * @param bdTitle the title to search for; must not be {@code null} or blank
     * @return an {@link Optional} containing the {@link BibliographicalDataID} if a match
     *         is found, or an empty {@link Optional} if no match exists
     * @throws NullPointerException     if {@code bdTitle} is {@code null}
     * @throws IllegalArgumentException if {@code bdTitle} is blank
     */
    Optional<BibliographicalDataID> findBibliographicalDataIdByTitle(String bdTitle);

    /**
     * Retrieves the identifiers of all bibliographical data entries associated with a given author.
     *
     * @param authorId the identifier of the author whose bibliographical data identifiers are to be retrieved
     * @return a list of {@link BibliographicalDataID} associated with the given author
     * @throws IllegalArgumentException if {@code authorId} is null
     */
    List<BibliographicalDataID> findBibliographicalDataIdByAuthorId(AuthorId authorId);


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

    List<BibliographicalData> getAllPublicationsBD();

    /**
     * Retrieves the titles of all bibliographical entries in the list.
     *
     * @return a {@link List} of {@link String} containing all titles from {@code _bdList},
     *         or an empty list if no entries are present.
     */
    List<String> getAllTitles();




}
