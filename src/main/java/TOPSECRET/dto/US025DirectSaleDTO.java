package TOPSECRET.dto;

import lombok.Value;
import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object representing a direct sale item for use in US025.
 */
@Value
public class US025DirectSaleDTO {

    /**
     * The title of the publication.
     */
    String publicationTitle;

    /**
     * The list of author names.
     */
    List<String> authors;

    /**
     * The price of the direct sale.
     */
    BigDecimal price;

    /**
     * The title of the listing.
     */
    String listingTitle;

    /**
     * The seller identifier.
     */
    String seller;
}