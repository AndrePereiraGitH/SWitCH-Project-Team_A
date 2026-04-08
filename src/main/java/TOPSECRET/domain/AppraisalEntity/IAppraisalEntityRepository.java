package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.dto.US004GenreDTO;
import TOPSECRET.dto.US004PublicationTypeDTO;

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
     *     <li>Converts Genre DTOs into existing {@link GenreID}</li>
     *     <li>Converts PublicationType DTOs into existing {@link PublicationTypeId}</li>
     *     <li>Ensures no duplicate AppraisalEntity exists with the same name and contact</li>
     *     <li>Delegates creation to {@link AppraisalEntityFactory}</li>
     *     <li>Stores the created entity</li>
     * </ul>
     * </p>
     *
     * @param name name of the AppraisalEntity
     * @param contact contact information
     * @param selectedGenresDTO selected Genre DTOs
     * @param selectedPublicationTypesDTO selected PublicationType DTOs
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
                                 List<US004GenreDTO> selectedGenresDTO,
                                 List<US004PublicationTypeDTO> selectedPublicationTypesDTO);


}
