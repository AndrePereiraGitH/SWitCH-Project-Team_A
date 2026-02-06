package TOPSECRET.dto;

import TOPSECRET.domain.Genre;

public class GenreDTO_US013 {
    public String genreName;

    public GenreDTO_US013(Genre genre) {
        this.genreName = genre.getGenreType();
    }
}
