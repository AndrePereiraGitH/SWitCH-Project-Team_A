package TOPSECRET.domain;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a listing for selling publication samples.
 * A listing contains one or more publication samples, pricing information,
 * and details about the sale.
 */
public class Listing {

    private final String _listingTitle;
    private final List<PublicationSample> _items;
    private final SaleType _saleType;
    private final Price _price;
    private final String _shortDescription;
    private final User _seller;
    private final List<Picture> _pictures;

    /**
     * Creates a new listing with the specified details.
     * Upon creation, all items in the listing are automatically marked as FOR_SALE.
     *
     * @param listingTitle the title of the listing (must be between 5 and 100 characters)
     * @param items the list of publication samples to include in the listing (must contain at least one item)
     * @param saleType the type of sale (e.g., direct sale, auction)
     * @param price the price for the listing
     * @param shortDescription a brief description of the listing (must be at least 10 characters)
     * @param pictures the list of pictures associated with the listing
     * @throws IllegalArgumentException if listingTitle is null or not between 5 and 100 characters
     * @throws IllegalArgumentException if shortDescription is null or less than 10 characters
     * @throws IllegalArgumentException if items is null or empty
     * @throws IllegalArgumentException if price is null
     * @throws IllegalArgumentException if pictures is null
     * @throws IllegalArgumentException if any item is already in a listing (status is FOR_SALE)
     * @throws IllegalArgumentException if any item is already sold (status is SOLD)
     */
    protected Listing(String listingTitle, List<PublicationSample> items, SaleType saleType,
                   Price price, String shortDescription, List<Picture> pictures) {

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

        if (pictures == null)
            throw new IllegalArgumentException("Pictures cannot be null");



        _listingTitle = listingTitle;
        _items = List.copyOf(items);
        _saleType = saleType;
        _price = price;
        _shortDescription = shortDescription;
        _seller = items.get(0).getOwner();
        _pictures = List.copyOf(pictures);

        if(checkIfItemIsAlreadyInAListing()){
            throw new IllegalArgumentException("Item is already on a listing");
        }

        if(checkIfItemIsAlreadySold()){
            throw new IllegalArgumentException("Item was already sold");
        }

        changePublicationSampleStatusToSale();

    }

    /**
     * Returns a copy of the items in this listing.
     *
     * @return a new list containing the publication samples in this listing
     */
    public List<PublicationSample> getItems() {
        List<PublicationSample> copyItems = new ArrayList<>(_items);
        return copyItems;
    }


    /**
     * Changes the status of all publication samples in the listing to FOR_SALE.
     * Only updates items that are not already marked as FOR_SALE.
     */
    private void changePublicationSampleStatusToSale(){
        for(PublicationSample item: _items){
            if(!item.getPublicationStatus().equals(PublicationStatus.FOR_SALE)){
                item.changeStatusToForSale();
            }
        }
    }

    /**
     * Checks if any item in the listing is already marked as FOR_SALE.
     *
     * @return true if at least one item has status FOR_SALE, false otherwise
     */
    private boolean checkIfItemIsAlreadyInAListing(){
        for(PublicationSample item: _items){
            if(item.getPublicationStatus().equals(PublicationStatus.FOR_SALE)){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if any item in the listing is already marked as SOLD.
     *
     * @return true if at least one item has status SOLD, false otherwise
     */
    private boolean checkIfItemIsAlreadySold(){
        for(PublicationSample item: _items){
            if(item.getPublicationStatus().equals(PublicationStatus.SOLD)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the price of this listing.
     *
     * @return the listing price
     */
    public Price getPrice() {
        return _price;
    }

    /**
     * Returns the title of this listing.
     *
     * @return the listing title
     */
    public String getListingTitle() {
        return _listingTitle;
    }

    /**
     * Returns the seller of this listing.
     * The seller is determined as the owner of the first item in the listing.
     *
     * @return the user who is selling the items
     */
    public User getSeller() {
        return _seller;
    }

    /**
     * Returns the sale type (direct sale or auction) of this listing.
     *
     * @return the sale type (direct sale or auction) of a listing.
     */

    public SaleType getSaleType() {
        return _saleType;
    }

    /**
     * Returns the short description of this listing.
     *
     * @return the short description of a listing.
     */

    public String getShortDescription() {
        return _shortDescription;
    }

    /**
     * Returns the pictures of this listing.
     *
     * @return the pictures of a listing.
     */

    public List<Picture> getPictures() {
        return _pictures;
    }
}
