package TOPSECRET.domain;

import TOPSECRET.domain.Enum.AuctionStatus;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Auction {

    private Listing _listing;
    private LocalDateTime _initialDate;
    private LocalDateTime _finalDate;
    private Price _initialPrice;
    private Price _buyOutPrice;
    private List<Bid> _bidHistory;
    private AuctionStatus _auctionStatus;
    private Clock _clock;


    private Bid _winningBid;
    private Price _finalPrice;
    private LocalDateTime _closedAt;

    public Auction() {
    }

    public Auction(Listing listing, LocalDateTime initialDate, LocalDateTime finalDate, Price initialPrice, Price buyOutPrice) {
        this(listing, initialDate, finalDate, initialPrice, buyOutPrice, Clock.systemDefaultZone());
    }

    public Auction(Listing listing, LocalDateTime initialDate, LocalDateTime finalDate, Price initialPrice, Price buyOutPrice, Clock clock) {

        if (clock == null)
            throw new IllegalArgumentException("Clock cannot be null");

        this._clock = clock;
        LocalDateTime now = LocalDateTime.now(clock);

        if (listing == null)
            throw new IllegalArgumentException("Listing cannot be null.");

        if (initialDate == null || initialDate.isBefore(now))
            throw new IllegalArgumentException("Initial date must not be before the current date.");
        if (finalDate == null || finalDate.isBefore(initialDate) || finalDate.isEqual(initialDate))
            throw new IllegalArgumentException("Final date must not be before the initial date.");
        if (buyOutPrice.getPrice().compareTo(initialPrice.getPrice()) <= 0) {
            throw new IllegalArgumentException("Buy out price must be greater than Initial price.");
        }

        this._listing = listing;
        this._initialDate = initialDate;
        this._finalDate = finalDate;
        this._initialPrice = listing.getPrice();
        this._buyOutPrice = buyOutPrice;
        this._initialPrice = initialPrice;
        this._bidHistory = new ArrayList<>(); // start empty history

        _auctionStatus = initialDate.isAfter(now) ? AuctionStatus.SCHEDULED : AuctionStatus.ACTIVE;

        }


    public boolean acceptBid(Bid bid) {

        boolean added = false;
        if (bid == null)
            throw new IllegalArgumentException("Bid cannot be null");

        refreshStatus();
        closeIfExpired();

        if (_auctionStatus != AuctionStatus.ACTIVE)
            throw new IllegalStateException("Auction is not active");


        if (_bidHistory == null)
            _bidHistory = new ArrayList<>();

        if (!_bidHistory.isEmpty()) {

            Bid lastBid = _bidHistory.get(_bidHistory.size() - 1);

            if (lastBid.getValue().getPrice().compareTo(bid.getValue().getPrice()) >= 0)
                throw new IllegalArgumentException("Bid value must be greater than last bid value: " + lastBid.getValue());
        }

        if (bid.getValue().getPrice().compareTo(_initialPrice.getPrice()) <= 0)
            throw new IllegalArgumentException("Bid value must be greater than initial Price.");

        _bidHistory.add(bid);
        _winningBid = bid;
        _finalPrice = bid.getValue();
        added = true;

        if (bid.getValue().getPrice().equals(_buyOutPrice.getPrice())) {
            _auctionStatus = AuctionStatus.BOUGHT_OUT;
            _closedAt = LocalDateTime.now(_clock);
            _finalPrice = _buyOutPrice;
        }

        return added;

    }

    public void refreshStatus() {
        if (_clock == null) _clock = Clock.systemDefaultZone();

        if (isTerminalStatus(_auctionStatus)) return;

        LocalDateTime now = LocalDateTime.now(_clock);

        if (now.isBefore(_initialDate)) {
            _auctionStatus = AuctionStatus.SCHEDULED;
            return;
        }

        if (now.isBefore(_finalDate)) {
            _auctionStatus = AuctionStatus.ACTIVE;
        }
    }

    private boolean isTerminalStatus(AuctionStatus auctionStatus) {

        if (auctionStatus == AuctionStatus.CANCELLED
                || auctionStatus == AuctionStatus.ENDED_WITH_WINNER
                || auctionStatus == AuctionStatus.ENDED_WITHOUT_BIDS
                || auctionStatus == AuctionStatus.BOUGHT_OUT) {
            return true;
        }
        return false;
    }

    private boolean closeIfExpired() {

        if (_clock == null)
            _clock = Clock.systemDefaultZone();

        if (isTerminalStatus(_auctionStatus))
            return false;

        LocalDateTime now = LocalDateTime.now(_clock);

        if (now.isBefore(_finalDate))
            return false;

        if (_bidHistory == null || _bidHistory.isEmpty()) {
            _auctionStatus = AuctionStatus.ENDED_WITHOUT_BIDS;
            _winningBid = null;
            _finalPrice = null;

        } else {
            _winningBid = _bidHistory.get(_bidHistory.size() - 1);
            _finalPrice = _winningBid.getValue();
            _auctionStatus = AuctionStatus.ENDED_WITH_WINNER;

        }

        _closedAt = now;
        return true;
    }

    public List<Bid> get_bidHistory() {
        return _bidHistory;
    }

    public AuctionStatus getAuctionStatus() {
        return _auctionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return Objects.equals(_listing, auction._listing)
                && Objects.equals(_initialDate, auction._initialDate)
                && Objects.equals(_finalDate, auction._finalDate)
                && Objects.equals(_initialPrice, auction._initialPrice)
                && Objects.equals(_buyOutPrice, auction._buyOutPrice)
                && Objects.equals(_bidHistory, auction._bidHistory)
                && _auctionStatus == auction._auctionStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_listing, _initialDate, _finalDate, _initialPrice, _buyOutPrice, _bidHistory, _auctionStatus);
    }
}
