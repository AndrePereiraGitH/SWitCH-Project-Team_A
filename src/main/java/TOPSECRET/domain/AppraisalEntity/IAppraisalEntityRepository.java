package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;

import java.util.List;

/**
 * Contract for managing {@link AppraisalEntity} objects.
 *
 * <p>
 * Defines operations for creating and storing AppraisalEntity instances,
 * including validation of input data and prevention of duplicates.
 * </p>
 */
public interface IAppraisalEntityRepository {

    /**
     * Creates and stores a new {@link AppraisalEntity}.
     *
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Validates that selected DTO lists are not null or empty</li>
     *     <li>Converts Genre DTOs into existing {@link GenreId}</li>
     *     <li>Converts PublicationType DTOs into existing {@link PublicationTypeId}</li>
     *     <li>Ensures no duplicate AppraisalEntity exists with the same name and contact</li>
     *     <li>Delegates creation to {@link AppraisalEntityFactory}</li>
     *     <li>Stores the created entity</li>
     * </ul>
     * </p>
     *
     * @param name name of the AppraisalEntity
     * @param contact contact information
     * @param genresId selected GenreId
     * @param publicationTypesId selected PublicationTypeId
     * @return the created AppraisalEntity
     *
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>DTO lists are null or empty</li>
     *     <li>Referenced Genre or PublicationType does not exist</li>
     *     <li>An AppraisalEntity with the same name and contact already exists</li>
     * </ul>
     */
    AppraisalEntity createAndAdd(String name,
                                 Contact contact,
                                 List<GenreId> genresId,
                                 List<PublicationTypeId> publicationTypesId);


}
