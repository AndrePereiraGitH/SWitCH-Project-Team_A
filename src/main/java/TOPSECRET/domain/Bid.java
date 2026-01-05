package TOPSECRET.domain;

public class Bid {
    //    private User bidder;
    private String bidder;
    private double value;


    //    public Bid (User bidder, double value) {
    public Bid(String bidder, double value) {


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
    }

//    public User getBidder(){
//        return bidder;                      // Not for now
//    }
//
    public double getValue(){
        return value;
    }
//
//
//    public String getBidInfo() {
//        return "Bidder: " + bidder.getUserName() + ", Value: " + value;
//    }


}
