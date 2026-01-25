package TOPSECRET.dto;

import java.util.List;
/**
 * Data Transfer Object used in US022.
 *
 * Represents one item that is currently on auction,
 * containing only the information needed by the UI
 * to display auctions filtered by genre.
 */
public class AuctionDTO_US022 {

    public String title;
    public List<String> genres;

    public AuctionDTO_US022(String title, List<String> genres) {
        this.title = title;
        this.genres = genres;
    }
}

