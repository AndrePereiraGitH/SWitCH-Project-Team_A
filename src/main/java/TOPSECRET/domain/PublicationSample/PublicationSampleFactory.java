package TOPSECRET.domain.PublicationSample;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.stereotype.Service;

/**
 * Factory responsible for creating and reconstructing {@link PublicationSample} instances.
 *
 * <p>Two creation paths are provided:</p>
 * <ul>
 *   <li><b>New object</b> — identity is generated internally by the domain object.</li>
 *   <li><b>Reconstruction</b> — identity is supplied externally, restoring a previously
 *       persisted object. This path is intended exclusively for {@code PublicationSampleAssembler}.</li>
 * </ul>
 *
 * <p>Validation is delegated to the {@link PublicationSample} constructor in both cases.</p>
 */
@Service
public class PublicationSampleFactory {

    /**
     * Creates a new {@link PublicationSample} instance.
     *
     * @param bibliographicalDataId the ID of the bibliographical data associated with the publication
     * @param userId the ID of the owner of the publication sample
     * @param publicationStatus the current status of the publication
     * @param editionId the ID of the edition details of the publication
     * @param condition the physical condition of the publication
     * @return a newly created {@code PublicationSample}
     * @throws IllegalArgumentException if any of the provided arguments are {@code null}
     */
    public PublicationSample create(BibliographicalDataID bibliographicalDataId, UserId userId,
                                    PublicationStatus publicationStatus, EditionID editionId, Condition condition){

        return new PublicationSample(bibliographicalDataId, userId, publicationStatus, editionId, condition);
    }


    /**
     * Reconstructs an existing {@link PublicationSample} from persisted data.
     *
     * <p>Use this method exclusively in the assembler when converting a
     * {@code PublicationSampleDM} back into a domain object. The supplied
     * {@code pubSampleId} must be the exact identity originally assigned
     * at creation time — this method does not generate a new identity.</p>
     *
     * <p>Domain invariants are still enforced via the {@code PublicationSample}
     * constructor — reconstruction does not bypass validation.</p>
     *
     * @param pubSampleId           the existing identity of the publication sample; must not be {@code null}
     * @param bibliographicalDataId the ID of the bibliographical data associated with the publication; must not be {@code null}
     * @param userId                the ID of the owner of the publication sample; must not be {@code null}
     * @param publicationStatus     the current status of the publication; must not be {@code null}
     * @param editionId             the ID of the edition details of the publication; must not be {@code null}
     * @param condition             the physical condition of the publication; must not be {@code null}
     * @return a {@code PublicationSample} restored from persisted state
     * @throws IllegalArgumentException if any of the provided arguments are {@code null}
     */
    public PublicationSample create(PublicationSampleId pubSampleId,
                                    BibliographicalDataID bibliographicalDataId, UserId userId,
                                    PublicationStatus publicationStatus, EditionID editionId,
                                    Condition condition) {

        return new PublicationSample(pubSampleId, bibliographicalDataId, userId, publicationStatus, editionId, condition);
    }

}
