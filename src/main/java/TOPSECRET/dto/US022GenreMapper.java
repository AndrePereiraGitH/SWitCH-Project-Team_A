package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;

import java.util.ArrayList;
import java.util.List;
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
    public static US022GenreDTO toDTO(Genre genre) {
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
    public static List<US022GenreDTO> toDTOList(List<Genre> genres) {

        List<US022GenreDTO> result = new ArrayList<>();

        for (Genre genre : genres) {
            result.add(toDTO(genre));
        }

        return result;
    }
}