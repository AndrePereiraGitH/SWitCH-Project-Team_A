package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.GenreID;

/**
 * DTO representing Genre for US004.
 */
public record US004GenreDTO(
        GenreID id,
        String title
) {

    public US004GenreDTO {
        if (id == null) {
            throw new IllegalArgumentException("GenreID cannot be null.");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
    }
}

