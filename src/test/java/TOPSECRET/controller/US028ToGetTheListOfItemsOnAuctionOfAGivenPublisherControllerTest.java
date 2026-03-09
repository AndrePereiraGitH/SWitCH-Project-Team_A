package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.AuctionDTO_US028;
import TOPSECRET.dto.PublisherDTO_US028;
import TOPSECRET.repository.PublisherRepository;
import TOPSECRET.repository.AuctionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

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
    void setUp() throws InstanceAlreadyExistsException {
        CountryFactory countryFactory = new CountryFactory();
        Country portugal = countryFactory.create("Portugal");
        // Common contact
        Profile profile = new Profile("Alice", portugal, LocalDate.of(1990, 1, 1));
        Email email = new Email("alice", "example.com");
        Address address = new Address("Street", "1", "1000-100", portugal);
        Contact contact = new Contact("912345678", "+351", email, address);

        // Publishers (domain objects)
        PublisherFactory publisherFactory = new PublisherFactory();
        publisher1 = publisherFactory.create("Tech Books", "PT123456789", null);
        publisher2 = publisherFactory.create("Other Books", "PT987654321", null);

        // Bibliographical data
        AuthorFactory authorFactory = new AuthorFactory();
        Author author = authorFactory.createWithExistingProfile(profile, "Author bio");

        // Book published by publisher1
        BibliographicalData data1 = new BibliographicalData(
                "Java Fundamentals",
                Arrays.asList(author),
                Locale.ENGLISH,
//                publisher1,
                LocalDate.of(2020, 1, 1),
                Arrays.asList(new Genre("Tech")),
//                300,
                "Description"
        );

        // Book published by publisher2
        BibliographicalData data2 = new BibliographicalData(
                "Python Basics",
                Arrays.asList(author),
                Locale.ENGLISH,
//                publisher2,
                LocalDate.of(2021, 1, 1),
                Arrays.asList(new Genre("Tech")),
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
        Code code1 = Code.fromIsbn13 ("9783161484120");
        Code code2 = Code.fromIsbn13 ("9783161484130");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
//        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition1= new Edition("1st", code1, physicalAttributes, pubType, publisher1, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
        Edition edition2= new Edition("1st", code2, physicalAttributes, pubType, publisher2, Locale.ENGLISH, LocalDate.of(2020, 1, 1));


        // -------- Publication Samples (items on auction) --------
        PublicationSample item1 = new PublicationSample(
                data1,
                seller,
                PublicationStatus.READ,   // status doesn't matter for this repo method
                edition1,
                Condition.NEW
        );

        PublicationSample item2 = new PublicationSample(
                data2,
                seller,
                PublicationStatus.READ,
                edition2,
                Condition.LIKE_NEW
        );

       // -------- Listings --------
        ListingFactory listingFactory = new ListingFactory();

        Listing listing1 = listingFactory.create(
                "Auction 1",
                Arrays.asList(item1),
                SaleType.AUCTION,
                new Price(BigDecimal.valueOf(40)),
                "ShortDescription 1",
                Arrays.asList(new Picture("img1.jpg"))
        );

        Listing listing2 = listingFactory.create(
                "Auction 2",
                Arrays.asList(item2),
                SaleType.AUCTION,
                new Price(BigDecimal.valueOf(35)),
                "ShortDescription 2",
                Arrays.asList(new Picture("img2.jpg"))
        );

        // IMPORTANT: Auction needs buyOutPrice (4-arg constructor)
        Price buyOut = new Price(BigDecimal.valueOf(999));

        LocalDateTime start = LocalDateTime.now().plusMinutes(2);
        LocalDateTime end = start.plusDays(7);

        AuctionFactory auctionFactory = new AuctionFactory();


        auction1 = auctionFactory.create(listing1, start, end, buyOut);
        auction2 = auctionFactory.create(listing2, start, end, buyOut);

        // Repos
        publisherRepo = new PublisherRepository();
        auctionRepo = new AuctionRepository(auctionFactory);

        // Seed publishers into repo using add(...)
        publisherRepo.createAndAdd(publisher1.getName(), publisher1.getVatNumber(), contact);
        publisherRepo.createAndAdd(publisher2.getName(), publisher2.getVatNumber(), contact);

        auctionRepo.createAndAdd(listing1, start, end, buyOut);
        auctionRepo.createAndAdd(listing2, start, end, buyOut);

        // Controller ctor order: (AuctionRepository, PublisherRepository, AuctionMapper, PublisherMapper)
        controller = new US028ToGetTheListOfItemsOnAuctionOfAGivenPublisherController(
                auctionRepo, publisherRepo
        );
    }

    /**
     * Tests if the controller returns all publishers as DTOs.
     */
    @Test
    void getAllPublishers_returnsAllPublishersAsDTOs() {

        List<PublisherDTO_US028> result = controller.getAllPublishers();

        // Check that both publishers are returned
        assertEquals(2, result.size());

        List<String> names = result.stream().map(dto -> dto.name).toList();
        assertTrue(names.contains("Tech Books"));
        assertTrue(names.contains("Other Books"));
    }

    /**
     * Tests if the controller returns auction items
     * for the first publisher.
     */
    @Test
    void getListOfItemsOnAuction_returnsItemsForPublisher1() {
        List<AuctionDTO_US028> result =
                controller.getListOfItemsOnAuction(publisher1.getVatNumber());

        // With the sample data, only auction1 has an item whose BibliographicalData publisher is publisher1
        assertEquals(1, result.size());
    }

    /**
     * Tests if the controller returns auction items
     * for the second publisher.
     */

    @Test

    void getListOfItemsOnAuction_returnsItemsForPublisher2() {
        List<AuctionDTO_US028> result =
                controller.getListOfItemsOnAuction(publisher2.getVatNumber());

        assertEquals(1, result.size());
    }

    /**
     * Tests if an unknown VAT number returns an empty list.
     */
    @Test
    void getListOfItemsOnAuction_returnsEmptyForUnknownVat() {
        List<AuctionDTO_US028> result =
                controller.getListOfItemsOnAuction("PT000000000");

        assertEquals(0, result.size());

    }

}
