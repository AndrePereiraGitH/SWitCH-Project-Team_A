package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.AuthorDTO_US025;
import TOPSECRET.dto.AuthorMapper_US025;
import TOPSECRET.dto.DirectSaleDTO_US025;
import TOPSECRET.dto.DirectSaleMapper_US025;
import TOPSECRET.repository.AuthorRepository;
import TOPSECRET.repository.DirectSaleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorControllerTest {

    private ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController controller;
    private AuthorRepository authorRepository;
    private DirectSaleRepository directSaleRepository;
    private Author author1;
    private Author author2;

    @BeforeEach
    void setUp() {

        authorRepository = new AuthorRepository(new ArrayList<>());

        author1 = authorRepository.createAndAddAuthor(
                "Author One",
                new Country("PORTUGAL"),
                LocalDate.of(1970, 1, 1),
                "Biography One"
        );

        author2 = authorRepository.createAndAddAuthor(
                "Author Two",
                new Country("PORTUGAL"),
                LocalDate.of(1980, 1, 1),
                "Biography Two"
        );

        Contact publisherContact = new Contact(
                "912345678",
                "+351",
                new Email("publisher", "mail.com"),
                new Address("Street A", "10", "1000-001", new Country("PORTUGAL"))
        );

        Publisher publisher = new Publisher(
                "Publisher A",
                "VAT123",
                publisherContact
        );

        BibliographicalData bd = new BibliographicalData(
                "Book One",
                List.of(author1),
                Locale.ENGLISH,
                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
                300,
                "Some text"
        );

        PublicationType publicationType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Hardcover edition"
        );

        Profile sellerProfile = new Profile(
                "Seller Name",
                new Country("PORTUGAL"),
                LocalDate.of(1990, 1, 1)
        );

        User seller = new User(
                "Seller Name",
                new Contact(),
                sellerProfile,
                UserRole.NORMAL_USER
        );

        Publication publication = new Publication(
                bd,
                publicationType,
                seller,
                PublicationStatus.FOR_SALE,
                "1st Edition",
                Code.fromIsbn13("9783161484100")
        );

        Item item = new Item(
                Condition.NEW,
                1.2,
                new Dimensions(10, 20, 3),
                publication
        );

        Listing listing = new Listing(
                List.of(item),
                new Price(new BigDecimal("25.00")),
                "Valid short description",
                "Listing Title",
                seller,
                List.of(new Picture("img.jpg"))
        );

        DirectSale directSale = new DirectSale(
                listing,
                LocalDateTime.now()
        );

        directSaleRepository = new DirectSaleRepository(List.of(directSale));

        controller = new ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
                authorRepository,
                new AuthorMapper_US025(),
                directSaleRepository,
                new DirectSaleMapper_US025()
        );
    }

    // AUTHOR --- Controller should return the list of authors
    @Test
    void shouldReturnAuthorsDTOList() {
        List<AuthorDTO_US025> authors = controller.getAllAuthors();

        assertNotNull(authors);
        assertEquals(2, authors.size());
    }

    // DIRECT SALES --- Controller should return a valid list for an author with direct sales
    @Test
    void shouldReturnDirectSalesListForAuthorWithSales() {
        List<DirectSaleDTO_US025> result = controller.getListOfItemsOnDirectSale(author1);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // DIRECT SALES --- Controller should return an empty list for an author without direct sales
    @Test
    void shouldReturnEmptyListForAuthorWithoutSales() {
        List<DirectSaleDTO_US025> result = controller.getListOfItemsOnDirectSale(author2);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}