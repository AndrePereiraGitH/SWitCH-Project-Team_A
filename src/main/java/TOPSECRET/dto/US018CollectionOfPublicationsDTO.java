package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;

/**
 * Data Transfer Object representing a Collection of Publications.
 *
 * <p>
 * This DTO is used to expose collection data to the UI layer,
 * containing only the necessary information for presentation.
 * </p>
 *
 * @param id unique identifier of the collection
 * @param collectionName name of the collection
 */
public record US018CollectionOfPublicationsDTO(
        CollectionOfPublicationsId id,
        String collectionName
) {

    /**
     * Validates DTO fields.
     *
     * @throws IllegalArgumentException if any field is invalid
     */
    public US018CollectionOfPublicationsDTO {
        if (id == null) {
            throw new IllegalArgumentException("Collection ID cannot be null.");
        }

        if (collectionName == null || collectionName.isBlank()) {
            throw new IllegalArgumentException("Collection name cannot be null or empty.");
        }
    }
}