package TOPSECRET.domain;

import TOPSECRET.domain.Enum.SaleType;

import java.util.List;

public class ListingFactory {
    public Listing create(
            String listingTitle,
            List<PublicationSample> items,
            SaleType saleType,
            Price listingPrice,
            String shorDescription,
            List<Picture> pictures) {

        return new Listing(listingTitle, items, saleType, listingPrice, shorDescription, pictures);
    }
}
