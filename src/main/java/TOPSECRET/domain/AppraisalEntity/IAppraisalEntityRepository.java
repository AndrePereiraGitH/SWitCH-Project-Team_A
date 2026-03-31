package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;

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
     * Creates a new AppraisalEntity and stores it in the repository.
     *
     * <p>
     * Converts provided DTOs into domain objects and ensures that
     * all required data is valid before creation.
     * </p>
     *
     * @param name the name of the appraisal entity;
     *             must not be null or blank
     * @param contact the contact information of the entity;
     *                must not be null
     * @param selectedGenresDTO list of selected genres (DTO);
     *                          must not be null or empty
     * @param selectedPublicationTypesDTO list of selected publication types (DTO);
     *                                    must not be null or empty
     * @return the newly created {@link AppraisalEntity}
     * @throws IllegalArgumentException if any argument is invalid,
     *                                  if referenced domain objects do not exist,
     *                                  or if an entity with the same name and contact already exists
     */
    AppraisalEntity createAndAdd(String name,
                                 Contact contact,
                                 List<GenreDetailsDTO_US004> selectedGenresDTO,
                                 List<PublicationTypeDTO_US004> selectedPublicationTypesDTO);


}
