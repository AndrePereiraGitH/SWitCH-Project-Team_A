package TOPSECRET.domain;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.Price;

import java.time.LocalDateTime;

/**
 * Represents a bid placed by a user in an auction.
 * A bid contains information about the bidder, the offered price,
 * and the exact moment the bid was placed.
 */
public class Bid {
    private User _bidder;
    private Price _value; //
    private LocalDateTime _timeOfBid;


    /**
     * Creates a new bid with the specified bidder and value.
     * The time of the bid is automatically set to the current date and time.
     *
     * @param bidder the user placing the bid (must not be null)
     * @param value the price offered in the bid (must not be null)
     * @throws IllegalArgumentException if bidder is null
     * @throws IllegalArgumentException if value is null
     */
    public Bid (User bidder, Price value) {

        //Considered that the bidder can't be null
        if (bidder == null) {
            throw new IllegalArgumentException("Bidder can't be null.");
        }

        //Considered that the value can't be null
        if (value == null) {
            throw new IllegalArgumentException("Bid value can't be null.");
        }

        this._bidder = bidder;
        this._value = value;
        this._timeOfBid = LocalDateTime.now(); // Generated at the moment of the bid
    }

    /**
     * Returns the user who placed this bid.
     *
     * @return the bidder
     */
    public User getBidder(){
        return _bidder;
    }

    /**
     * Returns the price offered in this bid.
     *
     * @return the bid value
     */
    public Price getValue(){
        return _value;
    }

    /**
     * Returns the date and time when this bid was placed.
     *
     * @return the timestamp of the bid
     */
    public LocalDateTime getTimeOfBid(){
        return _timeOfBid;
    }

    /**
     * Returns a formatted string with the bid details,
     * including the bidder's username, the bid value, and the time of the bid.
     *
     * @return a string representation of the bid information
     */
    public String getBidInfo() {
        return "Bidder: " + getBidder().getUserName() +
                ", Value: " + getValue().getPrice() +
                ", Time: " + getTimeOfBid();
    }
}
