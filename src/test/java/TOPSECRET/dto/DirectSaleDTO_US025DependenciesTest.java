package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DirectSaleDTO_US025}.
 * Validates correct mapping from domain objects to DTOs.
 * Uses mocked Country objects only for domain instantiation.
 */
class DirectSaleDTO_US025DependenciesTest {

    private User seller1;
    private User seller2;
    private Author author1;
    private Author author2;
    private PublicationSample sample1;
    private PublicationSample sample2;
    private Listing listing1;
    private Listing listing2;
    private DirectSale directSale;
    private AuthorRepository authorRepo;

    @BeforeEach
    void setup() {
        // --- COUNTRY INSTANCES (mock only to bypass validation) ---
        Country portugal1 = mock(Country.class);
        when(portugal1.getCountryName()).thenReturn("Portugal");

        Country portugal2 = mock(Country.class);
        when(portugal2.getCountryName()).thenReturn("Portugal");

        // --- USERS ---
        seller1 = UserFactory.create(
                "namee",
                new Contact("wer", "fd", new Email("s", "example.com"),
                        new Address("s", "s", "s", portugal1)),
                new Profile("s", portugal1, LocalDate.of(1983, 2, 3)),
                UserRole.NORMAL_USER
        );

        seller2 = UserFactory.create(
                "nmeee",
                new Contact("wer", "fd", new Email("s", "example.com"),
                        new Address("s", "s", "s", portugal2)),
                new Profile("s", portugal2, LocalDate.of(1983, 2, 3)),
                UserRole.NORMAL_USER
        );

        // --- AUTHORS ---
        authorRepo = new AuthorRepository(new AuthorFactory());
        author1 = authorRepo.createAndAddAuthor("author1", portugal1, LocalDate.of(1945, 2, 4), "bio");
        author2 = authorRepo.createAndAddAuthor("author2", portugal2, LocalDate.of(1945, 2, 4), "bio");

        // --- PUBLICATIONS ---
        Genre genre1 = new Genre("genre");
        PublisherFactory publisherFactory = new PublisherFactory();
        Publisher publisher = publisherFactory.create("Porto Editora", "PT987654321", null);
        EditionFactory editionFactory = new EditionFactory();

        sample1 = new PublicationSample(
                new BibliographicalData(
                        "Title One",
                        new ArrayList<>(List.of(author2)),
                        Locale.ENGLISH,
                        LocalDate.of(1956, 6, 4),
                        new ArrayList<>(List.of(genre1)),
                        "text"
                ),
                seller1,
                PublicationStatus.READ,
                editionFactory.create(
                "1",
                new Code("s", "s", "s"),
                new PhysicalAttributes(new Dimensions(1, 1, 1), 1, 1),
                new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "details"),
                publisher,
                Locale.ENGLISH,
                LocalDate.of(1923, 4, 6)
        ),
        Condition.NEW
        );

        sample2 = new PublicationSample(
                new BibliographicalData(
                        "Title Two",
                        new ArrayList<>(List.of(author2)),
                        Locale.ENGLISH,
                        LocalDate.of(1945, 6, 9),
                        new ArrayList<>(List.of(genre1)),
                        "text"
                ),
                seller2,
                PublicationStatus.READ,
                editionFactory.create(
                        "1",
                        new Code("s", "s", "s"),
                        new PhysicalAttributes(new Dimensions(1, 1, 1), 1, 1),
                        new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "details"),
                        publisher,
                        Locale.ENGLISH,
                        LocalDate.of(1956, 4, 8)
                ),
                Condition.NEW
        );

        // --- LISTINGS ---
        ListingFactory listingFactory = new ListingFactory();
        listing1 = listingFactory.create(
                "Listing One",
                new ArrayList<>(List.of(sample1)),
                SaleType.DIRECT_SALE,
                new Price(new BigDecimal("10.00")),
                "Good condition book",
                new ArrayList<>(List.of(new Picture("img1.jpg")))
        );

        listing2 = listingFactory.create(
                "Listing Two",
                new ArrayList<>(List.of(sample2)),
                SaleType.DIRECT_SALE,
                new Price(new BigDecimal("15.00")),
                "Another good book",
                new ArrayList<>(List.of(new Picture("img2.jpg")))
        );

        // --- DIRECT SALE ---
        DirectSaleFactory factory = new DirectSaleFactory();
        directSale = factory.create(new ArrayList<>(List.of(listing1, listing2)), seller1);
    }

    /**
     * Tests that {@link DirectSaleDTO_US025#fromItemAndDirectSale(PublicationSample, DirectSale)}
     * correctly maps a single publication sample and aggregates data from the direct sale.
     */
    @Test
    void testFromItemAndDirectSale() {
        DirectSaleDTO_US025 dto = DirectSaleDTO_US025.fromItemAndDirectSale(sample1, directSale);

        assertEquals("Title One", dto.getTitle());
        assertEquals(new ArrayList<>(List.of("author2")), dto.getAuthors());
        assertEquals(new BigDecimal("25.00"), dto.getPrice());
        assertEquals("Pack of 2 listings", dto.getListingTitle());
        assertEquals("nmeee", dto.getSeller());
    }

    /**
     * Tests that {@link DirectSaleDTO_US025#fromDirectSale(DirectSale)}
     * returns a DTO for each publication sample in all listings.
     */
    @Test
    void testFromDirectSale() {
        List<DirectSaleDTO_US025> dtos = DirectSaleDTO_US025.fromDirectSale(directSale);

        assertEquals(2, dtos.size());

        DirectSaleDTO_US025 dto1 = dtos.get(0);
        assertEquals("Title One", dto1.getTitle());
        assertEquals(new ArrayList<>(List.of("author2")), dto1.getAuthors());
        assertEquals(new BigDecimal("25.00"), dto1.getPrice());

        DirectSaleDTO_US025 dto2 = dtos.get(1);
        assertEquals("Title Two", dto2.getTitle());
        assertEquals(new ArrayList<>(List.of("author2")), dto2.getAuthors());
        assertEquals(new BigDecimal("25.00"), dto2.getPrice());
    }
}