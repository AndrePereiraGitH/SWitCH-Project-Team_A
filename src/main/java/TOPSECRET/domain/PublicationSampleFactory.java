package TOPSECRET.domain;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;

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
     * @param bibliographicalData the bibliographical data associated with the publication
     * @param userName the owner of the publication sample
     * @param publicationStatus the current status of the publication
     * @param edition the edition details of the publication
     * @param condition the physical condition of the publication
     * @return a newly created {@code PublicationSample}
     * @throws IllegalArgumentException if any of the provided arguments are {@code null}
     */

    public PublicationSample create(BibliographicalData bibliographicalData, User userName,
                                    PublicationStatus publicationStatus, Edition edition, Condition condition){

        return new PublicationSample(bibliographicalData, userName, publicationStatus, edition, condition);
    }

}
