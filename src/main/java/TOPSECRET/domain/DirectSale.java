package TOPSECRET.domain;

import java.time.LocalDateTime;
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

    private Listing _listing;
    private LocalDateTime _directSaleDate;

    //===========================
    // Constructor for DirectSale

    public DirectSale() {}

    public DirectSale(Listing listing, LocalDateTime directSaleDate) {

        if(listing == null){
            throw new IllegalArgumentException("Listing cannot be null");
        }
        if(directSaleDate == null){
            throw new IllegalArgumentException("BuyDate cannot be null");
        }

        this._listing = listing;
        this._directSaleDate = directSaleDate;

    }

    public Listing getListing() {
        return _listing;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DirectSale that = (DirectSale) o;
        return Objects.equals(_listing, that._listing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_listing, _directSaleDate);
    }

    // US023 Start --------------------------------------------------------------------------------------------------

    public DirectSaleDetails getDetails() {
        return new DirectSaleDetails(_listing, _directSaleDate);
    }

    public record DirectSaleDetails(
            Listing listing,
            LocalDateTime directSaleDate
    ) {}

    // US023 End --------------------------------------------------------------------------------------------------

}