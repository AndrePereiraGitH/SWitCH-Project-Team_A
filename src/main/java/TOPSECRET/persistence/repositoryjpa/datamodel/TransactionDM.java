package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.ValueObjects.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * JPA data model representing a transaction as persisted in the
 * {@code transactions} table.
 *
 * <p>This class is a persistence-layer entity and should not be used directly
 * in domain logic. Domain objects should be mapped to and from this class
 * via an assembler or mapper.</p>
 *
 * <p>Instantiation is restricted to the provided constructor — the no-argument
 * constructor is reserved for JPA's internal use only.</p>
 */
@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionDM {

    @EmbeddedId
    @AttributeOverride(name = "transaction_id", column = @Column(name = "transaction_id"))
    private TransactionID transactionId;

    @Embedded
    @AttributeOverride(name = "userId", column = @Column(name = "buyer_id"))
    private UserId buyerId;

    @Embedded
    @AttributeOverride(name = "price", column = @Column(name = "price"))
    private Price price;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street",  column = @Column(name = "shipping_street")),
            @AttributeOverride(name = "number",  column = @Column(name = "shipping_number")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "shipping_zip_code")),
            @AttributeOverride(name = "cityID.cityID", column = @Column(name = "shipping_city_id"))
    })
    private Address shippingAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "cardHolderName",   column = @Column(name = "payment_card_holder_name")),
            @AttributeOverride(name = "maskedCardNumber", column = @Column(name = "payment_masked_card_number")),
            @AttributeOverride(name = "expiryMonth",      column = @Column(name = "payment_expiry_month")),
            @AttributeOverride(name = "expiryYear",       column = @Column(name = "payment_expiry_year"))
    })
    private PaymentDetails paymentDetails;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @ElementCollection
    @CollectionTable(
            name = "transaction_listing_ids",
            joinColumns = @JoinColumn(name = "transaction_id")
    )
    @AttributeOverride(name = "listingId", column = @Column(name = "listing_id"))
    private List<ListingId> listingIds;

    /**
     * Constructs a fully initialized {@code TransactionDM}.
     *
     * <p>All parameters are mandatory — passing {@code null} will throw a
     * {@link NullPointerException}.</p>
     *
     * @param transactionId  the unique identifier for this transaction; must not be {@code null}
     * @param buyerId        the identifier of the buyer; must not be {@code null}
     * @param price          the total transaction price; must not be {@code null}
     * @param transactionDate the date and time of the transaction; must not be {@code null}
     * @param shippingAddress the shipping address; must not be {@code null}
     * @param paymentDetails  the payment details; must not be {@code null}
     * @param status          the current transaction status; must not be {@code null}
     * @param listingIds      the list of purchased listing identifiers; must not be {@code null}
     * @throws NullPointerException if any parameter is {@code null}
     */
    public TransactionDM (TransactionID transactionId,
                         UserId buyerId,
                         Price price,
                         LocalDateTime transactionDate,
                         Address shippingAddress,
                         PaymentDetails paymentDetails,
                         TransactionStatus status,
                         List<ListingId> listingIds) {

        this.transactionId   = Objects.requireNonNull(transactionId,   "TransactionID cannot be null");
        this.buyerId         = Objects.requireNonNull(buyerId,         "BuyerId cannot be null");
        this.price           = Objects.requireNonNull(price,           "Price cannot be null");
        this.transactionDate = Objects.requireNonNull(transactionDate, "TransactionDate cannot be null");
        this.shippingAddress = Objects.requireNonNull(shippingAddress, "ShippingAddress cannot be null");
        this.paymentDetails  = Objects.requireNonNull(paymentDetails,  "PaymentDetails cannot be null");
        this.status          = Objects.requireNonNull(status,          "TransactionStatus cannot be null");
        this.listingIds      = Objects.requireNonNull(listingIds,      "ListingIds cannot be null");
    }
}
