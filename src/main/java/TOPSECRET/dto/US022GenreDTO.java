package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.GenreId;
/**
 * DTO representing a Genre for US022.
 *
 * @param genreId the genre identifier (must not be null)
 * @param name the genre name (must not be null)
 *
 * @throws IllegalArgumentException if any field is null
 */
public record US022GenreDTO(
        GenreId genreId,
        String name
) {
    public US022GenreDTO {

        if (genreId == null) {
            throw new IllegalArgumentException("genreId is null");
    }
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }

    }


}
