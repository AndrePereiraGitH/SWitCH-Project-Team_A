package TOPSECRET.domain.Listing;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.ValueObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Aggregate root representing a listing of publication samples available for sale.
 * <p>
 * A Listing encapsulates:
 * <ul>
 *     <li>a unique identity ({@link ListingId})</li>
 *     <li>a title and short description</li>
 *     <li>a collection of publication samples being sold</li>
 *     <li>the sale type and price</li>
 *     <li>the seller (owner of the first item)</li>
 *     <li>associated pictures</li>
 * </ul>
 * <p>
 * Upon creation, all items are automatically marked as {@code FOR_SALE},
 * and several invariants are enforced to guarantee domain consistency.
 */
public class Listing implements AggregateRoot<ListingId> {

    private final ListingId _listingId;
    private final String _listingTitle;
    private final List<PublicationSampleID> _itemsId;
    private final SaleType _saleType;
    private final Price _price;
    private final String _shortDescription;
    private final UserId _sellerId;
    private final List<Picture> _pictures;

    /**
     * Creates a new listing with the specified details.
     * Upon creation, all items in the listing are automatically marked as FOR_SALE.
     *
     * @param listingTitle the title of the listing (must be between 5 and 100 characters)
     * @param itemsId the list of unique identifiers of publication samples to include in the listing (must contain at least one item)
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
    protected Listing(String listingTitle, List<PublicationSampleID> itemsId, SaleType saleType,
                   Price price, String shortDescription, UserId sellerId, List<Picture> pictures) {

        if (listingTitle == null || listingTitle.length() < 5) {
            throw new IllegalArgumentException("Listing title must be at least 5 characters.");
        }
        if (listingTitle.length() > 100) {
            throw new IllegalArgumentException("Listing title must be at most 100 characters.");
        }

        if (shortDescription == null || shortDescription.length() < 10 || shortDescription.length() > 100) {
            throw new IllegalArgumentException("Listing shortDescription must be between 10 and 100 characters.");
        }

        if (itemsId == null || itemsId.isEmpty()) {
            throw new IllegalArgumentException("A listing must contain at least one item ID");
        }

        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }

        if (sellerId == null) {
            throw new IllegalArgumentException("Seller ID cannot be null.");
        }

        if (pictures == null) {
            throw new IllegalArgumentException("Pictures cannot be null");
        }

        _listingId = new ListingId(UUID.randomUUID().toString());
        _listingTitle = listingTitle;
        _itemsId = List.copyOf(itemsId);
        _saleType = saleType;
        _price = price;
        _shortDescription = shortDescription;
        _sellerId = sellerId;
        _pictures = List.copyOf(pictures);

//        if(checkIfItemIsAlreadyInAListing()){
//            throw new IllegalArgumentException("Item is already on a listing");
//        }
//
//        if(checkIfItemIsAlreadySold()){
//            throw new IllegalArgumentException("Item was already sold");
//        }
//
//        changePublicationSampleStatusToSale();
    }

    /**
     * Returns a copy of the unique identifiers of items in this listing.
     *
     * @return a new list containing the publication samples ID in this listing
     */
    public List<PublicationSampleID> getItemsId() {
        List<PublicationSampleID> copyItemsId = new ArrayList<>(_itemsId);
        return copyItemsId;
    }

    // TODO: validate PublicationSample state in Application Layer
    // With DDD (and the use of IDs to communicate between aggregates) the following methods don't apply (they should be on
    // the application layer, but we don't have it yet)

//    /**
//     * Changes the status of all publication samples in the listing to FOR_SALE.
//     * Only updates items that are not already marked as FOR_SALE.
//     */
//    private void changePublicationSampleStatusToSale(){
//        for(PublicationSample item: _itemsId){
//            if(!item.getPublicationStatus().equals(PublicationStatus.FOR_SALE)){
//                item.changeStatusToForSale();
//            }
//        }
//    }
//
//    /**
//     * Checks if any item in the listing is already marked as FOR_SALE.
//     *
//     * @return true if at least one item has status FOR_SALE, false otherwise
//     */
//    private boolean checkIfItemIsAlreadyInAListing(){
//        for(PublicationSample item: _itemsId){
//            if(item.getPublicationStatus().equals(PublicationStatus.FOR_SALE)){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Checks if any item in the listing is already marked as SOLD.
//     *
//     * @return true if at least one item has status SOLD, false otherwise
//     */
//    private boolean checkIfItemIsAlreadySold(){
//        for(PublicationSample item: _itemsId){
//            if(item.getPublicationStatus().equals(PublicationStatus.SOLD)){
//                return true;
//            }
//        }
//        return false;
//    }

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
     * Returns the seller unique identifier of this listing.
     *
     * @return the user ID who is selling the items
     */
    public UserId getSellerId() {
        return _sellerId;
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

    /**
     * Returns the unique identifier of this Listing.
     *
     * @return the ListingId associated with this aggregate root
     */
    @Override
    public ListingId id() {
        return _listingId;
    }

    /**
     * Compares this listing with another object based on identity.
     * <p>
     * Two Listings are considered equal if and only if they share the same {@link ListingId}.
     *
     * @param object the object to compare with
     * @return {@code true} if the object is a Listing with the same identity, {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Listing)) return false;
        Listing other = (Listing) object;
        return this._listingId.equals(other._listingId);
    }

    /**
     * Determines whether this Listing is semantically equivalent to another object.
     * <p>
     * Unlike {@link #equals(Object)}, which compares Listings solely by identity
     * ({@link ListingId}), this method evaluates whether two Listings represent
     * the same conceptual sale in the domain.
     *
     * @param object the object to compare with this Listing
     * @return {@code true} if the given object is a Listing with the same conceptual sale; {@code false} otherwise
     */

    @Override
    public boolean sameAs(Object object) {
        if (!(object instanceof Listing)) return false;

        Listing other = (Listing) object;

        return this._listingTitle.equals(other._listingTitle)
                && this._itemsId.equals(other._itemsId)
                && this._saleType.equals(other._saleType)
                && this._price.equals(other._price)
                && this._shortDescription.equals(other._shortDescription)
                && this._sellerId.equals(other._sellerId)
                && this._pictures.equals(other._pictures);
    }
}
