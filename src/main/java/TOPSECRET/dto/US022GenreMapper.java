package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Mapper responsible for converting {@link Genre} domain objects into {@link US022GenreDTO}.
 */
public class US022GenreMapper {
    /**
     * Converts a {@link Genre} into a {@link US022GenreDTO}.
     *
     * @param genre the genre to convert
     * @return the corresponding DTO
     */
    public US022GenreDTO toDTO(Genre genre) {
        return new US022GenreDTO(
                genre.id(),
                genre.getGenreType()
        );
    }
    /**
     * Converts a list of {@link Genre} objects into a list of {@link US022GenreDTO}.
     *
     * @param genres the list of genres to convert
     * @return list of DTOs
     */
    public List<US022GenreDTO> toDTOList(List<Genre> genres) {

        Objects.requireNonNull(genres, "genres cannot be null");

        List<US022GenreDTO> dtoList = new ArrayList<>();
        for (Genre genre : genres) {
            dtoList.add(toDTO(genre));
        }

        return dtoList;

    }
}