package TOPSECRET.domain;

import TOPSECRET.domain.Enum.TransactionStatus;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Represents a financial transaction in the system.
 *
 * <p>A Transaction records the result of a sale or auction, including:
 * - the buyer
 * - the seller
 * - the final price
 * - the date when the transaction occurred
 * - the listing that was sold
 *
 * <p>This class belongs to the domain layer and contains business data
 * related to a completed sale.
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
    // private SupportService _supportService;  //TODO WAITING FOR CONFIRMATION
    // private LogisticsServices _logisticsServices;   //TODO WAITING FOR CONFIRMATION

    public Transaction() {}

    // ====================================
    // Constructor for Auction
    public Transaction(User buyer, Auction auction, Price price, LocalDateTime transactionDate, LocalDateTime paymentDate, List<User> sellers, TransactionStatus status) {


        if (auction == null){
            throw new IllegalArgumentException("Auction cannot be null");
        }
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
        this._auction = auction;
        this._price = price;
        this._transactionDate = transactionDate;
        this._paymentDate = paymentDate;
        this._sellers = sellers;
        this._status = TransactionStatus.PENDING;
        // this._supportService = supportService;
        // this._logisticsServices = logisticsServices;
    }


    // ====================================
    // Constructor for direct sale
    public Transaction(User buyer, Price price, DirectSale directSale, LocalDateTime transactionDate, LocalDateTime paymentDate, List<User> sellers){

        if (directSale == null){
            throw new IllegalArgumentException("Cart cannot be null");
        }
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
            throw new IllegalArgumentException("Transaction payment date cannot be null");
        }
        if (sellers == null){
            throw new IllegalArgumentException("Seller list cannot be null");
        }

        this._buyer = buyer;
        this._directSale = directSale;
        this._price = price;
        this._transactionDate = transactionDate;
        this._paymentDate = paymentDate;
        this._sellers = sellers;
        this._status = TransactionStatus.PENDING;
        // this._supportService = supportService;
        // this._logisticsServices = logisticsServices;

    }

}

