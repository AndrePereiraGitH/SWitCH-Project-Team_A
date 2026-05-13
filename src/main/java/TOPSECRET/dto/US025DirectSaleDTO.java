package TOPSECRET.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object representing a direct sale item for use in US025.
 */
public class US025DirectSaleDTO {

    /**
     * The title of the publication.
     */
    private final String publicationTitle;

    /**
     * The list of author names.
     */
    private final List<String> authors;

    /**
     * The price of the direct sale.
     */
    private final BigDecimal price;

    /**
     * The title of the listing.
     */
    private final String listingTitle;

    /**
     * The seller identifier.
     */
    private final String seller;

    public US025DirectSaleDTO(String publicationTitle, List<String> authors, BigDecimal price, String listingTitle, String seller) {
        this.publicationTitle = publicationTitle;
        this.authors = authors;
        this.price = price;
        this.listingTitle = listingTitle;
        this.seller = seller;
    }

    public String getPublicationTitle() { return publicationTitle; }
    public List<String> getAuthors() { return authors; }
    public BigDecimal getPrice() { return price; }
    public String getListingTitle() { return listingTitle; }
    public String getSeller() { return seller; }
}