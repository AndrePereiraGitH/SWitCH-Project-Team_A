package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting Genre domain objects
 * into US004GenreDTO.
 */
public class US004GenreMapper {

    public static List<US004GenreDTO> toDTO(List<Genre> genres) {
        if (genres == null) return new ArrayList<>();

        List<US004GenreDTO> list = new ArrayList<>();

        for (Genre genre : genres) {
            list.add(toDTO(genre));
        }

        return list;
    }

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