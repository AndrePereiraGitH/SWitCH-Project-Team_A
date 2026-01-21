package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.repository.DirectSaleRepository;
import TOPSECRET.repository.PublicationRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationControllerTest {

    // Valid primitive attributes
    private final String validDescription = "Valid Description";
    private final String validTitle = "PS5 Pro";


    //Price
    Price validPrice = new Price(BigDecimal.valueOf(50.99));

    // Pictures
    List<Picture> validPictures = List.of(new Picture("path/to/pic.jpg"));

    // Dimensions
    Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);

    // User
    LocalDate validBirthday = LocalDate.of(2000, 1, 1);
    Profile validProfile = new Profile("John",new Country("PORTUGAL"),validBirthday);
    Email validEmail = new Email("john.doe", "example.com");
    Address validAddress = new Address("dummyStreet", "dummyNumber", "dummyZipCode", new Country("PORTUGAL"));
    Contact validContact = new Contact("123456789","+351",validEmail,validAddress);
    User validSeller = new User("Seller1",validContact,validProfile, UserRole.NORMAL_USER);

    // BibliographicalData
    Profile profile = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
    Author author = new Author(profile, "valio bio");
    Address address = new Address("R Maria", "12", "156-5", new Country("PARAGUAY"));
    Email email = new Email("Maria1", "gmail.com");
    Contact contact = new Contact("915945695", "+351", email, address);
    Publisher publisher = new Publisher("Some Publisher", "PT123456789", contact);
    BibliographicalData bibliographicalData = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author),
            Locale.ENGLISH,
            publisher,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre ("Fiction")),
            200,
            "Some text"
    );


    PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback"
    );

    User user = new User("maria1", contact, profile, UserRole.NORMAL_USER);
    PublicationStatus publicationStatus = PublicationStatus.FOR_SALE;

    Code code1 = Code.fromIsbn13("9781234567890");
    Code code2 = Code.fromIsbn13("9781234567891");
    Code code3 = Code.fromIsbn13("9781234567892");

    Item validItem1 = new Item(Condition.LIKE_NEW, 1.2, dimensions,
            new Publication(bibliographicalData, publicationType, user, publicationStatus, "1st edition", code1));

    Item validItem2 = new Item(Condition.POOR, 1.2, dimensions,
            new Publication(bibliographicalData, publicationType, user, publicationStatus, "2nd edition", code2));

    Item validItem3 = new Item(Condition.POOR, 1.2, dimensions,
            new Publication(bibliographicalData, publicationType, user, publicationStatus, "3rd edition", code3));




    private final List<Item> severalItemList =
            new ArrayList<>(List.of(validItem1, validItem2, validItem3));

    Listing validListing = new Listing(severalItemList,validPrice,validDescription,validTitle,validSeller, validPictures);


    DirectSale validDirectSale = new DirectSale(validListing, LocalDateTime.now());

    @Test
    void getAllPublicationsReturnsAllPublicationsFromRepository() {
        // arrange
        Publication pub1 = new Publication (bibliographicalData, publicationType, user, publicationStatus, "1st edition", code1);

        Publication pub2 = new Publication(bibliographicalData, publicationType, user, publicationStatus, "2nd edition", code2);

        PublicationRepository publicationRepo = new PublicationRepository(List.of(pub1, pub2));

        DirectSaleRepository directSaleRepo = new DirectSaleRepository(List.of());

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller = new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(publicationRepo, directSaleRepo);

        // act
        List<Publication> result = controller.getAllPublications();

        // assert
        assertEquals(2, result.size());
        assertTrue(result.contains(pub1));
        assertTrue(result.contains(pub2));
    }

    @Test
    void getAllPublicationsReturnsCopyNotInternalList() {
        // arrange
        Publication pub = new Publication(bibliographicalData, publicationType, user, publicationStatus, "1st edition", code1);

        PublicationRepository publicationRepo = new PublicationRepository(List.of(pub));

        DirectSaleRepository directSaleRepo = new DirectSaleRepository(List.of());

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller = new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(publicationRepo, directSaleRepo);

        // act
        List<Publication> result = controller.getAllPublications();
        result.clear();                                                 // tries to change list

        // assert
        assertEquals(1, controller.getAllPublications().size());
    }
