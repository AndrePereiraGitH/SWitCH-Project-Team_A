package TOPSECRET.dto;

import TOPSECRET.domain.Genre;

public class GenreDTO_US013 {
    public final String genreName;

    public GenreDTO_US013(String genreName) {
        if (genreName == null || genreName.isBlank()) {
            throw new IllegalArgumentException("Genre name cannot be null or blank");
        }
        this.genreName = genreName;
    }

    public static GenreDTO_US013 toDTO(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }
        return new GenreDTO_US013(genre.getGenreType());
    }
}

