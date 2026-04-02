package TOPSECRET.domain.DirectSale;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.ValueObjects.DirectSaleID;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a direct sale composed of a list of listings (with saleType Direct Sale), a buyer and a flag
 * indicating whether the sale has been completed. It represents a buyer's cart.
 */

public class DirectSale implements AggregateRoot<DirectSaleID> {

    private final DirectSaleID _directSaleID;
    private List<ListingId> _listings;
    private UserId _buyer;
    private boolean _isFinished;

    //==================================================================================================================
    // Constructor for DirectSale
    //==================================================================================================================

    /**
     * Constructs a DirectSale with the given listings, buyer, and completion status.
     *
     * <p>This constructor initializes a new DirectSale instance with a generated
     * unique identifier. It validates that the listings list and buyer are not null.
     * Additionally, all listings are expected to be of type DIRECT_SALE (validation
     * logic to be implemented).</p>
     *
     * @param _listings     the list of listings included in this direct sale; must not be null
     * @param _buyer        the user purchasing the listings; must not be null
     * @param _isFinished   indicates whether the direct sale has been completed
     *
     * @throws IllegalArgumentException if the listings list is null
     * @throws IllegalArgumentException if the buyer is null
     * @throws IllegalArgumentException if any listing is not of type DIRECT_SALE
     */
    protected DirectSale (List<ListingId> _listings, UserId _buyer, boolean _isFinished) {

        if(_listings == null){
            throw new IllegalArgumentException("List of listings cannot be null");
        }
        if(_buyer == null){
            throw new IllegalArgumentException("Buyer cannot be empty");
        }

        // TO BE UPDATED LATER
        // method to check is listing is of direct sale type
        boolean invalid = false;

        if (invalid) {
            throw new IllegalArgumentException(
                    "All listings must be of type DIRECT_SALE to create a DirectSale"
            );
        }


        this._directSaleID = new DirectSaleID(UUID.randomUUID().toString());
        this._listings = _listings;
        this._buyer = _buyer;
        this._isFinished = _isFinished;
    }

    //==================================================================================================================

    /**
     * Determines whether this DirectSale is equal to another object.
     * Two DirectSale instances are considered equal if they contain the same listings
     * and the same buyer. The finished state is not considered for equality.
     *
     * @param o the object to compare with
     * @return true if the given object is a DirectSale with the same listings and buyer; false otherwise
     */

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DirectSale that = (DirectSale) o;
        return Objects.equals(_listings, that._listings) && Objects.equals(_buyer, that._buyer);
    }

    /**
     * Computes the hash code for this DirectSale.
     * The hash is based on the listings and buyer.
     *
     * @return the hash code value
     */

    @Override
    public int hashCode() {
        return Objects.hash(_listings, _buyer);
    }

    /**
     * Returns the list of listings associated with this sale.
     *
     * @return the list of listings
     */

    public List<ListingId> getListings() {
        return _listings;
    }

    /**
     * Returns a snapshot of the details of this DirectSale.
     * The returned record is immutable.
     *
     * @return a DirectSaleDetails record containing listings, buyer and finished state
     */

    public DirectSaleDetails getDetails() {
        return new DirectSaleDetails(_listings, _buyer, _isFinished);
    }

    /**
     * Returns the unique identifier of this DirectSale.
     *
     * @return the DirectSaleID associated with this direct sale
     */
    @Override
    public DirectSaleID id() { return _directSaleID; }

    /**
     * Determines whether this bibliographical data shares the same identity
     * as the given object, based solely on {@link DirectSaleID}.
     *
     * @param o the object to compare against
     * @return {@code true} if both objects are {@code DirectSaleID} instances
     *         with equal identifiers; {@code false} otherwise
     */
    @Override
    public boolean sameAs(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DirectSale that = (DirectSale) o;
        return Objects.equals(_directSaleID, that._directSaleID);
    }

    /**
     * Immutable record representing the details of a DirectSale.
     *
     * @param listings   the listings included in the sale
     * @param buyer      the buyer involved
     * @param isFinished whether the sale is completed
     */

    public record DirectSaleDetails(
            List<ListingId> listings,
            UserId buyer,
            boolean isFinished
    ) {}
}