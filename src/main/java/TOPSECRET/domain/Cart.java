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

    // Add listing to cart
    public void addListing(Listing listing) {
        if (finished) {
            throw new IllegalStateException("Listings cannot be added to a finished cart");
        }
        if (listing == null) {
            throw new IllegalArgumentException("Listing cannot be null");
        }
        listingList.add(listing);
    }

    // Remove listing from cart
    public boolean removeListing(Listing listing) {
        if (finished) {
            throw new IllegalStateException("Listings cannot be removed from a finished cart");
        }
        return listingList.remove(listing);
    }

    // Get listings list
    public List<Listing> getListingsList() {
        return List.copyOf(listingList);
    }

    // Check if cart is empty
    public boolean isEmpty() {
        return listingList.isEmpty();
    }

    // Get cart's buyer
    public User getBuyer() {
        return buyer;
    }

    // Mark cart as finished
    public void finish() {
        if (listingList.isEmpty()) {
            throw new IllegalStateException("It's not possible to finish an empty cart");
        }
        this.finished = true;
    }

    // Check if cart is finished
    public boolean isFinished() {
        return finished;
    }
}