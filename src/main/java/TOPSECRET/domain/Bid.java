package TOPSECRET.domain;

import java.time.LocalDateTime;

public class Bid {
//    private User bidder;
    private String bidder;
    private double value;
    private LocalDateTime bidDateTime;


//    public Bid (User bidder, double value, LocalDateTime bidDateTime) {
      public Bid (String bidder, double value, LocalDateTime bidDateTime) {

        //Considered that the bidder can't be null
        if (bidder == null) {
            throw new IllegalArgumentException("Bidder can't be null.");
        }

        // Value cannot be negative
        if (value <= 0) {
            throw new IllegalArgumentException("Bid value must be positive.");
        }

        this.bidder = bidder;
        this.value = value;
        this.bidDateTime = bidDateTime;
    }

//    public User getBidder(){
       public String getBidder() {
        return bidder;
    }

    public double getValue(){
        return value;
    }


//    public String getBidInfo() {
//        return "Bidder: " + bidder.getUserName() + ", Value: " + value;
//    }


    public LocalDateTime getBidDateTime(){
        return bidDateTime;
    }

}
