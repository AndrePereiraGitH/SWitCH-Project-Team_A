package TOPSECRET.domain.BibliographicalData;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Factory class responsible for creating and recreating {@link BibliographicalData} instances.
 * <p>
 * This class encapsulates the construction logic for {@link BibliographicalData},
 * distinguishing between the creation of new instances and the reconstruction
 * of existing ones (e.g., from persistence storage).
 */
@Service
public class BibliographicalDataFactory {

    /**
     * Creates a new {@link BibliographicalData} instance.
     *
     * @param title             the title of the bibliographical work
     * @param authors           the list of author identifiers
     * @param originalLanguage  the original language of the work
     * @param publicationDate   the publication date
     * @param genres            the list of genre identifiers
     * @param chunksOfText      the textual content or chunks associated with the work
     * @return a new {@link BibliographicalData} instance
     */
    public BibliographicalData create(
            String title,
            List<AuthorId> authors,
            Locale originalLanguage,
            LocalDate publicationDate,
            List<GenreID> genres, String chunksOfText){

        return new BibliographicalData(
                title,
                authors,
                originalLanguage,
                publicationDate,
                genres,
                chunksOfText);
    }

    /**
     * Recreates an existing {@link BibliographicalData} instance from persisted data.
     * <p>
     * This method should be used when restoring objects from a database or other
     * persistence mechanisms where the identifier is already known.
     *
     * @param bibliographicalDataID the unique identifier of the bibliographical data
     * @param title                 the title of the bibliographical work
     * @param authors               the list of author identifiers
     * @param originalLanguage      the original language of the work
     * @param publicationDate       the publication date
     * @param genres                the list of genre identifiers
     * @param chunksOfText          the textual content or chunks associated with the work
     * @return a reconstructed {@link BibliographicalData} instance
     */
    public BibliographicalData create(
            BibliographicalDataID bibliographicalDataID,
            String title,
            List<AuthorId> authors,
            Locale originalLanguage,
            LocalDate publicationDate,
            List<GenreID> genres,
            String chunksOfText
    ) {
        return new BibliographicalData(
                bibliographicalDataID,
                title,
                authors,
                originalLanguage,
                publicationDate,
                genres,
                chunksOfText
        );
    }

}

