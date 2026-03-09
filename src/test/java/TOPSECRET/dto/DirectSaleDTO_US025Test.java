//package TOPSECRET.dto;
//import TOPSECRET.domain.*;
//import TOPSECRET.domain.Enum.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Locale;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for DirectSaleDTO_US025.
// * Validates correct mapping from domain objects to DTOs.
// */
//class DirectSaleDTO_US025Test {
//
//    private User seller1;
//    private User seller2;
//    private Author author1;
//    private Author author2;
//    private PublicationSample sample1;
//    private PublicationSample sample2;
//    private Listing listing1;
//    private Listing listing2;
//    private DirectSale directSale;
//
//    @BeforeEach
//    void setup() {
//        // --- USERS ---
//        seller1 = new User("namee",new Contact("wer","fd",new Email("s","example.com"),new Address("s","s","s",new Country("PORTUGAL"))),new Profile("s",new Country("Portugal"), LocalDate.of(1983,2,3)), UserRole.NORMAL_USER);
//        seller2 = new User("nmeee",new Contact("wer","fd",new Email("s","example.com"),new Address("s","s","s",new Country("PORTUGAL"))),new Profile("s",new Country("Portugal"), LocalDate.of(1983,2,3)), UserRole.NORMAL_USER);
//
//        // --- AUTHORS ---
//        AuthorFactory authorFactory = new AuthorFactory();
//        author1 = authorFactory.create("author1",new Country("PORTUGAL"),LocalDate.of(1945,2,4),"bio");
//        author2 = authorFactory.create("author2",new Country("PORTUGAL"),LocalDate.of(1945,2,4),"bio");
//
//        // --- PUBLICATIONS ---
//        Genre genre1 = new Genre("genre");
//
//        sample1 = new PublicationSample(
//                new BibliographicalData("Title One", List.of(author2), Locale.ENGLISH,LocalDate.of(1956,6,4),List.of(genre1),"text"),
//                seller1,
//                    PublicationStatus.READ,
//                new Edition("1",new Code("s","s","s"),new PhysicalAttributes(new Dimensions(1,1,1),1,1),new PublicationType(MediaFormat.BOOK,TypeOfBinding.HARDBACK,"details"),new Publisher("name","number",new Contact("wer","fd",new Email("s","example.com"),new Address("s","s","s",new Country("PORTUGAL")))),Locale.ENGLISH,LocalDate.of(1923,4,6)),
//                Condition.NEW
//        );
//
//        sample2 = new PublicationSample(
//                new BibliographicalData("Title Two", List.of(author2), Locale.ENGLISH,LocalDate.of(1945,6,9),List.of(genre1),"text"),
//                seller2,
//                PublicationStatus.READ,
//                new Edition("1",new Code("s","s","s"),new PhysicalAttributes(new Dimensions(1,1,1),1,1),new PublicationType(MediaFormat.BOOK,TypeOfBinding.HARDBACK,"details"),new Publisher("name","number",new Contact("wer","fd",new Email("s","example.com"),new Address("s","s","s",new Country("PORTUGAL")))),Locale.ENGLISH,LocalDate.of(1956,4,8)),
//                Condition.NEW
//        );
//
//        // --- LISTINGS ---
//        ListingFactory listingFactory = new ListingFactory();
//        listing1 = listingFactory.create(
//                "Listing One",
//                List.of(sample1),
//                SaleType.DIRECT_SALE,
//                new Price(new BigDecimal("10.00")),
//                "Good condition book",
//                List.of(new Picture("img1.jpg"))
//        );
//
//        listing2 = listingFactory.create(
//                "Listing Two",
//                List.of(sample2),
//                SaleType.DIRECT_SALE,
//                new Price(new BigDecimal("15.00")),
//                "Another good book",
//                List.of(new Picture("img2.jpg"))
//        );
//
//        // --- DIRECT SALE ---
//        DirectSaleFactory factory = new DirectSaleFactory();
//        directSale = factory.create(List.of(listing1, listing2), seller1);
//    }
//
//    /**
//     * Tests that fromItemAndDirectSale creates a DTO with correct aggregated data.
//     */
//    @Test
//    void testFromItemAndDirectSale() {
//        DirectSaleDTO_US025 dto = DirectSaleDTO_US025.fromItemAndDirectSale(sample1, directSale);
//
//        assertEquals("Title One", dto.getTitle());
//        assertEquals(List.of("author2"), dto.getAuthors());
//        assertEquals(new BigDecimal("25.00"), dto.getPrice());
//        assertEquals("Pack of 2 listings", dto.getListingTitle());
//        assertEquals("nmeee", dto.getSeller());
//    }
//
//    /**
//     * Tests that fromDirectSale returns a DTO for each publication sample in all listings.
//     */
//    @Test
//    void testFromDirectSale() {
//        List<DirectSaleDTO_US025> dtos = DirectSaleDTO_US025.fromDirectSale(directSale);
//
//        assertEquals(2, dtos.size());
//
//        // Check first DTO
//        DirectSaleDTO_US025 dto1 = dtos.get(0);
//        assertEquals("Title One", dto1.getTitle());
//        assertEquals(List.of("author2"), dto1.getAuthors());
//        assertEquals(new BigDecimal("25.00"), dto1.getPrice());
//
//        // Check second DTO
//        DirectSaleDTO_US025 dto2 = dtos.get(1);
//        assertEquals("Title Two", dto2.getTitle());
//        assertEquals(List.of("author2"), dto2.getAuthors());
//        assertEquals(new BigDecimal("25.00"), dto2.getPrice());
//    }
//
//    /*@Test
//    void fromItemAndDirectSale_shouldNotDuplicateSellerNames_sameSeller() {
//
//        User sameSeller = seller1;
//
//        ListingFactory listingFactory = new ListingFactory();
//
//        Listing listingA = listingFactory.create(
//                "Listing A",
//                List.of(sample1),
//                SaleType.DIRECT_SALE,
//                new Price(new BigDecimal("5.00")),
//                "Description A",
//                List.of(new Picture("picA.jpg"))
//        );
//
//        Listing listingB = listingFactory.create(
//                "Listing B",
//                List.of(sample2),
//                SaleType.DIRECT_SALE,
//                new Price(new BigDecimal("7.00")),
//                "Description B",
//                List.of(new Picture("picB.jpg"))
//        );
//
//        DirectSale dsSameSeller = new DirectSale(List.of(listingA, listingB), sameSeller, true);
//        DirectSaleDTO_US025 dto = DirectSaleDTO_US025.fromItemAndDirectSale(sample1, dsSameSeller);
//        assertEquals(sameSeller.getUserName(), dto.getSeller());
//    }*/
//}