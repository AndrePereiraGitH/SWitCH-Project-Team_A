package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.GenreID;

/**
 * DTO representing Genre for US004.
 */
public record US004GenreDTO(
        GenreID id,
        String title
) {

    /**
     * Constructs a US004GenreDTO with validation.
     *
     * @param id unique identifier of the genre
     * @param title genre title
     * @throws IllegalArgumentException if id is null or title is null/blank
     */
    public US004GenreDTO {
        if (id == null) {
            throw new IllegalArgumentException("GenreID cannot be null.");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
    }
}

