package TOPSECRET.dto;

import TOPSECRET.domain.Genre;

public class GenreMapper_US017 {

    public GenreDTO_US017 toDTO(Genre genre) {

        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null.");
        }

        return new GenreDTO_US017(genre.getGenreType());
    }
}
