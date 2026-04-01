package TOPSECRET.domain.Transaction;

import TOPSECRET.domain.Auction;
import TOPSECRET.domain.DirectSale.DirectSale;
import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.Price;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Represents a commercial transaction within the system.
 * <p>
 * A {@code Transaction} models the financial and ownership exchange
 * resulting either from an {@link Auction} or a {@link DirectSale}.
 * <p>
 * Each transaction records:
 * <ul>
 *     <li>The buyer</li>
 *     <li>The sellers involved</li>
 *     <li>The agreed price</li>
 *     <li>The transaction date</li>
 *     <li>The payment date</li>
 *     <li>The current transaction status</li>
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
public class Transaction {

    private User _buyer;
    private Auction _auction;
    private DirectSale _directSale;
    private Price _price;
    private LocalDateTime _transactionDate;
    private LocalDateTime _paymentDate;
    private List<User> _sellers;
    private TransactionStatus _status;

    /** Private constructor with commun logic
     *
     * This way code will not be repeated
     *
     * @param buyer
     * @param price
     * @param transactionDate
     * @param paymentDate
     * @param sellers
     * @param status
     */

    private Transaction(User buyer, Price price, LocalDateTime transactionDate, LocalDateTime paymentDate, List<User> sellers, TransactionStatus status){

        if (buyer == null){
            throw new IllegalArgumentException("Buyer cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        if (transactionDate == null || transactionDate.isAfter(LocalDateTime.now())){
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        if (paymentDate == null || paymentDate.isAfter(LocalDateTime.now())){
            throw new IllegalArgumentException("Future Payment date cannot be null");
        }
        if (sellers == null){
            throw new IllegalArgumentException("Seller list cannot be null");
        }

        this._buyer = buyer;
        this._price = price;
        this._transactionDate = transactionDate;
        this._paymentDate = paymentDate;
        this._sellers = sellers;
        this._status = status;

    }

    /**
     * Default constructor.
     * <p>
     * Intended for framework usage or future extensions.
     */
    public Transaction() {}

    // ====================================
    /**
     * Constructs a transaction resulting from an auction.
     *
     * @param buyer the user who won the auction and initiated the purchase
     * @param auction the auction associated with this transaction
     * @param price the final agreed price
     * @param transactionDate the date and time when the transaction occurred
     * @param paymentDate the date and time when payment was completed
     * @param sellers the list of sellers involved in the transaction
     * @param status the initial transaction status
     *
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>{@code auction} is null</li>
     *     <li>{@code buyer} is null</li>
     *     <li>{@code price} is null</li>
     *     <li>{@code transactionDate} is null or in the future</li>
     *     <li>{@code paymentDate} is null or in the future</li>
     *     <li>{@code sellers} is null</li>
     * </ul>
     */
    public Transaction(User buyer, Auction auction, Price price, LocalDateTime transactionDate, LocalDateTime paymentDate, List<User> sellers, TransactionStatus status) {

        this(buyer, price, transactionDate, paymentDate, sellers, status);

        if (auction == null){
            throw new IllegalArgumentException("Auction cannot be null");
        }
        this._auction = auction;

    }


    // ====================================
    /**
     * Constructs a transaction resulting from a direct sale.
     *
     * @param buyer the purchasing user
     * @param price the agreed price of the sale
     * @param directSale the direct sale instance associated with the transaction
     * @param transactionDate the date and time when the transaction occurred
     * @param paymentDate the date and time when payment was completed
     * @param sellers the list of sellers involved in the transaction
     *
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>{@code directSale} is null</li>
     *     <li>{@code buyer} is null</li>
     *     <li>{@code price} is null</li>
     *     <li>{@code transactionDate} is null or in the future</li>
     *     <li>{@code paymentDate} is null or in the future</li>
     *     <li>{@code sellers} is null</li>
     * </ul>
     */
    public Transaction(User buyer, Price price, DirectSale directSale, LocalDateTime transactionDate, LocalDateTime paymentDate, List<User> sellers, TransactionStatus status){

        this(buyer, price, transactionDate, paymentDate, sellers, status);
        if (directSale == null){
            throw new IllegalArgumentException("Cart cannot be null");
        }
        this._directSale = directSale;

    }

}

