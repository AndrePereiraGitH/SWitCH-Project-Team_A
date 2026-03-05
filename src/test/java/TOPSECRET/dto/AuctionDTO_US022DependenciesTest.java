package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


class AuctionDTO_US022DependenciesTest {

    // ==========================================================
    // ===================== Basic Tests =========================
    // ==========================================================

    @Test
    void fromDomainListEmptyListReturnsEmptyList() {
        // Act
        List<AuctionDTO_US022> result =
                AuctionDTO_US022.fromDomainList(new ArrayList<>());
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

   @Test
    void fromDomainListOneAuctionReturnsOneDTO() {
        // Arrange
        GenreFactory factory = new GenreFactory();
        Genre fantasy = factory.createGenre("Fantasy");

        Auction auction = createAuctionWithGenre(fantasy);

        // Act
        List<AuctionDTO_US022> result =
                AuctionDTO_US022.fromDomainList(List.of(auction));
        // Assert
        assertEquals(1, result.size());
        assertEquals("Book Title", result.get(0).title);
        assertTrue(result.get(0).genres.contains("Fantasy"));
    }


    @Test
    void fromDomainListMultipleAuctionsReturnsMultipleDTOs() {
        // Arrange
        GenreFactory factory = new GenreFactory();
        Genre fantasy = factory.createGenre("Fantasy");

        Auction auction1 = createAuctionWithGenre(fantasy);
        Auction auction2 = createAuctionWithGenre(fantasy);
        // Act
        List<AuctionDTO_US022> result =
                AuctionDTO_US022.fromDomainList(List.of(auction1, auction2));
        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void fromDomainListExtractsMultipleGenresCorrectly() {
        // Arrange
        GenreFactory factory = new GenreFactory();
        Genre fantasy = factory.createGenre("Fantasy");
        Genre romance = factory.createGenre("Romance");

        Auction auction =
                createAuctionWithMultipleGenres(fantasy, romance);
        // Act
        List<AuctionDTO_US022> result =
                AuctionDTO_US022.fromDomainList(List.of(auction));
        // Assert
        assertEquals(1, result.size());

        List<String> genres = result.get(0).genres;

        assertEquals(2, genres.size());
        assertTrue(genres.contains("Fantasy"));
        assertTrue(genres.contains("Romance"));
    }
 // ==========================================================
 // ===================== Helper Methods ======================
 // ==========================================================


    private Auction createAuctionWithGenre(Genre genre) {

        BibliographicalDataFactory bibliographicalDataFactory = new BibliographicalDataFactory();
        AuthorFactory authorFactory = new AuthorFactory();
        BibliographicalData bd1 = bibliographicalDataFactory.create(
                "Book Title",
                List.of(authorFactory.create(
                        "Author",
                        new Country("Portugal"),
                        LocalDate.of(1980, 1, 1),
                        "bio"
                )),
                new Locale("pt","PT"),
                LocalDate.of(2020,1,1),
                List.of(genre),
                "Description"
        );

        User owner = createUser("Owner");

        Code code = Code.fromIsbn13("1234567899123");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes1 =
                new PhysicalAttributes(dimensions, 400.0, 250);

        FactoryPublicationType pTypeFactory = new FactoryPublicationType();
        PublicationType pubType1 =
                pTypeFactory.create(MediaFormat.BOOK,
                        TypeOfBinding.CASE_BINDING,
                        "Some details");

        Publisher publisher1 =
                new Publisher("Porto Editora",
                        "PT987654321",
                        null);

        EditionFactory editionFactory = new EditionFactory();
        Edition edition1 = editionFactory.create(
                "1st",
                code,
                physicalAttributes1,
                pubType1,
                publisher1,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        PublicationSampleFactory publicationSampleFactory = new PublicationSampleFactory();
        PublicationSample item = publicationSampleFactory.create(
                bd1,
                owner,
                PublicationStatus.READ,
                edition1,
                Condition.NEW
        );

        ListingFactory listingFactory = new ListingFactory();
        Listing listing = listingFactory.create(
                "Valid Title",
                List.of(item),
                SaleType.AUCTION,
                new Price(new BigDecimal("50.00")),
                "Short desc",
                List.of(new Picture("pic1"))
        );

        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        AuctionFactory auctionFactory = new AuctionFactory();
        return auctionFactory.create(
                listing,
                start,
                end,
                new Price(new BigDecimal("125.00"))
        );
    }


    private Auction createAuctionWithMultipleGenres(Genre... genres) {

        BibliographicalDataFactory bibliographicalDataFactory = new BibliographicalDataFactory();
        AuthorFactory authorFactory = new AuthorFactory();
        BibliographicalData bd = bibliographicalDataFactory.create(
                "Book Title",
                List.of(authorFactory.create(
                        "Author",
                                new Country("Portugal"),
                                LocalDate.of(1980,1,1),
                        "bio")),
                new Locale("pt","PT"),
                LocalDate.of(2020,1,1),
                List.of(genres),
                "Description"
        );

        User owner = createUser("Owner");

        Code code = Code.fromIsbn13("1234567891123");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes =
                new PhysicalAttributes(dimensions, 400.0, 250);

        FactoryPublicationType pTypeFactory = new FactoryPublicationType();
        PublicationType pubType =
                pTypeFactory.create(MediaFormat.BOOK,
                        TypeOfBinding.CASE_BINDING,
                        "Some details");

        Publisher publisher =
                new Publisher("Porto Editora",
                        "PT987654321",
                        null);

        EditionFactory editionFactory = new EditionFactory();
        Edition edition = editionFactory.create(
                "1st",
                code,
                physicalAttributes,
                pubType,
                publisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        PublicationSampleFactory publicationSampleFactory = new PublicationSampleFactory();
        PublicationSample item = publicationSampleFactory.create(
                bd,
                owner,
                PublicationStatus.READ,
                edition,
                Condition.NEW
        );

        ListingFactory listingFactory = new ListingFactory();
        Listing listing = listingFactory.create(
                "Valid Title",
                List.of(item),
                SaleType.AUCTION,
                new Price(new BigDecimal("50.00")),
                "Short desc",
                List.of(new Picture("pic1"))
        );

        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        AuctionFactory auctionFactory = new AuctionFactory();
        return auctionFactory.create(
                listing,
                start,
                end,

new Price(new BigDecimal("125.00"))
        );
    }

    private User createUser(String name) {

        UserFactory userFactory = new UserFactory();
        return userFactory.create(
                name,
                new Contact("91233213","+351",
                        new Email("a","b.com"),
                        new Address("Rua","1","1111",
                                new Country("Portugal"))),
                new Profile(name,
                        new Country("Portugal"),
                        LocalDate.of(1990,1,1)),
                UserRole.NORMAL_USER
        );
    }
}