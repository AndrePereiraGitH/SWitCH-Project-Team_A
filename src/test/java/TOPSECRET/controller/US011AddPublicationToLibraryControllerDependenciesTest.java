//package TOPSECRET.controller;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import TOPSECRET.domain.*;
//import TOPSECRET.domain.Enum.*;
//import TOPSECRET.repository.IPublicationSampleRepositoryMem;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.time.LocalDate;
//import java.util.Locale;
//
//
//class US011AddPublicationToLibraryControllerDependenciesTest {
//    //verify that the controller asks the library for the publications
//    //verify that the controller asks the repository for the remaining publications
//    //verify that the controller returns the correct list
//
//    private Library library;
//    private IPublicationSampleRepositoryMem publicationSampleRepository;
//    private US011AddPublicationToLibraryController controller;
//    private AuthorFactory authorFactory = new AuthorFactory();
//
//    private PublicationSample publicationSample1;
//    private PublicationSample publicationSample2;
//    private PublicationSample publicationSample3;
//
//    private User user4;
//    private PublicationType type;
//    private Country country1, country2, country3;
//    private Author author1, author2, author3;
//    private Genre genre1, genre2, genre3;
//    private Contact contact1, contact2, contact3;
//    private Publisher publisher1, publisher2, publisher3;
//    private BibliographicalData data1, data2, data3;
//    //Atributos desta classe
//
//    @BeforeEach //Vai fazer antes de cada teste
//    void preparation() {
//        publicationSampleRepository = new IPublicationSampleRepositoryMem();
//
//        //Creating a valid PublicationType
//        PublicationType type = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Details1");
//
//        //Creating valid countries
//        CountryFactory countryFactory = new CountryFactory();
//
//        Country country1 = countryFactory.create("Portugal");
//        Country country2 = countryFactory.create("France");
//        Country country3 = countryFactory.create("Germany");
//
//        //Creating valid Authors
//        Author author1 = authorFactory.create("Author One", country1, LocalDate.of(1980, 1, 1), "Bio1");
//        Author author2 = authorFactory.create("Author Two", country2, LocalDate.of(1985, 2, 2), "Bio2");
//        Author author3 = authorFactory.create("Author Three", country3, LocalDate.of(1990, 3, 3), "Bio3");
//
//        //Creating valid Genres
//        Genre genre1 = new Genre("Fiction");
//        Genre genre2 = new Genre("Adventure");
//        Genre genre3 = new Genre("Mystery");
//
//        //Creating valid Publisher
//        Contact contact1 = new Contact();
//        Contact contact2 = new Contact();
//        Contact contact3 = new Contact();
//
//        PublisherFactory publisherFactory = new PublisherFactory();
//        Publisher publisher1 = publisherFactory.create("Publisher1", "VAT001", null);
//        Publisher publisher2= publisherFactory.create("Publisher2", "VAT002", null);
//        Publisher publisher3 = publisherFactory.create("Publisher3", "VAT003", null);
//
//        List<PublicationSample> otherPublications = controller2.getListOfOtherPublications();
//
//        assertEquals(3, otherPublications.size(), "There should be 3 publications, none of which are in the Library");
//        assertTrue(otherPublications.contains(publicationSample1), "The list should contain p1");
//        assertTrue(otherPublications.contains(publicationSample2), "The list should contain p2");
//        assertTrue(otherPublications.contains(publicationSample3), "The list should contain p3");
//    }
/*
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
}*/