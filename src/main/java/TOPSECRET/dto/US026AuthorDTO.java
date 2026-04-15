package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.ValueObjects.AuthorId;

/**
 * Data Transfer Object (DTO) for author data used in use case US026.
 *
 * @param name the author's name
 */
public record US026AuthorDTO(
        String authorId,
        String name
) {

    public US026AuthorDTO {
        if (authorId == null || authorId.isBlank()) {
            throw new IllegalArgumentException("auctionId cannot be null or blank");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or blank");
        }
    }

}
