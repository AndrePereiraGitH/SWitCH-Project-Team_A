package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.Listing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DirectSaleMapper_US025 {

    // Extracts the authors' names for each item
    private List<String> extractAuthorNames(Item item) {
        List<String> authors = new ArrayList<>();
        for (Author a : item.getPublication().getBibliographicalData().getAuthors()) {
            authors.add(a.getProfile().getName());
        }
        return authors;
    }

    // Maps an item to DTO
    private DirectSaleDTO_US025 mapItemToDTO(Item item, DirectSale ds) {

        String title = item.getPublication().getBibliographicalData().getTitle();

        List<String> authors = extractAuthorNames(item);

//        BigDecimal price = ds.getListing().getPrice().getPrice();

        // As a directSale can have 1 ou more listings, the price is the sum of each listing price
        BigDecimal price = BigDecimal.ZERO;

        for (Listing listing : ds.getListings()) {
            price = price.add(listing.getPrice().getPrice());
        }

//        String listingTitle = ds.getListing().getListingTitle();

        // If a directSale has only 1 listing, it shows that listing title. Otherwise, it shows "Pack of X listings"
        String listingTitle;
            if (ds.getListings().size() == 1) {
                listingTitle = ds.getListings().get(0).getListingTitle();
            }
            else {
            listingTitle = "Pack of " + ds.getListings().size() + " listings";
            }


//        String seller = ds.getListing().getSeller().getUserName();

        // If a directSale has only 1 listing, it shows that listing seller. Otherwise, it shows "Multiple sellers"
        List<String> sellers = new ArrayList<>();
            for (Listing listing : ds.getListings()) {
                String sellerName = listing.getSeller().getUserName();
                if (!sellers.contains(sellerName)) {
                    sellers.add(sellerName);
                }
            }

        String seller;
            if (sellers.size() == 1) {
                seller = sellers.get(0);
            }
            else {
                seller = "Multiple sellers";
            }

        return new DirectSaleDTO_US025(title, authors, price, listingTitle, seller);
    }

    // Creates a list of DTOs for all items in all direct sales
    public List<DirectSaleDTO_US025> toDTO(List<DirectSale> directSales) {
        List<DirectSaleDTO_US025> dtos = new ArrayList<>();

//        for (DirectSale ds : directSales) {
//            for (Item item : ds.getListings().getItems()) {
//                dtos.add(mapItemToDTO(item, ds));
//            }
//        }
        for (DirectSale ds : directSales) {
            for (Listing listing : ds.getListings()) {
                for (Item item : listing.getItems()) {
                    dtos.add(mapItemToDTO(item, ds));
                }
            }
        }

        return dtos;
    }
}