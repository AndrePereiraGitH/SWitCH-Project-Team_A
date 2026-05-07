package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.BibliographicalDataDM;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Assembler responsible for converting between the domain model
 * {@link BibliographicalData} and the persistence data model
 * {@link BibliographicalDataDM}.
 *
 * This class encapsulates the transformation logic required to map
 * domain objects to their database representation and vice versa,
 * ensuring separation of concerns between domain and persistence layers.
 */
@Service
public class BibliographicalDataAssembler {

    private final BibliographicalDataFactory factory;

    /**
     * Constructs a {@code BibliographicalDataAssembler} with the given factory.
     *
     * @param factory the factory used to create domain {@link BibliographicalData} instances
     */
    public  BibliographicalDataAssembler(BibliographicalDataFactory factory) {
        this.factory = factory;
    }

    /**
     * Converts a domain {@link BibliographicalData} object into its
     * persistence representation {@link BibliographicalDataDM}.
     *
     * @param bd the domain object to convert
     * @return a {@link BibliographicalDataDM} instance representing the given domain object
     */
    public BibliographicalDataDM toDM(BibliographicalData bd) {

        return new BibliographicalDataDM(
                bd.id(),
                bd.getTitle(),
                bd.getOriginalLanguage().toString(),
                bd.getPublicationDate(),
                bd.getChunksOfText(),
                bd.getAuthors(),
                bd.getGenres()
        );
    }

    /**
     * Converts a persistence {@link BibliographicalDataDM} object into
     * its domain representation {@link BibliographicalData}.
     *
     * @param bdDM the data model object to convert
     * @return a domain {@link BibliographicalData} instance
     */
    public BibliographicalData toDomain(BibliographicalDataDM bdDM){

        return factory.create(
                bdDM.getId(),
                bdDM.getTitle(),
                bdDM.getAuthorIds(),
                convertLanguageToDomain(bdDM.getOriginalLanguage()),
                bdDM.getPublicationDate(),
                bdDM.getGenreIds(),
                bdDM.getChunksOfText()
        );
    }

    /**
     * Converts a language string into a {@link Locale} object.
     *
     * The method normalizes the input by trimming whitespace,
     * replacing underscores with hyphens, and converting to lowercase
     * before creating the {@link Locale}.
     *
     * @param languageString the language string (e.g., "en_US", "pt-PT")
     * @return the corresponding {@link Locale}, or {@code null} if the input is null or empty
     */
    private static Locale convertLanguageToDomain(String languageString){
        if (languageString == null || languageString.trim().isEmpty()) {
            return null;
        }

        // Normalize format
        String normalized = languageString.trim()
                .replace('_', '-')
                .toLowerCase();

        return Locale.forLanguageTag(normalized);
    }

}
