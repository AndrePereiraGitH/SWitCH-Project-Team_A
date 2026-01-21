package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DirectSaleMapper {

    public DirectSaleDTO toDTO(DirectSale ds) {
        Item firstItem = ds.getListing().getItems().get(0);
        String publicationTitle = firstItem.getPublication().getBibliographicalData().getTitle();
        List<String> authors = new ArrayList<>();
        for (Author author : firstItem.getPublication().getBibliographicalData().getAuthors()) {
            authors.add(author.getProfile().getName());
        }        BigDecimal price = ds.getListing().getPrice().getPrice();
        String listingTitle = ds.getListing().getListingTitle();
        String seller = ds.getListing().getSeller().getProfile().getName();

        return new DirectSaleDTO(publicationTitle,authors,price,listingTitle,seller);
    }

    public List<DirectSaleDTO> toDTO(List<DirectSale> directSales) {
        List<DirectSaleDTO> dtos = new ArrayList<>();
        for (DirectSale ds : directSales) {
            dtos.add(toDTO(ds));
        }
        return dtos;
    }
}