package TOPSECRET.dto;

import TOPSECRET.domain.Author;

/**
 * Data Transfer Object (DTO) for author data used in use case US026.
 *
 * @param name the author's name
 */
public record AuthorDTO_US026(
        String name
) {
    /**
     * Creates an {@link AuthorDTO_US026} from an {@link Author} domain object.
     *
     * @param author the author domain object to convert
     * @return a new {@link AuthorDTO_US026} containing the author's name
     * @throws IllegalArgumentException if {@code author} is {@code null}
     */
    public static AuthorDTO_US026 fromAuthor(Author author) {

        if (author == null) {
            throw new IllegalArgumentException("author cannot be null");
        }

        return new AuthorDTO_US026(author.getName());
    }
}
