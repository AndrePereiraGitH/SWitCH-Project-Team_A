package TOPSECRET.domain.PublicationSample;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.domain.ValueObjects.*;
import org.springframework.stereotype.Service;

/**
 * Factory responsible for creating and reconstructing {@link PublicationSample} instances.
 *
 * <p>This factory centralises the creation logic of {@code PublicationSample}
 * aggregates and provides two distinct creation paths:</p>
 *
 * <ul>
 *   <li>
 *     <b>Creation of new domain objects</b> — used when a new publication
 *     sample is introduced into the system. In this case, the aggregate
 *     generates its own identity internally.
 *   </li>
 *   <li>
 *     <b>Reconstruction of persisted domain objects</b> — used when restoring
 *     an existing aggregate from the persistence layer. In this case, the
 *     original identity and persisted associations are supplied externally.
 *   </li>
 * </ul>
 *
 * <p>All validation and invariant enforcement are delegated to the
 * {@link PublicationSample} constructors.</p>
 */
@Service
public class PublicationSampleFactory {

    /**
     * Creates a new {@link PublicationSample} aggregate.
     *
     * <p>This method is intended for the creation of brand-new publication
     * samples. The aggregate identity is generated internally by the
     * {@link PublicationSample} constructor.</p>
     *
     * @param bibliographicalDataId the bibliographical data identifier associated
     *                              with the publication; must not be {@code null}
     * @param userId the identifier of the user who owns the publication sample;
     *               must not be {@code null}
     * @param editionId the identifier of the publication edition;
     *                  must not be {@code null}
     * @param condition the physical condition of the publication sample;
     *                  must not be {@code null}
     *
     * @return a newly created {@link PublicationSample}
     *
     * @throws IllegalArgumentException if any argument is {@code null}
     */
    public PublicationSample create(BibliographicalDataId bibliographicalDataId, UserId userId,
                                    EditionId editionId, Condition condition) {

        return new PublicationSample(bibliographicalDataId, userId, editionId, condition);
    }



    /**
     * Reconstructs an existing {@link PublicationSample} aggregate from persisted data.
     *
     * <p>This method is intended exclusively for persistence-layer reconstruction,
     * typically through an assembler responsible for converting persistence models
     * into domain objects.</p>
     *
     * <p>No new identity is generated during reconstruction. The provided
     * {@code pubSampleId} must correspond to the original aggregate identity
     * previously persisted.</p>
     *
     * <p>All domain invariants and validations remain enforced through the
     * {@link PublicationSample} constructor.</p>
     *
     * @param pubSampleId the existing identity of the publication sample;
     *                    must not be {@code null}
     * @param bibliographicalDataId the bibliographical data identifier associated
     *                              with the publication; must not be {@code null}
     * @param userId the identifier of the owner of the publication sample;
     *               must not be {@code null}
     * @param publicationStatus the current publication status;
     *                          must not be {@code null}
     * @param editionId the identifier of the publication edition;
     *                  must not be {@code null}
     * @param condition the physical condition of the publication sample;
     *                  must not be {@code null}
     * @param libraryId the identifier of the library currently associated with
     *                  the publication sample; may be {@code null}
     * @param collectionOfPublicationsId the identifier of the collection of
     *                                   publications associated with the sample;
     *                                   may be {@code null}
     *
     * @return a reconstructed {@link PublicationSample} instance
     *
     * @throws NullPointerException if any mandatory argument is {@code null}
     */
    public PublicationSample create(
            PublicationSampleId pubSampleId,
            BibliographicalDataId bibliographicalDataId,
            UserId userId,
            PublicationStatus publicationStatus,
            EditionId editionId,
            Condition condition,
            LibraryId libraryId,
            CollectionOfPublicationsId collectionOfPublicationsId) {

        return new PublicationSample(pubSampleId, bibliographicalDataId, userId, publicationStatus, editionId, condition, libraryId, collectionOfPublicationsId);
    }

}
