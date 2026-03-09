package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.repository.PublicationSampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.time.LocalDate;
import java.util.Locale;


class US011AddPublicationToLibraryControllerDependenciesTest {
    //verify that the controller asks the library for the publications
    //verify that the controller asks the repository for the remaining publications
    //verify that the controller returns the correct list

    private Library library;
    private PublicationSampleRepository publicationSampleRepository;
    private US011AddPublicationToLibraryController controller;
    private AuthorFactory authorFactory = new AuthorFactory();

    private PublicationSample publicationSample1;
    private PublicationSample publicationSample2;
    private PublicationSample publicationSample3;

    private User user4;
    private PublicationType type;
    private Country country1, country2, country3;
    private Author author1, author2, author3;
    private Genre genre1, genre2, genre3;
    private Contact contact1, contact2, contact3;
    private Publisher publisher1, publisher2, publisher3;
    private BibliographicalData data1, data2, data3;
    //Atributos desta classe

    @BeforeEach //Vai fazer antes de cada teste
    void preparation() {
        publicationSampleRepository = new PublicationSampleRepository();

        //Creating a valid PublicationType
        PublicationType type = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Details1");

        //Creating valid countries
        CountryFactory countryFactory = new CountryFactory();

        Country country1 = countryFactory.create("Portugal");
        Country country2 = countryFactory.create("France");
        Country country3 = countryFactory.create("Germany");

        //Creating valid Authors
        Author author1 = authorFactory.create("Author One", country1, LocalDate.of(1980, 1, 1), "Bio1");
        Author author2 = authorFactory.create("Author Two", country2, LocalDate.of(1985, 2, 2), "Bio2");
        Author author3 = authorFactory.create("Author Three", country3, LocalDate.of(1990, 3, 3), "Bio3");

        //Creating valid Genres
        Genre genre1 = new Genre("Fiction");
        Genre genre2 = new Genre("Adventure");
        Genre genre3 = new Genre("Mystery");

        //Creating valid Publisher
        Contact contact1 = new Contact();
        Contact contact2 = new Contact();
        Contact contact3 = new Contact();

        PublisherFactory publisherFactory = new PublisherFactory();
        Publisher publisher1 = publisherFactory.create("Publisher1", "VAT001", null);
        Publisher publisher2= publisherFactory.create("Publisher2", "VAT002", null);
        Publisher publisher3 = publisherFactory.create("Publisher3", "VAT003", null);

        //Creating BibliographicalData
        BibliographicalData data1 = new BibliographicalData(
                "Title1", List.of(author1), Locale.ENGLISH,
                LocalDate.of(2020, 1, 1), List.of(genre1), "Some text 1"
        );
        BibliographicalData data2 = new BibliographicalData(
                "Title2", List.of(author2), Locale.ENGLISH,
                LocalDate.of(2021, 2, 2), List.of(genre2),  "Some text 2"
        );

        BibliographicalData data3 = new BibliographicalData(
                "Title3", List.of(author3), Locale.ENGLISH,
                LocalDate.of(2022, 3, 3), List.of(genre3),  "Some text 3"
        );

        //Creating Profiles
        Profile profile1 = new Profile("profile1", country1, LocalDate.of(1997, 8, 15));

        //Creating Users
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user4 = new User("user4", contact1, profile1, UserRole.NORMAL_USER);

        // Edition
        Code code = Code.fromIsbn13 ("9783161484123");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK,TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = publisherFactory.create("Porto Editora", "PT987654321", null);
        EditionFactory  editionFactory = new EditionFactory();
        Edition edition= editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        //Creating some sample publications, using empty users from the empty constructor in User class
//        publication1 = new Publication(data1, type, user1, PublicationStatus.READ, "1st", Code.fromIsbn10("001"));
//        publication2 = new Publication(data2, type, user2, PublicationStatus.READ, "1st", Code.fromIsbn13("002"));
//        publication3 = new Publication(data3, type, user3, PublicationStatus.READ, "1st", Code.fromIsbn10("003"));
        publicationSample1 = new PublicationSample(data1, user1, PublicationStatus.READ, edition, Condition.LIKE_NEW);
        publicationSample2 = new PublicationSample(data2, user2, PublicationStatus.READ, edition, Condition.NEW);
        publicationSample3 = new PublicationSample(data3, user3, PublicationStatus.READ, edition, Condition.POOR);

        //Add samples to repository
        publicationSampleRepository.addPublication(publicationSample1);
        publicationSampleRepository.addPublication(publicationSample2);
        publicationSampleRepository.addPublication(publicationSample3);

        //Library
        LibraryFactory libraryFactory = new LibraryFactory();

        library = libraryFactory.create("UserTest Library", user1);

        //Controller
        controller = new US011AddPublicationToLibraryController(library, publicationSampleRepository);

        //Add publication to library
        library.addPublication(publicationSample1);
    }
    @Test //This test should return a list with 2 publications since we added 1 publication to the library
    void getListOfOtherPublications_shouldReturnPublicationsNotInLibrary(){
        //Act
        List<PublicationSample> otherPublications = controller.getListOfOtherPublications();

        //Assert
        assertEquals(2, otherPublications.size(), "There should be 2 publications not in the Library");
        assertTrue(otherPublications.contains(publicationSample2), "The list should contain p2");
        assertTrue(otherPublications.contains(publicationSample3), "The list should contain p3");
        assertFalse(otherPublications.contains(publicationSample1), "The list should not contain the p1 because it's in the library");
    }

//    @Test //This test should return all publications since we create a new library but without any publication
//    void getListOfOtherPublications_shouldReturnAllPublications_emptyLibrary(){
//        //Make sure the library is clear
//        library = new Library("User4's Library", user4);
//        US011AddPublicationToLibraryController controller2 = new US011AddPublicationToLibraryController(library, publicationSampleRepository);
//
//        List<PublicationSample> otherPublications = controller2.getListOfOtherPublications();
//
//        assertEquals(3, otherPublications.size(), "There should be 3 publications, none of which are in the Library");
//        assertTrue(otherPublications.contains(publicationSample1), "The list should contain p1");
//        assertTrue(otherPublications.contains(publicationSample2), "The list should contain p2");
//        assertTrue(otherPublications.contains(publicationSample3), "The list should contain p3");
//    }

