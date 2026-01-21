package TOPSECRET.domain;

import java.time.LocalDateTime;
import java.util.Objects;

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
        return Objects.equals(_listing, that._listing) && Objects.equals(_directSaleDate, that._directSaleDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_listing, _directSaleDate);
    }
}