/*
    @Test
    void getListOfItemsOnDirectSaleReturnsMappedDTOs() {
        // arrange
        Publication targetPublication = validItem1.getPublication();

        // Creates 2 DS with items with the same publication
        Listing listing1 = new Listing(List.of(validItem1), validPrice, validDescription, validTitle, validSeller, validPictures);

        Listing listing2 = new Listing(List.of(new Item(Condition.LIKE_NEW, 1.2, dimensions, targetPublication)), validPrice, validDescription, validTitle, validSeller, validPictures);

        DirectSale ds1 = new DirectSale(listing1, LocalDateTime.now());
        DirectSale ds2 = new DirectSale(listing2, LocalDateTime.now());

        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of(ds1, ds2));
        PublicationRepository pubRepo = new PublicationRepository(List.of());

        DirectSaleMapper mapper = new DirectSaleMapper() {
            @Override
            public DirectSaleDTO toDTO(DirectSale ds) {
                return new DirectSaleDTO(
                        ds.getListing().getTitle(),
                        ds.getListing().getPrice().getValue()
                );
            }

            @Override
            public List<DirectSaleDTO> toDTO(List<DirectSale> list) {
                return list.stream().map(this::toDTO).toList();
            }
        };

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller = new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(pubRepo, dsRepo, mapper);

        // act
        List<DirectSaleDTO> result = controller.getListOfItemsOnDirectSale(targetPublication);

        // assert
        assertEquals(2, result.size());
        assertEquals(validTitle, result.get(0).title());
        assertEquals(validPrice.getValue(), result.get(0).price());
    }

    @Test
    void getListOfItemsOnDirectSaleReturnsEmptyListWhenNoMatches() {
        // arrange
        Publication unrelatedPublication = new Publication(bibliographicalData, publicationType, user, publicationStatus, "Unrelated", Code.fromIsbn13("9999999999999"));

        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of());
        PublicationRepository pubRepo = new PublicationRepository(List.of());

        DirectSaleMapper mapper = new DirectSaleMapper() {
            @Override
            public DirectSaleDTO toDTO(DirectSale ds) {
                return new DirectSaleDTO("dummy", BigDecimal.ZERO);
            }

            @Override
            public List<DirectSaleDTO> toDTO(List<DirectSale> list) {
                return list.stream().map(this::toDTO).toList();
            }
        };

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller = new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(pubRepo, dsRepo, mapper);

        // act
        List<DirectSaleDTO> result = controller.getListOfItemsOnDirectSale(unrelatedPublication);

        // assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getListOfItemsOnDirectSaleMapsAllFieldsCorrectly() {
        // arrange
        Publication targetPublication = validItem1.getPublication();

        Listing listing = new Listing(List.of(validItem1), validPrice, validDescription, validTitle, validSeller, validPictures);

        DirectSale ds = new DirectSale(listing, LocalDateTime.now());

        DirectSaleRepository dsRepo = new DirectSaleRepository(List.of(ds));
        PublicationRepository pubRepo = new PublicationRepository(List.of());

        DirectSaleMapper mapper = new DirectSaleMapper() {
            @Override
            public DirectSaleDTO toDTO(DirectSale ds) {
                return new DirectSaleDTO(
                        ds.getListing().getTitle(),
                        ds.getListing().getPrice().getValue()
                );
            }

            @Override
            public List<DirectSaleDTO> toDTO(List<DirectSale> list) {
                return list.stream().map(this::toDTO).toList();
            }
        };

        ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController controller = new ToGetTheListOfItemsOnDirectSaleOfAGivenPublicationController(pubRepo, dsRepo, mapper);

        // act
        List<DirectSaleDTO> result = controller.getListOfItemsOnDirectSale(targetPublication);

        // assert
        DirectSaleDTO dto = result.get(0);
        assertEquals(validTitle, dto.title());
        assertEquals(validPrice.getValue(), dto.price());
    }
 */
}