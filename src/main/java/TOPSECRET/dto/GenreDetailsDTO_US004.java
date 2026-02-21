package TOPSECRET.dto;

import TOPSECRET.domain.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object used to safely expose Genre information
 * to the UI layer without exposing the internal domain object.
 */
public class GenreDetailsDTO_US004 {

    private String title;

// --------------------------------- US004 starts ---------------------------------
    /**
     * Creates a GenreDTO with the provided title.
     * @param title Genre title.
     */
    public GenreDetailsDTO_US004 (String title) {
        this.title = title;
    }

    /**
     * Returns the genre title.
     * @return Genre title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Converts a Genre domain object into a GenreDTO.
     * @param genre Genre domain object.
     * @return GenreDTO object.
     */
    public GenreDetailsDTO_US004 genreToDTO (Genre genre) {
        return new GenreDetailsDTO_US004(genre.getTitle());
    }

    /**
     * Converts a list of Genre domain objects into DTOs.
     * @param genres List of Genre.
     * @return List of GenreDTO.
     */
    public List<GenreDetailsDTO_US004> genreListToDTO (List<Genre> genres) {

        List<GenreDetailsDTO_US004> result = new ArrayList<>();

        for (Genre genre : genres) {
            result.add(genreToDTO(genre));
        }

        return result;
    }

    /**
     * Converts this DTO into a Genre domain object.
     * @return Genre domain object.
     */
    public Genre dtoToGenre () {
        return new Genre(this.title);
    }
// --------------------------------- US004 ends ---------------------------------

}

