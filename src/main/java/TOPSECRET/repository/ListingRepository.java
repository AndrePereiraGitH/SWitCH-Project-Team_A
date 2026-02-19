package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.SaleType;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

public class ListingRepository {

    List<Listing> listings;

    public ListingRepository() {
        this.listings = new ArrayList<>();
    }

//    public Listing createAndAdd(String listingTitle, List<Item> items, SaleType saleType,
//                                Price price, String shortDescription, User seller, List<Picture> pictures) throws InstanceAlreadyExistsException {
//
//        Listing newListing = new Listing(listingTitle, items, saleType, price, shortDescription, seller, pictures);
//
//        if (isExistent(newListing))
//            throw new InstanceAlreadyExistsException("Listing already exists.");
//
//        listings.add(newListing);
//
//        return newListing;
//    }

    private boolean isExistent(Listing listing) {
        return listings.contains(listing);
    }

}
