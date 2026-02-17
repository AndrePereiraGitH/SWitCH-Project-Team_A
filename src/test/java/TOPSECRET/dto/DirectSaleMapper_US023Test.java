package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class DirectSaleMapper_US023Test {

    private final DirectSaleMapper_US023 mapper = new DirectSaleMapper_US023();

    // ---------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------

    private Country country() {
        return new Country("Portugal");
    }

    private Profile profile(String name) {
        return new Profile(name, country(), LocalDate.of(1990, 1, 1));
    }

    private Author author(String name) {
        return new Author(profile(name), "bio");
    }

    private BibliographicalData bd (String title, Author author) {
        return new BibliographicalData(
                title,
                List.of(author),
                Locale.ENGLISH,
//                new Publisher("Porto Editora", "123", null),
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
//                200,
                "desc"
        );
    }

    // Edition
    Code code = Code.fromIsbn13 ("978316148412");
    Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
    Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
    Edition edition= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    private Publication publication(String title, Author author) {
        return new Publication(
                bd (title, author),
                new PublicationType(TypeOfBinding.PAPERBACK, "PB"),
                new User("seller", null, profile("Seller"), UserRole.NORMAL_USER),
                PublicationStatus.FOR_SALE,
//                "1st",
                edition,
                Code.fromIsbn10("1234567890")
        );
    }

    private Item item(String title, String authorName) {
        return new Item(
                Condition.LIKE_NEW,
                10.0,
                new Dimensions(10, 10, 10),
                publication(title, author(authorName))
        );
    }

    private Listing listingWithItems(Item... items) {
        return new Listing(
                List.of(items),
                new Price(new BigDecimal("19.99")),
                "best book ever",
                "Listing Title",
                new User("seller", null, profile("Seller"), UserRole.NORMAL_USER),
                List.of(new Picture("pic"))
        );
    }

    // Teste 1 - DirectSale with 1 item
    @Test
    void toDTO_shouldMapSingleItemCorrectly() {
        // Arrange
        Item item = item("SCRUM for Dummies", "Prince");
        DirectSale ds = new DirectSale(listingWithItems(item), LocalDateTime.now());

        // Act
        List<DirectSaleDTO_US023> result = mapper.toDTO(ds);

        // Assert
        assertEquals(1, result.size());

        DirectSaleDTO_US023 dto = result.get(0);

        assertEquals("SCRUM for Dummies", dto.publicationTitle);
        assertEquals(List.of("Prince"), dto.authors);
        assertEquals(new BigDecimal("19.99"), dto.price);
        assertEquals("Listing Title", dto.listingTitle);
        assertEquals("Seller", dto.seller);
    }


    // Test 2 - DirectSale with more than 1 item------------------------------------------------------------------
    @Test
    void toDTO_shouldMapMultipleItemsIntoMultipleDTOs() {
        // Arrange
        Item item1 = item("Book A", "Author A");
        Item item2 = item("Book B", "Author B");

        DirectSale ds = new DirectSale(listingWithItems(item1, item2), LocalDateTime.now());

        // Act
        List<DirectSaleDTO_US023> result = mapper.toDTO(ds);

        // Assert
        assertEquals(2, result.size());

        assertEquals("Book A", result.get(0).publicationTitle);
        assertEquals("Book B", result.get(1).publicationTitle);
    }

    // Test 3 - DirectSale list-----------------------------------------------------------------------------------------------
    @Test
    void toDTOList_shouldFlattenAllDirectSales() {
        // Arrange
        DirectSale ds1 = new DirectSale(listingWithItems(item("A", "X")), LocalDateTime.now());
        DirectSale ds2 = new DirectSale(listingWithItems(item("B", "Y")), LocalDateTime.now());

        List<DirectSale> list = List.of(ds1, ds2);

        // Act
        List<DirectSaleDTO_US023> result = mapper.toDTO(list);

        // Assert
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).publicationTitle);
        assertEquals("B", result.get(1).publicationTitle);
    }

    // Test 4 - Empty list---------------------------------------------------------------------------------------------
    @Test
    void toDTOList_shouldReturnEmptyListWhenInputIsEmpty() {
        // Act
        List<DirectSaleDTO_US023> result = mapper.toDTO(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }


}