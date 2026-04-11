package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;

import java.util.List;


/**
 * Mapper responsible for converting {@link Author} domain objects
 * into {@link US026AuthorDTO} data transfer objects.
 */
public class US026AuthorMapper {


    /**
     * Converts a domain {@link Author} into a {@link US026AuthorDTO}.
     *
     * @param author the author to be converted
     * @return a DTO containing the author's data
     * @throws IllegalArgumentException if the author is null
     */
    public static US026AuthorDTO fromDomain(Author author) {

        if (author == null) {
            throw new IllegalArgumentException("author cannot be null");
        }

        return new US026AuthorDTO(author.id(), author.getName());
    }

    /**
     * Converts a list of domain {@link Author} objects into
     * a list of {@link US026AuthorDTO}.
     *
     * @param authors the list of authors to be converted
     * @return a list of DTOs containing the authors' data
     * @throws IllegalArgumentException if the list of authors is null
     */
    public static List<US026AuthorDTO> fromDomainList(List<Author> authors) {

        if (authors == null) {
            throw new IllegalArgumentException("list of authors cannot be null");
        }

        return authors.stream().map(US026AuthorMapper::fromDomain).toList();
    }
}
