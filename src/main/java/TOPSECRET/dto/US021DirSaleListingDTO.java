package TOPSECRET.dto;

import java.math.BigDecimal;


/**
 * Data Transfer Object (DTO) for US021 - Get list of items on direct sale of a given genre.
 *
 * <p>Encapsulates the essential information about a direct sale listing
 * for transfer between the controller and the presentation layer.</p>
 */
public record US021DirSaleListingDTO(
        String listingId,
        String listingTitle,
        BigDecimal price,
        String shortDescription,
        String sellerId
) {
    public US021DirSaleListingDTO {
        if (listingId == null) throw new IllegalArgumentException("listingId cannot be null");
        if (listingTitle == null) throw new IllegalArgumentException("listingTitle cannot be null");
        if (price == null) throw new IllegalArgumentException("price cannot be null");
        if (shortDescription == null) throw new IllegalArgumentException("shortDescription cannot be null");
        if (sellerId == null) throw new IllegalArgumentException("sellerId cannot be null");
    }
}
