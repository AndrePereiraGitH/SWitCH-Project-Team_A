package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ListingRepositoryDependenciesTest {

    private ListingRepository listingRepo = new ListingRepository();
    private EditionFactory editionFactory = new EditionFactory();
    PublicationSampleRepository publicationSampleRepository = new PublicationSampleRepository();

    @Test
    void testConstructor_shouldCreateEmptyList() {
        assertNotNull(listingRepo);
      //  assertEquals(0, listingRepo. listings.size());
    }

//    @Test
//    void createAndAdd_shouldReturn_validListing() throws InstanceAlreadyExistsException {
//
//        // arrange
//        List<PublicationSample> items = new ArrayList<>();
//        BibliographicalData bd = instantiateBibliographicalData();
//        User owner = instantiateUser("Owner");
//        Edition edition = instanciateEdition();
//        PublicationSample item = publicationSampleRepository.createAndAdd(bd, owner, PublicationStatus.READING, edition, Condition.LIKE_NEW );
//        items.add(item);
//
//        // act
//        Listing list = listingRepo.createAndAdd("Title Auction",items, SaleType.DIRECT_SALE, price("35.50"), "This is the listing description", instantiateUser("ImSeller"),List.of(new Picture("pic1")));
//
//        // assert
//        assertNotNull(list);
//        assertEquals(1, listingRepo.listings.size());
//
//    }

//    @Test
//    void shouldThrow_ifDuplicatedListing() throws InstanceAlreadyExistsException {
//
//        // arrange
//        List<Item> items = new ArrayList<>();
//        Item item = itemRepo.createAndAddItem(Condition.LIKE_NEW, 2, new Dimensions(10, 10, 10), instaciatePublication());
//        items.add(item);
//        User owner = instantiateUser("ImSeller");
//        listingRepo.createAndAdd("Title Auction", items, SaleType.DIRECT_SALE, price("35.50"), "This is the listing description", owner, List.of(new Picture("pic1")));
//
//
//        // act + assert
//        InstanceAlreadyExistsException e = assertThrows(InstanceAlreadyExistsException.class, () -> listingRepo.createAndAdd("Title Auction", items, SaleType.DIRECT_SALE, price("35.50"), "This is the listing description", owner, List.of(new Picture("pic1"))));
//
//        assertEquals("Listing already exists.", e.getMessage());
//
//    }

    // Edition
    Code code = Code.fromIsbn13("9783161484123");
    Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
    Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
    Edition edition = editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    private Publication instaciatePublication() {
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "some details");
//        Publication pub = new Publication(instantiateBibliographicalData(), pubType, instantiateUser("Owner"), PublicationStatus.FOR_SALE, "2Edition", Code.fromIsbn13("ghjgajhsggkdagdk"));
        Publication pub = new Publication(instantiateBibliographicalData(), pubType, instantiateUser("Owner"), PublicationStatus.FOR_SALE, edition, Code.fromIsbn13("9781234567897"));

        return pub;
    }

    private static BibliographicalData instantiateBibliographicalData() {
        Profile authorProfile = new Profile("J. R. R. Tolkien", new Country("UNITED_KINGDOM"), LocalDate.of(1892, 1, 3));
        AuthorFactory authorFactory = new AuthorFactory();
        Author author = authorFactory.createAuthorWithExistingProfile(authorProfile, "valid bio");

        List<Author> authors = new ArrayList<>();
        authors.add(author);


        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("Fantasy"));
        genres.add(new Genre("Mister"));

        String chunkOfText =
                "Continuing the story begun in The Hobbit, all three parts of the epic masterpiece, " +
                        "The Lord of the Rings, in one paperback. Features the definitive edition of the text, " +
                        "fold-out flaps with the original two-colour maps, and a revised and expanded index.";

