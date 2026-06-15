package TOPSECRET.domain.Transaction;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.ValueObjects.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Represents a transaction aggregate in the domain.
 * <p>
 * A transaction records the purchase of one or more listings by a buyer,
 * including payment information, shipping address, transaction date,
 * total price, and current transaction status.
 * </p>
 *
 * <p>
 * This class is an Aggregate Root identified by a unique
 * {@link TransactionID}.
 * </p>
 */
public class Transaction implements AggregateRoot<TransactionID> {

    private final TransactionID _id;

    private UserId _buyerId;
    private Price _price;
    private LocalDateTime _transactionDate;
    private Address _shippingAddress;
    private PaymentDetails _paymentDetails;
    private TransactionStatus _status;
    private List<ListingId> _listingIds;

    /**
     * Creates a new transaction.
     *
     * @param buyerId the identifier of the buyer performing the transaction
     * @param price the total price of the transaction
     * @param transactionDate the date and time when the transaction occurred
     * @param shippingAddress the address where purchased items will be delivered
     * @param paymentDetails the payment information associated with the transaction
     * @param status the current status of the transaction
     * @param listingIds the identifiers of the purchased listings
     *
     * @throws IllegalArgumentException if buyerId, price, transactionDate,
     *                                  shippingAddress, paymentDetails,
     *                                  or listingIds are null
     */
    protected Transaction(UserId buyerId,
                          Price price,
                          LocalDateTime transactionDate,
                          Address shippingAddress,
                          PaymentDetails paymentDetails,
                          TransactionStatus status,
                          List<ListingId> listingIds) {

        if (buyerId == null) {
            throw new IllegalArgumentException("buyerId cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("price cannot be null");
        }
        if (transactionDate == null || transactionDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("transactionDate cannot be null");
        }
        if (shippingAddress == null) {
            throw new IllegalArgumentException("shippingAddress cannot be null");
        }
        if (paymentDetails == null) {
            throw new IllegalArgumentException("paymentDetails cannot be null");
        }
        if (listingIds == null) {
            throw new IllegalArgumentException("listingIds cannot be null");
        }

        this._id = new TransactionID(UUID.randomUUID().toString());
        this._buyerId = buyerId;
        this._price = price;
        this._transactionDate = transactionDate;
        this._shippingAddress = shippingAddress;
        this._paymentDetails = paymentDetails;
        this._status = status;
        this._listingIds = listingIds;
    }

    /**
     * Reconstructs a transaction from persistence storage.
     * <p>
     * Intended to be used by JPA or repository implementations when
     * loading an existing transaction.
     * </p>
     *
     * @param id the unique transaction identifier
     * @param buyerId the identifier of the buyer
     * @param price the total transaction price
     * @param transactionDate the date and time of the transaction
     * @param shippingAddress the shipping address associated with the transaction
     * @param paymentDetails the payment information used in the transaction
     * @param status the current transaction status
     * @param listingIds the identifiers of the purchased listings
     *
     * @throws IllegalArgumentException if id, buyerId, price,
     *                                  transactionDate, shippingAddress,
     *                                  paymentDetails, or listingIds are null
     */
    protected Transaction(TransactionID id,
                          UserId buyerId,
                          Price price,
                          LocalDateTime transactionDate,
                          Address shippingAddress,
                          PaymentDetails paymentDetails,
                          TransactionStatus status,
                          List<ListingId> listingIds) {

        if (id == null) {
            throw new IllegalArgumentException("transaction id cannot be null");
        }
        if (buyerId == null) {
            throw new IllegalArgumentException("buyerId cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("price cannot be null");
        }
        if (transactionDate == null || transactionDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("transactionDate cannot be null");
        }
        if (shippingAddress == null) {
            throw new IllegalArgumentException("shippingAddress cannot be null");
        }
        if (paymentDetails == null) {
            throw new IllegalArgumentException("paymentDetails cannot be null");
        }
        if (listingIds == null) {
            throw new IllegalArgumentException("listingIds cannot be null");
        }

        this._id = id;
        this._buyerId = buyerId;
        this._price = price;
        this._transactionDate = transactionDate;
        this._shippingAddress = shippingAddress;
        this._paymentDetails = paymentDetails;
        this._status = status;
        this._listingIds = listingIds;
    }

    public UserId getBuyerId() { return _buyerId; }

    public Price getPrice() { return _price; }

    public LocalDateTime getTransactionDate() { return _transactionDate; }

    public Address getShippingAddress() { return _shippingAddress; }

    public PaymentDetails getPaymentDetails() { return _paymentDetails; }

    public TransactionStatus getStatus() { return _status; }

    public List<ListingId> getListingIds() { return _listingIds; }

    @Override
    public TransactionID id() {
        return _id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Transaction)) return false;

        Transaction other = (Transaction) obj;
        return _id.equals(other._id);
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }

    @Override
    public boolean sameAs(Object obj) {
        if (!(obj instanceof Transaction)) {
            return false;
        }

        Transaction newObj = (Transaction) obj;

        return this._id.equals(newObj._id) &&
                this._shippingAddress.equals(newObj._shippingAddress) &&
                this._buyerId.equals(newObj._buyerId) &&
                this._price.equals(newObj._price) &&
                this._transactionDate.equals(newObj._transactionDate) &&
                this._status.equals(newObj._status) &&
                this._paymentDetails.equals(newObj._paymentDetails) &&
                this._listingIds.equals(newObj._listingIds);
    }
}