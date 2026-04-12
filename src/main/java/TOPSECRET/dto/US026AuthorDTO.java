package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.ValueObjects.AuthorId;

/**
 * Data Transfer Object (DTO) for author data used in use case US026.
 *
 * @param name the author's name
 */
public record US026AuthorDTO(
        AuthorId id,
        String name
) {

}
