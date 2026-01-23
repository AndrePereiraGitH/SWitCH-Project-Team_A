package TOPSECRET.repository;

import TOPSECRET.domain.Item;
import TOPSECRET.domain.Listing;
import TOPSECRET.domain.Price;
import TOPSECRET.domain.User;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

public class ListingRepository {

    List<Listing> listings;

    public ListingRepository() {
        this.listings = new ArrayList<>();
    }

    public Listing createAndAdd(List<Item> items, Price price, String shortDescription, String listingTitle, User seller) throws InstanceAlreadyExistsException {

        Listing newListing = new Listing(items, price, shortDescription, listingTitle, seller );

        if (isExistent(newListing))
            throw new InstanceAlreadyExistsException("Listing already exists.");

        listings.add(newListing);

        return newListing;
    }

    private boolean isExistent(Listing listing) {
        return listings.contains(listing);
    }

}
