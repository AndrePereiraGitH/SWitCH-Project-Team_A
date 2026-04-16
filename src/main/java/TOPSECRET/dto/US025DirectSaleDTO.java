package TOPSECRET.dto;

import java.util.List;

/**
 * Data Transfer Object representing a direct sale item.
 *
 * @param publicationTitle the title of the publication; must not be {@code null}
 * @param authors          the list of author names associated with the publication
 * @param price            the price of the item; must not be {@code null}
 * @param listingTitle     the title of the listing; must not be {@code null}
 * @param seller           the identifier of the seller; must not be {@code null}
 */
public record US025DirectSaleDTO(
        String publicationTitle,
        List<String> authors,
        double price,
        String listingTitle,
        String seller) {

    public US025DirectSaleDTO {
        if (publicationTitle == null || publicationTitle.isBlank())
            throw new IllegalArgumentException("Publication title cannot be null or blank");
        if (authors == null || authors.isEmpty())
            throw new IllegalArgumentException("Authors cannot be null or empty");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        if (listingTitle == null || listingTitle.isBlank())
            throw new IllegalArgumentException("Listing title cannot be null or blank");
        if (seller == null || seller.isBlank()) throw new IllegalArgumentException("Seller cannot be null or blank");
    }
}