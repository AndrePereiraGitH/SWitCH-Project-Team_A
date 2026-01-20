package TOPSECRET.domain;

import TOPSECRET.domain.Enum.TransactionStatus;

import java.time.LocalDateTime;
import java.util.List;

public class Transaction {

    private User _buyer;
    private Auction _auction;
    private Cart _cart;
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
    // Constructor for Cart (direct sale)
    public Transaction(User buyer, Price price, Cart cart, LocalDateTime transactionDate, LocalDateTime paymentDate, List<User> sellers){

        if (cart == null){
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
        this._cart = cart;
        this._price = price;
        this._transactionDate = transactionDate;
        this._paymentDate = paymentDate;
        this._sellers = sellers;
        this._status = TransactionStatus.PENDING;
        // this._supportService = supportService;
        // this._logisticsServices = logisticsServices;

    }

    // ====================================
    // Getters

    /*public LocalDateTime getTransactionDate() {
        return this._transactionDate;
    }
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public List<String> getSellers() {
        return this._sellers;
    }

    public double getTotalAmount() {
        return this._totalAmount;
    }

    public String getBuyer(){
        return this.buyer;
    }

    public String getAuction(){
        return this.auction;
    }

    public String getCart(){
        return this.cart;
    }*/

}

