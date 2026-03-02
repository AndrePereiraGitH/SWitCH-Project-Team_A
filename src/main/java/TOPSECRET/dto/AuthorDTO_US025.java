package TOPSECRET.dto;

import TOPSECRET.domain.Author;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing an author with just the name.
 */
public class AuthorDTO_US025 {

    private String name;

    /**
     * Private constructor to enforce use of factory methods.
     *
     * @param name The author's name.
     */
    AuthorDTO_US025(String name) {
        this.name = name;
    }

    /**
     * Creates a DTO from a given Author entity.
     *
     * @param author The Author entity to map.
     * @return A new AuthorDTO_US025 instance, or null if author is null.
     */
    public static AuthorDTO_US025 fromAuthor(Author author) {
        if (author == null) return null;
        return new AuthorDTO_US025(author.getProfile().getName());
    }

    /**
     * Converts a list of Author entities to a list of DTOs using a traditional loop.
     *
     * @param authors List of Author entities.
     * @return List of AuthorDTO_US025. Returns an empty list if input is null.
     */
    public static List<AuthorDTO_US025> fromAuthors(List<Author> authors) {
        List<AuthorDTO_US025> dtos = new ArrayList<>();
        if (authors == null) return dtos;

        for (Author author : authors) {
            AuthorDTO_US025 dto = fromAuthor(author);
            if (dto != null) {
                dtos.add(dto);
            }
        }
        return dtos;
    }

    // ----- GETTER -----
    public String getName() {
        return name;
    }
}