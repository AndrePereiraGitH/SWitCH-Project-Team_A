package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting Listing and PublicationSample entities into DirectSaleDTO_US025.
 */
public class DirectSaleMapper_US025 {

    private final IBibliographicalDataRepository bibliographicalDataRepository;
    private final IListingRepository listingRepository;
    private final IPublicationSampleRepository publicationSampleRepository;

    /**
     * Constructs a new {@code DirectSaleMapper_US025} with the required repositories.
     *
     * @param bibliographicalDataRepository the repository for bibliographical data; must not be {@code null}
     * @param listingRepository             the repository for listings; must not be {@code null}
     * @param publicationSampleRepository   the repository for publication samples; must not be {@code null}
     * @throws IllegalArgumentException if any repository is {@code null}
     */
    public DirectSaleMapper_US025(IBibliographicalDataRepository bibliographicalDataRepository,
                                  IListingRepository listingRepository,
                                  IPublicationSampleRepository publicationSampleRepository) {

        if (bibliographicalDataRepository == null)
            throw new IllegalArgumentException("BibliographicalDataRepository cannot be null");
        if (listingRepository == null)
            throw new IllegalArgumentException("ListingRepository cannot be null");
        if (publicationSampleRepository == null)
            throw new IllegalArgumentException("PublicationSampleRepository cannot be null");

        this.bibliographicalDataRepository = bibliographicalDataRepository;
        this.listingRepository = listingRepository;
        this.publicationSampleRepository = publicationSampleRepository;
    }

    /**
     * Converts a PublicationSample and its associated Listing into a DTO,
     * using the given author.
     *
     * @param author  the author already loaded; must not be {@code null}
     * @param item    the PublicationSample to map; must not be {@code null}
     * @param listing the Listing containing the item; must not be {@code null}
     * @return a new DirectSaleDTO_US025
     */
    private DirectSaleDTO_US025 toDTO(Author author, PublicationSample item, Listing listing) {

        BibliographicalData bd = bibliographicalDataRepository
                .findBibliographicalDataById(item.getBibliographicalDataID())
                .orElseThrow(() -> new IllegalArgumentException("BibliographicalData not found"));

        return new DirectSaleDTO_US025(
                bd.getTitle(),
                List.of(author.getName()),
                listing.getPrice().getPrice(),
                listing.getListingTitle(),
                listing.getSellerId().toString()
        );
    }

    /**
     * Returns a list of DTOs for all direct sale items whose publication is associated
     * with the given author.
     *
     * @param author the author already loaded; must not be {@code null}
     * @return list of DirectSaleDTO_US025 for the given author
     * @throws IllegalArgumentException if author is {@code null}
     */
    public List<DirectSaleDTO_US025> toDTOListByAuthor(Author author) {

        if (author == null)
            throw new IllegalArgumentException("Author cannot be null");

        List<Listing> allListings = listingRepository.findAllDirectSaleListings();
        List<DirectSaleDTO_US025> result = new ArrayList<>();

        for (Listing listing : allListings) {
            for (PublicationSampleId itemId : listing.getItemsId()) {

                PublicationSample item = publicationSampleRepository
                        .findPublicationSampleById(itemId)
                        .orElseThrow(() -> new IllegalArgumentException("PublicationSample not found"));

                BibliographicalData bd = bibliographicalDataRepository
                        .findBibliographicalDataById(item.getBibliographicalDataID())
                        .orElseThrow(() -> new IllegalArgumentException("BibliographicalData not found"));

                if (bd.getAuthors().contains(author.id())) {
                    result.add(toDTO(author, item, listing));
                }
            }
        }
        return result;
    }
}