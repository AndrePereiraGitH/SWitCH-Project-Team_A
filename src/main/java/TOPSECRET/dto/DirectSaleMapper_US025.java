package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting DirectSale and PublicationSample entities into DirectSaleDTO_US025.
 */
public class DirectSaleMapper_US025 {

    private final IBibliographicalDataRepository bibliographicalDataRepository;
    private final IAuthorRepository authorRepository;
    private final IListingRepository listingRepository;
    private final IPublicationSampleRepository publicationSampleRepository;

    public DirectSaleMapper_US025(IBibliographicalDataRepository bibliographicalDataRepository,
                                  IAuthorRepository authorRepository,
                                  IListingRepository listingRepository,
                                  IPublicationSampleRepository publicationSampleRepository) {
        this.bibliographicalDataRepository = bibliographicalDataRepository;
        this.authorRepository = authorRepository;
        this.listingRepository = listingRepository;
        this.publicationSampleRepository = publicationSampleRepository;
    }

    /**
     * Creates a DTO from a given item and its associated direct sale.
     *
     * @param item The PublicationSample to map.
     * @param listing  The Listing containing the item.
     * @return A new instance of DirectSaleDTO_US025.
     */
    public DirectSaleDTO_US025 toDTO(PublicationSample item, Listing listing) {

        BibliographicalData bd = bibliographicalDataRepository
                .findBibliographicalDataById(item.getBibliographicalDataID())
                .orElseThrow(() -> new IllegalArgumentException("BibliographicalData not found"));

        String title = bd.getTitle();

        List<String> authorNames = new ArrayList<>();
        for (AuthorId authorId : bd.getAuthors()) {
            authorRepository
                    .findAuthorById(authorId)
                    .ifPresent(author -> authorNames.add(author.getName()));
        }

        BigDecimal price = listing.getPrice().getPrice();
        String listingTitle = listing.getListingTitle();
        String seller = listing.getSellerId().toString();

        return new DirectSaleDTO_US025(title, authorNames, price, listingTitle, seller);
    }

    /**
     * Creates a list of DTOs for all items in the given listings.
     *
     * @param listings The list of listings containing the items.
     * @return List of DirectSaleDTO_US025 representing all items.
     */
    public List<DirectSaleDTO_US025> toDTOList(List<Listing> listings) {
        List<DirectSaleDTO_US025> dtos = new ArrayList<>();

        for (Listing listing : listings) {
            for (PublicationSampleId itemId : listing.getItemsId()) {
                PublicationSample item = publicationSampleRepository
                        .findPublicationSampleById(itemId)
                        .orElseThrow(() -> new IllegalArgumentException("PublicationSample not found"));
                dtos.add(toDTO(item, listing));
            }
        }
        return dtos;
    }

    /**
     * Creates a list of DTOs for all items on direct sale associated with a given author.
     *
     * @param author   The author to filter by.
     * @return List of DirectSaleDTO_US025 representing the author's items on direct sale.
     */
    public List<DirectSaleDTO_US025> toDTOListByAuthor(Author author) {
        List<Listing> listings = listingRepository.findAllDirectSaleListings();
        List<DirectSaleDTO_US025> result = new ArrayList<>();

        for (Listing listing : listings) {
            for (PublicationSampleId itemId : listing.getItemsId()) {

                PublicationSample item = publicationSampleRepository
                        .findPublicationSampleById(itemId)
                        .orElseThrow(() -> new IllegalArgumentException("PublicationSample not found"));

                BibliographicalData bd = bibliographicalDataRepository
                        .findBibliographicalDataById(item.getBibliographicalDataID())
                        .orElseThrow(() -> new IllegalArgumentException("BibliographicalData not found"));

                if (bd.getAuthors().contains(author.id())) {
                    result.add(toDTO(item, listing));
                }
            }
        }
        return result;
    }
}