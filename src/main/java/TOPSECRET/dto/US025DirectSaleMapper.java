package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;

import java.util.List;

/**
 * Mapper responsible for converting Listing and PublicationSample entities into DirectSaleDTO_US025.
 */
public class US025DirectSaleMapper {

    private US025DirectSaleMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Converts a PublicationSample, its associated Listing, and BibliographicalData into a DTO.
     *
     * @param author  the author already loaded; must not be {@code null}
     * @param listing the Listing containing the item; must not be {@code null}
     * @param bd      the BibliographicalData associated with the item; must not be {@code null}
     * @return a new DirectSaleDTO_US025
     */
    public static US025DirectSaleDTO toDTO(Author author, Listing listing, BibliographicalData bd) {
        return new US025DirectSaleDTO(
                bd.getTitle(),
                List.of(author.getName()),
                listing.getPrice().getPrice().doubleValue(),
                listing.getListingTitle(),
                listing.getSellerId().toString()
        );
    }
}