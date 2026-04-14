package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper that converts {@link Genre} domain objects into {@link US017GenreDTO} — US017.
 */
public class US017GenreMapper {

    private US017GenreMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Converts a single {@link Genre} domain object into a {@link US017GenreDTO}.
     *
     * @param genre the genre to convert; must not be null
     * @return a new DTO containing the genre name
     * @throws NullPointerException if genre is null
     */
    public static US017GenreDTO toDTO(Genre genre) {
        if (genre == null) {
            throw new NullPointerException("Genre cannot be null.");
        }
        return new US017GenreDTO(genre.getGenreType());
    }

    /**
     * Converts a list of {@link Genre} domain objects into a list of {@link US017GenreDTO}.
     *
     * @param genres the list of genres to convert; must not be null
     * @return a new list of DTOs, one per genre
     * @throws IllegalArgumentException if genres is null
     */
    public static List<US017GenreDTO> toDTOList(List<Genre> genres) {
        if (genres == null) {
            throw new IllegalArgumentException("Genre list cannot be null.");
        }

        List<US017GenreDTO> result = new ArrayList<>();
        for (Genre genre : genres) {
            result.add(toDTO(genre));
        }
        return result;
    }
}
