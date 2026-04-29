package TOPSECRET.domain.PublicationType;


import TOPSECRET.domain.ValueObjects.PublicationTypeId;

/**
 * Factory responsible for creating PublicationType instances.
 *
 * <p>
 * Encapsulates the creation logic for both:
 * <ul>
 *     <li>New instances (with generated ID)</li>
 *     <li>Recreate instances (from persistence)</li>
 * </ul>
 * </p>
 */
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
