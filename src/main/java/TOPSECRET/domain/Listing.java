package TOPSECRET.domain;


import java.util.Objects;

public class Listing {

    private final String _item; // TODO change to Item type
    private double _price;
    private String _shortDescription;
    private String _listingTitle;
    private final String _saleType; // TODO change to SaleType
    private final String _seller; // TODO change to User type

    public Listing(String item, double price, String shortDescription, String listingTitle, String saleType, String seller) {

        if (listingTitle == null || listingTitle.isBlank()) {
            throw new IllegalArgumentException("Listing title cannot be null or blank.");
        }  //insert title validator

        if (listingTitle.length() < 5) {
            throw new IllegalArgumentException("Listing title must be at least 5 characters.");
        } else if (listingTitle.length() > 100) {
            throw new IllegalArgumentException("Listing title must be at most 100 characters.");
        }

        this._item = item;
        this._price = PriceValidator.validatePrice(price);
        this._shortDescription = shortDescription;
        this._listingTitle = listingTitle;
        this._saleType = saleType;
        this._seller = seller;
    }

    //Idea: Throw exception when two different listings sell the same (physical) item

}
