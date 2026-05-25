package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Embeddable JPA Data Model representing a Bid in the persistence layer.
 * <p>
 * This class is used exclusively by the persistence layer as part of the
 * {@link AuctionDM} aggregate. It maps bid information into the database
 * as an embeddable value object rather than a standalone entity.
 * </p>
 *
 * <p>
 * A {@code BidDM} captures immutable bid data including:
 * <ul>
 *     <li>the bidder identity ({@link UserId})</li>
 *     <li>the bid monetary value</li>
 *     <li>the timestamp when the bid was placed</li>
 * </ul>
 * </p>
 *
 * <p>
 * This class does not contain business logic and is not responsible for
 * enforcing domain rules. It is strictly a persistence representation
 * of a domain {@code Bid}.
 * </p>
 *
 * <p>
 * Instances of this class are typically created by the assembler when
 * converting a domain {@code Bid} into a persistence model, or when
 * reconstructing an {@code AuctionDM} from the database.
 * </p>
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidDM {

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "bidder_id"))
    private UserId bidderId;

    @Column(name = "bid_value")
    private BigDecimal value;

    private LocalDateTime timeOfBid;

    // ============================================================
    // ============== CONSTRUCTORS ================================
    // ============================================================

    /**
     * Creates a new {@code BidDM} instance.
     * <p>
     * This constructor is typically used by the assembler and tests when
     * converting a domain bid into its persistence representation.
     * </p>
     *
     * @param bidderId the identifier of the user who placed the bid
     * @param value the monetary value of the bid
     * @param timeOfBid the timestamp when the bid was placed
     */
    public BidDM(UserId bidderId, BigDecimal value, LocalDateTime timeOfBid) {
        this.bidderId = bidderId;
        this.value = value;
        this.timeOfBid = timeOfBid;
    }
}
