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

        Country country = new Country("PORTUGAL");
        Author author1 = new Author("Author One", country, LocalDate.of(1980, 1, 1), "Bio One");

        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                new Country("PORTUGAL"));

        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "Test User",
                new Country("Portugal"),
                LocalDate.of(2000, 5, 10));

        BibliographicalData biblio = new BibliographicalData( "Childhood, Youth, Dependency: The Copenhagen Trilogy",
                List.of(author1),
                Locale.ENGLISH,
                new Publisher("John Murray Press", "GB205505305", new Contact("987654321", "351", email, address)),
                LocalDate.of(2020, 1, 1),
                List.of(new Genre ("Fiction")),
                200,
                "Some text");

        Publication publication = new Publication(biblio,new PublicationType(MediaFormat.BOOK,TypeOfBinding.HARDBACK,"DFD"),new User("Diogo",contact,profile, UserRole.NORMAL_USER), PublicationStatus.FOR_SALE,"12",new Code("123","123","123"));

        List<Picture> pictures = List.of(new Picture("path/to/pic.jpg"));
        
        Item item1 = new Item(Condition.GOOD,1.2,new Dimensions(1.1,1.1,1.1),publication);
        Item item2 = new Item(Condition.GOOD,1.2,new Dimensions(1.1,1.1,1.1),publication);

        List<Item> items = List.of(item1, item2);

        Listing listing = new Listing(
                items,
                new Price(new BigDecimal("50.00")),
                "Short description 1",
                "Listing Title 1",
                new User("Seller Name", null, null, UserRole.NORMAL_USER),
                pictures
        );

        DirectSale directSale = new DirectSale(listing, LocalDateTime.now());

        List<DirectSaleDTO_US025> dtos = mapper.toDTO(directSale);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        DirectSaleDTO_US025 dto1 = dtos.get(0);
        DirectSaleDTO_US025 dto2 = dtos.get(1);

        assertEquals(1, dto1.getAuthors().size());
        assertEquals("Author One", dto1.getAuthors().get(0));
        assertEquals(1, dto2.getAuthors().size());
        assertEquals("Author One", dto2.getAuthors().get(0));

        assertEquals("Childhood, Youth, Dependency: The Copenhagen Trilogy", dto1.getTitle());
        assertEquals(2, dto1.getAuthors().size() + dto2.getAuthors().size());
        assertTrue(dto1.getAuthors().contains("Author One"));
        assertTrue(dto1.getAuthors().contains("Author One"));
        assertTrue(dto1.getPrice().compareTo(new BigDecimal("50.00")) == 0);
        assertEquals("Listing Title 1", dto1.getListingTitle());
        assertEquals("Seller Name", dto1.getSeller());
    }
}