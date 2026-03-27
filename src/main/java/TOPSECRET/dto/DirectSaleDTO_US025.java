package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.DirectSale.DirectSale;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    /**
     * Private constructor to enforce usage of factory methods.
     *
     * @param publicationTitle The title of the publication.
     * @param authors List of authors' names.
     * @param price Total price of the direct sale.
     * @param listingTitle Listing title or "Pack of X listings".
     * @param seller Seller username or "Multiple sellers".
     */
    private DirectSaleDTO_US025(String publicationTitle, List<String> authors,
                                BigDecimal price, String listingTitle, String seller) {
        this.publicationTitle = publicationTitle;
        this.authors = authors;
        this.price = price;
        this.listingTitle = listingTitle;
        this.seller = seller;
    }

    /**
     * Creates a DTO from a given item and its associated direct sale.
     *
     * @param item The item to map.
     * @param ds The direct sale containing the item.
     * @return A new instance of DirectSaleDTO_US025.
     */
    public static DirectSaleDTO_US025 fromItemAndDirectSale(PublicationSample item, DirectSale ds) {

        // Publication title
        String title = item.getBibliographicalData().getTitle();

        // Authors' names
        List<String> authors = new ArrayList<>();
        for (Author a : item.getBibliographicalData().getAuthors()) {
            authors.add(a.getName());
        }

        // Total price
        BigDecimal price = BigDecimal.ZERO;
        for (Listing listing : ds.getListings()) {
            price = price.add(listing.getPrice().getPrice());
        }

        // Listing title
        String listingTitle;
        if (ds.getListings().size() == 1) {
            listingTitle = ds.getListings().get(0).getListingTitle();
        } else {
            listingTitle = "Pack of " + ds.getListings().size() + " listings";
        }

        // Seller
        String seller = "";
        for (Listing listing : ds.getListings()) {
            seller = listing.getSeller().getUserName();
        }

        return new DirectSaleDTO_US025(title, authors, price, listingTitle, seller);
    }

    /**
     * Creates a list of DTOs for all items in the given direct sale.
     *
     * @param ds The direct sale containing multiple listings and items.
     * @return List of DirectSaleDTO_US025 representing all items.
     */
    public static List<DirectSaleDTO_US025> fromDirectSale(DirectSale ds) {
        List<DirectSaleDTO_US025> dtos = new ArrayList<>();
        for (Listing listing : ds.getListings()) {
            for (PublicationSample item : listing.getItems()) {
                dtos.add(fromItemAndDirectSale(item, ds));
            }
        }
        return dtos;
    }

    // ----- GETTERS -----
    public String getTitle() { return publicationTitle; }

    public List<String> getAuthors() { return authors; }

    public BigDecimal getPrice() { return price; }

    public String getListingTitle() { return listingTitle; }

    public String getSeller() { return seller; }
}