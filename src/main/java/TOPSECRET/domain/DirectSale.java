package TOPSECRET.domain;

import java.util.List;
import java.util.Objects;
/**
 * Represents a Direct Sale in the system.
 *
 * A DirectSale is a type of sale where a listing is sold immediately
 * for a fixed price, without bidding.
 *
 * This class links a Listing with the date when the direct sale
 * was created.
 */
public class DirectSale {

    private List<Listing> _listings;
    private User _buyer;
    private boolean _isFinished;

//    private Listing _listing;
//    private LocalDateTime _directSaleDate;


    //============================================================================================================
    // Constructor for DirectSale

    public DirectSale() {}

    public DirectSale (List<Listing> _listings, User _buyer, boolean _isFinished) {

        if(_listings == null){
            throw new IllegalArgumentException("List of listings cannot be null");
        }
        if(_buyer == null){
            throw new IllegalArgumentException("Buyer cannot be empty");
        }

        this._listings = _listings;
        this._buyer = _buyer;
        this._isFinished = _isFinished;
    }

//    public DirectSale(Listing listing, LocalDateTime directSaleDate) {
//
//        if(listing == null){
//            throw new IllegalArgumentException("Listing cannot be null");
//        }
//        if(directSaleDate == null){
//            throw new IllegalArgumentException("BuyDate cannot be null");
//        }
//
//        this._listing = listing;
//        this._directSaleDate = directSaleDate;
//
//    }

    // It's the same directSale if it has the same list of listings and buyer
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DirectSale that = (DirectSale) o;
        return Objects.equals(_listings, that._listings) && Objects.equals(_buyer, that._buyer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_listings, _buyer);
    }

//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        DirectSale that = (DirectSale) o;
//        return Objects.equals(_listing, that._listing);
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(_listing, _directSaleDate);
//    }


    //============================================================================================================

    public List<Listing> getListings() {
        return _listings;
    }

    public DirectSaleDetails getDetails() {
        return new DirectSaleDetails(_listings, _buyer, _isFinished);
    }

    public record DirectSaleDetails(
            List<Listing> listings,
            User buyer,
            boolean isFinished
    ) {}

//    public Listing getListing() {
//        return _listing;
//    }

//    public DirectSaleDetails getDetails() {
//        return new DirectSaleDetails(_listing, _directSaleDate);
//    }
//
//    public record DirectSaleDetails(
//            Listing listing,
//            LocalDateTime directSaleDate
//    ) {}

}