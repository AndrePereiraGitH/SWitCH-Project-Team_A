package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.PublicationSampleId;

/**
 * Data Transfer Object representing a Publication Sample.
 *
 * <p>
 * This DTO provides the necessary information about a publication
 * to be displayed in the UI layer.
 * </p>
 *
 * @param id unique identifier of the publication
 * @param title title of the publication
 */
public record US018PublicationSampleDTO(
        PublicationSampleId id,
        String title
) {

    /**
     * Validates DTO fields.
     *
     * @throws IllegalArgumentException if any field is invalid
     */
    public US018PublicationSampleDTO {
        if (id == null) {
            throw new IllegalArgumentException("Publication ID cannot be null.");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
    }
}