//        Contact contact = new Contact();
//        Publisher publisher = new Publisher("HarperCollins Publishers", "1111111111", contact);

        return new BibliographicalData(
                "The Lord Of The Rings",
                authors,
                Locale.ENGLISH,
//                publisher,
                LocalDate.of(1991, 11, 1),
                List.of(new Genre("Fantasy")),
//                700,
                chunkOfText
        );
    }

    private Edition instanciateEdition() {
        return editionFactory.create(
                "1º",
                Code.fromIsbn13("1111111111"),
                instanciatePhysicalAttributes(),
                instanciatePublicationType(),
                new Publisher("Publisher", "1223322233", instanciateContact()),
                Locale.ENGLISH, LocalDate.of(1989, 5, 19)
        );
    }

    private PublicationType instanciatePublicationType() {
        return new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.PAPERBACK,
                "some details"
        );
    }

    private PhysicalAttributes instanciatePhysicalAttributes() {
        return new PhysicalAttributes(
                new Dimensions(10, 10, 10),
                2.5,
                700
        );
    }

    private static User instantiateUser(String name) {

        Contact contact = instanciateContact();
        LocalDate birthday = LocalDate.of(1982, 12, 3);
        Profile profile = new Profile("Albert Trump", new Country("EL_SALVADOR"), birthday);
        UserRole userRole = UserRole.NORMAL_USER;
        return new User(name, contact, profile, userRole);
    }

    private static Contact instanciateContact() {
        Email email = new Email("switcher", "example.com");
        Address add = new Address("Rua x", "12B", "4444-111", new Country("PORTUGAL"));
        return new Contact("911111111", "351", email, add);
    }

    private static Price price(String value) {
        return new Price(new BigDecimal(value));
    }


    // US021 - START ------------------------------------------------------------------------------------
    //Method getListOfItemsOnDirectSaleOfAGivenGenre--------------------------------------------------
    @Test
    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldThrownErrorWhenGenreIsNull() {
        // Arrange
        ListingRepository ListingRepository = new ListingRepository();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ListingRepository.getListOfItemsOnDirectSaleOfAGivenGenre(null);
        });
        assertEquals("Genre cannot be null", exception.getMessage());
    }

    Genre validGenre = new Genre("Fantasy");

    @Test
    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnEmptyList() {
        // Arrange
        ListingRepository ListingRepository = new ListingRepository();

        // Act
        List<PublicationSample> result = ListingRepository.getListOfItemsOnDirectSaleOfAGivenGenre(validGenre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnEmptyListIfNoMatchingGenre() throws InstanceAlreadyExistsException {
        // Arrange
        ListingRepository listingRepository = new ListingRepository();

        // Criar edition local para este teste
        Code code = Code.fromIsbn13("9783161484190");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.1);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Some information");
        Publisher publisher = new Publisher("Porto Editora", "PT987654329", null);

        Edition localEdition = editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ITALY, LocalDate.of(2019, 7, 14));


        PublicationSample item = new PublicationSample(
                instantiateBibliographicalData(),  // tem genre "Fantasy"
                instantiateUser("Owner"),
                PublicationStatus.READ,
                edition,
                Condition.LIKE_NEW
        );

        listingRepository.createAndAdd(
                "Title",
                List.of(item),
                SaleType.DIRECT_SALE,
                price("10.00"),
                "description",
                List.of(new Picture("pic1"))
        );

        Genre otherGenre = new Genre("Science"); // género que não existe

        // Act
        List<PublicationSample> result = listingRepository.getListOfItemsOnDirectSaleOfAGivenGenre(otherGenre);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnListWithOneMatch() throws InstanceAlreadyExistsException {
        // Arrange
        ListingRepository listingRepository = new ListingRepository();

        // Criar edition local para este teste
        Code code = Code.fromIsbn13("9783161484100");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition localEdition = editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        PublicationSample item = new PublicationSample(
                instantiateBibliographicalData(),  // tem genre "Fantasy"
                instantiateUser("Owner"),
                PublicationStatus.READING,
                edition,
                Condition.LIKE_NEW
        );

        listingRepository.createAndAdd(
                "Title",
                List.of(item),
                SaleType.DIRECT_SALE,
                price("10.00"),
                "description",
                List.of(new Picture("pic1"))
        );

        Genre genre = new Genre("Fantasy");

        // Act
        List<PublicationSample> result = listingRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnListWithMultipleMatches() throws InstanceAlreadyExistsException {
        // Arrange
        ListingRepository listingRepository = new ListingRepository();

        // Criar edition local para este teste
        Code code = Code.fromIsbn13("9783161484150");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Here are some details");
        Publisher publisher = new Publisher(" Areal Editores", "PT987654321", null);
        Edition localEdition = editionFactory.create("3st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));


        PublicationSample item1 = new PublicationSample(
                instantiateBibliographicalData(),  // tem genre "Fantasy"
                instantiateUser("Owner1"),
                PublicationStatus.READ,
                edition,
                Condition.LIKE_NEW
        );

        PublicationSample item2 = new PublicationSample(
                instantiateBibliographicalData(),  // tem genre "Fantasy"
                instantiateUser("Owner2"),
                PublicationStatus.READ,
                edition,
                Condition.GOOD
        );

        listingRepository.createAndAdd(
                "Title 1",
                List.of(item1),
                SaleType.DIRECT_SALE,
                price("10.00"),
                "description",
                List.of(new Picture("pic1"))
        );

        listingRepository.createAndAdd(
                "Title 2",
                List.of(item2),
                SaleType.DIRECT_SALE,
                price("15.00"),
                "description",
                List.of(new Picture("pic2"))
        );

        Genre genre = new Genre("Fantasy");

        // Act
        List<PublicationSample> result = listingRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnItemsWithCorrectGenre() throws InstanceAlreadyExistsException {
        // Arrange
        ListingRepository listingRepository = new ListingRepository();

        // Criar edition local para este teste
        Code code = Code.fromIsbn13("9883161484190");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.1);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.SADDLE_STICHING, "Detailed information");
        Publisher publisher = new Publisher(" Areal Editores", "PT987654329", null);
        Edition localEdition = editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2010, 1, 1));

        PublicationSample item = new PublicationSample(
                instantiateBibliographicalData(),  // tem genre "Fantasy"
                instantiateUser("Owner"),
                PublicationStatus.READ,
                edition,
                Condition.LIKE_NEW
        );

        listingRepository.createAndAdd(
                "Title",
                List.of(item),
                SaleType.DIRECT_SALE,
                price("10.00"),
                "description",
                List.of(new Picture("pic1"))
        );

        Genre genre = new Genre("Fantasy");

        // Act
        List<PublicationSample> result = listingRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).hasGenre(genre));
    }

    @Test
    void getListOfItemsOnDirectSaleOfAGivenGenre_shouldReturnEmptyListWhenNoItemMatchesGenre() throws InstanceAlreadyExistsException {
        // Arrange
        ListingRepository listingRepository = new ListingRepository();

        // Criar edition local para este teste
        Code code = Code.fromIsbn13("9783161484190");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.1);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Some information");
        Publisher publisher = new Publisher(" Areal Editores", "PT987654329", null);
        Edition localEdition = editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.JAPANESE, LocalDate.of(2010, 7, 14));


        PublicationSample item = new PublicationSample(
                instantiateBibliographicalData(),  // tem genre "Fantasy"
                instantiateUser("Owner"),
                PublicationStatus.READING,
                edition,
                Condition.ACCEPTABLE
        );

        listingRepository.createAndAdd(
                "Title",
                List.of(item),
                SaleType.DIRECT_SALE,
                price("10.00"),
                "description",
                List.of(new Picture("pic1"))
        );

        Genre genre = new Genre("Suspense"); // género que não existe

        // Act
        List<PublicationSample> result = listingRepository.getListOfItemsOnDirectSaleOfAGivenGenre(genre);

        // Assert
        assertTrue(result.isEmpty());
    }
   // US021 - END ------------------------------------------------------------------------------------------------


}