package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DirectSaleMapper {

    public List<DirectSaleDTO> toDTO(DirectSale ds) {

        List<DirectSaleDTO> dtos = new ArrayList<>();

        for (Item item : ds.getListing().getItems()) {

            String publicationTitle = item.getPublication().getBibliographicalData().getTitle();

            List<String> authors = new ArrayList<>();
            for (Author author : item.getPublication().getBibliographicalData().getAuthors()) {
                authors.add(author.getProfile().getName());
            }

            BigDecimal price = ds.getListing().getPrice().getPrice();

            String listingTitle = ds.getListing().getListingTitle();

            String seller = ds.getListing().getSeller().getProfile().getName();

            dtos.add(new DirectSaleDTO(publicationTitle, authors, price, listingTitle, seller));
        }

        return dtos;
    }

    public List<DirectSaleDTO> toDTO(List<DirectSale> directSales) {

        List<DirectSaleDTO> result = new ArrayList<>();

        for (DirectSale ds : directSales) {
            List<DirectSaleDTO> itemDTOs = toDTO(ds);
            result.addAll(itemDTOs);
        }

        return result;
    }
}