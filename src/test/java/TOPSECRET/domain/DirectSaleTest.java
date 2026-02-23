package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

//import static TOPSECRET.controller.US021ToGetListOfItemsOnDirectSaleOfGivenGenreControllerTest.createBibliographicalData;
import static org.junit.jupiter.api.Assertions.*;

class DirectSaleTest {

    //==============================================================================================================
    // HELPERS - Private Methods to Help Instantiate

    private static Country createCountry(){
        return new Country(
                "Portugal"
        );
    }

    private static Profile createProfile(){
        return new Profile(
                "Freida",
                createCountry(),
                LocalDate.of(1968, 10, 12)
        );
    }

    private static Author createAuthor(){
        return new Author(
                createProfile(),
                "valid bio"
        );
    }

    private static BibliographicalData createBibliographicalData1(){
        return new BibliographicalData(
                "Harry Potter: Pedra Filosofal",
                List.of(createAuthor()),
                new Locale("pt", "PT"),
//                new Publisher("PortoEditora", "192394938", new Contact("917485647", "+351", new Email("asa", "example.com"),new Address("Rua Dr. Angelo Miranda", "182", "4540-340", createCountry()))),
                LocalDate.of(2020, 1, 30),
                List.of(new Genre("FANTASY")),
//                504,
                "Text Description"
        );
    }
    private static BibliographicalData createBibliographicalData2(){
        return new BibliographicalData(
                "Harry Potter: Cálice de Fogo",
                List.of(createAuthor()),
                new Locale("pt", "PT"),
//                new Publisher("PortoEditora", "192394938", new Contact("917485647", "+351", new Email("asa", "example.com"),new Address("Rua Dr. Angelo Miranda", "182", "4540-340", createCountry()))),
                LocalDate.of(2020, 1, 30),
                List.of(new Genre("FANTASY")),
//                504,
                "Text Description"
        );
    }

    private static PublicationType createPublicationType(){
        return new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Details"
        );
    }

    private static Code createCode(){
        return Code.fromIsbn10(
                "1928375849"
        );
    }

    private static PhysicalAttributes createPhysicalAttributes(){
        return new PhysicalAttributes(
                new Dimensions(123, 242, 124),
                100,
                230
        );
    }

    private static Publisher createPublisher(){
        return new Publisher(
                "John Snow",
                "UK198067290",
                null
        );
    }

    private static Edition createEdition(){
        return new Edition(
                "1st",
                createCode(),
                createPhysicalAttributes(),
                createPublicationType(),
                createPublisher(),
                Locale.ENGLISH,
                LocalDate.of(2025,10, 12)
        );
    }

    private static Publication createPublication1(){
        return new Publication(
                createBibliographicalData1(),
                createPublicationType(),
                createSeller(),
                PublicationStatus.READ,
//                "1st Edition",
                createEdition(),
                createCode()
        );
    }

    private static Publication createPublication2(){
        return new Publication(
                createBibliographicalData2(),
                createPublicationType(),
                createSeller(),
                PublicationStatus.READING,
//                "1st Edition",
                createEdition(),
                createCode()
        );
    }

    private static PublicationSample createItem1(){
        // Edition
        Edition edition1= new Edition("1st",
                createCode(),
                createPhysicalAttributes(),
                createPublicationType(),
                createPublisher(),
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1));

        //PublicationSample
        PublicationSample item = new PublicationSample(
                createBibliographicalData1(),
                createSeller(),
                PublicationStatus.READ,
                edition1,
                Condition.LIKE_NEW);

        return item;
    }

    private static PublicationSample createItem2(){
        // Edition
        Edition edition2= new Edition("2st",
                createCode(),
                createPhysicalAttributes(),
                createPublicationType(),
                createPublisher(),
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1));

        //PublicationSample
        PublicationSample item2 = new PublicationSample(
                createBibliographicalData2(),
                createSeller(),
                PublicationStatus.READING,
                edition2,
                Condition.LIKE_NEW);

        return item2;
    }

