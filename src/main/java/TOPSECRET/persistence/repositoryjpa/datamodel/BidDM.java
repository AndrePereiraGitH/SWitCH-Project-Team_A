package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA Data Model representing a Bid persistence entity.
 * <p>
 * This class maps the {@code bids} table and is used exclusively by the
 * persistence layer. It represents the database model of a bid associated
 * with an {@link AuctionDM}.
 * </p>
 * <p>
 * It contains bid metadata such as bidder identity, value, timestamp,
 * and its relationship to an auction.
 * </p>
 */
@Entity
@Table(name = "bids")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidDM {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bidId;

    @ManyToOne
    @JoinColumn(name = "auction_id")
    private AuctionDM auction;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "bidder_id"))
    private UserId bidderId;

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
