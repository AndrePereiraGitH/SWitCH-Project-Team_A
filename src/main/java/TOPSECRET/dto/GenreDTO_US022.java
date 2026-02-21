package TOPSECRET.dto;

import TOPSECRET.domain.Genre;

public class GenreDTO_US022 {

    public String name;

    public GenreDTO_US022(Genre genre) {
        this.name = genre.getTitle();
    }
}
