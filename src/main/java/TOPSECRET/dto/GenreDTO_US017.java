package TOPSECRET.dto;

import TOPSECRET.domain.Genre;

/**
 * DTO do Genre.
 * Tem a conversão aqui em vez de usar um Mapper à parte.
 */
public class GenreDTO_US017 {

    public final String name;

    private GenreDTO_US017(String name) {
        this.name = name;
    }

    /**
     * Converte um Genre para DTO.
     */
    public static GenreDTO_US017 toDTO(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null.");
        }
        return new GenreDTO_US017(genre.getGenreType());
    }
}