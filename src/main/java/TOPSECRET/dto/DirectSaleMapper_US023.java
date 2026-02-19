package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Item;
import TOPSECRET.domain.Listing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DirectSaleMapper_US023 {
//
//    public List<DirectSaleDTO_US023> toDTO(DirectSale ds) {
//
//        DirectSale.DirectSaleDetails details = ds.getDetails();
//        List<DirectSaleDTO_US023> dtos = new ArrayList<>();
//
//        for (Listing listing : ds.getListings()) {
//
//            for (Item item : listing.getItems()) {
//
//                String publicationTitle = item.getPublication().getBibliographicalData().getTitle();
//
//                List<String> authors = new ArrayList<>();
//                for (Author author : item.getPublication().getBibliographicalData().getAuthors()) {
//                    authors.add(author.getProfile().getName());
//                }
//
////                BigDecimal price = ds.getListing().getPrice().getPrice();
//                BigDecimal price = listing.getPrice().getPrice();
//
////                String listingTitle = ds.getListing().getListingTitle();
//                String listingTitle = listing.getListingTitle();
//
////                String seller = ds.getListing().getSeller().getProfile().getName();
//                String seller = listing.getSeller().getProfile().getName();
//
//                dtos.add(new DirectSaleDTO_US023(publicationTitle, authors, price, listingTitle, seller));
//            }
//        }
//
//        return dtos;
//    }
//
//    public List<DirectSaleDTO_US023> toDTO(List<DirectSale> directSales) {
//
//        List<DirectSaleDTO_US023> result = new ArrayList<>();
//
//        for (DirectSale ds : directSales) {
//            result.addAll(toDTO(ds));
//        }
//
//        return result;
//    }
}