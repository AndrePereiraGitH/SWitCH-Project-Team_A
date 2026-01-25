package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Item;

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
        BigDecimal price = ds.getListing().getPrice().getPrice();
        String listingTitle = ds.getListing().getListingTitle();
        String seller = ds.getListing().getSeller().getUserName();

        return new DirectSaleDTO_US025(title, authors, price, listingTitle, seller);
    }

    // Creates a list of DTOs for all items in all direct sales
    public List<DirectSaleDTO_US025> toDTO(List<DirectSale> directSales) {
        List<DirectSaleDTO_US025> dtos = new ArrayList<>();

        for (DirectSale ds : directSales) {
            for (Item item : ds.getListing().getItems()) {
                dtos.add(mapItemToDTO(item, ds));
            }
        }
        return dtos;
    }
}