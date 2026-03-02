package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.dto.AuctionDTO_US026;
import TOPSECRET.dto.AuthorDTO_US025;
import TOPSECRET.dto.AuthorDTO_US026;
import TOPSECRET.dto.DirectSaleDTO_US025;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.AuthorRepository;
import TOPSECRET.repository.DirectSaleRepository;
import TOPSECRET.repository.ListingRepository;

import java.util.ArrayList;
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

    private final AuthorRepository authorRepository;
    private final ListingRepository listingRepository;

    public ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
            AuthorRepository authorRepository,
            ListingRepository listingRepository) {

        if (authorRepository == null)
            throw new IllegalArgumentException("AuthorRepository cannot be null");

        if (listingRepository == null)
            throw new IllegalArgumentException("AuctionRepository cannot be null");

        this.authorRepository = authorRepository;
        this.listingRepository = listingRepository;
    }

    /**
     * Returns all authors in the system, converted into DTOs.
     *
     * @return list of AuthorDTO_US025 representing all authors
     */
    public List<AuthorDTO_US025> getAllAuthors() {
        return AuthorDTO_US025.fromAuthors(authorRepository.getAllAuthors());
    }

    /**
     * Returns the list of items on direct sale for a specific author,
     * converted into DTOs.
     *
     * @param author the author whose items are being queried
     * @return list of DirectSaleDTO_US025 representing the author's items
     * @throws IllegalArgumentException if the author is null
     */
    public List<PublicationSample> getListOfItemsOnDirectSaleOfAGivenAuthor(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        return listingRepository.getListOfItemsOnDirectSaleofAGivenAuthor(author);
    }
}