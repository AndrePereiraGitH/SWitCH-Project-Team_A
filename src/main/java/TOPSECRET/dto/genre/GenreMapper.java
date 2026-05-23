package TOPSECRET.dto.genre;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.dto.genre.response.GenreResponseDTO;

import java.util.List;
import java.util.Objects;

/**
 * Mapper responsible for converting {@link Genre} domain objects
 * into Genre DTO representations.
 */
public class GenreMapper {

    /**
     * Converts a {@link Genre} domain object into a
     * {@link GenreResponseDTO}.
     *
     * @param genre the Genre domain object to convert
     * @return the corresponding GenreResponseDTO
     * @throws IllegalArgumentException if the genre is null
     */
    public static GenreResponseDTO toDTO (Genre genre){

       Objects.requireNonNull(genre, "Genre can't be null");

        return new GenreResponseDTO(genre.id().getID(), genre.getGenreType());
    }

    /**
     * Converts a list of {@link Genre} domain objects into
     * a list of {@link GenreResponseDTO}.
     *
     * @param genres the list of Genre objects to convert
     * @return the corresponding list of GenreResponseDTO
     * @throws IllegalArgumentException if the genre list is null
     */
    public static List<GenreResponseDTO> toDTOs(List<Genre> genres){

        Objects.requireNonNull(genres, "Genre list can't be null");

        return genres.stream()
                .map(GenreMapper::toDTO)
                .toList();

    }

}
