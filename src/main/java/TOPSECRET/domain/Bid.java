package TOPSECRET.domain;

import org.apache.commons.lang3.Validate;

import java.time.LocalDateTime;

public class Bid {
    private User _bidder;
    private double _value;
    private LocalDateTime _timeOfBid;


    public Bid (User bidder, double value, LocalDateTime timeOfBid) {

        //Considered that the bidder can't be null
        if (bidder == null) {
            throw new IllegalArgumentException("Bidder can't be null.");
        }

        // Value cannot be negative
        if (value <= 0) {
            throw new IllegalArgumentException("Bid value must be positive.");
        }

        // timeOfBid cannot be null
        if (timeOfBid == null) {
            throw new IllegalArgumentException("Bid date/time cannot be null.");
        }

        // Making sure that the date is not in the future
        if (timeOfBid.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Bid date/time cannot be in the future.");
        }

        this._bidder = bidder;
        this._value = value;
        this._timeOfBid = timeOfBid;
    }

    public User getBidder(){  // Correct
        return _bidder;
    }

    public double getValue(){  // Correct
        return _value;
    }


    public String getBidInfo() { // Correct
      return "Bidder: " + getBidder().getUserName() + ", Value: " + getValue() + ", Time: " + getTimeOfBid();
    }

    public LocalDateTime getTimeOfBid(){ // Correct
        return _timeOfBid;
    }

}
