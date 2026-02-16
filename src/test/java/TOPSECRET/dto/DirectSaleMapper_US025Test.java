package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class DirectSaleMapper_US025Test {

    private DirectSaleMapper_US025 mapper;

    @BeforeEach
    void setUp() {
        mapper = new DirectSaleMapper_US025();
    }

    // Auxiliary methods

    private DirectSale createDirectSale(String bookTitle, String authorName, BigDecimal price) {
        Author author = new Author(authorName, new Country("PORTUGAL"), LocalDate.of(1980, 1, 1), "Bio");
        User seller = new User("Seller Name", null, null, UserRole.NORMAL_USER);
        Item item = new Item(Condition.GOOD, 1.2, new Dimensions(1.1, 1.1, 1.1), createPublication(bookTitle, author, seller));
        Listing listing = new Listing(List.of(item), new Price(price), "Short description", "Listing Title 1", seller, List.of());
        return new DirectSale(listing, LocalDateTime.now());
    }

    private Publication createPublication(String title, Author author, User seller) {
        BibliographicalData biblio = new BibliographicalData(
                title,
                List.of(author),
                Locale.ENGLISH,
                new Publisher("Publisher", "VAT123", new Contact()),
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
                200,
                "Some text"
        );
        return new Publication(biblio, new PublicationType(TypeOfBinding.HARDBACK, "Hardcover"),
                seller, PublicationStatus.FOR_SALE, "12", new Code("123", "123", "123"));
    }

    // To test if it maps a single direct sale's items correctly
    @Test
    void toDTO_shouldMapSingleDirectSaleItemsCorrectly() {

        DirectSale directSale = createDirectSale("Book One", "Author One", new BigDecimal("50.00"));

        List<DirectSaleDTO_US025> dtos = mapper.toDTO(List.of(directSale));

        assertNotNull(dtos);
        assertEquals(1, dtos.size());

        // Tests the DTO of a single item
        DirectSaleDTO_US025 dto = dtos.get(0);
        assertEquals("Book One", dto.getTitle());
        assertEquals(1, dto.getAuthors().size());
        assertEquals("Author One", dto.getAuthors().get(0));
        assertEquals("50.00", dto.getPrice().toString());
        assertEquals("Listing Title 1", dto.getListingTitle());
        assertEquals("Seller Name", dto.getSeller());
    }

    // To test if it maps multiple direct sales' items correctly
    @Test
    void toDTO_shouldMapItemsOfMultipleDirectSalesCorrectly() {

        DirectSale directSale1 = createDirectSale("Book One", "Author One", new BigDecimal("50.00"));
        DirectSale directSale2 = createDirectSale("Book Two", "Author Two", new BigDecimal("60.00"));

        List<DirectSaleDTO_US025> dtos = mapper.toDTO(List.of(directSale1, directSale2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        // Tests the DTOs for each direct sale
        dtos.forEach(dto -> {
            if (dto.getTitle().equals("Book One")) {
                assertEquals(1, dto.getAuthors().size());
                assertEquals("Author One", dto.getAuthors().get(0));
                assertEquals("50.00", dto.getPrice().toString());
                assertEquals("Listing Title 1", dto.getListingTitle());
                assertEquals("Seller Name", dto.getSeller());
            } else if (dto.getTitle().equals("Book Two")) {
                assertEquals(1, dto.getAuthors().size());
                assertEquals("Author Two", dto.getAuthors().get(0));
                assertEquals("60.00", dto.getPrice().toString());
                assertEquals("Listing Title 1", dto.getListingTitle());
                assertEquals("Seller Name", dto.getSeller());
            } else {
                fail("Unexpected book title");
            }
        });
    }

    // To test if the mapping works when there are no direct sales
    @Test
    void toDTO_shouldReturnEmptyListWhenNoDirectSales() {
        List<DirectSaleDTO_US025> dtos = mapper.toDTO(List.of());
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}