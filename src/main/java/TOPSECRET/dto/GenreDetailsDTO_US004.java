package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object used to safely expose Genre information
 * to the UI layer without exposing the internal domain object.
 */
public class GenreDetailsDTO_US004 {

    private String title;

    /**
     * Creates a GenreDTO with the provided title.
     * @param title Genre title.
     */
    public GenreDetailsDTO_US004 (String title) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Genre title cannot be null or blank.");
        }

        this.title = title;
    }

    /**
     * Converts a list of Genre domain objects into DTOs.
     *
     * @param genres list of Genre
     * @return list of GenreDetailsDTO_US004
     */
    public static List<GenreDetailsDTO_US004> genreToDTO (List<Genre> genres) {

        List<GenreDetailsDTO_US004> result = new ArrayList<>();

        for (Genre genre : genres) {
            result.add(new GenreDetailsDTO_US004(genre.getGenreType()));
        }

        return result;
    }

    /**
     * Returns the genre title.
     * @return Genre title.
     */
    public String getTitle() {
        return title;
    }

}

