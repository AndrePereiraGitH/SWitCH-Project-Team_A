package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class responsible for converting {@link Genre} domain objects
 * into {@link US013GenreDTO} .
 */

public class US013GenreMapper {

    public List<US013GenreDTO> toDTO(List<Genre> genres) {

        List<US013GenreDTO> genresDTO = new ArrayList<>();

        for (Genre genre : genres) {
            genresDTO.add(new US013GenreDTO(genre.getGenreType()));
        }
        return genresDTO;
    }
}
