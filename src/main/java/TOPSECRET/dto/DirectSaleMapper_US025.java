package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DirectSaleMapper_US025 {

    public List<DirectSaleDTO_US025> toDTO(DirectSale ds) {
        List<DirectSaleDTO_US025> dtos = new ArrayList<>();
        for (Item item : ds.getListing().getItems()) {

            String title = item.getPublication().getBibliographicalData().getTitle();

            List<String> authors = new ArrayList<>();
            for (Author a : item.getPublication().getBibliographicalData().getAuthors()) {
                authors.add(a.getProfile().getName());
            }

            BigDecimal price = ds.getListing().getPrice().getPrice();
            String listingTitle = ds.getListing().getListingTitle();
            String seller = ds.getListing().getSeller().getUserName();

            dtos.add(new DirectSaleDTO_US025(title, authors, price, listingTitle, seller));
        }
        return dtos;
    }

    public List<DirectSaleDTO_US025> toDTO(List<DirectSale> directSales) {
        List<DirectSaleDTO_US025> result = new ArrayList<>();
        for (DirectSale ds : directSales) {
            result.addAll(toDTO(ds));
        }
        return result;
    }
}