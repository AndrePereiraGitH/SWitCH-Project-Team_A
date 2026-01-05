package TOPSECRET.domain;

import java.time.LocalDateTime;

public class Bid {
    private User bidder;
    private double value;
    private LocalDateTime bidDateTime;


    public Bid (User bidder, double value, LocalDateTime bidDateTime) {

        //Considered that the bidder can't be null
        if (bidder == null) {
            throw new IllegalArgumentException("Bidder can't be null.");
        }

        // Value cannot be negative
        if (value <= 0) {
            throw new IllegalArgumentException("Bid value must be positive.");
        }

        // bidDateTime cannot be null
        if (bidDateTime == null) {
            throw new IllegalArgumentException("Bid date/time cannot be null.");
        }

        // Making sure that the date is not in the future
        if (bidDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Bid date/time cannot be in the future.");
        }

        this.bidder = bidder;
        this.value = value;
        this.bidDateTime = bidDateTime;
    }

    public User getBidder(){
        return bidder;
    }

    public double getValue(){
        return value;
    }


    public String getBidInfo() {
      return "Bidder: " + bidder.getUserName() + ", Value: " + value;
    }


    public LocalDateTime getBidDateTime(){
        return bidDateTime;
    }

}
