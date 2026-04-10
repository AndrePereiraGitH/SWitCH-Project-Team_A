package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper that converts {@link Genre} domain objects into {@link GenreDTO_US017} — US017.
 */
public class GenreMapper_US017 {

    /**
     * Converts a single {@link Genre} domain object into a {@link GenreDTO_US017}.
     *
     * @param genre the genre to convert; must not be null
     * @return a new DTO containing the genre name
     * @throws IllegalArgumentException if genre is null
     */
    public GenreDTO_US017 toDTO(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null.");
        }
        return new GenreDTO_US017(genre.getGenreType());
    }

    /**
     * Converts a list of {@link Genre} domain objects into a list of {@link GenreDTO_US017}.
     *
     * @param genres the list of genres to convert; must not be null
     * @return a new list of DTOs, one per genre
     * @throws IllegalArgumentException if genres is null
     */
    public List<GenreDTO_US017> toDTOList(List<Genre> genres) {
        if (genres == null) {
            throw new IllegalArgumentException("Genre list cannot be null.");
        }

        List<GenreDTO_US017> result = new ArrayList<>();
        for (Genre genre : genres) {
            result.add(toDTO(genre));
        }
        return result;
    }
}
