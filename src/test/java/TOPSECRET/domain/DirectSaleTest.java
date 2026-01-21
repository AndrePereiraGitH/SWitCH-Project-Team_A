package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class DirectSaleTest {

    @Test
    void DirectSaleEmptyConstructor(){
        DirectSale directSale = new DirectSale();
    }

    @Test
    void DirectSaleValidConstructor(){

        DirectSale directSale = new DirectSale(
                createListing(),
                LocalDateTime.now()
        );

    }

    @Test
    void DirectSaleListingNullConstructor(){
        // Act
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new DirectSale(
                        null,
                        LocalDateTime.now()
                )
        );

        // Assert
        assertEquals("Listing cannot be null", e.getMessage());
    }

    @Test
    void DirectSaleNullBuyDateConstructor(){
        // Act
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                new DirectSale(
                        createListing(),
                        null
                )
        );

        // Assert
        assertEquals("BuyDate cannot be null", e.getMessage());
    }


    @Test
    void ensureDifferentObjectsReturnFalse(){
        //Arrange
        DirectSale dsale1 = new DirectSale(createListing(),LocalDateTime.now());
        DirectSale dsale2 = new DirectSale(createListing(), LocalDateTime.of(2026,1,21,10,0));

        //Act
        boolean expectedResult = dsale1.equals(dsale2);

        //Assert
        assertFalse(expectedResult);

    }

    @Test
    void ensureSameObjectsReturnTrue(){
        //Arrange
        Listing sameListing = createListing();
        DirectSale dsale1 = new DirectSale(sameListing,LocalDateTime.of(2026,1,21,10,0));
        DirectSale dsale2 = new DirectSale(sameListing, LocalDateTime.of(2026,1,21,10,0));

        //Act
        boolean expectedResult = dsale1.equals(dsale2);

        //Assert
        assertTrue(expectedResult);

    }

    @Test
    void sameObjectHasToHaveTheSameHashcode(){
        //Arrange
        DirectSale dsale1 = new DirectSale(createListing(),LocalDateTime.now());


        //Act & Assert
        assertEquals(dsale1.hashCode(), dsale1.hashCode());


    }

    @Test
    void difObjectHasToHaveTheDifHashcode(){
        //Arrange
        DirectSale dsale1 = new DirectSale(createListing(),LocalDateTime.now());
        DirectSale dsale2 = new DirectSale(createListing(),LocalDateTime.now());


        //Act & Assert
        assertNotEquals(dsale1.hashCode(), dsale2.hashCode());


    }


    //===============================================
    // Private Methods to Help Instantiate

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

    private static BibliographicalData createBibliographicalData(){
        return new BibliographicalData(
                "Harry Potter: Pedra Filosofal",
                List.of(createAuthor()),
                new Locale("pt", "PT"),
                new Publisher(
                        "PortoEditora",
                        "192394938",
                        new Contact(
                                "917485647",
                                "+351",
                                new Email("asa", "example.com"),
                                new Address("Rua Dr. Angelo Miranda", "182", "4540-340", createCountry())
                        )
                ),
                LocalDate.of(2020, 1, 30),
                List.of(new Genre("FANTASY")),
                504,
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

    private static Publication createPublication(){
        return new Publication(
                createBibliographicalData(),
                createPublicationType(),
                createUser(),
                PublicationStatus.FOR_SALE,
                "1st Edition",
                createCode()
        );
    }

    private static Item createItem(){
        return new Item(
                Condition.LIKE_NEW,
                230.30,
                new Dimensions(123, 242, 124),
                createPublication()
        );
    }

    private static User createUser(){
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

    private static Listing createListing(){
        return new Listing(
                List.of(createItem()),
                new Price(new BigDecimal("19.99")),
                "Short Description",
                "Listing Title",
                createUser(),
                List.of(new Picture("pic"))
        );
    }
}