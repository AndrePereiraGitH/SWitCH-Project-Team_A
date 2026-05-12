package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class US013GenreMapper {

    public List<US013GenreDTO> toDTOList(List<Genre> genres) {
        Objects.requireNonNull(genres, "Genres list cannot be null");

        List<US013GenreDTO> genresDTO = new ArrayList<>();

        for (Genre genre : genres) {
            genresDTO.add(toDTO(genre));
        }

        return genresDTO;
    }

    public US013GenreDTO toDTO(Genre genre) {
        Objects.requireNonNull(genre, "Genre cannot be null");

        return new US013GenreDTO(
                genre.id().getID(),
                genre.getGenreType()
        );
    }
}