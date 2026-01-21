package TOPSECRET.domain;

import java.time.LocalDateTime;

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
}
