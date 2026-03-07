package TOPSECRET.dto;

import TOPSECRET.domain.Genre;

/**
 * Genre DTO.
 * The conversion is done here instead of using a separate Mapper.
 */
public class GenreDTO_US017 {

    public final String name;

    private GenreDTO_US017(String name) {
        this.name = name;
    }

    /**
     * Converts a Genre to DTO.
     */
    public static GenreDTO_US017 toDTO(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null.");
        }
        return new GenreDTO_US017(genre.getGenreType());
    }
}