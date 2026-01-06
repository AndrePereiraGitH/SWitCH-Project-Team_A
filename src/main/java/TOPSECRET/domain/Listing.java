package TOPSECRET.domain;

import TOPSECRET.domain.Enum.SaleType;

public class Listing {

    private Item _item;
    private Price _price;
    private String _shortDescription;
    private String _listingTitle;
    private final SaleType _saleType;
    private final User _seller;

    public Listing(Item item, Price price, String shortDescription, String listingTitle, SaleType saleType, User seller) {

        if (listingTitle.length() < 5) {
            throw new IllegalArgumentException("Listing title must be at least 5 characters.");
        } else if (listingTitle.length() > 100) {
            throw new IllegalArgumentException("Listing title must be at most 100 characters.");
        }

        if (shortDescription.length() < 10) {
            throw new IllegalArgumentException("Listing shortDescription must be at least 10 character.");
        }
        _item = item;
        _price = price;
        _shortDescription = shortDescription;
        _listingTitle = listingTitle;
        _saleType = saleType;
        _seller = seller;
    }


    public Price getPrice() {
        return _price;
    }


}
