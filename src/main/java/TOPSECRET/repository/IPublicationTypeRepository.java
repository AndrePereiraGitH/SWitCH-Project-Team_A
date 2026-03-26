package TOPSECRET.repository;

import TOPSECRET.domain.PublicationType;

import java.util.List;


/**
 * Repository interface responsible for managing {@link PublicationType} entities.
 * Provides methods for creating, storing, and retrieving publication types.
 */
public interface IPublicationTypeRepository {

    /**
     * Creates a new {@link PublicationType} instance with the given media format.
     *
     * @param mediaFormat the media format of the publication type (e.g., digital, print)
     * @return a new PublicationType instance
     */
    PublicationType create(String mediaFormat);

    /**
     * Adds a {@link PublicationType} to the repository's internal collection.
     *
     * @param pubType the publication type to be added
     * @return the added PublicationType instance
     */
    PublicationType addToList(PublicationType pubType);

    /**
     * Retrieves all stored publication types.
     *
     * @return a list containing all PublicationType objects in the repository
     */
    List<PublicationType> getAllPublicationTypes();

    /**
     * Finds a {@link PublicationType} by its media format.
     *
     * @param mediaFormat the media format to search for
     * @return the matching PublicationType, or null if not found
     */
    PublicationType findByAttributes(String mediaFormat);
}
