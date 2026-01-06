package TOPSECRET.domain;

import java.time.LocalDateTime;

public class Bid {
    private User _bidder;
    private double _value;
    private LocalDateTime _timeOfBid;
    // Does this class has a need to have a reference to auction, like a type of auction id?


    public Bid (User bidder, double value) {

        //Considered that the bidder can't be null
        if (bidder == null) {
            throw new IllegalArgumentException("Bidder can't be null.");
        }

        // Value cannot be negative
        if (value <= 0) {
            throw new IllegalArgumentException("Bid value must be positive.");
        }

        this._bidder = bidder;
        this._value = value;
        this._timeOfBid = LocalDateTime.now(); // Generated at the moment of the bid
    }

    public User getBidder(){  // TODO Correct
        return _bidder;
    }

    public double getValue(){  // TODO Correct
        return _value;
    }


    public String getBidInfo() { // TODO Correct
      return "Bidder: " + getBidder().getUserName() + ", Value: " + getValue() + ", Time: " + getTimeOfBid();
    }

    public LocalDateTime getTimeOfBid(){ // Correct
        return _timeOfBid;
    }

}
