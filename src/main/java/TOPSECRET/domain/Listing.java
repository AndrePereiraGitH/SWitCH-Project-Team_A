package TOPSECRET.domain;

import TOPSECRET.domain.Enum.SaleType;

public class Listing {

    // Correct type of attributes and constructor
//    private final Item _item;
//    private double _price;
//    private String _shortDescription;
//    private String _listingTitle;
//    private final SaleType _saleType;
//    private final User _seller;

//    public Listing(Item item, double price, String shortDescription, String listingTitle, SaleType saleType, User seller) {
//
//        if (listingTitle.length() < 5) {
//            throw new IllegalArgumentException("Listing title must be at least 5 characters.");
//        } else if (listingTitle.length() > 100) {
//            throw new IllegalArgumentException("Listing title must be at most 100 characters.");
//        }
//
//        this._item = item;
//        this._price = PriceValidator.validatePrice(price);
//        this._shortDescription = Validator.isNullOrBlank(shortDescription) ? "" : shortDescription;
//        this._listingTitle = listingTitle;
//        this._saleType = saleType;
//        this._seller = seller;
//    }

    private final String _item; // TODO change to Item type
    private double _price;
    private String _shortDescription;
    private String _listingTitle;
    private final String _saleType; // TODO change to SaleType
    private final String _seller; // TODO change to User type

    public Listing(String item, double price, String shortDescription, String listingTitle, String saleType, String seller) {


        if (listingTitle.length() < 5) {
            throw new IllegalArgumentException("Listing title must be at least 5 characters.");
        } else if (listingTitle.length() > 100) {
            throw new IllegalArgumentException("Listing title must be at most 100 characters.");
        }

        this._item = item;
            this._price = PriceValidator.validatePrice(price);
            this._shortDescription = Validator.isNullOrBlank(shortDescription) ? "" : shortDescription;
            this._listingTitle = listingTitle;
            this._saleType = saleType;
            this._seller = seller;
    }

    public String copyItem() {
        String copyItem = _item;
        return copyItem;
    }

    public double copyPrice() {
        double copyPrice = _price;
        return copyPrice;
    }

    public String copyShortDescriptionCopy() {
        return _shortDescription;
    }

    public String copyListingTitle() {
        return _listingTitle;
    }

    public String copySeller() {
        String copySeller = _seller;
        return copySeller;
    }

    public String copySaleType() {
        return _saleType;
    }


    //Idea: Throw exception when two different listings sell the same (physical) item

}
