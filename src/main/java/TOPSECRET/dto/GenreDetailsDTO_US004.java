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
     * Returns the genre title.
     * @return Genre title.
     */
    public String getTitle() {
        return title;
    }

}

