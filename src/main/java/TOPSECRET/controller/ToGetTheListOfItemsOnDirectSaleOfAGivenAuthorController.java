package TOPSECRET.controller;

import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.dto.AuthorDTO_US025;

import java.util.List;

/**
 * Controller responsible for providing:
 * 1. A list of all authors.
 * 2. A list of items on direct sale of a specific author.
 *
 * The controller only orchestrates repository calls and delegates
 * DTO conversion to the DTO classes.
 */
public class ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController {

    private final IAuthorRepository IAuthorRepository;
    private final IListingRepository listingRepository;

    public ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
            IAuthorRepository IAuthorRepository,
            IListingRepository listingRepository) {

        if (IAuthorRepository == null)
            throw new IllegalArgumentException("AuthorRepository cannot be null");

        if (listingRepository == null)
            throw new IllegalArgumentException("ListingRepository cannot be null");

        this.IAuthorRepository = IAuthorRepository;
        this.listingRepository = listingRepository;
    }

    /**
     * Returns all authors in the system, converted into DTOs.
     *
     * @return list of AuthorDTO_US025 representing all authors
     */
    public List<AuthorDTO_US025> getAllAuthors() {
        return AuthorDTO_US025.fromAuthors(IAuthorRepository.getAllAuthors());
    }

    /**
     * Returns the list of items on direct sale for a specific author,
     * converted into DTOs.
     *
     * @param author the author whose items are being queried
     * @return list of DirectSaleDTO_US025 representing the author's items
     * @throws IllegalArgumentException if the author is null
     */
//    public List<PublicationSample> getListOfItemsOnDirectSaleOfAGivenAuthor(Author author) {
//        if (author == null) {
//            throw new IllegalArgumentException("Author cannot be null");
//        }
//        return listingRepository.getListOfItemsOnDirectSaleOfAGivenAuthor(author);
//    }
}