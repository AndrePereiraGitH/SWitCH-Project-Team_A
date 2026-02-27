package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.AuctionDTO_US028;
import TOPSECRET.dto.PublisherDTO_US028;
import TOPSECRET.dto.AuctionMapper_US028;
import TOPSECRET.dto.PublisherMapper_US028;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the controller that retrieves:
 * - the list of publishers
 * - the list of items on auction for a given publisher
 */
class US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherControllerTest {

    // Test publishers
    private Publisher publisher1;
    private Publisher publisher2;

    // Auctions related to different publishers
    private Auction auction1;
    private Auction auction2;

    // Repositories used by the controller
    private PublisherRepository publisherRepo;
    private AuctionRepository auctionRepo;

    // Controller being tested
    private US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController controller;

    /**
     * This method runs before each test.
     * It prepares all the data needed for the tests.
     */
    @BeforeEach
    void setUp() {
        // Common contact
        Profile profile = new Profile("Alice", new Country("PORTUGAL"), LocalDate.of(1990, 1, 1));
        Email email = new Email("alice", "example.com");
        Address address = new Address("Street", "1", "1000-100", new Country("PORTUGAL"));
        Contact contact = new Contact("912345678", "+351", email, address);

        // Publishers (domain objects)
        publisher1 = new Publisher("Tech Books", "PT123456789", contact);
        publisher2 = new Publisher("Other Books", "PT987654321", contact);

        // Bibliographical data
        Author author = new Author(profile, "Author bio");

        // Book published by publisher1
        BibliographicalData data1 = new BibliographicalData(
                "Java Fundamentals",
                List.of(author),
                Locale.ENGLISH,
//                publisher1,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Tech")),
//                300,
                "Description"
        );

        // Book published by publisher2
        BibliographicalData data2 = new BibliographicalData(
                "Python Basics",
                List.of(author),
                Locale.ENGLISH,
//                publisher2,
                LocalDate.of(2021, 1, 1),
                List.of(new Genre("Tech")),
//                250,
                "Description"
        );

        // -------- Publication type --------
        PublicationType type = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );

        // Seller user
        User seller = new User("seller", contact, profile, UserRole.NORMAL_USER);

        // Edition
        Code code1 = Code.fromIsbn13 ("978316148412");
        Code code2 = Code.fromIsbn13 ("978316148413");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
//        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition1= new Edition("1st", code1, physicalAttributes, pubType, publisher1, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
        Edition edition2= new Edition("1st", code2, physicalAttributes, pubType, publisher2, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        // -------- Publications --------
        Publication pub1 = new Publication(
                data1, type, seller, PublicationStatus.FOR_SALE,
//                "1st edition", Code.fromIsbn13("9780000000001")
                edition1, Code.fromIsbn13("9780000000001")
        );

        Publication pub2 = new Publication(
                data2, type, seller, PublicationStatus.FOR_SALE,
//                "1st edition", Code.fromIsbn13("9780000000002")
                edition2, Code.fromIsbn13("9780000000002")
        );

        // -------- Items --------
        Item item1 = new Item(Condition.NEW, 1.0, new Dimensions(10, 2, 20), pub1);
        Item item2 = new Item(Condition.LIKE_NEW, 1.1, new Dimensions(11, 2, 21), pub2);

//        // -------- Listings --------
//        Listing listing1 = new Listing(
//                "Auction 1",
//                List.of(item1),
//                SaleType.AUCTION,
//                new Price(BigDecimal.valueOf(40)),
//                "ShortDescription 1",
//                seller,
//                List.of(new Picture("img1.jpg"))
//        );

//        Listing listing2 = new Listing(
//                "Auction 2",
//                List.of(item2),
//                SaleType.AUCTION,
//                new Price(BigDecimal.valueOf(35)),
//                "ShortDescription 2",
//                seller,
//                List.of(new Picture("img2.jpg"))
//        );

        // IMPORTANT: Auction needs buyOutPrice (4-arg constructor)
        Price buyOut = new Price(BigDecimal.valueOf(999));

        LocalDateTime start = LocalDateTime.now().plusMinutes(2);
        LocalDateTime end = start.plusDays(7);

//        auction1 = new Auction(listing1, start, end, buyOut);
//        auction2 = new Auction(listing2, start, end, buyOut);

        // Repos
        publisherRepo = new PublisherRepository();
        //auctionRepo = new AuctionRepository(List.of(auction1, auction2));

        // Seed publishers into repo using add(...)
        publisherRepo.add(publisher1.getName(), publisher1.getVatNumber(), contact);
        publisherRepo.add(publisher2.getName(), publisher2.getVatNumber(), contact);

        // Mappers
        AuctionMapper_US028 auctionMapper = new AuctionMapper_US028();
        PublisherMapper_US028 publisherMapper = new PublisherMapper_US028();

        // Controller ctor order: (AuctionRepository, PublisherRepository, AuctionMapper, PublisherMapper)
        controller = new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                auctionRepo, publisherRepo, auctionMapper, publisherMapper
        );
    }

    /**
     * Tests if the controller returns all publishers as DTOs.
     */
//    @Test
//    void getAllPublishers_returnsAllPublishersAsDTOs() {
//
//        List<PublisherDTO_US028> result = controller.getAllPublishers();
//
//        // Check that both publishers are returned
//        assertEquals(2, result.size());
//
//        List<String> names = result.stream().map(dto -> dto.name).toList();
//        assertTrue(names.contains("Tech Books"));
//        assertTrue(names.contains("Other Books"));
//    }

    /**
     * Tests if the controller returns auction items
     * for the first publisher.
     */
//    @Test
//    void getListOfItemsOnAuction_returnsItemsForPublisher1() {
//        List<AuctionDTO_US028> result =
//                controller.getListOfItemsOnAuction(publisher1.getVatNumber());
//
//        // With the sample data, only auction1 has an item whose BibliographicalData publisher is publisher1
//        assertEquals(1, result.size());
//    }

    /**
     * Tests if the controller returns auction items
     * for the second publisher.
     */
//    @Test
//    void getListOfItemsOnAuction_returnsItemsForPublisher2() {
//        List<AuctionDTO_US028> result =
//                controller.getListOfItemsOnAuction(publisher2.getVatNumber());
//
//        assertEquals(1, result.size());
//    }

    /**
     * Tests if an unknown VAT number returns an empty list.
     */
//    @Test
//    void getListOfItemsOnAuction_returnsEmptyForUnknownVat() {
//        List<AuctionDTO_US028> result =
//                controller.getListOfItemsOnAuction("PT000000000");
//
//        assertEquals(0, result.size());
//    }
}
