package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper that converts a list of {@link Genre} domain objects into
 * a list of {@link GenreDTO_US017} — US017.
 */
public class GenreMapper_US017 {

    /**
     * Converts a list of {@link Genre} domain objects into a list of {@link GenreDTO_US017}.
     *
     * @param genres the list of genres to convert; must not be null
     * @return a new list of DTOs, one per genre
     * @throws IllegalArgumentException if genres is null
     */
    public List<GenreDTO_US017> toDTO(List<Genre> genres) {

        if (genres == null) {
            throw new IllegalArgumentException("Genre list cannot be null.");
        }

        List<GenreDTO_US017> result = new ArrayList<>();

        for (Genre genre : genres) {
            result.add(GenreDTO_US017.toDTO(genre));
        }

        return result;
    }
}
