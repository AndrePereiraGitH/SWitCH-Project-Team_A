package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.GenreId;

/**
 * DTO representing a Genre for US021.
 *
 * @param genreId the genre identifier (must not be null)
 * @param name the genre name (must not be null)
 *
 * @throws IllegalArgumentException if any field is null
 */
public record US021GenreDTO(
        GenreId genreId,
        String name
) {
    public US021GenreDTO {
        if (genreId == null) {
            throw new IllegalArgumentException("genreId cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
    }
}
