package TOPSECRET.dto;

import java.math.BigDecimal;
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
public record DirectSaleDTO_US025(
        String publicationTitle,
        List<String> authors,
        BigDecimal price,
        String listingTitle,
        String seller) {}