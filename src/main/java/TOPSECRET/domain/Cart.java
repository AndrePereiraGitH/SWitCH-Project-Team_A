package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public class Cart {

/**
 * Creates a new cart for the given buyer.
 * The cart starts empty and can receive and remove listings until it is finished.
 */

    //Attributes
    private List<Listing> listingList;
    private final User buyer;
    private boolean finished;

    //Constructor
    public Cart(User buyer) {
        if (buyer == null) {
            throw new IllegalArgumentException("Buyer cannot be null");
        }
        this.buyer = buyer;
        this.listingList = new ArrayList<>();
        this.finished = false;
    }

    // To ensure cart is not finished
    private void ensureNotFinished() {
        if (finished) {
            throw new IllegalStateException("Cart is already finished");
        }
    }

    // To add a listing
    public void addListing(Listing listing) {
        ensureNotFinished();
        if (listing == null) {
            throw new IllegalArgumentException("Listing cannot be null");
        }
        listingList.add(listing);
    }

    // To remove a listing
    public boolean removeListing(Listing listing) {
        ensureNotFinished();
        return listingList.remove(listing);
    }

    // To finish the cart
    public void finish() {
        if (listingList.isEmpty()) {
            throw new IllegalStateException("It's not possible to finish an empty cart");
        }
        this.finished = true;
    }

    // For encapsulation
    public boolean isEmpty() { return listingList.isEmpty(); }


    // Getters
    public List<Listing> getListingsList() {
        return List.copyOf(listingList);
    }

    public User getBuyer() {
        return buyer;
    }

    public boolean isFinished() {
        return finished;
    }
}