    @Test //This test should return an empty list since we add the remaining 2 publications to the library
    void getListOfOtherPublications_shouldReturnNothing_libraryHasAllPublications(){
        //Make sure the library has all publications (add the missing ones)
        library.addPublication(publicationSample2);
        library.addPublication(publicationSample3);

        US011AddPublicationToLibraryController controller3 = new US011AddPublicationToLibraryController(library, publicationSampleRepository);

        List<PublicationSample> otherPublications = controller3.getListOfOtherPublications();

        assertTrue(otherPublications.isEmpty(), "There are no publications outside the library");
    }

    @Test //This test verifies that the getAll returns a copy of the original list so it can't be modified externally
    void getListOfOtherPublications_shouldNotBeAbleToModifyTheList(){
        List<PublicationSample> originalListReadOnly = library.getAllPublications();
        assertThrows(UnsupportedOperationException.class, () -> originalListReadOnly.add(publicationSample2),
                "The list from the library is unmodifiable!");
    }

    @Test //This test should check if a publication is added to the library
    void addPublicationToLibraryShouldAddANewPublicationToLibrary(){

        boolean result = controller.addPublicationToLib(publicationSample2);

        assertTrue(result, "The publication has been added successfully");
        assertTrue(library.containsPublication(publicationSample2), "The library now contains the added publication");
    }

    @Test
    void addPublicationToLibraryShouldNotAddDuplicates(){

        assertThrows(IllegalArgumentException.class, () ->
                controller.addPublicationToLib(publicationSample1)
        );

        assertEquals(1, library.getAllPublications().size(),
                "Library should still contain only 1 publication");
    }

    @Test //This test will check if its possible to add multiple publications
    void addPublicationsShouldBeAbleToAddMultiplePublications(){

        boolean result2 = controller.addPublicationToLib(publicationSample2);
        boolean result3 = controller.addPublicationToLib(publicationSample3);

        assertTrue(result2, "Publication 2 should have been added to the Library");
        assertTrue(result3, "Publication 3 should have been added to the Library");

        assertEquals(3, library.getAllPublications().size(), "Library should contain 3 publications");
    }

    @Test //This test will check if an exception is thrown in case of a null publication
    void addPublicationsShouldThrowExceptionInCaseOfNullPublication() {

        assertThrows(IllegalArgumentException.class, () -> controller.addPublicationToLib(null), "Null Publications cannot be added");
    }
}