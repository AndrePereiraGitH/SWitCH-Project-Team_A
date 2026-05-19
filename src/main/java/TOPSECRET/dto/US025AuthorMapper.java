package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting {@link Author} domain objects
 * into {@link US025AuthorDTO} data transfer objects.
 */
public class US025AuthorMapper {

    /**
     * Converts a single {@link Author} domain object into a {@link US025AuthorDTO}.
     *
     * @param author The {@link Author} to convert.
     * @return A new {@link US025AuthorDTO}, or {@code null} if the author is {@code null}.
     */
    public US025AuthorDTO toDTO(Author author) {
        if (author == null) return null;
        return new US025AuthorDTO(author.getName());
    }

    /**
     * Converts a list of {@link Author} domain objects into a list of {@link US025AuthorDTO}.
     *
     * @param authors The list of {@link Author} entities to convert.
     * @return A list of {@link US025AuthorDTO}. Returns an empty list if input is {@code null}.
     */
    public List<US025AuthorDTO> toDTOList(List<Author> authors) {
        List<US025AuthorDTO> dtos = new ArrayList<>();
        if (authors == null) return dtos;
        for (Author author : authors) {
            US025AuthorDTO dto = toDTO(author);
            if (dto != null) dtos.add(dto);
        }
        return dtos;
    }
}