//    private static User createUser(){
//        return new User(
//                "Seller",
//                new Contact(
//                        "918374628",
//                        "+351",
//                        new Email("user", "example.com"),
//                        new Address("Avenida da Liberdade", "25", "3470-302", createCountry())
//                ),
//                new Profile(
//                        "Seller",
//                        createCountry(),
//                        LocalDate.of(1993, 9, 19)
//                ),
//                UserRole.NORMAL_USER
//        );
//    }

    private static User createSeller(){
        return new User(
                "Seller",
                new Contact(
                        "918374628",
                        "+351",
                        new Email("user", "example.com"),
                        new Address("Avenida da Liberdade", "25", "3470-302", createCountry())
                ),
                new Profile(
                        "Seller",
                        createCountry(),
                        LocalDate.of(1993, 9, 19)
                ),
                UserRole.NORMAL_USER
        );
    }

    private static User createBuyer(){
        return new User(
                "Seller",
                new Contact(
                        "918374628",
                        "+351",
                        new Email("user", "example.com"),
                        new Address("Avenida da Liberdade", "25", "3470-302", createCountry())
                ),
                new Profile(
                        "Seller",
                        createCountry(),
                        LocalDate.of(1993, 9, 19)
                ),
                UserRole.NORMAL_USER
        );
    }

    private static Listing createListing1(){
        return new Listing(
                "Listing Title",
                List.of(createItem1()),
                SaleType.DIRECT_SALE,
                new Price(new BigDecimal("19.99")),
                "Short Description",
                List.of(new Picture("pic"))
        );
    }

    private static Listing createListing2(){
        return new Listing(
                "Listing Title",
                List.of(createItem2()),
                SaleType.DIRECT_SALE,
                new Price(new BigDecimal("9.99")),
                "Short Description",
                List.of(new Picture("pic"))
        );
    }

    //=====================================================================================================
    // TESTS

    @Test
    void DirectSaleEmptyConstructor(){
        DirectSale directSale = new DirectSale();
    }

    @Test
    void DirectSaleValidConstructor(){
//        DirectSale directSale = new DirectSale(createListing(), LocalDateTime.now());
        DirectSale directSale = new DirectSale(List.of(createListing1(), createListing2()), createBuyer(), false);
    }

    @Test
    void DirectSaleWithListingNull(){
        // Act
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
//                new DirectSale(null,LocalDateTime.now()));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new DirectSale(null, createBuyer(), false));

        // Assert
        assertEquals("List of listings cannot be null", e.getMessage());
    }

