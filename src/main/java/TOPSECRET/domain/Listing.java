package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Listing {

    private final List<Item> _items;
    private final Price _price;
    private final String _shortDescription;
    private final String _listingTitle;
    private final User _seller;
    private List<Picture> _pictures;

    public Listing(List<Item> items, Price price, String shortDescription, String listingTitle, User seller) {

        if (listingTitle == null || listingTitle.length() < 5) {
            throw new IllegalArgumentException("Listing title must be at least 5 characters.");
        }
        if (listingTitle.length() > 100) {
            throw new IllegalArgumentException("Listing title must be at most 100 characters.");
        }

        if (shortDescription == null || shortDescription.length() < 10) {
            throw new IllegalArgumentException("Listing shortDescription must be at least 10 characters.");
        }

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("A listing must contain at least one item");
        }

        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }

        if (seller == null) {
            throw new IllegalArgumentException("Seller cannot be null");
        }

        _items = new ArrayList<>(items);
        _price = price;
        _shortDescription = shortDescription;
        _listingTitle = listingTitle;
        _seller = seller;
    }

    public Listing(List<Item> items, Price price, String shortDescription, String listingTitle, User seller, List<Picture> pictures) {

        if (listingTitle == null || listingTitle.length() < 5) {
            throw new IllegalArgumentException("Listing title must be at least 5 characters.");
        }
        if (listingTitle.length() > 100) {
            throw new IllegalArgumentException("Listing title must be at most 100 characters.");
        }

        if (shortDescription == null || shortDescription.length() < 10) {
            throw new IllegalArgumentException("Listing shortDescription must be at least 10 characters.");
        }

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("A listing must contain at least one item");
        }

        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }

        if (seller == null) {
            throw new IllegalArgumentException("Seller cannot be null");
        }
        if (pictures == null)
            throw new IllegalArgumentException("Pictures cannot be null");



        _items = new ArrayList<>(items);
        _price = price;
        _shortDescription = shortDescription;
        _listingTitle = listingTitle;
        _seller = seller;
        _pictures = List.copyOf(pictures);
    }


    public List<Item> getItems() {
        List<Item> copyItems = new ArrayList<>(_items);
        return copyItems;
    }

    public Price getPrice() {
        return _price;
    }

    public String getListingTitle() {
        return _listingTitle;
    }

    public User getSeller() {
        return _seller;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Listing other = (Listing) o;

        if (!Objects.equals(this._seller, other._seller)) return false;

        return samePublications(this._items, other._items);
    }

    private boolean samePublications(List<Item> a, List<Item> b) {
        if (a == null || b == null) return a == b;
        if (a.size() != b.size()) return false;

        // sorted
        for (int i = 0; i < a.size(); i++) {
            Publication pa = a.get(i).getPublication();
            Publication pb = b.get(i).getPublication();
            if (!Objects.equals(pa, pb)) return false;
        }
        return true;

    }

}
