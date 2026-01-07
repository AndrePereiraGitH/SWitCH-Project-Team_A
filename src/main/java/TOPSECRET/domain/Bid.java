package TOPSECRET.domain;

import java.time.LocalDateTime;

public class Bid {
    private User _bidder;
    private Price _value; //
    private LocalDateTime _timeOfBid;
    // Does this class needs to have a reference to auction, like a type of auction id?


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

    public User getBidder(){  // TODO Correct
        return _bidder;
    }

    public Price getValue(){  // TODO Correct
        return _value;
    }


    public LocalDateTime getTimeOfBid(){ // TODO Correct
        return _timeOfBid;
    }

    public String getBidInfo() {
        return "Bidder: " + getBidder().getUserName() +
                ", Value: " + getValue().getPrice() +
                ", Time: " + getTimeOfBid();
    }
}
