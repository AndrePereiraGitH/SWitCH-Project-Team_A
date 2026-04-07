package TOPSECRET.domain.Transaction;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.DirectSale.DirectSale;
import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.ValueObjects.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Represents a commercial transaction within the system.
 * <p>
 * A {@code Transaction} models the financial and ownership exchange
 * resulting either from an {@link Auction} or a {@link DirectSale}.
 * </ul>
 * <p>
 * A transaction can be created through:
 * <ul>
 *     <li>An auction-based purchase</li>
 *     <li>A direct sale purchase</li>
 * </ul>
 * <p>
 * The transaction status is initialized as {@link TransactionStatus#PENDING}.
 */
public class Transaction implements AggregateRoot<TransactionID> {

    private final TransactionID _id;

    private UserId _buyerId;
    private AuctionId _auctionId;
    private DirectSaleID _directSaleId;
    private Price _price;
    private LocalDateTime _transactionDate;
    private LocalDateTime _paymentDate;
    private List<UserId> _sellersId;
    private TransactionStatus _status;

    /**
     * Default constructor.
     * <p>
     * Intended for framework usage or future extensions.
     */
    public Transaction() {

        this._id = new TransactionID(UUID.randomUUID().toString());
    }

    /**
     * Constructs a transaction resulting from an auction.
     *
     * @param buyerId         the user who won the auction and initiated the purchase
     * @param auctionId       the auction associated with this transaction
     * @param price           the final agreed price
     * @param transactionDate the date and time when the transaction occurred
     * @param paymentDate     the date and time when payment was completed
     * @param sellersId       the list of sellersId involved in the transaction
     * @param status          the initial transaction status
     * @throws IllegalArgumentException if:
     *                                  <ul>
     *                                      <li>{@code auction} is null</li>
     *                                      <li>{@code buyerId} is null</li>
     *                                      <li>{@code price} is null</li>
     *                                      <li>{@code transactionDate} is null or in the future</li>
     *                                      <li>{@code paymentDate} is null or in the future</li>
     *                                      <li>{@code sellersId} is null</li>
     *                                  </ul>
     */
    protected Transaction(UserId buyerId, AuctionId auctionId, Price price, LocalDateTime transactionDate,
                          LocalDateTime paymentDate, List<UserId> sellersId, TransactionStatus status) {

        this(buyerId, price, transactionDate, paymentDate, sellersId, status);

        if (auctionId == null) {
            throw new IllegalArgumentException("Auction cannot be null");
        }

        this._auctionId = auctionId;
    }

    // ====================================

    /**
     * Constructs a transaction resulting from a direct sale.
     *
     * @param buyer           the purchasing user
     * @param price           the agreed price of the sale
     * @param directSaleId    the direct sale instance associated with the transaction
     * @param transactionDate the date and time when the transaction occurred
     * @param paymentDate     the date and time when payment was completed
     * @param sellersId       the list of sellersId involved in the transaction
     * @throws IllegalArgumentException if:
     *                                  <ul>
     *                                      <li>{@code directSaleId} is null</li>
     *                                      <li>{@code buyer} is null</li>
     *                                      <li>{@code price} is null</li>
     *                                      <li>{@code transactionDate} is null or in the future</li>
     *                                      <li>{@code paymentDate} is null or in the future</li>
     *                                      <li>{@code sellersId} is null</li>
     *                                  </ul>
     */
    protected Transaction(UserId buyer, Price price, DirectSaleID directSaleId, LocalDateTime transactionDate,
                          LocalDateTime paymentDate, List<UserId> sellersId, TransactionStatus status) {


        this(buyer, price, transactionDate, paymentDate, sellersId, status);
        if (directSaleId == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
        this._directSaleId = directSaleId;

    }

    /**
     * Private constructor with common logic
     * <p>
     * This way code will not be repeated
     *
     * @param buyerId         the identifier of the buyer involved in the transaction
     * @param price           the agreed price of the transaction
     * @param transactionDate the date and time when the transaction occurred
     * @param paymentDate     the date and time when the payment was completed
     * @param sellersId       the list of seller identifiers involved in the transaction
     * @param status          the initial status of the transaction
     */

    private Transaction(UserId buyerId, Price price, LocalDateTime transactionDate, LocalDateTime paymentDate,
                        List<UserId> sellersId, TransactionStatus status) {

        if (buyerId == null) {
            throw new IllegalArgumentException("Buyer cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        if (transactionDate == null || transactionDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        if (paymentDate == null || paymentDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Future Payment date cannot be null");
        }
        if (sellersId == null) {
            throw new IllegalArgumentException("Seller list cannot be null");
        }

        if (status == null) {
            throw new IllegalArgumentException("TransactionStatus cannot be null");
        }

        this._id = new TransactionID(UUID.randomUUID().toString());

        this._buyerId = buyerId;
        this._price = price;
        this._transactionDate = transactionDate;
        this._paymentDate = paymentDate;
        this._sellersId = sellersId;
        this._status = status;
    }

    @Override
    public TransactionID id() {
        return _id;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj instanceof Transaction) {
            Transaction newObj = (Transaction) obj;

            if (this._id.equals(newObj._id))
                return true;
        }
        return false;
    }

    @Override
    public boolean sameAs(Object obj) {
        if (!(obj instanceof Transaction)) {
            return false;
        }

        Transaction newObj = (Transaction) obj;

        return this._buyerId.equals(newObj._buyerId) &&
                this._price.equals(newObj._price) &&
                this._transactionDate.equals(newObj._transactionDate) &&
                this._paymentDate.equals(newObj._paymentDate) &&
                this._sellersId.equals(newObj._sellersId) &&
                this._status.equals(newObj._status);

    }
}

