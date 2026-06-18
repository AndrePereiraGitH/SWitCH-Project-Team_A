package TOPSECRET.domain.Listing;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.Enum.ListingStatus;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.ValueObjects.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final List<PublicationSampleId> _itemsId;
    private final SaleType _saleType;
    private final Price _price;
    private final String _shortDescription;
    private final UserId _sellerId;
    private UserId _buyerId;
    private final List<Picture> _pictures;
    private final LocalDate _publicationDate;
    private ListingStatus _listingStatus;

    // ============================================================
    // ============== CONSTRUCTOR =================================
    // ============================================================

    /**
     * Creates a new {@link Listing} with a generated identifier and validates all input fields.
     *
     * <p>This constructor is protected and intended for use by
     * {@link ListingFactory#create(String, List, SaleType, Price, String, UserId, List)} only.
     * A new {@link ListingId} is generated automatically via {@link UUID#randomUUID()}, and the
     * publication date is automatically set to the current date.
     *
     * @param listingTitle     the title of the listing; must be between 5 and 100 characters
     * @param itemsId          the list of publication sample IDs; must not be {@code null} or empty
     * @param saleType         the type of sale (e.g. direct sale, auction); must not be {@code null}
     * @param price            the price of the listing; must not be {@code null}
     * @param shortDescription a brief description of the listing; must be between 10 and 100 characters
     * @param sellerId         the ID of the seller; must not be {@code null}
     * @param pictures         the list of pictures associated with the listing; must not be {@code null}
     * @throws IllegalArgumentException if {@code listingTitle} is {@code null} or not between 5 and 100 characters
     * @throws IllegalArgumentException if {@code shortDescription} is {@code null} or not between 10 and 100 characters
     * @throws IllegalArgumentException if {@code itemsId} is {@code null} or empty
     * @throws IllegalArgumentException if {@code price} is {@code null}
     * @throws IllegalArgumentException if {@code sellerId} is {@code null}
     * @throws IllegalArgumentException if {@code pictures} is {@code null}
     * @throws NullPointerException     if {@code saleType} is {@code null}
     */
    protected Listing(String listingTitle,
                      List<PublicationSampleId> itemsId,
                      SaleType saleType,
                      Price price,
                      String shortDescription,
                      UserId sellerId,
                      List<Picture> pictures) {

        _listingId = new ListingId(UUID.randomUUID().toString());
        _listingTitle = validateTitle(listingTitle);
        _itemsId = List.copyOf(validateItemsId(itemsId));
        _saleType = Objects.requireNonNull(saleType, "Sale type cannot be null.");
        _price = validatePrice(price);
        _shortDescription = validateDescription(shortDescription);
        _sellerId = validateSellerId(sellerId);
        _buyerId = null;
        _pictures = List.copyOf(validatePictures(pictures));
        _publicationDate = LocalDate.now();
        _listingStatus = ListingStatus.ACTIVE;

    }

    /**
     * Reconstructs an existing {@link Listing} from a known state, typically when rehydrating
     * from persistence.
     *
     * <p>This constructor is protected and intended for use by the domain factory only
     * Unlike the creation constructor, this does not generate a new identifier —
     * it uses the {@link ListingId} provided.
     *
     * <p>Domain invariants are still enforced here to guarantee the integrity of the
     * reconstructed object, even when the data originates from a trusted persistence source.
     *
     * @param listingId        the existing unique identifier of the listing; must not be {@code null}
     * @param listingTitle     the display title of the listing; must not be {@code null}
     * @param itemsId          the list of publication sample IDs grouped in this listing; must not be {@code null}
     * @param saleType         the type of sale; must not be {@code null}
     * @param price            the price of the listing; must not be {@code null}
     * @param shortDescription a brief description of the listing; must not be {@code null}
     * @param sellerId         the ID of the seller; must not be {@code null}
     * @param pictures         the list of pictures associated with the listing; must not be {@code null}
     * @param publicationDate  the original publication date of the listing; must not be {@code null}
     * @throws NullPointerException if {@code listingId}, {@code saleType}, or {@code publicationDate} are {@code null}
     */
    protected Listing(ListingId listingId,
                      String listingTitle,
                      List<PublicationSampleId> itemsId,
                      SaleType saleType,
                      Price price,
                      String shortDescription,
                      UserId sellerId,
                      UserId buyerId,
                      List<Picture> pictures,
                      LocalDate publicationDate,
                      ListingStatus listingStatus) {

        _listingId = Objects.requireNonNull(listingId, "Listing ID cannot be null.");
        _listingTitle = validateTitle(listingTitle);
        _itemsId = List.copyOf(validateItemsId(itemsId));
        _saleType = Objects.requireNonNull(saleType, "Sale type cannot be null.");
        _price = validatePrice(price);
        _shortDescription = validateDescription(shortDescription);
        _sellerId = validateSellerId(sellerId);
        _buyerId = buyerId;
        _pictures = List.copyOf(validatePictures(pictures));
        _publicationDate = Objects.requireNonNull(publicationDate, "Publication date cannot be null.");
        _listingStatus = Objects.requireNonNull(listingStatus, "Listing status cannot be null.");
    }

    // ============================================================
    // ================= BUSINESS METHODS =========================
    // ============================================================


    /**
     * Checks whether this listing is linked to an auction.
     *
     * @return {@code true} if the sale type is {@link SaleType#AUCTION}, {@code false} otherwise.
     */
    public boolean isListingLinkedToAnAuction(){
        if(_saleType.equals(SaleType.AUCTION)){
            return true;
        }
        return false;
    }

    /**
     * Checks whether this listing is linked to a direct sale.
     *
     * @return {@code true} if the sale type is {@link SaleType#DIRECT_SALE}, {@code false} otherwise.
     */
    public boolean isListingLinkedToADirectSale(){
        if(_saleType.equals(SaleType.DIRECT_SALE)){
            return true;
        }
        return false;
    }


    /**
     * Checks whether the given {@link PublicationSampleId} is present in the items ID collection.
     *
     * @param pubId the {@link PublicationSampleId} to search for
     * @return {@code true} if the collection contains the specified ID, {@code false} otherwise
     */
    public boolean containsPubSampleId(PublicationSampleId pubId){
        if(_itemsId.contains(pubId)){
            return true;
        }
        return false;
    }

    /**
     * Marks the current listing as sold by the given buyer.
     * <p>
     * This method transitions the internal state of the listing by setting
     * its status to {@link ListingStatus#SOLD} and recording the buyer's identity.
     *
     * @param buyerId the identifier of the user who purchased this listing; must not be {@code null}
     * @return the current {@link Listing} instance updated to the sold state
     * @throws NullPointerException if {@code buyerId} is {@code null}
     */
    public Listing markListingAsSold(UserId buyerId){
        Objects.requireNonNull(buyerId, "Buyer ID cannot be null.");
        _buyerId = buyerId;
        _listingStatus = ListingStatus.SOLD;
        return this;
    }


    // ============================================================
    // ============== GETTERS =====================================
    // ============================================================

    public List<PublicationSampleId> getItemsId() {
        List<PublicationSampleId> copyItemsId = new ArrayList<>(_itemsId);
        return copyItemsId;
    }

    public Price getPrice() {
        return _price;
    }

    public String getListingTitle() {
        return _listingTitle;
    }

    public UserId getSellerId() {
        return _sellerId;
    }

    public SaleType getSaleType() {
        return _saleType;
    }

    public String getShortDescription() {
        return _shortDescription;
    }

    public List<Picture> getPictures() {
        return _pictures;
    }

    public LocalDate getPublicationDate() {
        return _publicationDate;
    }

    public ListingStatus getListingStatus() {
        return _listingStatus;
    }

    /**
     * Returns the identifier of the user who purchased this listing.
     *
     * @return the buyer's {@link UserId}, or {@code null} if the listing has not been sold
     */
    public UserId getBuyerId() {
        return _buyerId;
    }

    @Override
    public ListingId id() {
        return _listingId;
    }

    // ============================================================
    // =========== EQUALS AND SAMEAS ==============================
    // ============================================================

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Listing)) return false;
        Listing other = (Listing) object;
        return this._listingId.equals(other._listingId);
    }

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
                && this._pictures.equals(other._pictures)
                && this._listingStatus == other._listingStatus
                && Objects.equals(this._buyerId, other._buyerId);
    }


    // ========== CONSTRUCTOR HELPERS ==========

    private static String validateTitle(String title) {
        if (title == null || title.length() < 5)
            throw new IllegalArgumentException("Listing title must be at least 5 characters.");
        if (title.length() > 100)
            throw new IllegalArgumentException("Listing title must be at most 100 characters.");
        return title;
    }

    private static List<PublicationSampleId> validateItemsId(List<PublicationSampleId> itemsId) {
        if (itemsId == null || itemsId.isEmpty())
            throw new IllegalArgumentException("A listing must contain at least one item ID.");
        return itemsId;
    }

    private static Price validatePrice(Price price) {
        if (price == null)
            throw new IllegalArgumentException("Price cannot be null.");
        return price;
    }

    private static String validateDescription(String description) {
        if (description == null || description.length() < 10 || description.length() > 100)
            throw new IllegalArgumentException("Listing shortDescription must be between 10 and 100 characters.");
        return description;
    }

    private static UserId validateSellerId(UserId sellerId) {
        if (sellerId == null)
            throw new IllegalArgumentException("Seller ID cannot be null.");
        return sellerId;
    }

    private static List<Picture> validatePictures(List<Picture> pictures) {
        if (pictures == null)
            throw new IllegalArgumentException("Pictures cannot be null.");
        return pictures;
    }
}
