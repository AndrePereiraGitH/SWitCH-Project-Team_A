package TOPSECRET.dto;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Listing.Listing;

import java.math.BigDecimal;
import java.util.List;

/**
 * Mapper responsible for converting {@link Listing} and {@link BibliographicalData}
 * domain objects into {@link US025DirectSaleDTO} data transfer objects.
 */
public class US025DirectSaleMapper {

    /**
     * Converts a {@link Listing}, its associated {@link BibliographicalData},
     * and a list of resolved author names into a {@link US025DirectSaleDTO}.
     *
     * @param listing     the listing to convert; must not be {@code null}
     * @param bd          the bibliographical data associated with the listing; must not be {@code null}
     * @param authorNames the list of author names resolved from the bibliographical data; must not be {@code null}
     * @return a new {@link US025DirectSaleDTO}
     */
    public US025DirectSaleDTO toDTO(Listing listing, BibliographicalData bd, List<String> authorNames) {
        String title = bd.getTitle();
        BigDecimal price = listing.getPrice().getPrice();
        String listingTitle = listing.getListingTitle();
        String seller = listing.getSellerId().toString();
        return new US025DirectSaleDTO(title, authorNames, price, listingTitle, seller);
    }
}