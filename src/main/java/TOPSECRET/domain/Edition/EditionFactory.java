package TOPSECRET.domain.Edition;

import TOPSECRET.domain.Enum.TypeOfBinding;

import TOPSECRET.domain.ValueObjects.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Locale;

/**
 * Factory responsible for creating {@link Edition} instances.
 * <p>
 * This factory centralizes creation logic and extracts only the required
 * data from other aggregates, ensuring that {@code Edition} depends only
 * on identifiers and primitive values.
 */
@Service
public class EditionFactory {

    /**
     * Creates a new {@code Edition}.
     *
     * @param editionNumber the edition number
     * @param code the identification code
     * @param physicalAttributes the physical attributes
     * @param publicationTypeId the publication type aggregate
     * @param publisherID the publisher aggregate
     * @param mediaFormat the media format
     * @param editionLanguage the language
     * @param editionDate the publication date
     * @param typeOfBinding the binding type
     *
     * @return a new {@code Edition} instance
     */
    public Edition create(String editionNumber,
                          Code code,
                          PhysicalAttributes physicalAttributes,
                          PublicationTypeId publicationTypeId,
                          PublisherID publisherID,
                          String mediaFormat,
                          Locale editionLanguage,
                          LocalDate editionDate,
                          TypeOfBinding typeOfBinding) {

        return new Edition(
                editionNumber,
                code,
                physicalAttributes,
                publicationTypeId,
                publisherID,
                mediaFormat,
                editionLanguage,
                editionDate,
                typeOfBinding
        );
    }

    /**
     * Recreates an existing Edition from persistence.
     *
     * <p>
     * This method is used when loading an Edition from the database.
     * It preserves the original identity (EditionID) and ensures that
     * all domain validations are still applied.
     * </p>
     *
     * @param editionID the existing edition identifier
     * @param editionNumber the edition number
     * @param code the identification code
     * @param physicalAttributes the physical attributes
     * @param publicationTypeId the publication type identifier
     * @param publisherID the publisher identifier
     * @param mediaFormat the media format
     * @param editionLanguage the language
     * @param editionDate the publication date
     * @param typeOfBinding the binding type
     *
     * @return a reconstructed Edition instance
     */
    public Edition create(EditionID editionID,
                          String editionNumber,
                          Code code,
                          PhysicalAttributes physicalAttributes,
                          PublicationTypeId publicationTypeId,
                          PublisherID publisherID,
                          String mediaFormat,
                          Locale editionLanguage,
                          LocalDate editionDate,
                          TypeOfBinding typeOfBinding) {
        return new Edition(
                editionID,
                editionNumber,
                code,
                physicalAttributes,
                publicationTypeId,
                publisherID,
                mediaFormat,
                editionLanguage,
                editionDate,
                typeOfBinding
        );
    }
}
