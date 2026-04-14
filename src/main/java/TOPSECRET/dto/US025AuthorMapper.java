package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting Author entities to DTOs.
 */
public class US025AuthorMapper {

    private US025AuthorMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Converts a single Author entity to its DTO representation.
     *
     * @param author The Author entity to map.
     * @return A new AuthorDTO_US025, or null if author is null.
     */
    public static US025AuthorDTO toDTO(Author author) {
        if (author == null)
            throw new IllegalArgumentException("Author cannot be null");
        return new US025AuthorDTO(author.getName());
    }

    /**
     * Converts a list of Author entities to a list of DTOs.
     *
     * @param authors List of Author entities.
     * @return List of AuthorDTO_US025. Returns an empty list if input is null.
     */
    public static List<US025AuthorDTO> toDTOList(List<Author> authors) {
        if (authors == null)
            throw new IllegalArgumentException("Authors list cannot be null");

        List<US025AuthorDTO> dtos = new ArrayList<>();
        for (Author author : authors) {
            dtos.add(toDTO(author));
        }
        return dtos;
    }
}