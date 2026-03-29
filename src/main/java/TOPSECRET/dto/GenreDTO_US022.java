package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreDTO_US022 {

    public String name;

    public GenreDTO_US022(Genre genre) {
        this.name = genre.getGenreType();
    }

    // Create a list of GenreDTO from a List of domain object
    public static List<GenreDTO_US022> fromDomainList(List<Genre> genres) {
        // Creates an empty DTO List
        List<GenreDTO_US022> result = new ArrayList<>();
        // Iterates over all genres domain
        for (Genre g : genres) {
            // adds new GenreDTO to the DTO List
            result.add(new GenreDTO_US022(g));
        }
        // gives final DTO List
        return result;
    }
}
