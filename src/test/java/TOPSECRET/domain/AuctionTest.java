package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.Country;
import TOPSECRET.domain.Enum.Genre;
import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class AuctionTest {
    @Test
    public void testEmptyConstructor() {
        Auction auction = new Auction();
        assertNotNull(auction);
    }

    @Test
    public void testListingNullException() {

        // ---- Instantiate List<Picture>
        List<String> pictures = instantiatePictures();

        // ---- Instantiate BibliographicalData
        BibliographicalData bibliographicalData = instantiateBibliographicalData();

        // ---- Instantiate Item
        Dimensions dimensions = new Dimensions(12.5, 5,10);
        Item bookItem = instantiateItem(bibliographicalData, pictures, dimensions);

        // ---- Instantiate User
        User seller = instantiateUser();

        // ---- Instantiate Listing

        Listing listing = instantiateListing();


        LocalDateTime initialDate = LocalDateTime.now();
        LocalDateTime finalDate = LocalDateTime.of(2025, 12, 31, 23, 0, 0);

        // ---- Auction Constructor
        //Auction auction = new Auction(null, initialDate, finalDate, 35.50, 150);


        assertThrows(IllegalArgumentException.class, () -> new Auction(null, initialDate, finalDate, 35.50, 150));


    }

    @Test
    public void testInitialDateBeforeTodayException() {

        // ---- Instantiate List<Picture>
        List<String> pictures = instantiatePictures();

        // ---- Instantiate BibliographicalData
        BibliographicalData bibliographicalData = instantiateBibliographicalData();

        // ---- Instantiate Item
        Dimensions dimensions = new Dimensions(12.5, 5,10);
        Item bookItem = instantiateItem(bibliographicalData, pictures, dimensions);

        // ---- Instantiate User
        User seller = instantiateUser();

        // ---- Instantiate Listing

        Listing listing = instantiateListing();


        LocalDateTime initialDate = LocalDateTime.of(2026, 1, 1, 12, 0);
        LocalDateTime finalDate = LocalDateTime.of(2025, 12, 31, 23, 0, 0);

        // ---- Auction Constructor
        //Auction auction = new Auction(null, initialDate, finalDate, 35.50, 150);


        assertThrows(IllegalArgumentException.class, () -> new Auction(listing, initialDate, finalDate, 35.50, 150));


    }

    @Test
    public void testFinalDateBeforeInitialDateException() {

        // ---- Instantiate List<Picture>
        List<String> pictures = instantiatePictures();

        // ---- Instantiate BibliographicalData
        BibliographicalData bibliographicalData = instantiateBibliographicalData();

        // ---- Instantiate Item
        Dimensions dimensions = new Dimensions(12.5, 5,10);
        Item bookItem = instantiateItem(bibliographicalData, pictures, dimensions);

        // ---- Instantiate User
        User seller = instantiateUser();

        // ---- Instantiate Listing

        Listing listing = instantiateListing();


        LocalDateTime initialDate = LocalDateTime.of(2026, 10, 1, 12, 0);
        LocalDateTime finalDate = LocalDateTime.of(2026, 5, 31, 23, 0, 0);

        // ---- Auction Constructor
        //Auction auction = new Auction(null, initialDate, finalDate, 35.50, 150);


        assertThrows(IllegalArgumentException.class, () -> new Auction(listing, initialDate, finalDate, 35.50, 150));


    }

    @Test
    public void testInitialPriceLessOrEqualsToZeroException() {

        // ---- Instantiate List<Picture>
        List<String> pictures = instantiatePictures();

        // ---- Instantiate BibliographicalData
        BibliographicalData bibliographicalData = instantiateBibliographicalData();

        // ---- Instantiate Item
        Dimensions dimensions = new Dimensions(12.5, 5,10);
        Item bookItem = instantiateItem(bibliographicalData, pictures, dimensions);

        // ---- Instantiate User
        User seller = instantiateUser();

        // ---- Instantiate Listing

        Listing listing = instantiateListing();


        LocalDateTime initialDate = LocalDateTime.of(2026, 2, 1, 12, 0);
        LocalDateTime finalDate = LocalDateTime.of(2026, 5, 31, 23, 0, 0);

        // ---- Auction Constructor
        //Auction auction = new Auction(null, initialDate, finalDate, 35.50, 150);


        assertThrows(IllegalArgumentException.class, () -> new Auction(listing, initialDate, finalDate, 0, 150));


    }

    @Test
    public void testBuyOutPriceLessOrEqualsToInitialPriceException() {

        // ---- Instantiate List<Picture>
        List<String> pictures = instantiatePictures();

        // ---- Instantiate BibliographicalData
        BibliographicalData bibliographicalData = instantiateBibliographicalData();

        // ---- Instantiate Item
        Dimensions dimensions = new Dimensions(12.5, 5,10);
        Item bookItem = instantiateItem(bibliographicalData, pictures, dimensions);

        // ---- Instantiate User
        User seller = instantiateUser();

        // ---- Instantiate Listing

        Listing listing = instantiateListing();


        LocalDateTime initialDate = LocalDateTime.of(2026, 2, 1, 12, 0);
        LocalDateTime finalDate = LocalDateTime.of(2026, 5, 31, 23, 0, 0);

        // ---- Auction Constructor
        //Auction auction = new Auction(null, initialDate, finalDate, 35.50, 150);


        assertThrows(IllegalArgumentException.class, () -> new Auction(listing, initialDate, finalDate, 35.50, 35.50));


    }

    // ==== Accept Bid

    @Test
    public void testBidBidderNullException() {

        // ---- Instantiate List<Picture>
        List<String> pictures = instantiatePictures();

        // ---- Instantiate BibliographicalData
        BibliographicalData bibliographicalData = instantiateBibliographicalData();

        // ---- Instantiate Item
        Dimensions dimensions = new Dimensions(12.5, 5,10);
        Item bookItem = instantiateItem(bibliographicalData, pictures, dimensions);

        // ---- Instantiate User
        User seller = instantiateUser();

        // ---- Instantiate Listing

        Listing listing = instantiateListing();


        LocalDateTime initialDate = LocalDateTime.of(2026, 2, 1, 12, 0);
        LocalDateTime finalDate = LocalDateTime.of(2026, 5, 31, 23, 0, 0);

        // ---- Auction Constructor
        Auction auction = new Auction(listing, initialDate, finalDate, 35.50, 150);

        //Bid bid1 = new Bid("Rodolph", 100);

       // auction.acceptBid(bid1);

        assertThrows(IllegalArgumentException.class, () -> auction.acceptBid(null));


    }

    @Test
    public void testBidValueLesaThanLastBidException() {

        // ---- Instantiate List<Picture>
        List<String> pictures = instantiatePictures();

        // ---- Instantiate BibliographicalData
        BibliographicalData bibliographicalData = instantiateBibliographicalData();

        // ---- Instantiate Item
        Dimensions dimensions = new Dimensions(12.5, 5,10);
        Item bookItem = instantiateItem(bibliographicalData, pictures, dimensions);

        // ---- Instantiate User
        User seller = instantiateUser();

        // ---- Instantiate Listing

        Listing listing = instantiateListing();


        LocalDateTime initialDate = LocalDateTime.of(2026, 2, 1, 12, 0);
        LocalDateTime finalDate = LocalDateTime.of(2026, 5, 31, 23, 0, 0);

        // ---- Auction Constructor
        Auction auction = new Auction(listing, initialDate, finalDate, 35.50, 150);

        Bid bid1 = new Bid("Rodolph", 100);
        auction.getBidHistory().add(bid1);

        Bid bid2 = new Bid("John", 90);


        assertThrows(IllegalArgumentException.class, () -> auction.acceptBid(bid2));


    }

    @Test
    public void testBidValueGraterThanInitialPriceException() {

        // ---- Instantiate List<Picture>
        List<String> pictures = instantiatePictures();

        // ---- Instantiate BibliographicalData
        BibliographicalData bibliographicalData = instantiateBibliographicalData();

        // ---- Instantiate Item
        Dimensions dimensions = new Dimensions(12.5, 5,10);
        Item bookItem = instantiateItem(bibliographicalData, pictures, dimensions);

        // ---- Instantiate User
        User seller = instantiateUser();

        // ---- Instantiate Listing

        Listing listing = instantiateListing();


        LocalDateTime initialDate = LocalDateTime.of(2026, 2, 1, 12, 0);
        LocalDateTime finalDate = LocalDateTime.of(2026, 5, 31, 23, 0, 0);

        // ---- Auction Constructor
        Auction auction = new Auction(listing, initialDate, finalDate, 35.50, 150);

        Bid bid1 = new Bid("Rodolph", 20);


        assertThrows(IllegalArgumentException.class, () -> auction.acceptBid(bid1));


    }






    // ============================================================
    // ======= Auxiliary methods to instantiate Objects ===========
    // ============================================================

    private static List<String> instantiatePictures() {
        //Pictures pic = new Pictures("myurl", "description");
        //List<Pictures> pictures = new ArrayList<>();
        //pictures.add(pic);

        String pic = "new picture";
        List<String> pictures = new ArrayList<>();

        return pictures;
    }

    private static Listing instantiateListing() {
        // Listing listing = new Listing(bookItem, 10, "short description", "listing title", "auction", seller);

        Listing listing = new Listing("item", 12.25, "short description", "This is our first listing", "sale type: auction", "seller");

        return listing;
    }

    private static User instantiateUser() {
        Email email = new Email("switcher", "example.com");
        Address add = new Address("Rua x", "12B", "4444-111", Country.PORTUGAL);
        Contact contact = new Contact("911111111", "351", email, add);
        LocalDate birthday = LocalDate.of(1982, 12, 3);
        Profile profile = new Profile("Albert Trump", Country.EL_SALVADOR, birthday, false);

        User seller = new User("switcher", contact, profile);
        return seller;
    }

    private static BibliographicalData instantiateBibliographicalData() {

        Profile[] authors = new Profile[]{new Profile("J. R. R. Tolkien", Country.UNITED_KINGDOM, LocalDate.of(1892, 1, 3), true)};

        /*
         List<Genre> genres = new ArrayList<>();
         genres.add(Genre.FANTASY);
         genres.add(Genre.MYSTERY);

         */
        Genre[] genres = new Genre[]{Genre.FANTASY, Genre.MYSTERY};


        String chunkOfText = "Continuing the story begun in The Hobbit, all three parts of the epic masterpiece, The Lord of the Rings, in one paperback. Features the definitive edition of the text, fold-out flaps with the original two-colour maps, and a revised and expanded index.";

        BibliographicalData bibliographicalData = new BibliographicalData("The Lord Of The Rings", authors, Locale.ENGLISH, "HarperCollins Publishers", LocalDate.of(1991, 11, 1), genres, 700, chunkOfText);

        return bibliographicalData;
    }

    private static Item instantiateItem(BibliographicalData bd, List<String> pictures, Dimensions dimensions) {
        Item bookItem = new Item(Condition.LIKE_NEW, 1.2, pictures, dimensions, TypeOfBinding.PAPERBACK, "2nd", bd);
        //Item bookItem = new Item("book", Condition.LIKE_NEW, 1.2, pictures, dimensions, TypeOfBinding.HARDBACK, "2");

        return bookItem;
    }





}