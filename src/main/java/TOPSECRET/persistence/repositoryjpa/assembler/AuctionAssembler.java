package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.AuctionFactory;
import TOPSECRET.domain.Auction.Bid;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.datamodel.AuctionDM;
import TOPSECRET.persistence.repositoryjpa.datamodel.BidDM;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Assembler responsible for converting between the domain model
 * ({@link Auction}) and the persistence model ({@link AuctionDM}),
 * and vice versa.
 * <p>
 * This class acts as a translation layer between the domain and
 * infrastructure layers, ensuring that the domain model remains
 * persistence-agnostic.
 * </p>
 * <p>
 * It handles the mapping of:
 * <ul>
 *     <li>Auction core attributes</li>
 *     <li>Bid history and relationships</li>
 *     <li>Auction state (status, final price, winner, closing time)</li>
 * </ul>
 * </p>
 */
@Service
public class AuctionAssembler {

    AuctionFactory auctionFactory;

    public AuctionAssembler(AuctionFactory auctionFactory) {
        if(auctionFactory == null) {
            throw new IllegalArgumentException("auctionFactory cannot be null");
        }
        this.auctionFactory = auctionFactory;
    }

    // ============================================================
    // ================ toDM ======================================
    // ============================================================

    /**
     * Converts a domain {@link Auction} into its persistence representation
     * {@link AuctionDM}.
     * <p>
     * This method maps all relevant auction data, including:
     * <ul>
     *     <li>Basic auction attributes (listing, prices, dates)</li>
     *     <li>Bid history and relationships</li>
     *     <li>Auction state (status, final price, winning bid, closure time)</li>
     * </ul>
     * </p>
     *
     * @param auction the domain auction to convert; may be {@code null}
     * @return the corresponding {@link AuctionDM}, or {@code null} if input is {@code null}
     */
    public AuctionDM toDM(Auction auction) {

        if (auction == null) return null;

        List<BidDM> bidHistoryDM = new ArrayList<>();

        for (Bid bid: auction.getBidHistory()) {
            BidDM bidDM = new BidDM(bid.getBidderId(), bid.getValue().getPrice(), bid.getTimeOfBid());
            bidHistoryDM.add(bidDM);
        }

        BidDM winningBidDM = new BidDM(
                auction.getWinningBid().getBidderId(),
                auction.getWinningBid().getValue().getPrice(),
                auction.getWinningBid().getTimeOfBid());

        return new AuctionDM(
                auction.id(),
                auction.getListingId(),
                auction.getAuctionStartDateTime(),
                auction.getAuctionEndDateTime(),
                auction.getInitialPrice().getPrice(),
                auction.getBuyoutPrice() != null ? auction.getBuyoutPrice().getPrice() : null,
                bidHistoryDM,
                winningBidDM,
                auction.getAuctionStatus(),
                auction.getFinalPrice() != null ? auction.getFinalPrice().getPrice() : null,
                auction.getClosedAt());
    }

    // ============================================================
    // ================ toDomain ==================================
    // ============================================================

    /**
     * Converts a persistence {@link AuctionDM} into its domain representation
     * {@link Auction}.
     * <p>
     * This method reconstructs the full domain aggregate, including:
     * <ul>
     *     <li>Core auction data (listing, prices, dates)</li>
     *     <li>Bid history (reconstructed via factory)</li>
     *     <li>Auction state (status, final price, closure time)</li>
     * </ul>
     * </p>
     *
     * <p>
     * The winning bid is re-derived internally during state rehydration,
     * ensuring domain consistency.
     * </p>
     *
     * @param auctionDM the persistence model to convert; may be {@code null}
     * @return the reconstructed {@link Auction}, or {@code null} if input is {@code null}
     */
    public Auction toDomain(AuctionDM auctionDM) {

        if (auctionDM == null) return null;

        Price initialPrice = new Price(auctionDM.getInitialPrice());
        Price buyoutPrice = auctionDM.getBuyoutPrice() != null
                ? new Price(auctionDM.getBuyoutPrice())
                : null;

        Auction auction = auctionFactory.create(
                auctionDM.getAuctionId(),
                auctionDM.getListingId(),
                auctionDM.getAuctionStartDateTime(),
                auctionDM.getAuctionEndDateTime(),
                initialPrice,
                buyoutPrice
        );

        // state attributes
        List<Bid> bidHistory = new ArrayList<>();
        for (BidDM bidDM : auctionDM.getBidHistory()) {
            Bid bid = auction.rehydrateBid(bidDM.getBidderId(), new Price(bidDM.getValue()), bidDM.getTimeOfBid());
            bidHistory.add(bid);
        }

        Bid winningBid = auction.rehydrateBid(
                auctionDM.getWinningBid().getBidderId(),
                new Price(auctionDM.getWinningBid().getValue()),
                auctionDM.getWinningBid().getTimeOfBid()
        );

        Price finalPrice = auctionDM.getFinalPrice() != null
                ? new Price(auctionDM.getFinalPrice())
                : null;

        if (auctionDM.getAuctionStatus() != null) {
            auction.rehydrateState(
                    bidHistory,
                    winningBid,
                    auctionDM.getAuctionStatus(),
                    finalPrice,
                    auctionDM.getClosedAt()
            );
        }

        return auction;
    }
}
