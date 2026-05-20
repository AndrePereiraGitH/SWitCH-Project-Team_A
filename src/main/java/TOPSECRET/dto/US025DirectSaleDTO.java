package TOPSECRET.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object representing a direct sale item for use in US025.
 *
 * @param publicationTitle the title of the publication
 * @param authors          the list of author names
 * @param price            the price of the direct sale
 * @param listingTitle     the title of the listing
 * @param seller           the seller identifier
 */
public record US025DirectSaleDTO(
        String publicationTitle,
        List<String> authors,
        BigDecimal price,
        String listingTitle,
        String seller
) {}