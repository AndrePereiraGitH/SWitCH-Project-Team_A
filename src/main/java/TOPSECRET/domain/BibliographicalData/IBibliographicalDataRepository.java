package TOPSECRET.domain.BibliographicalData;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Genre.Genre;
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

}
