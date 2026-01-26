package TOPSECRET.dto;

import java.util.List;
/**
 * Data Transfer Object (DTO) used in use case US028.
 *
 * This class is used to send auction information
 * from the controller to the UI.
 * It contains only the data needed for display.
 */
public class AuctionDTO_US028 {

    // Name of the auction
    public String name;

    // List of publisher names related to the auction items
    public List<String> publishers;

    /**
     * Creates an AuctionDTO_US028 with the required data.
     *
     * @param name the auction name
     * @param publishers list of publisher names
     */
    public AuctionDTO_US028(String name, List<String> publishers) {
        this.name = name;
        this.publishers = publishers;
    }
}
