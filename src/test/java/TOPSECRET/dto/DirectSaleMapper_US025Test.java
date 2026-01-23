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

    @Test
    void toDTO_singleDirectSale_shouldMapCorrectly() {

        // Criação dos dados de teste

        Country country = new Country("PORTUGAL");
        Author author1 = new Author("Author One", country, LocalDate.of(1980, 1, 1), "Bio One");

        Email email = new Email("test.user", "example.com");
        Address address = new Address("Main Street", "10", "12345", country);
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile("Test User", country, LocalDate.of(2000, 5, 10));
        User seller = new User("Seller Name", contact, profile, UserRole.NORMAL_USER);

        BibliographicalData biblio = new BibliographicalData(
                "Childhood, Youth, Dependency: The Copenhagen Trilogy",
                List.of(author1),
                Locale.ENGLISH,
                new Publisher("John Murray Press", "GB205505305", contact),
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
                200,
                "Some text"
        );

        Publication publication = new Publication(
                biblio,
                new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Hardcover"),
                seller,
                PublicationStatus.FOR_SALE,
                "12",
                new Code("123", "123", "123")
        );

        Item item1 = new Item(Condition.GOOD, 1.2, new Dimensions(1.1, 1.1, 1.1), publication);
        Item item2 = new Item(Condition.GOOD, 1.2, new Dimensions(1.1, 1.1, 1.1), publication);

        Listing listing = new Listing(
                List.of(item1, item2),
                new Price(new BigDecimal("50.00")),
                "Short description 1",
                "Listing Title 1",
                seller,
                List.of(new Picture("path/to/pic.jpg"))
        );

        DirectSale directSale = new DirectSale(listing, LocalDateTime.now());

        List<DirectSaleDTO_US025> dtos = mapper.toDTO(directSale);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        for (DirectSaleDTO_US025 dto : dtos) {
            assertEquals("Childhood, Youth, Dependency: The Copenhagen Trilogy", dto.getTitle());
            assertEquals(1, dto.getAuthors().size());
            assertEquals("Author One", dto.getAuthors().get(0));
            assertEquals(new BigDecimal("50.00"), dto.getPrice());
            assertEquals("Listing Title 1", dto.getListingTitle());
            assertEquals("Seller Name", dto.getSeller());
        }
    }

    @Test
    void toDTO_listOfDirectSales_shouldMapCorrectly() {

        // Criação de duas vendas com itens diferentes

        Country country = new Country("PORTUGAL");
        Author author1 = new Author("Author One", country, LocalDate.of(1980, 1, 1), "Bio One");
        Author author2 = new Author("Author Two", country, LocalDate.of(1990, 2, 2), "Bio Two");

        Publication pub1 = createPublication("Book One", author1);
        Publication pub2 = createPublication("Book Two", author2);

        Item item1 = new Item(Condition.NEW, 1.0, new Dimensions(1,1,1), pub1);
        Item item2 = new Item(Condition.NEW, 1.0, new Dimensions(1,1,1), pub2);

        Listing list1 = new Listing(List.of(item1), new Price(new BigDecimal("10.00")), "Desc111111111111", "Title1",
                new User("Seller1", null, null, UserRole.NORMAL_USER), List.of());
        Listing list2 = new Listing(List.of(item2), new Price(new BigDecimal("20.00")), "Desc211111111111", "Title2",
                new User("Seller2", null, null, UserRole.NORMAL_USER), List.of());

        DirectSale ds1 = new DirectSale(list1, LocalDateTime.now());
        DirectSale ds2 = new DirectSale(list2, LocalDateTime.now());

        List<DirectSaleDTO_US025> dtos = mapper.toDTO(List.of(ds1, ds2));

        assertEquals(2, dtos.size());
        assertTrue(dtos.stream().anyMatch(d -> d.getTitle().equals("Book One") && d.getPrice().compareTo(new BigDecimal("10.00")) == 0));
        assertTrue(dtos.stream().anyMatch(d -> d.getTitle().equals("Book Two") && d.getPrice().compareTo(new BigDecimal("20.00")) == 0));
    }

    private Publication createPublication(String title, Author author) {
        BibliographicalData biblio = new BibliographicalData(
                title,
                List.of(author),
                Locale.ENGLISH,
                new Publisher("Publisher", "VAT123", new Contact()),
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
                100,
                "Some text"
        );
        return new Publication(
                biblio,
                new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Hardcover"),
                new User("Seller", null, null, UserRole.NORMAL_USER),
                PublicationStatus.FOR_SALE,
                "Edition 1",
                new Code("123","123","123")
        );
    }
}