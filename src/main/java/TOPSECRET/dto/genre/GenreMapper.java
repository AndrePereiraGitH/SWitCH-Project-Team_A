package TOPSECRET.dto.genre;

import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.dto.genre.response.GenreResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper responsible for converting {@link Genre} domain objects
 * into Genre DTO representations.
 */
@Component
public class GenreMapper {

    /**
     * Constructor
     */
    public GenreMapper (){

    }

    /**
     * Converts a {@link Genre} domain object into a
     * {@link GenreResponseDTO}.
     *
     * @param genre the Genre domain object to convert
     * @return the corresponding GenreResponseDTO
     * @throws IllegalArgumentException if the genre is null
     */
    public GenreResponseDTO toDTO (Genre genre){

        if(genre == null){
            throw new IllegalArgumentException ("Genre can't be null");
         }

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
    public List<GenreResponseDTO> toDTOs (List<Genre> genres){

        if(genres == null){
            throw new IllegalArgumentException ("Genre can't be null");
        }

        return genres.stream()
                .map(this::toDTO)
                .toList();

    }

}
