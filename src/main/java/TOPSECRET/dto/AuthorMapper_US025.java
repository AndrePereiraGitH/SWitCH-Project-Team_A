package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.dto.AuthorDTO_US025;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting Author entities to DTOs.
 */
public class AuthorMapper_US025 {

    private AuthorMapper_US025() {
        // Utility class — should not be instantiated
    }

    /**
     * Converts a single Author entity to its DTO representation.
     *
     * @param author The Author entity to map.
     * @return A new AuthorDTO_US025, or null if author is null.
     */
    public static AuthorDTO_US025 toDTO(Author author) {
        if (author == null) return null;
        return new AuthorDTO_US025(author.getName());
    }

    /**
     * Converts a list of Author entities to a list of DTOs.
     *
     * @param authors List of Author entities.
     * @return List of AuthorDTO_US025. Returns an empty list if input is null.
     */
    public static List<AuthorDTO_US025> toDTOList(List<Author> authors) {
        List<AuthorDTO_US025> dtos = new ArrayList<>();
        if (authors == null) return dtos;

        for (Author author : authors) {
            AuthorDTO_US025 dto = toDTO(author);
            if (dto != null) {
                dtos.add(dto);
            }
        }
        return dtos;
    }
}