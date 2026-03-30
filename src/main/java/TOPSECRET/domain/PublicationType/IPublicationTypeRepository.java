package TOPSECRET.domain.PublicationType;

import TOPSECRET.domain.ValueObjects.PublicationTypeId;

import java.util.List;


/**
 * Contract for managing {@link PublicationType} objects.
 *
 * <p>
 * Defines operations for creating, storing, and retrieving publication types.
 * Ensures validation of input data and prevention of duplicates.
 * </p>
 */
public interface IPublicationTypeRepository {


    /**
     * Creates a new publication type and stores it in the repository.
     *
     * @param mediaFormat the media format of the publication type;
     *                    must not be null or blank
     * @return the newly created {@link PublicationType} instance
     * @throws IllegalArgumentException if {@code mediaFormat} is null or blank,
     * or if the publication type already exists
     */
    PublicationType create(String mediaFormat);

    /**
     * Adds an existing publication type to the repository.
     *
     * @param pubType the publication type to add; must not be null
     * @return the added {@link PublicationType}
     * @throws IllegalArgumentException if {@code pubType} is null
     * or already exists in the repository
     */
    PublicationType addToList(PublicationType pubType);

    /**
     * Retrieves all stored publication types.
     *
     * @return an unmodifiable list of {@link PublicationType}
     */
    List<PublicationType> getAllPublicationTypes();


    /**
     * Finds a publication type by its media format.
     *
     * @param mediaFormat the media format to search for;
     *                    must not be null or blank
     * @return the matching {@link PublicationType}
     * @throws IllegalArgumentException if {@code mediaFormat} is null or blank,
     *                                  or if no publication type is found
     */
    PublicationType findByAttributes(String mediaFormat);
}
