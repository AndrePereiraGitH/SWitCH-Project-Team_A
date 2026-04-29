package TOPSECRET.domain.PublicationType;


import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.services.PublicationTypeService;
import org.springframework.stereotype.Service;

/**
 * Factory responsible for creating {@link PublicationType} instances.
 *
 * <p>
 * Encapsulates the creation logic for both:
 * <ul>
 *     <li>New instances (with generated ID)</li>
 *     <li>Recreated instances (from persistence layer)</li>
 * </ul>
 * </p>
 *
 * <p>
 * This factory should be used by {@link PublicationTypeService} to ensure that
 * all {@link PublicationType} objects are created in a consistent and
 * validated manner.
 * </p>
 *
 * <p>
 * It delegates validation to the domain entity and does not contain
 * business logic.
 * </p>
 */
@Service
public class PublicationTypeFactory {

    /**
     * Creates a new PublicationType with a generated ID.
     *
     * @param mediaFormat the publication format (e.g., Book, Magazine)
     * @return a new PublicationType instance
     * @throws IllegalArgumentException if mediaFormat is null or blank
     */
    public PublicationType create(String mediaFormat)
    {
        return new PublicationType(mediaFormat);
    }

    /**
     * Reconstructs a PublicationType from existing data.
     *
     * <p>
     * This method is typically used when converting from persistence
     * (e.g., JPA Data Model to Domain).
     * </p>
     *
     * @param id the existing publication type identifier
     * @param mediaFormat the publication format
     * @return a rehydrated PublicationType instance
     * @throws IllegalArgumentException if id or mediaFormat are invalid
     */
    public PublicationType create(PublicationTypeId id, String mediaFormat) {
        return new PublicationType(id, mediaFormat);
    }
}
