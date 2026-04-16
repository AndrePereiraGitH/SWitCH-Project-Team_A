package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting {@link Genre} domain objects into {@link US021GenreDTO}.
 */
public class US021GenreMapper {

    private US021GenreMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Converts a {@link Genre} into a {@link US021GenreDTO}.
     *
     * @param genre the genre to convert; must not be null
     * @return the corresponding DTO
     */
    public static US021GenreDTO toDTO(Genre genre) {
        if (genre == null) {
            throw new NullPointerException("Genre cannot be null.");
        }
        return new US021GenreDTO(
                genre.id(),
                genre.getGenreType()
        );
    }

    /**
     * Converts a list of {@link Genre} objects into a list of {@link US021GenreDTO}.
     *
     * @param genres the list of genres to convert; must not be null
     * @return list of DTOs
     */
    public static List<US021GenreDTO> toDTOList(List<Genre> genres) {
        if (genres == null) {
            throw new IllegalArgumentException("Genre list cannot be null.");
        }
        List<US021GenreDTO> result = new ArrayList<>();
        for (Genre genre : genres) {
            result.add(toDTO(genre));
        }
        return result;
    }
}
