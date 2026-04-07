package TOPSECRET.domain.Edition;

import TOPSECRET.domain.Enum.TypeOfBinding;

import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.ValueObjects.PhysicalAttributes;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.PublisherID;

import java.time.LocalDate;
import java.util.Locale;

/**
 * Factory responsible for creating {@link Edition} instances.
 * <p>
 * This factory centralizes creation logic and extracts only the required
 * data from other aggregates, ensuring that {@code Edition} depends only
 * on identifiers and primitive values.
 */
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
                editionNumber == null ? null : editionNumber.trim(),
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
