package TOPSECRET.controller;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
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

    public US025ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
            IAuthorRepository authorRepository,
            IListingRepository listingRepository,
            IBibliographicalDataRepository bibliographicalDataRepository,
            IPublicationSampleRepository publicationSampleRepository) {

        if (authorRepository == null)
            throw new IllegalArgumentException("AuthorRepository cannot be null");
        if (listingRepository == null)
            throw new IllegalArgumentException("ListingRepository cannot be null");
        if (bibliographicalDataRepository == null)
            throw new IllegalArgumentException("BibliographicalDataRepository cannot be null");
        if (publicationSampleRepository == null)
            throw new IllegalArgumentException("PublicationSampleRepository cannot be null");

        this.authorRepository = authorRepository;
        this.directSaleMapper = new DirectSaleMapper_US025(
                bibliographicalDataRepository,
                authorRepository,
                listingRepository,
                publicationSampleRepository);
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
     * @param author the author whose items are being queried
     * @return list of DirectSaleDTO_US025 representing the author's items
     * @throws IllegalArgumentException if the author is null
     */
    public List<DirectSaleDTO_US025> getListOfItemsOnDirectSaleOfAGivenAuthor(Author author) {
        if (author == null)
            throw new IllegalArgumentException("Author cannot be null");

        return directSaleMapper.toDTOListByAuthor(author);
    }
}