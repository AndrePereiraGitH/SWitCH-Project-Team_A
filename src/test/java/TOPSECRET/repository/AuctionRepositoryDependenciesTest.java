package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.AuthorDTO_US026;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class AuctionRepositoryDependenciesTest {

    private final AuctionFactory auctionFactory = new AuctionFactory();
    private final AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);
    static AuthorFactory authorFactory = new AuthorFactory();
    static EditionFactory editionFactory = new EditionFactory();
    private static final ListingRepository listingRepository = new ListingRepository();


    @Test
    void empty_constructor_should_create_empty_auctionList() {
        // arrange + act
        AuctionRepository auctionRepo = new AuctionRepository(auctionFactory);

        List<Auction> auctionsList = auctionRepo.getAllAuctions();

        assertNotNull(auctionRepo);
        assertNotNull(auctionsList);
        assertEquals(0, auctionsList.size());
    }

    @Test
    void create_and_add_shouldAddActionToRepository_ifNewAuctionIsValid() throws InstanceAlreadyExistsException {

        // arrange
        AuctionFactory auctionFactory = new AuctionFactory();
        AuctionRepository auctionRepo = new AuctionRepository(auctionFactory);
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();
        LocalDateTime initialDate = now;
        LocalDateTime finalDate = now.plusDays(2);

        // act
        assertEquals(0, auctionRepo.getAllAuctions().size());
        auctionRepo.createAndAdd(listing, initialDate, finalDate, new Price(new BigDecimal(150.50)));

        //assert
        assertEquals(1, auctionRepo.getAllAuctions().size());
    }

    @Test
    void create_and_add_shouldNotAddToRepository_ifNewAuctionAlreadyExistsInAuctionsList() throws InstanceAlreadyExistsException {
        // arrange
        AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);

        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();
        LocalDateTime initialDate = now;
        LocalDateTime finalDate = now.plusDays(2);

        auctionRepository.createAndAdd(listing, initialDate, finalDate, price("150.50"));
        assertEquals(1, auctionRepository.getAllAuctions().size());

        // act + assert
        assertThrows(InstanceAlreadyExistsException.class, () -> auctionRepository.createAndAdd(listing, initialDate, finalDate, price("150.50")));

        assertEquals(1, auctionRepository.getAllAuctions().size());
    }

//    @Test
//    void getItemWhenAuctionHasThePublication(){
//
//        //Arrange
//        AuctionRepository auctionRepository = new AuctionRepository();
//
//        Listing listing = instantiateListing();
//        LocalDateTime now = fixedNow();
//        LocalDateTime initialDate = now;
//        LocalDateTime finalDate = now.plusDays(2);
//        auctionRepository.create(listing,initialDate,finalDate,price("150.50"));
//        Publication publication= listing.getItems().get(0).getPublication();
//        String expectedPublicationTitle= publication.getBibliographicalData().getTitle();
//
//
//        // Act
//        List<Item> listOfItemsOnAuction = auctionRepository.getListOfItemsOnAuctionByGivenPublication(publication);
//        String realPublicationTitle = listOfItemsOnAuction.get(0).getPublication().getBibliographicalData().getTitle();
//
//        // Assert
//        assertEquals(1,listOfItemsOnAuction.size());
//        assertEquals(expectedPublicationTitle,realPublicationTitle);
//
//
//    }

//    @Test
//    void shouldReturnAnEmptyListWhenAuctionDoesNotHaveThePublication(){
//
//        //Arrange
//        AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);
//        Listing listing = instantiateListing();
//        LocalDateTime now = fixedNow();
//        LocalDateTime initialDate = now;
//        LocalDateTime finalDate = now.plusDays(2);
//        auctionRepository.createAndAdd(listing,initialDate,finalDate,price("150.50"));
//
//
//        Publication publication2 = createValidPublication2(instantiateBibliographicalData());
//
//        //Act
//        List<Item> listOfItemsOnAuction = auctionRepository.getListOfItemsOnAuctionByGivenPublication(publication2);
//
//
//        // Assert
//
//        assertEquals(0,listOfItemsOnAuction.size());
//        assertTrue(listOfItemsOnAuction.isEmpty());
//    }

    @Test
    void getListOfItemsOnAuctionOfGivenGenreNullGenre(){
        // Arrange
        AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> auctionRepository.getListOfItemsOnAuctionGivenGenre(null));

    }

    @Test
    void getListOfItemsOnAuctionOfGivenGenreEmptyList(){
        // Arrange
        AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);
        Genre fantasy = new Genre("Fantasy");

        // Act
        List<Auction> result = auctionRepository.getListOfItemsOnAuctionGivenGenre(fantasy);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

