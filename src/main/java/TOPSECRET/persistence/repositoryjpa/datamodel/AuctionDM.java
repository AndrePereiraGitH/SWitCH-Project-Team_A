package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Enum.AuctionStatus;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA Data Model representing an Auction persistence entity.
 * <p>
 * This class maps the {@code auctions} table and is used exclusively
 * by the persistence layer. It acts as the database representation
 * of an {@link Auction} domain entity.
 * </p>
 * <p>
 * It contains both the initial auction configuration and its final state,
 * including bids, winningBid, and lifecycle status.
 * </p>
 */
@Entity
@Table(name = "auctions")
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuctionDM {

    @EmbeddedId
    private AuctionId auctionId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "listing_id"))
    private ListingId listingId;

    private LocalDateTime auctionStartDateTime;
    private LocalDateTime auctionEndDateTime;
    private BigDecimal initialPrice;
    private BigDecimal buyoutPrice;

    // State attributes
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "bids", joinColumns = @JoinColumn(name = "auction_id"))
    private List<BidDM> bidHistory;

    @Embedded
    private BidDM winningBid;

    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;

    private BigDecimal finalPrice;
    private LocalDateTime closedAt;

    // ============================================================
    // ============== CONSTRUCTORS ================================
    // ============================================================

    /**
     * Creates a new {@code AuctionDM} with the initial auction data.
     * <p>
     * This constructor is typically used by the assembler when converting
     * a domain {@link Auction} into its persistence representation.
     * </p>
     *
     * @param auctionId the unique identifier of the auction
     * @param listingId the identifier of the associated listing
     * @param startDate the start date and time of the auction
     * @param endDate the end date and time of the auction
     * @param initialPrice the starting price of the auction
     * @param buyoutPrice the buyout price of the auction
     */
    public AuctionDM(AuctionId auctionId, ListingId listingId, LocalDateTime startDate, LocalDateTime endDate,
                     BigDecimal initialPrice, BigDecimal buyoutPrice, List<BidDM> bidHistory, BidDM winningBid,
                     AuctionStatus auctionStatus, BigDecimal finalPrice, LocalDateTime closedAt) {
        this.auctionId = auctionId;
        this.listingId = listingId;
        this.auctionStartDateTime = startDate;
        this.auctionEndDateTime = endDate;
        this.initialPrice = initialPrice;
        this.buyoutPrice = buyoutPrice;
        this.bidHistory = bidHistory;
        this.winningBid = winningBid;
        this.auctionStatus = auctionStatus;
        this.finalPrice = finalPrice;
        this.closedAt = closedAt;
    }
}
