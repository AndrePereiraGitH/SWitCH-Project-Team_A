package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuctionTest {
    @Test
    public void testEmptyConstructor() {
        Auction auction = new Auction();
    }

    @Test
    public void testAllArgumentsConstructor() {

        // ---- Instantiate Item
       // Pictures pic = new Pictures("myurl", "description");
       // List<Pictures> pictures = new ArrayList<>();
       // pictures.add(pic);
       // Dimensions dimensions = new Dimensions(12.5, 5,10);
       // Item bookItem = new Item("book", Condition.LIKE_NEW, 1.2, pictures, dimensions, TypeOfBinding.HARDBACK, "2");

        // ---- Instantiate User
       // Email email = new Email("switcher", "@example.com");
       // Address add = new Address("Rua x", "12B", "4444-111", Country.PORTUGAL);
       // Contact contact = new Contact("911111111", "351", email, add);
       // User seller = new User("switcher", contact, "profile");

        // ---- Instantiate Listing
       // Listing listing = new Listing(bookItem, 10, "short description", "listing title", "auction", seller);
        LocalDateTime initialDate = LocalDateTime.now();
        LocalDateTime finalDate = LocalDateTime.of(2025, 12, 31, 23, 0, 0);
        String listing = "listing";

        // ---- Auction Constructor
        Auction auction = new Auction(listing, initialDate, finalDate, 35.50, 150 );
    }

}