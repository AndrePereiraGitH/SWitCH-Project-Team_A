package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting Genre domain objects
 * into US004GenreDTO.
 */
public class US004GenreMapper {

    /**
     * Converts a list of Genre domain objects into a list of US004GenreDTO.
     *
     * @param genres list of Genre domain objects
     * @return list of US004GenreDTO; returns empty list if input is null
     */
    public static List<US004GenreDTO> toDTOs(List<Genre> genres) {
        if (genres == null) return new ArrayList<>();

        List<US004GenreDTO> list = new ArrayList<>();

        for (Genre genre : genres) {
            list.add(toDTO(genre));
        }

        return list;
    }

    /**
     * Converts a single Genre domain object into a US004GenreDTO.
     *
     * @param genre Genre domain object
     * @return corresponding US004GenreDTO
     * @throws IllegalArgumentException if genre is null
     */
    public static US004GenreDTO toDTO(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }

        return new US004GenreDTO(
                genre.id(),
                genre.getGenreType()
        );
    }
}