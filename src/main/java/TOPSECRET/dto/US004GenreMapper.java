package TOPSECRET.dto;

import TOPSECRET.domain.Genre.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting {@link Genre} domain objects
 * into {@link US004GenreDTO} data transfer objects.
 *
 * <p>This class isolates transformation logic between the domain layer
 * and the presentation layer, ensuring that domain entities are not
 * exposed directly to the UI.
 */
public class US004GenreMapper {

    /**
     * Converts a list of {@link Genre} objects into a list of DTOs.
     *
     * @param genres list of Genre domain objects
     * @return list of GenreDetailsDTO_US004
     */
    public List<US004GenreDTO> toDTO(List<Genre> genres) {

        List<US004GenreDTO> list = new ArrayList<>();

        for (Genre genre : genres) {
            list.add(new US004GenreDTO(
                    genre.id(),
                    genre.getGenreType()
            ));
        }

        return list;
    }
}