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
import static org.mockito.Mockito.mock;

class AuctionDTO_US022Test {

    // ==========================================================
    // ===================== Basic Tests =========================
    // ==========================================================

    @Test
    void fromDomainListEmptyListReturnsEmptyList() {
        List<AuctionDTO_US022> result =
                AuctionDTO_US022.fromDomainList(new ArrayList<>());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

   @Test
    void fromDomainListOneAuctionReturnsOneDTO() {
        Genre fantasy = new Genre("Fantasy");

        Auction auction = createAuctionWithGenre(fantasy);

        List<AuctionDTO_US022> result =
                AuctionDTO_US022.fromDomainList(List.of(auction));

        assertEquals(1, result.size());
        assertEquals("Book Title", result.get(0).title);
        assertTrue(result.get(0).genres.contains("Fantasy"));
    }


    @Test
    void fromDomainListMultipleAuctionsReturnsMultipleDTOs() {
        Genre fantasy = new Genre("Fantasy");

        Auction auction1 = createAuctionWithGenre(fantasy);
        Auction auction2 = createAuctionWithGenre(fantasy);

        List<AuctionDTO_US022> result =
                AuctionDTO_US022.fromDomainList(List.of(auction1, auction2));

        assertEquals(2, result.size());
    }

    @Test
    void fromDomainListExtractsMultipleGenresCorrectly() {
        Genre fantasy = new Genre("Fantasy");
        Genre romance = new Genre("Romance");

        Auction auction =
                createAuctionWithMultipleGenres(fantasy, romance);

        List<AuctionDTO_US022> result =
                AuctionDTO_US022.fromDomainList(List.of(auction));

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
       BibliographicalData bd1 = new BibliographicalData(
                "Book Title",
                List.of(new Author(
                        new Profile("Author",
                                new Country("Portugal"),
                                LocalDate.of(1980, 1, 1)),
                        "bio")),
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

        PublicationType pubType1 =
                new PublicationType(MediaFormat.BOOK,
                        TypeOfBinding.CASE_BINDING,
                        "Some details");

        Publisher publisher1 =
                new Publisher("Porto Editora",
                        "PT987654321",
                        null);

        Edition edition1 = new Edition(
                "1st",
                code,
                physicalAttributes1,
                pubType1,
                publisher1,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        PublicationSample item = new PublicationSample(
                bd1,
                owner,
                PublicationStatus.READ,
                edition1,
                Condition.NEW
        );

        Listing listing = new Listing(
                "Valid Title",
                List.of(item),
                SaleType.AUCTION,
                new Price(new BigDecimal("50.00")),
                "Short desc",
                List.of(new Picture("pic1"))
        );

        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        return new Auction(
                listing,
                start,
                end,
                new Price(new BigDecimal("125.00"))
        );
    }


    private Auction createAuctionWithMultipleGenres(Genre... genres) {

        BibliographicalData bd = new BibliographicalData(
                "Book Title",
                List.of(new Author(
                        new Profile("Author",
                                new Country("Portugal"),
                                LocalDate.of(1980,1,1)),
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

        PublicationType pubType =
                new PublicationType(MediaFormat.BOOK,
                        TypeOfBinding.CASE_BINDING,
                        "Some details");

        Publisher publisher =
                new Publisher("Porto Editora",
                        "PT987654321",
                        null);

        Edition edition = new Edition(
                "1st",
                code,
                physicalAttributes,
                pubType,
                publisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        PublicationSample item = new PublicationSample(
                bd,
                owner,
                PublicationStatus.READ,
                edition,
                Condition.NEW
        );

        Listing listing = new Listing(
                "Valid Title",
                List.of(item),
                SaleType.AUCTION,
                new Price(new BigDecimal("50.00")),
                "Short desc",
                List.of(new Picture("pic1"))
        );

        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        return new Auction(
                listing,
                start,
                end,

new Price(new BigDecimal("125.00"))
        );
    }

    private User createUser(String name) {
        return new User(
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