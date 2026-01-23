package TOPSECRET.dto;

import TOPSECRET.domain.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreMapper_US022 {
    // Create a list of GenreDTO from a List of domain object
    public List<GenreDTO_US022> toDTO(List<Genre> genres) {
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
