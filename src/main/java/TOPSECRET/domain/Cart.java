package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<Listing> listingList;
    private User buyer;
    private boolean finished;

    public Cart(User buyer) {
        this.listingList = new ArrayList<>();
        this.buyer = buyer;
        this.finished = false;
    }

    public void addListing(Listing listing) {
        if (finished) {
            throw new IllegalStateException("Listings cannot be added to a finished cart");
        }
        if (listing == null) {
            throw new IllegalArgumentException("Listing cannot be null");
        }
        listingList.add(listing);
    }

    public boolean removeListing(Listing listing) {
        if (finished) {
            throw new IllegalStateException("Listings cannot be removed from a finished cart");
        }
        return listingList.remove(listing);
    }

    public List<Listing> getListingsList() {
        return List.copyOf(listingList);
    }

    public boolean isEmpty() { return listingList.isEmpty(); }

    public User getBuyer() {
        return buyer;
    }

    public void finish() {
        if (listingList.isEmpty()) {
            throw new IllegalStateException("It's not possible to finish an empty cart");
        }
        this.finished = true;
    }

    public boolean isFinished() {
        return finished;
    }
}