//    @Test
//    void getListOfItemsOnAuctionOfGivenGenreOneMatchGenre(){
//        // Arrange
//        AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);
//        Genre fantasy = new Genre("Fantasy");
//        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));
//
//        // Act
//        List<Auction> result = auctionRepository.getListOfItemsOnAuctionGivenGenre(fantasy);
//
//        // Assert
//        assertEquals(1,result.size());
//
//    }

//    @Test
//    void getListOfItemsOnAuctionOfGivenGenreFilterByGenre(){
//        // Arrange
//        AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);
//        Genre fantasy = new Genre("Fantasy");
//        Genre romance = new Genre("Romance");
//
//        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));
//        auctionRepository.getAllAuctions().add(createAuctionWithGenre(romance));
//
//        // Act
//        List<Auction> result =  auctionRepository.getListOfItemsOnAuctionGivenGenre(fantasy);
//
//        // Assert
//        assertEquals(1,result.size());
//
//    }

//    @Test
//    void getListOfItemsOnAuctionOfGivenGenreMultipleMatchingGenre(){
//        // Arrange
//        AuctionRepository auctionRepository = new AuctionRepository(auctionFactory);
//        Genre fantasy = new Genre("Fantasy");
//        Genre romance = new Genre("Romance");
//        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));
//        auctionRepository.getAllAuctions().add(createAuctionWithGenre(fantasy));
//
//        // Act
//        List<Auction> result =  auctionRepository.getListOfItemsOnAuctionGivenGenre(fantasy);
//
//        Auction auction = result.get(0);
//        List<Genre> genres = auction.getListing().getItems().get(0).getBibliographicalData().getGenres();
//
//        // Assert
//        assertEquals(2,result.size());
//        assertTrue(genres.contains(fantasy));
//    }

    @Test
    void getListOfItemsOnAuctionGivenAuthor_shouldThrow_ifAuthorNull() throws InstanceAlreadyExistsException {
        // arrange
        AuctionRepository auctionRepo = new AuctionRepository(auctionFactory);


        Listing listing = instantiateListing();
        Listing listing2 = instantiateListing();
        LocalDateTime now = fixedNow();
        LocalDateTime initialDate = now;
        LocalDateTime finalDate = now.plusDays(2);
        LocalDateTime date2 = now.plusDays(4);


        auctionRepo.createAndAdd(listing, initialDate, finalDate, price("150.50"));
        auctionRepo.createAndAdd(listing2, finalDate, date2, price("200.00"));

        assertEquals(2, auctionRepo.getAllAuctions().size());


    }

    @Test
    void getListOfItemsOnAuctionGivenAuthor_shouldReturnEmptyList_ifDoesNotExistAuctionsOfThisAuthor() {
        // arrange
        AuthorDTO_US026 authorDTO = new AuthorDTO_US026("Ken Follett");
        // act
        List<Auction> result = auctionRepository.getListOfItemsOnAuctionGivenAuthor(authorDTO);

        // assert
        assertEquals(0, result.size());

    }

    @Test
    void getListOfItemsOnAuctionGivenAuthor_shouldReturnListSizedOne_ifThereIsAnAuctionOfThisAuthor() throws InstanceAlreadyExistsException {
        // arrange
        List<Author> authors = new ArrayList<>();
        Author author = instanciateAuthor();
        authors.add(author);
        AuthorDTO_US026 authorDto = AuthorDTO_US026.fromAuthor(author);
        Listing listing = instantiateListingAuthors(authors);

        auctionRepository.createAndAdd(listing, LocalDateTime.now().plusMinutes(5), LocalDateTime.now().plusDays(5), price("100"));

        // act
        List<Auction> result = auctionRepository.getListOfItemsOnAuctionGivenAuthor(authorDto);

        // assert
        assertEquals(1, result.size());
    }

    @Test
    void getListOfItemsOnAuctionGivenAuthor_shouldReturnListWithMatchingAuction_ifThereIsAnAuctionOfThisAuthor() throws InstanceAlreadyExistsException {
        // arrange
        List<Author> authors = new ArrayList<>();
        Author author = instanciateAuthor();
        authors.add(author);
        AuthorDTO_US026 authorDto = AuthorDTO_US026.fromAuthor(author);
        Listing listing = instantiateListingAuthors(authors);

        Auction auction = auctionRepository.createAndAdd(listing, LocalDateTime.now().plusMinutes(5), LocalDateTime.now().plusDays(5), price("100"));

        // act
        List<Auction> result = auctionRepository.getListOfItemsOnAuctionGivenAuthor(authorDto);

        // assert
        assertEquals(auction, result.get(0));
    }


    // ============================================================
    // ========================= Test Clock =======================
    // ============================================================

    private static final ZoneId ZONE = ZoneId.of("UTC");
    private static final Instant FIXED_INSTANT = Instant.parse("2030-01-01T10:00:00Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, ZONE);

    private static LocalDateTime fixedNow() {
        return LocalDateTime.ofInstant(FIXED_INSTANT, ZONE);
    }


    // ============================================================
    // ============================ Helpers =======================
    // ============================================================

    private static Price price(String value) {
        return new Price(new BigDecimal(value));
    }

    private Auction createAuctionWithGenre(Genre genre) throws InstanceAlreadyExistsException {
        BibliographicalData bd = new BibliographicalData(
                "Book",
                List.of( authorFactory.createAuthorWithExistingProfile(
                        new Profile("Author", new Country("Portugal"), LocalDate.of(1980, 1, 1)),
                        "bio"
                )),
                new java.util.Locale("pt","PT"),
//                new Publisher("Pub","123", null),
                LocalDate.of(2020,1,1),
                List.of(genre),
//                100,
                "Desc"
        );

        User seller = instantiateUser("Seller");

        // Edition
        Code code = Code.fromIsbn13 ("9783161484122");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher2 = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition= editionFactory.create("1st", code, physicalAttributes, pubType, publisher2, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        Publication publication = new Publication(
                bd,
                new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback"),
                seller,
                PublicationStatus.FOR_SALE,
//                "1st",
                edition,
                Code.fromIsbn13("9781234567890")
        );

        PublicationSample item = new PublicationSample(
                instantiateBibliographicalData(),
                instantiateUser("Johny"),
                PublicationStatus.FOR_SALE,
                instantiateEdition(),
                Condition.LIKE_NEW
        );

        Listing listing = listingRepository.createAndAdd(
                "Title",
                List.of(item),
                SaleType.AUCTION,
                new Price(new BigDecimal("10.00")),
                "Short desc",
                List.of()
        );

        LocalDateTime start = LocalDateTime.of(2026, 6, 30, 14, 30);
        LocalDateTime end = LocalDateTime.of(2026, 7, 1, 14, 30);

        return  auctionRepository.createAndAdd(
                listing,
                start,
                end,
                new Price(new BigDecimal("20.00"))
        );
    }

    private static Auction createValidActiveAuction() throws InstanceAlreadyExistsException {
        Listing listing = instantiateListing();
        LocalDateTime now = fixedNow();

        LocalDateTime initialDate = now;
        LocalDateTime finalDate = now.plusDays(2);

        return new Auction(listing, initialDate, finalDate, price("150.00"), FIXED_CLOCK);
    }

    private  Edition instantiateEdition(){
        Code code = Code.fromIsbn13 ("9783161484122");
        Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        return editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    }

    private static Listing instantiateListing() throws InstanceAlreadyExistsException {
        PublicationSample bookItem = createValidPublication( instantiateBibliographicalData());
        Price price = new Price(new BigDecimal("35.00"));
        final List<PublicationSample> items = new ArrayList<>(List.of(bookItem));
        return listingRepository.createAndAdd(
                "This is our first listing",
                items,
                SaleType.AUCTION,
                price,
                "short description",
                instantiatePictures()
        );
    }

    private static Listing instantiateListing2() throws InstanceAlreadyExistsException {
        PublicationSample bookItem = createValidPublication( instantiateBibliographicalData());
        Price price = new Price(new BigDecimal("35.00"));
        final List<PublicationSample> items = new ArrayList<>(List.of(bookItem));
        return listingRepository.createAndAdd(
                "This is our first listing",
                items,
                SaleType.AUCTION,
                price,
                "short description",
                instantiatePictures()
        );
    }

    private static Author instanciateAuthor() {
        Profile authorProfile = new Profile("J. R. R. Tolkien", new Country("UNITED_KINGDOM"), LocalDate.of(1892, 1, 3));
        return authorFactory.createAuthorWithExistingProfile(authorProfile, "valid bio");

    }

    private static BibliographicalData instantiateBibliographicalData() {

        List<Author> authors = new ArrayList<>();
        authors.add(instanciateAuthor());

        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre ("Fantasy"));
        genres.add(new Genre ("Mister"));

        String chunkOfText =
                "Continuing the story begun in The Hobbit, all three parts of the epic masterpiece, " +
                        "The Lord of the Rings, in one paperback. Features the definitive edition of the text, " +
                        "fold-out flaps with the original two-colour maps, and a revised and expanded index.";

        Contact contact = new Contact();
//        Publisher publisher = new Publisher("HarperCollins Publishers", "1111111111", contact);

        return new BibliographicalData(
                "The Lord Of The Rings",
                authors,
                Locale.ENGLISH,
//                publisher,
                LocalDate.of(1991, 11, 1),
                List.of(new Genre ("Fantasy")),
//                700,
                chunkOfText
        );
    }

    private static List<Picture> instantiatePictures() {
        Picture pic = new Picture("picture_url");
        List<Picture> pictures = new ArrayList<>();
        pictures.add(pic);
        return pictures;
    }

    private static PublicationSample createValidPublication(BibliographicalData bd) {

        // User (seller/owner of the publication)
        User user = instantiateUser("maria1");

        // Code
        Code code = Code.fromIsbn13("9781234567890");

        // Edition
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher2 = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition= editionFactory.create("1st", code, physicalAttributes, pubType, publisher2, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        return new PublicationSample(
                bd,
                user,
                PublicationStatus.READING,
                edition,
                Condition.LIKE_NEW
        );
    }

    private Publication createValidPublication2(BibliographicalData bd) {
        // PublicationType
        PublicationType publicationType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );

        // User (seller/owner of the publication)
        User user = instantiateUser("maria1");

        // Code
        Code code = Code.fromIsbn13("9781234567820");

        // Edition
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher2 = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition= editionFactory.create("1st", code, physicalAttributes, pubType, publisher2, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        return new Publication(
                bd,
                publicationType,
                user,
                PublicationStatus.FOR_SALE,
//                "1st edition",
                edition,
                code
        );
    }

    private static User instantiateUser(String name) {
        Email email = new Email("switcher", "example.com");
        Address add = new Address("Rua x", "12B", "4444-111", new Country("PORTUGAL"));
        Contact contact = new Contact("911111111", "351", email, add);
        LocalDate birthday = LocalDate.of(1982, 12, 3);
        Profile profile = new Profile("Albert Trump", new Country("EL_SALVADOR"), birthday);
        UserRole userRole = UserRole.NORMAL_USER;
        return new User(name, contact, profile,userRole);
    }

    private static Listing instantiateListingAuthors(List<Author> authors) throws InstanceAlreadyExistsException {
        PublicationSample bookItem = createValidPublication(instantiateBibliographicalDataAuthor(authors));
        Price price = new Price(new BigDecimal("35.00"));
        final List<PublicationSample> items = new ArrayList<>(List.of(bookItem));
        return listingRepository.createAndAdd(
                "This is our first listing",
                items,
                SaleType.AUCTION,
                price,
                "short description",
                instantiatePictures()
        );
    }

    private static  Profile profile(String name) {
        return new Profile(name, new Country("PORTUGAL"), LocalDate.of(12, 3, 1977));
    }

    private  Author author(String name) {
        return authorFactory.createAuthorWithExistingProfile(profile(name), "This is the Biography of " + name);
    }

    private static BibliographicalData instantiateBibliographicalDataAuthor(List<Author> authors) {
        Profile authorProfile = new Profile("J. R. R. Tolkien", new Country("UNITED_KINGDOM"), LocalDate.of(1892, 1, 3));
        //Author author = new Author(authorProfile, "valid bio");
        // List<Author> authors = new ArrayList<>();

//        for (int i = 0; i < authorsNameList.size(); i++) {
//
//            authors.add(
//                    new Author(
//                            profile(authorsNameList.get(i)),
//                            "this is my biography!"
//
//                            ));
//
//        }

        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre ("Fantasy"));
        genres.add(new Genre ("Mister"));

        String chunkOfText =
                "Continuing the story begun in The Hobbit, all three parts of the epic masterpiece, " +
                        "The Lord of the Rings, in one paperback. Features the definitive edition of the text, " +
                        "fold-out flaps with the original two-colour maps, and a revised and expanded index.";


        return new BibliographicalData(
                "The Lord Of The Rings",
                authors,
                Locale.ENGLISH,
//                publisher,
                LocalDate.of(1991, 11, 1),
                List.of(new Genre ("Fantasy")),
//                700,
                chunkOfText
        );
    }



}