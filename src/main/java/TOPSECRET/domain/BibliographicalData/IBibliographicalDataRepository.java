package TOPSECRET.domain.BibliographicalData;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Genre;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

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

    BibliographicalData createAndAdd(String title, List<Author> authors, Locale originalLanguage, LocalDate publicationDate,
                                     List<Genre> genres, String chunksOfText) throws InstanceAlreadyExistsException;


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

}
