package TOPSECRET.controller;

import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.dto.AuthorDTO_US025;
import TOPSECRET.dto.AuthorMapper_US025;
import TOPSECRET.dto.DirectSaleDTO_US025;
import TOPSECRET.dto.DirectSaleMapper_US025;

import java.util.List;

/**
 * Controller responsible for providing:
 * 1. A list of all authors.
 * 2. A list of items on direct sale of a specific author.
 *
 * The controller only orchestrates repository calls and delegates
 * DTO conversion to the Mapper classes.
 */
public class US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController {

    private final IAuthorRepository authorRepository;
    private final DirectSaleMapper_US025 directSaleMapper;

    /**
     * Constructs a new {@code US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController}
     * with the required dependencies.
     *
     * @param authorRepository  the repository for authors; must not be {@code null}
     * @param directSaleMapper  the mapper used to convert direct sale entities to DTOs; must not be {@code null}
     * @throws IllegalArgumentException if any parameter is {@code null}
     */
    public US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
            IAuthorRepository authorRepository,
            DirectSaleMapper_US025 directSaleMapper) {

        if (authorRepository == null)
            throw new IllegalArgumentException("AuthorRepository cannot be null");
        if (directSaleMapper == null)
            throw new IllegalArgumentException("DirectSaleMapper cannot be null");

        this.authorRepository = authorRepository;
        this.directSaleMapper = directSaleMapper;
    }

    /**
     * Returns all authors in the system, converted into DTOs.
     *
     * @return list of AuthorDTO_US025 representing all authors
     */
    public List<AuthorDTO_US025> getAllAuthors() {
        return AuthorMapper_US025.toDTOList(authorRepository.getAllAuthors());
    }

    /**
     * Returns the list of items on direct sale for a specific author, converted into DTOs.
     *
     * @param authorId the author whose items are being queried
     * @return list of DirectSaleDTO_US025 representing the author's items
     * @throws IllegalArgumentException if the author is null
     */
    public List<DirectSaleDTO_US025> getListOfItemsOnDirectSaleOfAGivenAuthor(String authorId) {
        if (authorId == null || authorId.isBlank())
            throw new IllegalArgumentException("AuthorID cannot be null or blank");

        AuthorId id = new AuthorId(authorId);
        return directSaleMapper.toDTOListByAuthor(id);
    }
}