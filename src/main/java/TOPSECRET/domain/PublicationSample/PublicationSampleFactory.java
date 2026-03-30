package TOPSECRET.domain.PublicationSample;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.User;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PublicationSampleID;
import TOPSECRET.domain.ValueObjects.UserId;

/**
 * Factory responsible for creating {@link PublicationSample} instances.
 * <p>
 * This class centralizes the creation logic of {@code PublicationSample}
 * objects, ensuring that all instances are created in a consistent and
 * controlled manner.
 * <p>
 * The factory delegates validation of mandatory fields to the
 * {@code PublicationSample} constructor.
 */
public class PublicationSampleFactory {

    /**
     * Creates a new {@link PublicationSample} instance.
     *
     * @param bibliographicalDataID the ID of the bibliographical data associated with the publication
     * @param userID the ID of the owner of the publication sample
     * @param publicationStatus the current status of the publication
     * @param editionID the ID of the edition details of the publication
     * @param condition the physical condition of the publication
     * @return a newly created {@code PublicationSample}
     * @throws IllegalArgumentException if any of the provided arguments are {@code null}
     */
    public PublicationSample create(BibliographicalDataID bibliographicalDataID, UserId userID,
                                    PublicationStatus publicationStatus, EditionID editionID, Condition condition){

        return new PublicationSample(bibliographicalDataID, userID, publicationStatus, editionID, condition);
    }

}
