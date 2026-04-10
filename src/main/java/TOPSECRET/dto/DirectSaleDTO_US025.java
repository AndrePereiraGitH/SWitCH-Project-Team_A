package TOPSECRET.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a direct sale item,
 * including publication details, authors, total price, listing title, and seller information.
 */
public class DirectSaleDTO_US025 {

    public final String publicationTitle;
    public final List<String> authors;
    public final BigDecimal price;
    public final String listingTitle;
    public final String seller;

    public DirectSaleDTO_US025(String publicationTitle, List<String> authors,
                               BigDecimal price, String listingTitle, String seller) {
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