//    @Test
//    void DirectSaleNullBuyDateConstructor(){
//        // Act
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
//                new DirectSale(createListing(),null));
//
//        // Assert
//        assertEquals("BuyDate cannot be null", e.getMessage());
//    }

    @Test
    void DirectSaleWithBuyerNull(){
        // Act
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new DirectSale(List.of(createListing1()), null, false));

        // Assert
        assertEquals("Buyer cannot be empty", e.getMessage());
    }

    @Test
    void ensureEqualsWithNullReturnsFalse() {
        // Arrange
//        DirectSale directSale = new DirectSale(createListing(), LocalDateTime.now());
        DirectSale directSale = new DirectSale(List.of(createListing1(), createListing2()), createBuyer(), false);

        // Act
        boolean result = directSale.equals(null);

        // Assert
        assertFalse(result);
    }


    @Test
    void ensureDifferentObjectsReturnsFalse(){
        //Arrange
//        DirectSale dsale1 = new DirectSale(createListing(),LocalDateTime.now());
//        DirectSale dsale2 = new DirectSale(createListing(), LocalDateTime.of(2026,1,21,10,0));
        DirectSale dsale1 = new DirectSale(List.of(createListing1()),createBuyer(), false);
        DirectSale dsale2 = new DirectSale(List.of(createListing2()),createBuyer(), true);

        //Act
        boolean expectedResult = dsale1.equals(dsale2);

        //Assert
        assertFalse(expectedResult);
    }

    @Test
    void ensureSameObjectsReturnTrue(){
        //Arrange
//        Listing sameListing = createListing();
//        DirectSale dsale1 = new DirectSale(sameListing,LocalDateTime.of(2026,1,21,10,0));
//        DirectSale dsale2 = new DirectSale(sameListing, LocalDateTime.of(2026,1,21,10,0));
        Listing sameListing = createListing1();
        User sameBuyer = createBuyer();
        DirectSale dsale1 = new DirectSale(List.of(sameListing), sameBuyer, false);
        DirectSale dsale2 = new DirectSale(List.of(sameListing), sameBuyer, false);

        //Act
        boolean expectedResult = dsale1.equals(dsale2);

        //Assert
        assertTrue(expectedResult);
    }

    @Test
    void sameObjectHasToHaveTheSameHashcode(){
        //Arrange
//        DirectSale dsale1 = new DirectSale(createListing(),LocalDateTime.now());
        DirectSale dsale1 = new DirectSale(List.of(createListing1()), createBuyer(),false);

        //Act & Assert
        assertEquals(dsale1.hashCode(), dsale1.hashCode());
    }

    @Test
    void difObjectHasToHaveTheDifHashcode(){
        //Arrange
//        DirectSale dsale1 = new DirectSale(createListing(),LocalDateTime.now());
//        DirectSale dsale2 = new DirectSale(createListing(),LocalDateTime.now());
        DirectSale dsale1 = new DirectSale(List.of(createListing1()), createBuyer(),false);
        DirectSale dsale2 = new DirectSale(List.of(createListing1()), createBuyer(),false);

        //Act & Assert
        assertNotEquals(dsale1.hashCode(), dsale2.hashCode());
    }

    @Test
    void getListing_shouldReturnListing() {
        // Arrange
//        Listing listing = createListing();
//        DirectSale directSale = new DirectSale(listing, LocalDateTime.now());
        List<Listing> listings = List.of(createListing1());
        DirectSale directSale = new DirectSale(listings, createBuyer(),false);

        // Act
//        Listing result = directSale.getListing();
        List<Listing> result = directSale.getListings();

        // Assert
        assertSame(listings, result);
    }

    @Test
    void getDetails_shouldReturnRecordWithCorrectValues() {
        // Arrange
//        Listing listing = createListing();
//        LocalDateTime date = LocalDateTime.of(2025, 5, 10, 14, 30);
//        DirectSale directSale = new DirectSale(listing, date);
        List<Listing> listings = List.of(createListing1(), createListing2());
        User buyer = createBuyer();
        DirectSale directSale = new DirectSale(listings, buyer,false);

        // Act
        DirectSale.DirectSaleDetails details = directSale.getDetails();

        // Assert
        assertNotNull(details);
        assertSame(listings, details.listings());
        assertSame(buyer, details.buyer());
//        assertEquals(date, details.directSaleDate());
    }

    @Test
    void directSaleDetailsRecord_shouldStoreValuesCorrectly() {
        // Arrange
//        Listing listing = createListing();
//        LocalDateTime date = LocalDateTime.of(2024, 12, 1, 9, 0);
        List<Listing> listings = List.of(createListing1(), createListing2());
        User buyer = createBuyer();

        // Act
        DirectSale.DirectSaleDetails details = new DirectSale.DirectSaleDetails(listings, buyer,false);

        // Assert
        assertSame(listings, details.listings());
        assertSame(buyer, details.buyer());
//        assertEquals(date, details.directSaleDate());
    }

    @Test
    void directSaleDetailsRecord_shouldAllowNullValues() {
        // Act
        DirectSale.DirectSaleDetails details = new DirectSale.DirectSaleDetails(null, null, false);

        // Assert
        assertNull(details.listings());
        assertNull(details.buyer());
//        assertNull(details.directSaleDate());
    }
}